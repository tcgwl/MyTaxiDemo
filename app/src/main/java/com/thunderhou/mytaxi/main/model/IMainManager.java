package com.thunderhou.mytaxi.main.model;

import com.thunderhou.mytaxi.common.lbs.LocationInfo;

public interface IMainManager {

    /**
     * 获取附近司机
     */
    void fetchNearDrivers(double latitude, double longitude);

    /**
     * 上报位置
     */
    void updateLocationToServer(LocationInfo locationInfo);

    /**
     * 呼叫司机
     */
    void callDriver(String key, float cost, LocationInfo startLocation, LocationInfo endLocation);

    /**
     * 取消订单
     */
    void cancelOrder(String orderId);

    /**
     * 支付
     */
    void pay(String orderId);

    /**
     * 获取正在处理中的订单
     */
    void getProcessingOrder();
}
