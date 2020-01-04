package com.ztercelstuido.demo013;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;

import android_serialport_api.SerialPort;
import com.ztercelstuido.demo014.IDeviceService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("SerialPort");
    }

    private FileInputStream     mInputStream    = null;
    private FileOutputStream    mOutputStream   = null;
    private SerialPort          mSerialPort     = null;
    private IDeviceService      mDeviceService  = null;

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
        intent.setAction("com.ztercelstudio.action.DEVICESERVICE");
        intent.setPackage("com.ztercelstuido.demo014");
        this.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        TextView textView = findViewById(R.id.sample_text);
        textView.setText("hello good");

        try {
            /* 打开串口 */
            mSerialPort = new SerialPort(new File("/dev/ttyMT2"), 19200, 0);
            mOutputStream = (FileOutputStream) mSerialPort.getOutputStream();
            mInputStream = (FileInputStream) mSerialPort.getInputStream();

            byte[] data = new byte[1024];

            while (true) {
                mInputStream.read(data);
                Log.d("zTercel", "data");
                Thread.sleep(100);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
