package com.yxc.zylibrary.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import org.apache.http.Header;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ZYLibrary--[**Request**]
 * Created by robin on 15/4/14.
 * author robin (yangxc)
 */
public class ZYRequest {

    public static final int ERROR_CODE_JSON_EXCEPTION = 9999;
    public static final String ERROR_MSG_JSON_EXCEPTION = "JSON解析失败";

    protected String tag;
    protected String url;
    protected ZYParam params;
    protected ZYRequestListener listener;
    protected HttpRequest.HttpMethod method;

    protected String charset = HTTP.UTF_8;

    private CommonParameterLogic commonParameterLogic;


    public interface ZYRequestListener {
        public void requestDidStart(ZYRequest request);

        public void requestDidCancel(ZYRequest request);

        public void requestDidSuccess(ZYRequest request, JSONObject jsonResponse);

        public void requestDidFail(ZYRequest request, HttpException error, String msg);
    }

    public ZYRequest(String tag, HttpRequest.HttpMethod method, String url, ZYParam params, ZYRequestListener listener) {
        this(tag, method, url, params, listener, HTTP.UTF_8);
    }

    public ZYRequest(String tag, HttpRequest.HttpMethod method, String url, ZYParam params, ZYRequestListener listener, String charset) {
        this(tag, method, url, params, listener, charset, null);
    }

    public ZYRequest(String tag, HttpRequest.HttpMethod method, String url, ZYParam params, ZYRequestListener listener, String charset, CommonParameterLogic commonParameterLogic) {
        this.tag = tag;
        this.method = method;
        this.url = url;
        this.params = params;
        this.listener = listener;
        this.charset = charset;
        this.commonParameterLogic = commonParameterLogic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ZYParam getParams() {
        return params;
    }

    public void setParams(ZYParam params) {
        this.params = params;
    }

    public ZYRequestListener getListener() {
        return listener;
    }

    public void setListener(ZYRequestListener listener) {
        this.listener = listener;
    }

    public void cancel(){
        if (listener!=null){
            listener.requestDidCancel(this);
        }
        listener = null;
    }

    public void start() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configResponseTextCharset(charset);
        RequestCallBack callBack = new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                Object result = responseInfo.result;
                if (result instanceof String) {
                    try {
                        JSONObject jsonObject = new JSONObject((String) result);
                        if (listener!=null) {
                            listener.requestDidSuccess(ZYRequest.this, jsonObject);
                        }
                    } catch (JSONException e) {
                        HttpException exception = new HttpException(ERROR_CODE_JSON_EXCEPTION, ERROR_MSG_JSON_EXCEPTION);
                        onFailure(exception, ERROR_MSG_JSON_EXCEPTION);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (listener!=null) {
                    listener.requestDidFail(ZYRequest.this, error, msg);
                }
            }
        };
        if (commonParameterLogic==null){
            commonParameterLogic = new CommonParameterLogic() {
                @Override
                public void setupCommonParameters(String url, ZYParam params) {
                    ZYCommonParameters commonParameters = ZYCommonParameters.getInstance();
                    for (Header header: commonParameters.getHeaderHashMap().values()){
                        params.addHeader(header);
                    }
                    for (String key: commonParameters.getParamHashMap().keySet()){
                        String value = commonParameters.getParamHashMap().get(key);
                        params.addQueryStringParameter(key, value);
                    }
                }
            };
        }
        commonParameterLogic.setupCommonParameters(url, params);
        httpUtils.send(method, url, params, callBack);
        if (listener!=null) {
            listener.requestDidStart(this);
        }
    }

    public abstract class CommonParameterLogic{
        public abstract void setupCommonParameters(String url, ZYParam params);
    }

}
