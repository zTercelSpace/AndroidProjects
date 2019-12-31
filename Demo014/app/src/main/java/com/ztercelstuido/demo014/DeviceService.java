package com.ztercelstuido.demo014;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class DeviceService extends Service {

    private IDeviceService.Stub mService = new IDeviceService.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void open() throws RemoteException {
            Log.d("zTercel", "IDeviceService.Stub::open");
        }

        @Override
        public void close() throws RemoteException {
            Log.d("zTercel", "IDeviceService.Stub::close");
        }
    };

    public DeviceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

       return mService.asBinder();
    }
}
