package com.thunderhou.mytaxi.common.http;

public interface IResponse {
    /**
     * 状态码
     */
    int getCode();

    /**
     * 响应数据
     */
    String getData();
}
