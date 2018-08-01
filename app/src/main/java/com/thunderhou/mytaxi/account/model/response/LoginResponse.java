package com.thunderhou.mytaxi.account.model.response;

import com.thunderhou.mytaxi.common.http.biz.BaseBizResponse;

public class LoginResponse extends BaseBizResponse {
    Account data;

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }
}
