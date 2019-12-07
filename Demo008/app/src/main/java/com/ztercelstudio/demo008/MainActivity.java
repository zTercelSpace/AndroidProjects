package com.ztercelstudio.demo008;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.ztercelstudio.aidl.IDataManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IDataManager mDataManager = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDataManager = IDataManager.Stub.asInterface(service);
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
            case R.id.btnBind:
                Intent intent = new Intent("com.ztercelstudio.demo008.DATA_ACTION");
                intent.setPackage("com.ztercelstudio.demo008");
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnSend:
                try {
                    if (mDataManager == null) return;
                    String command = mDataManager.getCommand();
                    Toast.makeText(this, command, Toast.LENGTH_LONG).show();
                } catch (RemoteException e) {
                    System.out.println(e);
                }
                break;
        }
    }
}
