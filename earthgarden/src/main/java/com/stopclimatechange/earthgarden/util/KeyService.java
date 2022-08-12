package com.stopclimatechange.earthgarden.util;

public class KeyService {
    private static String baseURL = "http://52.78.175.39:8080";
    private static String RESTAPIKey = "8c1f3d204433457bdfa95140abe7f4e7";
    private static String secretKey = "7e2af1b64a24fb234cba830b69928d84317a7fe7b674f2152864191774e9197b495f2665fb97140a9b4230215c9b09b636f917122f1a423b4dd19f11eac7ffc2";

    public static String getBaseURL(){
        return baseURL;
    }

    public static String getRESTAPIKey() {
        return RESTAPIKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }
}