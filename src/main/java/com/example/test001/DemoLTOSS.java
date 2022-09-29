package com.example.test001;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * @ClassName DemoLTOSS
 * @Author DdogRing
 * @Date 2022/4/12 0012 11:23
 * @Description
 * @Version 1.0
 */
public class DemoLTOSS {

    public static void main(String[] args) {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("C7486B8198154E2DA03575E8955D7C263370", "BDFEE0ECD3BD4E3A843024CAEF6C9CEC6543");

        // 创建OSSClient实例。
        AmazonS3 amazonS3 = new AmazonS3Client(basicAWSCredentials);
        // 设置endpoint
        amazonS3.setEndpoint("https://obs-jsnj.woyun.cn");

        S3ClientOptions s3ClientOptions = S3ClientOptions.builder().setPathStyleAccess(true).setPayloadSigningEnabled(true).build();
        amazonS3.setS3ClientOptions(s3ClientOptions);

        // 桶名称
        String bucketName = "demo";

        try {

            /**
             * =======================  上传 =====================================================
             */
            String objectKey = "甘雨神里.jpg";
            File file = new File("/Users/ddogring/Pictures/甘雨神里.jpeg");
            String fileName = file.getName();
            objectKey = String.format(objectKey, fileName);
            // 判断桶是否存在
            if (!amazonS3.doesBucketExist(bucketName)) {
                // 创建桶实例(容器, 无需重复创建)
                amazonS3.createBucket(bucketName);
            }
            // 创建对象请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, file);

            // 也可以文件流方式上传
            // InputStream is = new FileInputStream(file);
            // ObjectMetadata metadata =  new ObjectMetadata();
            // metadata.setContentType(fileName);
            // PutObjectRequest putObjectRequest = new PutObjectRequest("yqfk", objectKey, is, metadata);

            // 设置读写权限
            putObjectRequest.withCannedAcl(CannedAccessControlList.Private);
            // 提交对象
            PutObjectResult result = amazonS3.putObject(putObjectRequest);

            if (Objects.nonNull(result)){
                System.out.println("result:=============================================================");
                System.out.println("result.getContentMd5():"+result.getContentMd5());
                System.out.println("result.getETag():"+result.getETag());
                System.out.println("result.getMetadata():"+result.getMetadata());
                System.out.println("result.getVersionId():"+result.getVersionId());
                System.out.println("result.getExpirationTimeRuleId():"+result.getExpirationTimeRuleId());
                System.out.println("result.getExpirationTime():"+result.getExpirationTime());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
