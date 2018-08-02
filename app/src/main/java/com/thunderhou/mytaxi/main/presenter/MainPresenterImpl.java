package com.thunderhou.mytaxi.main.presenter;

import com.thunderhou.mytaxi.account.model.IAccountManager;
import com.thunderhou.mytaxi.account.model.response.LoginResponse;
import com.thunderhou.mytaxi.common.databus.RegisterBus;
import com.thunderhou.mytaxi.common.http.biz.BaseBizResponse;
import com.thunderhou.mytaxi.common.lbs.LocationInfo;
import com.thunderhou.mytaxi.main.model.IMainManager;
import com.thunderhou.mytaxi.main.model.response.NearDriversResponse;
import com.thunderhou.mytaxi.main.view.IMainView;

public class MainPresenterImpl implements IMainPresenter {
    private IMainView view;
    private IAccountManager accountManager;
    private IMainManager mainManager;

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
        view.showLocationChange(locationInfo);
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
}
