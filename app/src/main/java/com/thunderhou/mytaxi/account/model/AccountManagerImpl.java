package com.thunderhou.mytaxi.account.model;

import android.util.Log;

import com.google.gson.Gson;
import com.thunderhou.mytaxi.MyTaxiApplication;
import com.thunderhou.mytaxi.account.model.response.Account;
import com.thunderhou.mytaxi.account.model.response.LoginResponse;
import com.thunderhou.mytaxi.account.model.response.RegisterResponse;
import com.thunderhou.mytaxi.account.model.response.SmsCodeResponse;
import com.thunderhou.mytaxi.account.model.response.UserExistResponse;
import com.thunderhou.mytaxi.common.databus.RxBus;
import com.thunderhou.mytaxi.common.http.IHttpClient;
import com.thunderhou.mytaxi.common.http.IRequest;
import com.thunderhou.mytaxi.common.http.IResponse;
import com.thunderhou.mytaxi.common.http.api.API;
import com.thunderhou.mytaxi.common.http.biz.BaseBizResponse;
import com.thunderhou.mytaxi.common.http.impl.BaseRequest;
import com.thunderhou.mytaxi.common.http.impl.BaseResponse;
import com.thunderhou.mytaxi.common.storage.SharedPreferencesDao;
import com.thunderhou.mytaxi.common.util.DevUtil;

import rx.functions.Func1;

public class AccountManagerImpl implements IAccountManager {
    private static final String TAG = "AccountManagerImpl";

    // 网络请求库
    private IHttpClient httpClient;
    // 数据存储
    private SharedPreferencesDao sharedPreferencesDao;
//    // 发送消息 handler
//    private Handler handler;

    public AccountManagerImpl(IHttpClient httpClient, SharedPreferencesDao sharedPreferencesDao) {
        this.httpClient = httpClient;
        this.sharedPreferencesDao = sharedPreferencesDao;
    }

//    @Override
//    public void setHandler(Handler handler) {
//        this.handler = handler;
//    }

