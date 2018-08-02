package com.thunderhou.mytaxi.main.presenter;

import com.thunderhou.mytaxi.common.lbs.LocationInfo;

public interface IMainPresenter {
    void loginByToken();

    /**
     * 获取附近司机
     * @param latitude
     * @param longitude
     */
    void fetchNearDrivers(double latitude, double longitude);

    /**
     * 上报当前位置
     * @param locationInfo
     */
    void updateLocationToServer(LocationInfo locationInfo);
}
