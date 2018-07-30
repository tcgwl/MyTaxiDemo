package com.thunderhou.mytaxi.common.http;

/**
 * HttpClient 抽象接口
 */
public interface IHttpClient {
    IResponse get(IRequest request, boolean forceCache);
    IResponse post(IRequest request, boolean forceCache);
}
