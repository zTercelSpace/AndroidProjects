package com.ztercelstudio.demo007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DataManager mDataManager = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDataManager = DataManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDataManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBindService:

                //Intent intent = new Intent(this, DataService.class);
                Intent intent = new Intent("com.ztercelstudio.demo007.DataService.DATA");
                intent.setPackage("com.ztercelstudio.demo007");
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbindService: {
                try {
                    mDataManager.setData("ztercel", 30);
                    for (Data data : mDataManager.getData()) {
                        System.out.println(data.getName() + "  " + data.getAge());
                    }
                } catch (RemoteException e) {

                }
                break;
            }
        }
    }
}
