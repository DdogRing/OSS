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
//        ClientConfiguration configuration = new ClientConfiguration();
//        configuration.setSignerOverride("");
        // 创建OSSClient实例。
        AmazonS3 amazonS3 = new AmazonS3Client(basicAWSCredentials);
        // 设置endpoint
        amazonS3.setEndpoint("https://obs-jsnj.woyun.cn");

        S3ClientOptions s3ClientOptions = S3ClientOptions.builder().setPathStyleAccess(true).setPayloadSigningEnabled(true).build();
        amazonS3.setS3ClientOptions(s3ClientOptions);
        try {

            /**
             * =======================  上传 =====================================================
             */
            String objectKey = "2022-04-13/2.jpg";
            File file1 = new File("C:\\Users\\DdogRing\\Pictures\\Camera Roll\\2.jpg");
            String file1Name = file1.getName();
            objectKey = String.format(objectKey, file1Name);
//            amazonS3.createBucket(BUCKET);
            InputStream is = new FileInputStream(file1);
//            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, objectKey, file1);
            ObjectMetadata metadata =  new ObjectMetadata();
            // metadata.setContentType(file1Name);
            PutObjectRequest putObjectRequest = new PutObjectRequest("yqfk", objectKey, is, metadata);
            // 设置读写权限
            putObjectRequest.withCannedAcl(CannedAccessControlList.Private);
            // PutObjectResult result = amazonS3.putObject(putObjectRequest);

            amazonS3.deleteBucket("ssss");

            /*if (Objects.nonNull(result)){
                System.out.println("result:=============================================================");
                System.out.println("result.getContentMd5():"+result.getContentMd5());
                System.out.println("result.getETag():"+result.getETag());
                System.out.println("result.getMetadata():"+result.getMetadata());
                System.out.println("result.getVersionId():"+result.getVersionId());
                System.out.println("result.getExpirationTimeRuleId():"+result.getExpirationTimeRuleId());
                System.out.println("result.getExpirationTime():"+result.getExpirationTime());
            }*/
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
