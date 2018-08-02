package com.thunderhou.mytaxi.account.presenter;

import com.thunderhou.mytaxi.account.model.IAccountManager;
import com.thunderhou.mytaxi.account.model.response.LoginResponse;
import com.thunderhou.mytaxi.account.view.ILoginView;
import com.thunderhou.mytaxi.common.databus.RegisterBus;

public class LoginDialogPresenterImpl implements ILoginDialogPresenter {
    private ILoginView view;
    private IAccountManager accountManager;

    @RegisterBus
    public void onLoginResponse(LoginResponse response) {
        switch (response.getCode()) {
            case IAccountManager.LOGIN_SUC:
                // 登录成功
                view.showLoginSuc();
                break;
            case IAccountManager.PW_ERROR:
                // 密码错误
                view.showError(IAccountManager.PW_ERROR, "");
                break;
            case IAccountManager.SERVER_FAIL:
                // 服务器错误
                view.showError(IAccountManager.SERVER_FAIL, "");
                break;
        }
    }

    public LoginDialogPresenterImpl(ILoginView view,
                                    IAccountManager accountManager) {
        this.view = view;
        this.accountManager = accountManager;
    }

    @Override
    public void requestLogin(String phone, String password) {
        accountManager.login(phone, password);
    }
}
