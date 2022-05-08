package com.example.test001;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @ClassName OSSDelete
 * @Author DdogRing
 * @Date 2022/4/14 0014 10:11
 * @Description
 * @Version 1.0
 */
public class OSSLTDelete {

    public static void main(String[] args) {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("C7486B8198154E2DA03575E8955D7C263370", "BDFEE0ECD3BD4E3A843024CAEF6C9CEC6543");

        AmazonS3 amazonS3 = new AmazonS3Client(basicAWSCredentials);
        amazonS3.setEndpoint("https://obs-jsnj.woyun.cn");
        S3ClientOptions s3ClientOptions = S3ClientOptions.builder().setPathStyleAccess(true).setPayloadSigningEnabled(true).build();
        amazonS3.setS3ClientOptions(s3ClientOptions);

        try {

            /**
             * =======================  删除 =====================================================
             */
            String objectKey = "ddd.jpg";
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
            S3Object ddd = amazonS3.getObject("yqfk", objectKey);
            System.out.println(ddd.getKey());
            amazonS3.deleteObject("yqfk", objectKey);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
