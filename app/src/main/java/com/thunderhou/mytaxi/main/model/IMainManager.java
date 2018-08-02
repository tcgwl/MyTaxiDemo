package com.thunderhou.mytaxi.main.model;

import com.thunderhou.mytaxi.common.lbs.LocationInfo;

public interface IMainManager {

    /**
     * 获取附近司机
     * @param latitude
     * @param longitude
     */
    void fetchNearDrivers(double latitude, double longitude);

    /**
     * 上报位置
     * @param locationInfo
     */
    void updateLocationToServer(LocationInfo locationInfo);
}
