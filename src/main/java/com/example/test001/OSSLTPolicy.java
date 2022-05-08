package com.example.test001;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName OSSLTPolic
 * @Author DdogRing
 * @Date 2022/4/12 0012 16:26
 * @Description
 * @Version 1.0
 */
public class OSSLTPolicy {

    private static final String accessKeyId = "C7486B8198154E2DA03575E8955D7C263370";
    private static final String accessKeySecret = "BDFEE0ECD3BD4E3A843024CAEF6C9CEC6543";

    public static void main(String[] args) {
        JSONObject yqfk = getSignature("yqfk");
        System.out.println(yqfk);
    }

    /**
     * 获取临时访问OSS签名
     * 前端-用签名的方式上传文件
     * 官方链接地址：https://help.aliyun.com/document_detail/31926.html?spm=a2c4g.11186623.6.1737.5f3e3bd36kleqs
     *
     * @param bucketName bucket名称，由前端传入（上传不同文件到不同文件夹）
     */
    public static JSONObject getSignature(String bucketName) {
        JSONObject respMap = new JSONObject();
        //host的格式为 bucketName.endpoint
        String endpointForSig = "obs-jsnj.woyun.cn";
        String host = "https://" + bucketName + "." + endpointForSig;
        //callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        String callbackUrl = "";
        String dir = ""; // 用户上传文件时指定的前缀。
        OSSClient client = new OSSClient(endpointForSig, accessKeyId, accessKeySecret);
        try {
            //设置过期时间为半小时1800L
            long expireTime = 60 * 60 * 24;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            java.sql.Date expiration = new java.sql.Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConditions);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            respMap.put("accessid", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", callbackUrl);
            jasonCallback.put("callbackBody",
                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return respMap;
    }

}
