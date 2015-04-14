package com.yxc.zylibrary.http;

import org.apache.http.Header;

import java.util.HashMap;

/**
 * ZYLibrary--[**公共参数的管理**]
 * Created by robin on 15/4/14.
 * author robin (yangxc)
 */
public class ZYCommonParameters {

    private static ZYCommonParameters commonParameters;

    private ZYCommonParameters() {
        headerHashMap = new HashMap<String, Header>();
        paramHashMap = new HashMap<String, String>();
    }

    public synchronized static ZYCommonParameters getInstance(){
        if (commonParameters==null){
            commonParameters = new ZYCommonParameters();
        }
        return commonParameters;
    }

    private HashMap<String, Header> headerHashMap;
    private HashMap<String, String> paramHashMap;

    public void addCommonHeader(String key, Header header){
        headerHashMap.put(key, header);
    }

    public void removeCommonHeader(String key){
        headerHashMap.remove(key);
    }

    public void addCommonParam(String key, String value){
        paramHashMap.put(key, value);
    }

    public void removeCommonParam(String key){
        paramHashMap.remove(key);
    }

    public HashMap<String, Header> getHeaderHashMap() {
        return headerHashMap;
    }

    public HashMap<String, String> getParamHashMap() {
        return paramHashMap;
    }
}
