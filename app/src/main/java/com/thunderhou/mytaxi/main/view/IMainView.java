package com.thunderhou.mytaxi.main.view;

import com.thunderhou.mytaxi.account.view.IView;
import com.thunderhou.mytaxi.common.lbs.LocationInfo;
import com.thunderhou.mytaxi.main.model.bean.Order;

import java.util.List;

public interface IMainView extends IView {
    void showLoginSuc();

    /**
     * 附近司机
     * @param data
     */
    void showNears(List<LocationInfo> data);

    /**
     * 显示位置变化
     * @param locationInfo
     */
    void showLocationChange(LocationInfo locationInfo);

    /**
     * 显示呼叫成功发出
     */
    void showCallDriverSuc(Order order);

    /**
     * 显示呼叫未成功发出
     */
    void showCallDriverFail();

    /**
     * 显示取消订单成功
     */
    void showCancelSuc();
    /**
     * 显示取消订单失败
     */
    void showCancelFail();

    /**
     * 显示司机接单
     */
    void showDriverAcceptOrder(Order order);

    /**
     * 司机到达上车地点
     */
    void showDriverArriveStart(Order order);

    /**
     * 更新司机到上车点的路径
     */
    void updateDriver2StartRoute(LocationInfo locationInfo, Order order);

    /**
     * 开始行程
     */
    void showStartDrive(Order order);

    /**
     * 到达终点
     */
    void showArriveEnd(Order order);

    /**
     * 更新司机到终点的路径
     */
    void updateDriver2EndRoute(LocationInfo locationInfo, Order order);

    /**
     * 显示支付成功
     */
    void showPaySuc(Order mCurrentOrder);

    /**
     * 显示支付失败
     */
    void showPayFail();
}
