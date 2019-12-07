package com.ztercelstudio.demo007;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class DataService extends Service {

    private List<Data> mData = new ArrayList<>();

    private DataManager.Stub mDataManager = new DataManager.Stub() {
        @Override
        public void setData(String name, int age) throws RemoteException {
            mData.add(new Data(name, age));
        }

        @Override
        public List<Data> getData() throws RemoteException {
            return mData;
        }
    };

    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Service bind");
        return mDataManager;
    }
}
