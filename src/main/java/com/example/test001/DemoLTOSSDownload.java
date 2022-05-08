package com.example.test001;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * @ClassName DemoLTOSSDownload
 * @Author DdogRing
 * @Date 2022/4/12 0012 16:00
 * @Description
 * @Version 1.0
 */
public class DemoLTOSSDownload {

    public static void main(String[] args) {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("C7486B8198154E2DA03575E8955D7C263370", "BDFEE0ECD3BD4E3A843024CAEF6C9CEC6543");
//        ClientConfiguration configuration = new ClientConfiguration();
//        configuration.setSignerOverride("");
        //
        AmazonS3 amazonS3 = new AmazonS3Client(basicAWSCredentials);
        amazonS3.setEndpoint("https://obs-jsnj.woyun.cn");

        S3ClientOptions s3ClientOptions = S3ClientOptions.builder().setPathStyleAccess(true).setPayloadSigningEnabled(true).build();
        amazonS3.setS3ClientOptions(s3ClientOptions);
        try {

            /**
             * =======================  下载 =====================================================
             */
            String objectKey = "ddd.jpg";
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
            S3Object ddd = amazonS3.getObject("yqfk", objectKey);
            S3ObjectInputStream objectContent = ddd.getObjectContent();

            if (objectContent != null) {
                File file = new File("E:\\upload\\download\\ddd.jpg");
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int i = 0;
                while ((i = objectContent.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, i);
                }

                outputStream.flush();
                outputStream.close();
                objectContent.close();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }




}
