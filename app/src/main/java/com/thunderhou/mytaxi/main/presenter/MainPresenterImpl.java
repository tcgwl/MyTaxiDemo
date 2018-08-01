package com.thunderhou.mytaxi.main.presenter;

import android.os.Handler;
import android.os.Message;

import com.thunderhou.mytaxi.account.model.IAccountManager;
import com.thunderhou.mytaxi.main.view.IMainView;

import java.lang.ref.WeakReference;

public class MainPresenterImpl implements IMainPresenter {
    private IMainView view;
    private IAccountManager accountManager;
    /**
     * 接收子线程消息的 Handler
     */
    static class MyHandler extends Handler {
        WeakReference<MainPresenterImpl> dialogRef;

        public MyHandler(MainPresenterImpl presenter) {
            dialogRef = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            MainPresenterImpl presenter = dialogRef.get();
            if (presenter == null) {
                return;
            }
            // 处理UI 变化
            switch (msg.what) {
                case IAccountManager.LOGIN_SUC:
                    // 登录成功
                    presenter.view.showLoginSuc();
                    break;
                case IAccountManager.FIRST_USE:
                    // 首次使用
                    presenter.view.showError(IAccountManager.FIRST_USE, "");
                    break;
                case IAccountManager.TOKEN_INVALID:
                    // 登录过期
                    presenter.view.showError(IAccountManager.TOKEN_INVALID, "");
                    break;
                case IAccountManager.SERVER_FAIL:
                    // 服务器错误
                    presenter.view.showError(IAccountManager.SERVER_FAIL, "");
                    break;
            }

        }
    }

    public MainPresenterImpl(IMainView view, IAccountManager accountManager) {
        this.view = view;
        this.accountManager = accountManager;
        accountManager.setHandler(new MyHandler(this));
    }

    @Override
    public void loginByToken() {
        accountManager.loginByToken();
    }
}
