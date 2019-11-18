package com.ztercelstudio.demo005;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OrderReceiver extends BroadcastReceiver {

    final String TAG = "zTercel";

    public final static String ORDER_ACTION = "com.ztercelstudio.ORDER_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "OrderReceiver::onReceive received a message " + intent.getAction());

        abortBroadcast();
    }
}
