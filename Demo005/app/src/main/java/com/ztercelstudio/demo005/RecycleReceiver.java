package com.ztercelstudio.demo005;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RecycleReceiver extends BroadcastReceiver {

    final String TAG = "zTercel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "RecycleReceiver::onReceive received a message " + intent.getAction());

    }
}
