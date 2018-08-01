package com.thunderhou.mytaxi.main.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thunderhou.mytaxi.MyTaxiApplication;
import com.thunderhou.mytaxi.R;
import com.thunderhou.mytaxi.account.model.AccountManagerImpl;
import com.thunderhou.mytaxi.account.model.IAccountManager;
import com.thunderhou.mytaxi.account.view.PhoneInputDialog;
import com.thunderhou.mytaxi.common.http.IHttpClient;
import com.thunderhou.mytaxi.common.http.impl.OkHttpClientImpl;
import com.thunderhou.mytaxi.common.storage.SharedPreferencesDao;
import com.thunderhou.mytaxi.common.util.ToastUtil;
import com.thunderhou.mytaxi.main.presenter.IMainPresenter;
import com.thunderhou.mytaxi.main.presenter.MainPresenterImpl;

/**
 *  1 检查本地纪录(登录态检查)
 *  2 若用户没登录则登录
 *  3 登录之前先校验手机号码
 *  4 token 有效, 使用 token 自动登录
 *  todo : 地图初始化
 *
 */
public class MainActivity extends AppCompatActivity implements IMainView {
    private final static String TAG = "MainActivity";
    private IMainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IHttpClient httpClient =  new OkHttpClientImpl();
        SharedPreferencesDao dao =
                new SharedPreferencesDao(MyTaxiApplication.getInstance(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        IAccountManager manager = new AccountManagerImpl(httpClient, dao);
        mPresenter = new MainPresenterImpl(this, manager);
        mPresenter.loginByToken();
    }

    @Override
    public void showLoading() {

    }

    /**
     * 自动登录成功
     */
    @Override
    public void showLoginSuc() {
        ToastUtil.show(this, getString(R.string.login_suc));
    }

    /**
     * 显示手机输入框
     */
    private void showPhoneInputDialog() {
        PhoneInputDialog dialog = new PhoneInputDialog(this);
        dialog.show();
    }

    @Override
    public void showError(int code, String msg) {
        switch (code) {
            case IAccountManager.TOKEN_INVALID:
                // 登录过期
                ToastUtil.show(this, getString(R.string.token_invalid));
            case IAccountManager.FIRST_USE:
                showPhoneInputDialog();
                break;
            case IAccountManager.SERVER_FAIL:
                // 服务器错误
                showPhoneInputDialog();
                break;

        }
    }
}
