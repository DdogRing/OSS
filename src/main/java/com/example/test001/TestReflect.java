package com.example.test001;

import com.alibaba.fastjson.TypeReference;

/**
 * @BelongsProject: OSS
 * @BelongsPackage: com.example.test001
 * @Author: DdogRing
 * @CreateTime: 2022-06-25  15:05
 * @Description: TODO
 * @Version: 1.0
 */
public class TestReflect {

    public static void main(String[] args) throws ClassNotFoundException {

        Class cl = Class.forName("java.lang.Object");
        TypeReference typeReference = new TypeReference<Object>(){};
        System.out.println(typeReference.getType());

    }
}
