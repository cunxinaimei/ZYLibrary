package com.yxc.zylibrary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yxc.zylibrary.http.ZYRequest;
import com.yxc.zylibrary.http.ZYRequestGroup;
import com.yxc.zylibrary.http.ZYRequestQueue;
import org.json.JSONObject;

/**
 * ZYLibrary--[**Description**]
 * Created by robin on 15/4/14.
 * author robin (yangxc)
 */
public class TestActivity extends Activity implements ZYRequest.ZYRequestListener {

    private String url1 = "http://www.paipai.com/sinclude/app_index_headfirst.js";

    private String url2 = "http://www.paipai.com/sinclude/app_buycircle_index_hot.js";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ZYRequest request1 = new ZYRequest("test1", HttpRequest.HttpMethod.GET, url1, null, this, "GBK");
        request1.start();
//        ZYRequest request2 = new ZYRequest("test2", HttpRequest.HttpMethod.GET, url2, null, this, "GBK");
////        ZYRequestGroup group = new ZYRequestGroup();
////        group.add(request1, request2);
////        group.start();
//        ZYRequestQueue queue = new ZYRequestQueue(null, this);
//        queue.add(request1, request2);
//        queue.start();
    }

    @Override
    public void requestDidStart(ZYRequest request) {
        Log.d(request.getTag(), "requestDidStart");
    }

    @Override
    public void requestDidCancel(ZYRequest request) {

    }

    @Override
    public void requestDidSuccess(ZYRequest request, JSONObject jsonResponse) {
        Log.d(request.getTag(), jsonResponse.toString());
    }

    @Override
    public void requestDidFail(ZYRequest request, HttpException error, String msg) {
        Log.d(request.getTag(), msg);
    }
}