    /**
     * 获取验证码
     * @param phone
     */
    @Override
    public void fetchSMSCode(final String phone) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                String url = API.Config.getDomain() + API.GET_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                IResponse response = httpClient.get(request, false);
                Log.d(TAG, response.getData());
                SmsCodeResponse smsCodeResponse = new SmsCodeResponse();
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        smsCodeResponse.setCode(SMS_SEND_SUC);
                    } else  {
                        smsCodeResponse.setCode(SMS_SEND_FAIL);
                    }
                } else {
                    smsCodeResponse.setCode(SMS_SEND_FAIL);
                }

                return smsCodeResponse;
            }
        });
    }

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    @Override
    public void checkSmsCode(final String phone, final String smsCode) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                String url = API.Config.getDomain() + API.CHECK_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("code", smsCode);
                IResponse response = httpClient.get(request, false);
                Log.d(TAG, response.getData());
                SmsCodeResponse smsCodeResponse = new SmsCodeResponse();
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        smsCodeResponse.setCode(SMS_CHECK_SUC);
                    } else  {
                        smsCodeResponse.setCode(SMS_CHECK_FAIL);
                    }
                } else {
                    smsCodeResponse.setCode(SMS_CHECK_FAIL);
                }

                return smsCodeResponse;
            }
        });
    }

    /**
     * 检查用户是否存在
     * @param phone
     */
    @Override
    public void checkUserExist(final String phone) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                String url = API.Config.getDomain() + API.CHECK_USER_EXIST;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                IResponse response = httpClient.get(request, false);
                Log.d(TAG, response.getData());
                UserExistResponse userExistResponse = new UserExistResponse();
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_USER_EXIST) {
                        userExistResponse.setCode(USER_EXIST);
                    } else if (bizRes.getCode() == BaseBizResponse.STATE_USER_NOT_EXIST)  {
                        userExistResponse.setCode(USER_NOT_EXIST);
                    }
                } else {
                    userExistResponse.setCode(SERVER_FAIL);
                }

                return userExistResponse;
            }
        });
    }

    /**
     * 注册
     * @param phone
     * @param password
     */
    @Override
    public void register(final String phone, final String password) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                String url = API.Config.getDomain() + API.REGISTER;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("password", password);
                request.setBody("uid", DevUtil.UUID(MyTaxiApplication.getInstance()));

                IResponse response = httpClient.post(request, false);
                Log.d(TAG, response.getData());
                RegisterResponse registerResponse = new RegisterResponse();
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        registerResponse.setCode(REGISTER_SUC);
                    } else {
                        registerResponse.setCode(SERVER_FAIL);
                    }
                } else {
                    registerResponse.setCode(SERVER_FAIL);
                }

                return registerResponse;
            }
        });
    }

    /**
     * 登录
     * @param phone
     * @param password
     */
    @Override
    public void login(final String phone, final String password) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                String url = API.Config.getDomain() + API.LOGIN;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("password", password);

                IResponse response = httpClient.post(request, false);
                Log.d(TAG, response.getData());

                LoginResponse loginResponse = new LoginResponse();
                if (response.getCode() == BaseResponse.STATE_OK) {
                    loginResponse = new Gson().fromJson(response.getData(), LoginResponse.class);
                    if (loginResponse.getCode() == BaseBizResponse.STATE_OK) {
                        // 保存登录信息
                        Account account = loginResponse.getData();
                        sharedPreferencesDao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                        // 通知 UI
                        loginResponse.setCode(LOGIN_SUC);
                    } else if (loginResponse.getCode() == BaseBizResponse.STATE_PW_ERR){
                        loginResponse.setCode(PW_ERROR);
                    } else {
                        loginResponse.setCode(SERVER_FAIL);
                    }
                } else {
                    loginResponse.setCode(SERVER_FAIL);
                }

                return loginResponse;
            }
        });
    }

    /**
     * token 自动登录
     */
    @Override
    public void loginByToken() {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                // 获取本地登录信息
                Account account =
                        (Account) sharedPreferencesDao.get(SharedPreferencesDao.KEY_ACCOUNT,
                                Account.class);

                // 登录是否过期
                boolean tokenValid = false;

                // 检查token是否过期
                if (account != null) {
                    if (account.getExpired() > System.currentTimeMillis()) {
                        // token 有效
                        tokenValid = true;
                    }
                }

                LoginResponse loginResponse = new LoginResponse();
                if (!tokenValid) {
                    if (account != null) {
                        loginResponse.setCode(TOKEN_INVALID);
                    } else {
                        loginResponse.setCode(FIRST_USE);
                    }

                    return loginResponse;
                }

                // 请求网络，完成自动登录
                String url = API.Config.getDomain() + API.LOGIN_BY_TOKEN;
                IRequest request = new BaseRequest(url);
                request.setBody("token", account.getToken());
                IResponse response = httpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    loginResponse = new Gson().fromJson(response.getData(), LoginResponse.class);
                    if (loginResponse.getCode() == BaseBizResponse.STATE_OK) {
                        // 保存登录信息
                        account = loginResponse.getData();
                        // todo: 加密存储
                        sharedPreferencesDao.save(SharedPreferencesDao.KEY_ACCOUNT, account);
                        loginResponse.setCode(LOGIN_SUC);
                    } else if(loginResponse.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
                        loginResponse.setCode(TOKEN_INVALID);
                    }
                } else {
                    loginResponse.setCode(SERVER_FAIL);
                }

                return loginResponse;
            }
        });
    }

    @Override
    public boolean isLogin() {
        // 获取本地登录信息
        Account account =
                (Account) sharedPreferencesDao.get(SharedPreferencesDao.KEY_ACCOUNT,
                        Account.class);

        // 登录是否过期
        boolean tokenValid = false;

        // 检查token是否过期
        if (account != null) {
            if (account.getExpired() > System.currentTimeMillis()) {
                // token 有效
                tokenValid = true;
            }
        }
        return tokenValid;
    }
}
