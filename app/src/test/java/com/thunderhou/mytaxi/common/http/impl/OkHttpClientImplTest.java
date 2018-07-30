package com.thunderhou.mytaxi.common.http.impl;

import com.thunderhou.mytaxi.common.http.IHttpClient;
import com.thunderhou.mytaxi.common.http.IRequest;
import com.thunderhou.mytaxi.common.http.IResponse;
import com.thunderhou.mytaxi.common.http.api.API;

import org.junit.Before;
import org.junit.Test;

public class OkHttpClientImplTest {
    IHttpClient httpClient;

    @Before
    public void setUp() throws Exception {
        httpClient = new OkHttpClientImpl();
        API.Config.setDebug(true);
    }

    @Test
    public void get() {
        // 设置 request 参数
        String url = API.Config.getDomain() + API.TEST_GET;
        IRequest request = new BaseRequest(url);
        request.setHeader("testHeader", "test header");
        request.setBody("uid", "123456");
        IResponse response = httpClient.get(request, false);
        System.out.println("stateCode = " + response.getCode());
        System.out.println("body =" + response.getData());
    }

    @Test
    public void post() {
        // 设置 request 参数
        String url = API.Config.getDomain() + API.TEST_POST;
        IRequest request = new BaseRequest(url);
        request.setHeader("testHeader", "test header");
        request.setBody("uid", "123456");
        IResponse response = httpClient.post(request, false);
        System.out.println("stateCode = " + response.getCode());
        System.out.println("body =" + response.getData());
    }
}