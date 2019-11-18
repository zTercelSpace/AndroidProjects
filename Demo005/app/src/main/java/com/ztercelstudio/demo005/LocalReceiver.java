package com.ztercelstudio.demo005;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocalReceiver extends BroadcastReceiver {
    final String TAG = "zTercel";

    public static final String LOCAL_ACTION = "com.zterelstudio.LOCAL_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "LocalReceiver::onReceive - received a message");
    }
}
