package com.ztercelstudio.demo003;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "zTercel";
    private LifePeriodService.LocalBinder mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvStart: {
                Intent intent = new Intent(this, LifePeriodService.class);
                startService(intent);

                break;
            }
            case R.id.tvBind: {
                Intent intent = new Intent(this, LifePeriodService.class);
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

                break;
            }
            case R.id.tvUnbind: {
                unbindService(mServiceConnection);

                break;
            }
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = (LifePeriodService.LocalBinder)service;

            Log.d(TAG, "MainActivity::onServiceConnected: " + mService.add(10, 10));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;

            Log.d(TAG, "MainActivity::onServiceDisconnected");
        }
    };
}
