package com.ztercelstudio.demo004;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;


public class MessageService extends Service {

    final String TAG     = "zTercel";
    Messenger mMessenger = new Messenger(new MessageHandler());

    class MessageHandler extends Handler {
        @Override
        public void handleMessage(@org.jetbrains.annotations.NotNull Message msg) {

            Bundle bundle = msg.getData();
            Log.d(TAG, "MessageService receive a message");
            Log.d(TAG, "message what: " + msg.what);
            Log.d(TAG, "message name: " + bundle.getString("name"));
            Log.d(TAG, "message age: " + bundle.getInt("age", 0));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
