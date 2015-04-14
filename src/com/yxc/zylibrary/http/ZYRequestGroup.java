package com.yxc.zylibrary.http;

import com.lidroid.xutils.exception.HttpException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ZYLibrary--[**请求群组（并发）**]
 * Created by robin on 15/4/14.
 * author robin (yangxc)
 */
public class ZYRequestGroup implements ZYRequest.ZYRequestListener {

    public List<ZYRequest> queue;
    private ZYRequestGroupListener listener;
    private ZYRequest.ZYRequestListener requestListener;

    private int cursor = 0;

    public interface ZYRequestGroupListener{
        public void requestGroupDidStart();
        public void requestGroupDidFinish();
        public void requestGroupDidCancel();
    }

    public ZYRequestGroup() {
        queue = new ArrayList<ZYRequest>();
    }

    public void add(ZYRequest request){
        queue.add(request);
    }

    public void add(ZYRequest... requests){
        for (ZYRequest request: requests){
            queue.add(request);
        }
    }

    public void remove(ZYRequest request){
        queue.remove(request);
    }

    public void cancel(){
        queue.clear();
        requestListener = null;
        if (listener!=null){
            listener.requestGroupDidCancel();
        }
    }

    public void start(){
        if (listener!=null){
            listener.requestGroupDidStart();
        }
        for (ZYRequest request: queue){
            request.start();
        }
    }

    private void addCursor(){
        cursor++;
        if (cursor==queue.size()){
            if (listener!=null){
                listener.requestGroupDidFinish();
            }
        }
    }

    @Override
    public void requestDidStart(ZYRequest request) {
        if (requestListener!=null){
            requestListener.requestDidStart(request);
        }
    }

    @Override
    public void requestDidCancel(ZYRequest request) {
        if (requestListener!=null){
            requestListener.requestDidCancel(request);
        }
        addCursor();
    }

    @Override
    public void requestDidSuccess(ZYRequest request, JSONObject jsonResponse) {
        if (requestListener!=null){
            requestListener.requestDidSuccess(request, jsonResponse);
        }
        addCursor();
    }

    @Override
    public void requestDidFail(ZYRequest request, HttpException error, String msg) {
        if (requestListener!=null){
            requestListener.requestDidFail(request, error, msg);
        }
        addCursor();
    }
}
