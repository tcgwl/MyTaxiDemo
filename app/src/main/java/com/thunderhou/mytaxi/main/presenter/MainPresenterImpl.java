package com.thunderhou.mytaxi.main.presenter;

import com.thunderhou.mytaxi.account.model.IAccountManager;
import com.thunderhou.mytaxi.account.model.response.LoginResponse;
import com.thunderhou.mytaxi.common.databus.RegisterBus;
import com.thunderhou.mytaxi.main.view.IMainView;

public class MainPresenterImpl implements IMainPresenter {
    private IMainView view;
    private IAccountManager accountManager;

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

    public MainPresenterImpl(IMainView view, IAccountManager accountManager) {
        this.view = view;
        this.accountManager = accountManager;
    }

    @Override
    public void loginByToken() {
        accountManager.loginByToken();
    }
}
