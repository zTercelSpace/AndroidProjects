package com.ztercelstudio.demo008;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ztercelstudio.aidl.IDataManager;

public class DataService extends Service {

    public final IDataManager.Stub mDataManager = new IDataManager.Stub() {
        @Override
        public void Notify(String command) throws RemoteException {
            System.out.println("Service Received a command : " + command);
        }

        @Override
        public String getCommand() throws RemoteException {
            return "CommandFromService";
        }
    };

    public DataService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("zTercel", "Servie OnCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mDataManager.asBinder();
    }
}
