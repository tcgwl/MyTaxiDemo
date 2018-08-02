package com.thunderhou.mytaxi.main.model.response;

import com.thunderhou.mytaxi.common.http.biz.BaseBizResponse;
import com.thunderhou.mytaxi.common.lbs.LocationInfo;

import java.util.List;

public class NearDriversResponse extends BaseBizResponse {
    List<LocationInfo> data;

    public List<LocationInfo> getData() {
        return data;
    }

    public void setData(List<LocationInfo> data) {
        this.data = data;
    }
}
