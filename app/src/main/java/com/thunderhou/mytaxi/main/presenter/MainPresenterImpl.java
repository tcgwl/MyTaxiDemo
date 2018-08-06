package com.thunderhou.mytaxi.main.presenter;

import com.thunderhou.mytaxi.account.model.IAccountManager;
import com.thunderhou.mytaxi.account.model.response.LoginResponse;
import com.thunderhou.mytaxi.common.databus.RegisterBus;
import com.thunderhou.mytaxi.common.http.biz.BaseBizResponse;
import com.thunderhou.mytaxi.common.lbs.LocationInfo;
import com.thunderhou.mytaxi.common.util.LogUtil;
import com.thunderhou.mytaxi.main.model.IMainManager;
import com.thunderhou.mytaxi.main.model.bean.Order;
import com.thunderhou.mytaxi.main.model.response.NearDriversResponse;
import com.thunderhou.mytaxi.main.model.response.OrderStateOptResponse;
import com.thunderhou.mytaxi.main.view.IMainView;

public class MainPresenterImpl implements IMainPresenter {
    private static final String TAG  = "MainPresenterImpl";
    private IMainView view;
    private IAccountManager accountManager;
    private IMainManager mainManager;
    // 当前的订单
    private Order mCurrentOrder;

    @RegisterBus
    public void onLoginResponse(LoginResponse loginResponse) {
        switch (loginResponse.getCode()) {
            case IAccountManager.LOGIN_SUC:
                // 登录成功
                view.showLoginSuc();
                break;
            case IAccountManager.FIRST_USE:
                // 首次使用
                view.showError(IAccountManager.FIRST_USE, "");
                break;
            case IAccountManager.TOKEN_INVALID:
                // 登录过期
                view.showError(IAccountManager.TOKEN_INVALID, "");
                break;
            case IAccountManager.SERVER_FAIL:
                // 服务器错误
                view.showError(IAccountManager.SERVER_FAIL, "");
                break;
        }
    }

    @RegisterBus
    public void onNearDriversResponse(NearDriversResponse response){
        if (response == null) return;

        if (response.getCode() == BaseBizResponse.STATE_OK) {
            view.showNears(response.getData());
        }
    }

    @RegisterBus
    public void onLocationInfo(LocationInfo locationInfo) {
        if (mCurrentOrder != null
                && (mCurrentOrder.getState() ==
                OrderStateOptResponse.ORDER_STATE_ACCEPT)) {
            // 更新司机到上车点的路径信息
            view.updateDriver2StartRoute(locationInfo, mCurrentOrder);
        } else if (mCurrentOrder != null
                && mCurrentOrder.getState() ==
                OrderStateOptResponse.ORDER_STATE_START_DRIVE){
            // 更新司机到终点的路径信息
            view.updateDriver2EndRoute(locationInfo, mCurrentOrder);
        } else {
            view.showLocationChange(locationInfo);
        }
    }

    // todo 订单状态响应
    @RegisterBus
    public void onOrderOptResponse(OrderStateOptResponse response) {
        if (response.getState() == OrderStateOptResponse.ORDER_STATE_CREATE) {
            // 呼叫司机
            if (response.getCode() == BaseBizResponse.STATE_OK) {
                // 保存当前的订单
                mCurrentOrder = response.getData();
                view.showCallDriverSuc(mCurrentOrder);
            } else {
                view.showCallDriverFail();
            }
        } else if (response.getState() == OrderStateOptResponse.ORDER_STATE_CANCEL) {
            // 取消订单
            if (response.getCode() == BaseBizResponse.STATE_OK) {
                view.showCancelSuc();
            } else {
                view.showCancelFail();
            }
        } else if (response.getState() == OrderStateOptResponse.ORDER_STATE_ACCEPT) {
            // 司机接单
            mCurrentOrder = response.getData();
            view.showDriverAcceptOrder(mCurrentOrder);
        } else if (response.getState() == OrderStateOptResponse.ORDER_STATE_ARRIVE_START) {
            // 司机到达上车点
            mCurrentOrder = response.getData();
            view.showDriverArriveStart(mCurrentOrder);
        } else if (response.getState() == OrderStateOptResponse.ORDER_STATE_START_DRIVE) {
            // 开始行程
            mCurrentOrder = response.getData();
            view.showStartDrive(mCurrentOrder);
        } else if (response.getState() == OrderStateOptResponse.ORDER_STATE_ARRIVE_END) {
            // 到达终点
            mCurrentOrder = response.getData();
            view.showArriveEnd(mCurrentOrder);
        } else if (response.getState() == OrderStateOptResponse.PAY) {
            // 支付
            if (response.getCode() == BaseBizResponse.STATE_OK) {
                view.showPaySuc(mCurrentOrder);
            } else {
                view.showPayFail();
            }

        }
    }

    public MainPresenterImpl(IMainView view,
                             IAccountManager accountManager,
                             IMainManager mainManager) {
        this.view = view;
        this.accountManager = accountManager;
        this.mainManager = mainManager;
    }

    @Override
    public void loginByToken() {
        accountManager.loginByToken();
    }

    @Override
    public void fetchNearDrivers(double latitude, double longitude) {
        mainManager.fetchNearDrivers(latitude, longitude);
    }

    @Override
    public void updateLocationToServer(LocationInfo locationInfo) {
        mainManager.updateLocationToServer(locationInfo);
    }

    @Override
    public void callDriver(String key, float cost, LocationInfo startLocation, LocationInfo endLocation) {
        mainManager.callDriver(key, cost, startLocation, endLocation);
    }

    @Override
    public boolean isLogin() {
        return accountManager.isLogin();
    }

    @Override
    public void cancel() {
        if (mCurrentOrder != null) {
            mainManager.cancelOrder(mCurrentOrder.getOrderId());
        } else {
            view.showCancelSuc();
        }
    }

    /**
     * 支付
     */
    @Override
    public void pay() {
        if (mCurrentOrder != null) {
            mainManager.pay(mCurrentOrder.getOrderId());
        }
    }

    /**
     * 获取正在处理中的订单
     */
    @Override
    public void getProcessingOrder() {
        mainManager.getProcessingOrder();
        LogUtil.d(TAG, "getProcessingOrder");
    }
}
