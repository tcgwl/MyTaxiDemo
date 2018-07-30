package com.thunderhou.mytaxi.common.http.impl;

import android.support.annotation.NonNull;

import com.thunderhou.mytaxi.common.http.IHttpClient;
import com.thunderhou.mytaxi.common.http.IRequest;
import com.thunderhou.mytaxi.common.http.IResponse;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientImpl implements IHttpClient {
    OkHttpClient mOkHttpClient = new OkHttpClient.Builder().build();

    @Override
    public IResponse get(IRequest request, boolean forceCache) {
        // 指定请求方式
        request.setMethod(IRequest.GET);

        Request.Builder builder = addHeader(request);

        // 获取 url
        String url = request.getUrl();
        builder.url(url).get();

        Request okRequest = builder.build();
        // 执行 okRequest
        return  execute(okRequest);
    }

    @Override
    public IResponse post(IRequest request, boolean forceCache) {
        // 指定请求方式
        request.setMethod(IRequest.POST);

        Request.Builder builder = addHeader(request);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, request.getBody().toString());
        builder.url(request.getUrl()).post(body);

        Request okRequest = builder.build();
        return  execute(okRequest);
    }

    @NonNull
    private Request.Builder addHeader(IRequest request) {
        // OkHttp 的 Request.Builder
        Request.Builder builder = new Request.Builder();
        // 解析头部
        Map<String, String> header = request.getHeader();
        for (String key : header.keySet()) {
            // 组装成 OkHttp 的 Header
            builder.header(key, header.get(key));
        }
        return builder;
    }

    /**
     * 请求执行过程
     */
    private IResponse execute(Request request)  {
        BaseResponse commonResponse = new BaseResponse();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            // 设置状态码
            commonResponse.setCode(response.code());
            String body = response.body().string();
            // 设置响应数据
            commonResponse.setData(body);
            /*Log.d("OkHttpClientImpl" ,String.format("Received response body: %s ",
                    body));*/
        } catch (IOException e) {
            e.printStackTrace();
            commonResponse.setCode(commonResponse.STATE_UNKNOWN_ERROR);
            commonResponse.setData(e.getMessage());
        }
        return commonResponse;

    }
}
