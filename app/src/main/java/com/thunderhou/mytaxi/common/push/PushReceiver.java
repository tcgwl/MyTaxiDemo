package com.thunderhou.mytaxi.common.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.thunderhou.mytaxi.common.databus.RxBus;
import com.thunderhou.mytaxi.common.lbs.LocationInfo;
import com.thunderhou.mytaxi.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";
    private static final int MSG_TYPE_LOCATION = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            String msg = intent.getStringExtra("msg");
            LogUtil.d(TAG, "客户端收到推送内容：" + msg);
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
