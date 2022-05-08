package com.example.test001;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;

/**
 * @ClassName OSSLTPartUploadHigh
 * @Author DdogRing
 * @Date 2022/4/14 0014 19:43
 * @Description
 * @Version 1.0
 */
public class OSSLTPartUploadHigh {

    public static void main(String[] args) throws Exception {

        String bucketName = "yqfk";
        String keyName = "iii.rar";
        String filePath = "D:\\work\\tool.rar";
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("C7486B8198154E2DA03575E8955D7C263370", "BDFEE0ECD3BD4E3A843024CAEF6C9CEC6543");
        AmazonS3 s3Client = new AmazonS3Client(basicAWSCredentials);
        s3Client.setEndpoint("https://obs-jsnj.woyun.cn");
        try {

            TransferManager tm = TransferManagerBuilder.standard()
                    .withS3Client(s3Client)
                    .build();
            // TransferManager processes all transfers asynchronously,
            // so this call returns immediately.
            Upload upload = tm.upload(bucketName, keyName, new File(filePath));
            System.out.println("Object upload started");

            // Optionally, wait for the upload to finish before continuing.
            upload.waitForCompletion();
            System.out.println("Object upload complete");
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
    }
}
