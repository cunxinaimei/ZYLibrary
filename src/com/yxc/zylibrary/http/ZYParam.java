package com.yxc.zylibrary.http;

import com.lidroid.xutils.http.RequestParams;

/**
 * ZYLibrary--[**参数**]
 * Created by robin on 15/4/14.
 * author robin (yangxc)
 */
public class ZYParam extends RequestParams {

    public void addAll(ZYParam param){
        for (HeaderItem headerItem: param.getHeaders()){
            addHeader(headerItem.header);
        }
        addQueryStringParameter(param.getQueryStringParams());
    }

}
