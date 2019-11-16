package com.ztercelstudio.demo003;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LifePeriodService extends Service {

    final String TAG = "zTercel";

    public LifePeriodService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "LifePeriodService::onCreate");
    }

    public class LocalBinder extends Binder {

        public int add(int left, int right) {
            return (left + right);
        }

        LifePeriodService getService() {
            return LifePeriodService.this;
        }
    }

    public void toastInfo() {
        Toast.makeText(this, "i come from service", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.d(TAG, "LifePeriodService::onBind");

        return new LocalBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {

        Log.d(TAG, "LifePeriodService::onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "LifePeriodService::onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "LifePeriodService::onDestroy");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

        Log.d(TAG, "LifePeriodService::onRebind");
    }
}
