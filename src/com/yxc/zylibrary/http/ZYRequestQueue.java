package com.yxc.zylibrary.http;

import com.lidroid.xutils.exception.HttpException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ZYLibrary--[**请求队列**]
 * Created by robin on 15/4/14.
 * author robin (yangxc)
 */
public class ZYRequestQueue implements ZYRequest.ZYRequestListener {

    public List<ZYRequest> queue;
    private ZYRequestQueueListener listener;
    private ZYRequest.ZYRequestListener requestListener;
    private ZYRequest focusRequest;
    private int cursor = 0;

    private HashMap<String, ZYRequest.ZYRequestListener> tempListeners;

    public interface ZYRequestQueueListener{
        public void requestQueueDidStart();
        public void requestQueueDidFinish();
        public void requestQueueDidCancel();
        public void focusRequestChanged(ZYRequest request);
    }

    public ZYRequestQueue() {
        this(null, null);
    }

    public ZYRequestQueue(ZYRequestQueueListener listener, ZYRequest.ZYRequestListener requestListener) {
        this.queue = new ArrayList<ZYRequest>();
        this.listener = listener;
        this.requestListener = requestListener;
        this.tempListeners = new HashMap<String, ZYRequest.ZYRequestListener>();
    }

    public void setListener(ZYRequestQueueListener listener) {
        this.listener = listener;
    }

    public void setRequestListener(ZYRequest.ZYRequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public void add(ZYRequest request){
        if (request.getListener()!=null){
            tempListeners.put(request.getTag(), request.getListener());
        }
        request.setListener(this);
        queue.add(request);
    }

    public void add(ZYRequest... requests){
        for (ZYRequest request: requests){
            add(request);
        }
    }

    public void remove(ZYRequest request){
        queue.remove(request);
        request.setListener(tempListeners.get(request.getTag()));
        tempListeners.remove(request.getTag());
    }

    public void cancel(){
        queue.clear();
        requestListener = null;
        focusRequest.cancel();
        focusRequest = null;
        if (listener!=null){
            listener.requestQueueDidCancel();
        }
    }

    public void start(){
        if (queue.size()>0){
            focusRequest = queue.get(0);
            focusRequest.start();
            if (listener!=null){
                listener.requestQueueDidStart();
            }
        }
    }

    private ZYRequest next(){
        cursor++;
        if (cursor<queue.size()) {
            return queue.get(cursor);
        }
        return null;
    }

    private void continueRequest(){
        focusRequest = next();
        if (focusRequest==null){
            if (listener!=null) {
                listener.requestQueueDidFinish();
            }
        }else{
            focusRequest.start();
            if (listener!=null) {
                listener.focusRequestChanged(focusRequest);
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
    }

    @Override
    public void requestDidSuccess(ZYRequest request, JSONObject jsonResponse) {
        if (requestListener!=null){
            requestListener.requestDidSuccess(request, jsonResponse);
        }
        continueRequest();
    }

    @Override
    public void requestDidFail(ZYRequest request, HttpException error, String msg) {
        if (requestListener!=null){
            requestListener.requestDidFail(request, error, msg);
        }
        continueRequest();
    }
}
