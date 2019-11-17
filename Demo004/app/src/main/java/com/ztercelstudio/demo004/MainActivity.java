package com.ztercelstudio.demo004;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String TAG = "zTercel";
    Messenger mReceiver     = new Messenger(new MessageReceiver());
    Messenger mMessenger    = null;
    boolean mIsBound        = false;

    class MessageReceiver extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "MainActivity::MessageReceiver receive a message");
            Log.d(TAG, "message what: " + msg.what);
            Log.d(TAG, "message name: " + msg.getData().getString("name"));
            Log.d(TAG, "message age: " + msg.getData().getInt("age"));
            Log.d(TAG, "message tid: " + msg.getData().getInt("tid"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btSend).setEnabled(false);
        findViewById(R.id.btUnbind).setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsBound) {
            unbindService(mServiceConnection);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btBind: {
                Intent intent = new Intent(this, MessageService.class);
                intent.setAction("com.ztercelstudio.demo004.Service");
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

                break;
            }
            case R.id.btSend: {
                if (null == mMessenger) return;

                Message message = Message.obtain();
                message.what = 100;
                Bundle data = new Bundle();
                data.putString("name", "Greg");
                data.putInt("age", 30);
                data.putLong("tid", Thread.currentThread().getId());
                message.replyTo = mReceiver;
                message.setData(data);

                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            }

            case R.id.btUnbind: {
                unbindService(mServiceConnection);

                findViewById(R.id.btSend).setEnabled(false);
                findViewById(R.id.btUnbind).setEnabled(false);

                break;
            }
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "the service have been connected");

            findViewById(R.id.btSend).setEnabled(true);
            findViewById(R.id.btUnbind).setEnabled(true);

            if (mIsBound) return;
            mIsBound    = true;
            mMessenger  = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "the service have been disconnected");
            mIsBound = false;
        }
    };

}
