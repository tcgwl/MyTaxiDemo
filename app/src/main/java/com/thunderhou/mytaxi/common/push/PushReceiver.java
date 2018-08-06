package com.thunderhou.mytaxi.common.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.thunderhou.mytaxi.common.databus.RxBus;
import com.thunderhou.mytaxi.common.http.biz.BaseBizResponse;
import com.thunderhou.mytaxi.common.lbs.LocationInfo;
import com.thunderhou.mytaxi.common.util.LogUtil;
import com.thunderhou.mytaxi.main.model.bean.Order;
import com.thunderhou.mytaxi.main.model.response.OrderStateOptResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";
    // 司机位置变化
    private static final int MSG_TYPE_LOCATION = 1;
    // 订单状态变化
    private static final int MSG_TYPE_ORDER = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            String msg = intent.getStringExtra("msg");
            LogUtil.d("PushReceiver", "bmob 客户端收到推送内容：" + msg);
            //  通知业务或UI
            // {"data":
            //  {"key":"4913c896-2686-4230-86e5-5d9ae0f76c89",
            //  "latitude":23.135379999999998,
            //  "longitude":113.38665700000003,
            //  "rotation":-137.4028},
            // "type":1}
            try {
                JSONObject jsonObject = new JSONObject(msg);
                int type = jsonObject.optInt("type");
                if (type == MSG_TYPE_LOCATION) {
                    // 位置变化
                    LocationInfo locationInfo =
                            new Gson().fromJson(jsonObject.optString("data"), LocationInfo.class);
                    RxBus.getInstance().send(locationInfo);
                } else if (type == MSG_TYPE_ORDER){
                    // 订单变化
                    Order order =
                            new Gson().fromJson(jsonObject.optString("data"), Order.class);
                    OrderStateOptResponse stateOptResponse = new OrderStateOptResponse();
                    stateOptResponse.setData(order);
                    stateOptResponse.setState(order.getState());
                    stateOptResponse.setCode(BaseBizResponse.STATE_OK);
                    // 通知 UI
                    RxBus.getInstance().send(stateOptResponse);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
