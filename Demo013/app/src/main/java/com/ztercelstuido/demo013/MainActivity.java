package com.ztercelstuido.demo013;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android_serialport_api.SerialPort;
import com.ztercelstuido.demo014.IDeviceService;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("SerialPort");
    }

    private FileInputStream mInputStream = null;
    private SerialPort  mSerialPort = null;
    private IDeviceService mDeviceService = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDeviceService = IDeviceService.Stub.asInterface(service);

            try {
                mDeviceService.open();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setAction("com.ztercelstudio.action.DeviceService");
        intent.setPackage("com.ztercelstuido.demo014");
        this.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        try {
            /* 打开串口 */
            mSerialPort = new SerialPort(new File("/dev/" + "ttyMT2"), 115200, 0);
            //  mOutputStream = (FileOutputStream) mSerialPort.getOutputStream();
            mInputStream = (FileInputStream) mSerialPort.getInputStream();
            TextView textView = findViewById(R.id.sample_text);
            textView.setText("hello good");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
