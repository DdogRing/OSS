package com.example.test001;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.directory.model.ServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * @ClassName OSSPartUpload
 * @Author DdogRing
 * @Date 2022/4/14 0014 10:53
 * @Description
 * @Version 1.0
 */
public class OSSLTPartUpload {
    public static void main(String[] args) throws Exception {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("C7486B8198154E2DA03575E8955D7C263370", "BDFEE0ECD3BD4E3A843024CAEF6C9CEC6543");
//        ClientConfiguration configuration = new ClientConfiguration();
//        configuration.setSignerOverride("");
        AmazonS3 oss = new AmazonS3Client(basicAWSCredentials);
        oss.setEndpoint("https://obs-jsnj.woyun.cn");

        S3ClientOptions s3ClientOptions = S3ClientOptions.builder().setPathStyleAccess(true).setPayloadSigningEnabled(true).build();
        oss.setS3ClientOptions(s3ClientOptions);

// 填写Bucket名称，例如examplebucket。
        String bucketName = "yqfk";
// 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "hhh.rar";
// 填写本地文件完整路径，例如/storage/emulated/0/oss/examplefile.txt。
        String localFilepath = "D:\\work\\tool.rar";

// 初始化分片上传。
        InitiateMultipartUploadRequest init = new InitiateMultipartUploadRequest(bucketName, objectName);
        InitiateMultipartUploadResult initResult = oss.initiateMultipartUpload(init);
// 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关操作，例如取消分片上传、查询分片上传等。
        String uploadId = initResult.getUploadId();

// 设置单个Part的大小，单位为字节，取值范围为100 KB~5 GB。
        long partSize = 5 * 1024 * 1024 * 10;

        File file = new File(localFilepath);
        long contentLength = file.length();

// 分片上传。
        List<PartETag> partETags = new ArrayList<PartETag>();
        // Upload the file parts.
        long filePosition = 0;
        for (int i = 1; filePosition < contentLength; i++) {
            // Because the last part could be less than 5 MB, adjust the part size as needed.
            partSize = Math.min(partSize, (contentLength - filePosition));

            // Create the request to upload a part.
            UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(bucketName)
                    .withKey(objectName)
                    .withUploadId(uploadId)
                    .withPartNumber(i)
                    .withFileOffset(filePosition)
                    .withFile(file)
                    .withPartSize(partSize);

            // Upload the part and add the response's ETag to our list.
            UploadPartResult uploadResult = oss.uploadPart(uploadRequest);
            partETags.add(uploadResult.getPartETag());

            filePosition += partSize;
        }

        // Complete the multipart upload.
        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, objectName,
                initResult.getUploadId(), partETags);
        oss.completeMultipartUpload(compRequest);
    }
}
