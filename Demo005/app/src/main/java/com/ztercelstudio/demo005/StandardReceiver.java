package com.ztercelstudio.demo005;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class StandardReceiver extends BroadcastReceiver {
    final String TAG = "zTercel";

    public static final String STARDAND_ACTION = "com.ztercelstudio.STARDAND_ACTION";
    public Context mContext;
    StandardReceiver(Context context) {
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "StandardReceiver::onReceive - received a message");

    }

    public void registerBroadcast(final String action) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        mContext.registerReceiver(this, filter);
    }
}
