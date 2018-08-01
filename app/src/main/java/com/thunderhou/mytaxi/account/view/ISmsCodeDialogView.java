package com.thunderhou.mytaxi.account.view;

public interface ISmsCodeDialogView extends IView {
    /**
     * 显示倒计时
     */
    void showCountDownTimer();

    /**
     * 显示验证状态
     * @param suc
     */
    void showSmsCodeCheckState(boolean suc);

    /**
     * 用户是否存在
     * @param exist
     */
    void showUserExist(boolean exist);
}
