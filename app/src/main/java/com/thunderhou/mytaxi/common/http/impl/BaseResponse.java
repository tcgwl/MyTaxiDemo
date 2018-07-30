package com.thunderhou.mytaxi.common.http.impl;

import com.thunderhou.mytaxi.common.http.IResponse;

public class BaseResponse implements IResponse {
    public static final int STATE_UNKNOWN_ERROR = 100001;
    // 状态码
    private int code;
    // 响应数据
    private String data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getData() {
        return data;
    }
}
