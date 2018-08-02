package com.thunderhou.mytaxi.main.view;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.dalimao.mytaxi.R;
import com.thunderhou.mytaxi.MyTaxiApplication;
import com.thunderhou.mytaxi.account.model.AccountManagerImpl;
import com.thunderhou.mytaxi.account.model.IAccountManager;
import com.thunderhou.mytaxi.account.view.PhoneInputDialog;
import com.thunderhou.mytaxi.common.databus.RxBus;
import com.thunderhou.mytaxi.common.http.IHttpClient;
import com.thunderhou.mytaxi.common.http.api.API;
import com.thunderhou.mytaxi.common.http.impl.OkHttpClientImpl;
import com.thunderhou.mytaxi.common.lbs.GaodeLbsLayerImpl;
import com.thunderhou.mytaxi.common.lbs.ILbsLayer;
import com.thunderhou.mytaxi.common.lbs.LocationInfo;
import com.thunderhou.mytaxi.common.storage.SharedPreferencesDao;
import com.thunderhou.mytaxi.common.util.ToastUtil;
import com.thunderhou.mytaxi.main.model.IMainManager;
import com.thunderhou.mytaxi.main.model.MainMangerImpl;
import com.thunderhou.mytaxi.main.presenter.IMainPresenter;
import com.thunderhou.mytaxi.main.presenter.MainPresenterImpl;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/** －－－ 登录逻辑－－－
 *  1 检查本地记录(登录状态检查)
 *  2 若用户没登录则登录
 *  3 登录之前先校验手机号码
 *  4 token 若有效，则使用 token 自动登录
 *  －－－－ 地图初始化－－－
 *  1 地图接入
 *  2 定位自己的位置，显示蓝点
 *  3 使用 Marker 标记当前位置和方向
 *
 */
public class MainActivity extends AppCompatActivity implements IMainView {
    private final static String TAG = "MainActivity";
    private IMainPresenter mPresenter;
    private ILbsLayer mLbsLayer;
    private Bitmap mDriverBit;
    private String mPushKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IHttpClient httpClient =  new OkHttpClientImpl();
        SharedPreferencesDao dao =
                new SharedPreferencesDao(MyTaxiApplication.getInstance(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        IAccountManager manager = new AccountManagerImpl(httpClient, dao);
        IMainManager mainManager = new MainMangerImpl(httpClient);
        mPresenter = new MainPresenterImpl(this, manager, mainManager);
        RxBus.getInstance().register(mPresenter);
        mPresenter.loginByToken();

        // 地图服务
        mLbsLayer = new GaodeLbsLayerImpl(this);
        mLbsLayer.onCreate(savedInstanceState);
        mLbsLayer.setLocationChangeListener(new ILbsLayer.CommonLocationChangeListener() {
            @Override
            public void onLocationChanged(LocationInfo locationInfo) {

            }

            @Override
            public void onLocation(LocationInfo locationInfo) {
                // 首次定位，添加当前位置的标记
                mLbsLayer.addOrUpdateMarker(locationInfo, BitmapFactory.decodeResource(getResources(), R.drawable.navi_map_gps_locked));

                // 获取附近司机
                getNearDrivers(locationInfo.getLatitude(), locationInfo.getLongitude());
                // 上报当前位置
                updateLocationToServer(locationInfo);
            }
        });
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.activity_main);
        mapViewContainer.addView(mLbsLayer.getMapView());

        // 推送服务
        // 初始化BmobSDK
        Bmob.initialize(this, API.Config.getAppId());
        // 使用推送服务时的初始化操作
        BmobInstallation installation = BmobInstallation.getCurrentInstallation(this);
        installation.save();
        mPushKey = installation.getInstallationId();
        // 启动推送服务
        BmobPush.startWork(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mLbsLayer.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mLbsLayer.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLbsLayer.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(mPresenter);
        mLbsLayer.onDestroy();
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

    /**
     * 显示手机输入框
     */
    private void showPhoneInputDialog() {
        PhoneInputDialog dialog = new PhoneInputDialog(this);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                RxBus.getInstance().register(mPresenter);
            }
        });
        RxBus.getInstance().unRegister(mPresenter);
    }

    /**
     * 获取附近司机
     * @param latitude
     * @param longitude
     */
    private void getNearDrivers(double latitude, double longitude) {
        mPresenter.fetchNearDrivers(latitude, longitude);
    }

    /**
     * 上报当前位置
     * @param locationInfo
     */
    private void updateLocationToServer(LocationInfo locationInfo) {
        locationInfo.setKey(mPushKey);
        mPresenter.updateLocationToServer(locationInfo);
    }

    /**
     * 显示附近司机
     * @param data
     */
    @Override
    public void showNears(List<LocationInfo> data) {
        for (LocationInfo locationInfo : data) {
            showLocationChange(locationInfo);
        }
    }

    @Override
    public void showLocationChange(LocationInfo locationInfo) {
        if (mDriverBit == null || mDriverBit.isRecycled()) {
            mDriverBit = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        }
        mLbsLayer.addOrUpdateMarker(locationInfo, mDriverBit);
    }
}
