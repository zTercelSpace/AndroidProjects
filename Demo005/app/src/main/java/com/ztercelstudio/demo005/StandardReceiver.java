package com.ztercelstudio.demo005;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class StandardReceiver extends BroadcastReceiver {
    final String TAG = "zTercel";
    public static final String STARDAND_ACTION = "com.ztercelstudio.STARDAND_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "StandardReceiver::onReceive - received a message " + intent.getAction());

    }
}
