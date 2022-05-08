package com.example.test001;

import java.io.UnsupportedEncodingException;

import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.util.BinaryUtils;



public class PostObject {

    private static final String DEFAULT_ENCODING = "UTF-8";

    /* Signature method. */

    private static final String ALGORITHM = "HmacSHA1";

    private static final Object LOCK = new Object();

    private static Mac macInstance;

    //请替换成您的SecretKey
    private String accessKeySecret = "BDFEE0ECD3BD4E3A843024CAEF6C9CEC6543";

    //请替换成您的Bucket
    private String bucketName = "yqfk";

    // 对象名称 需和小程序端对象名称一致
    private String key = "2022-04-13/fff.jpg";

    private String getSignature() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, - (8 * 60) + 10);
        Date time = calendar.getTime();
        String format = sdf.format(time);
        System.out.println("format: " + format);

        String policy = "{\"expiration\": \""+ format +"\",\"conditions\": [[\"eq\",\"$bucket\", \""

                + bucketName + "\" ],[\"starts-with\", \"$key\", \"" + key

                + "\"],[\"content-length-range\", 0, 104857600],[\"eq\",\"$Content-Type\", \"text/plain\"],{\"acl\":\""

                + CannedAccessControlList.Private +"\"}]}";

        System.out.println("policy: " + policy);

        String encodePolicy = new String(Base64.getEncoder().encode(policy.getBytes()));

        String signature = computeSignature(accessKeySecret, encodePolicy);

        System.out.println("encodePolicy: " + encodePolicy);

        return signature;

    }



    public String computeSignature(String key, String data) {

        try {

            byte[] signData = sign(key.getBytes(DEFAULT_ENCODING), data.getBytes(DEFAULT_ENCODING), macInstance, LOCK,

                    ALGORITHM);

            return BinaryUtils.toBase64(signData);

        } catch (UnsupportedEncodingException ex) {

            throw new RuntimeException("Unsupported algorithm: " + DEFAULT_ENCODING, ex);

        }

    }



    protected byte[] sign(byte[] key, byte[] data, Mac macInstance, Object lock, String algorithm) {

        try {

            if (macInstance == null) {

                synchronized (lock) {

                    if (macInstance == null) {

                        macInstance = Mac.getInstance(algorithm);

                    }

                }

            }

            Mac mac;

            try {

                mac = (Mac) macInstance.clone();

            } catch (CloneNotSupportedException e) {

                mac = Mac.getInstance(algorithm);

            }

            mac.init(new SecretKeySpec(key, algorithm));

            return mac.doFinal(data);

        } catch (NoSuchAlgorithmException ex) {

            throw new RuntimeException("Unsupported algorithm: " + algorithm, ex);

        } catch (InvalidKeyException ex) {

            throw new RuntimeException("Invalid key: " + key, ex);

        }

    }

    public static void main(String[] args) {
        PostObject postObject = new PostObject();
        try {
            System.out.println("signature: " + postObject.getSignature());
            System.out.println("filename: " + postObject.key);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}