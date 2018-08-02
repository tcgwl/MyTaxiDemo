package com.thunderhou.mytaxi.main.view;

import com.thunderhou.mytaxi.account.view.IView;
import com.thunderhou.mytaxi.common.lbs.LocationInfo;

import java.util.List;

public interface IMainView extends IView {
    void showLoginSuc();

    /**
     * 附近司机
     * @param data
     */
    void showNears(List<LocationInfo> data);

    /**
     * 显示位置变化
     * @param locationInfo
     */
    void showLocationChange(LocationInfo locationInfo);
}
