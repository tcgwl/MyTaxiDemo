package com.thunderhou.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import com.thunderhou.mytaxi.account.model.IAccountManager;
import com.thunderhou.mytaxi.account.view.ICreatePasswordDialogView;

import java.lang.ref.WeakReference;

public class CreatePasswordDialogPresenterImpl implements ICreatePasswordDialogPresenter {
    private ICreatePasswordDialogView view;
    private IAccountManager accountManager;

    /**
     * 接收子线程消息的 Handler
     */
    static class MyHandler extends Handler {
        WeakReference<CreatePasswordDialogPresenterImpl> codeDialogRef;

        public MyHandler(CreatePasswordDialogPresenterImpl presenter) {
            codeDialogRef = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            CreatePasswordDialogPresenterImpl presenter = codeDialogRef.get();
            if (presenter == null) {
                return;
            }
            // 处理UI 变化
            switch (msg.what) {
                case IAccountManager.REGISTER_SUC:
                    // 注册成功
                    presenter.view.showRegisterSuc();
                    break;
                case IAccountManager.LOGIN_SUC:
                    // 登录成功
                    presenter.view.showLoginSuc();
                    break;
                case IAccountManager.SERVER_FAIL:
                    // 服务器错误
                    presenter.view.showError(IAccountManager.SERVER_FAIL, "");
                    break;
            }

        }
    }

    /**
     * 注入 view 和 accountManager 对象
     *
     * @param view
     * @param accountManager
     */
    public CreatePasswordDialogPresenterImpl(ICreatePasswordDialogView view,
                                             IAccountManager accountManager) {
        this.view = view;
        this.accountManager = accountManager;
        accountManager.setHandler(new MyHandler(this));
    }

    /**
     * 校验密码输入合法性
     *
     * @param pw
     * @param pw1
     */
    @Override
    public boolean checkPw(String pw, String pw1) {
        if (pw == null || pw.equals("")) {
            view.showPasswordNull();
            return false;
        }
        if (!pw.equals(pw1)) {
            view.showPasswordNotEqual();
            return false;
        }
       return true;

    }

    /**
     * 注册
     *
     * @param phone
     * @param pw
     */
    @Override
    public void requestRegister(String phone, String pw) {
        accountManager.register(phone, pw);
    }

    /**
     * 登录
     *
     * @param phone
     * @param pw
     */
    @Override
    public void requestLogin(String phone, String pw) {
        accountManager.login(phone, pw);
    }
}
