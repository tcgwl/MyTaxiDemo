package com.thunderhou.mytaxi.account.view;

public interface IView {
    /**
     * 显示loading
     */
    void showLoading();
    /**
     *  显示错误
     */
    void showError(int code, String msg);
}
