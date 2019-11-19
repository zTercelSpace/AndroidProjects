package com.ztercelstudio.demo005;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class RecycleReceiver extends BroadcastReceiver {

    final String TAG = "zTercel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "RecycleReceiver::onReceive received a message " + intent.getAction());

        Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
        //NetWorkHelper networkHelper = NetWorkHelper.getInstance(context);

    }
}
