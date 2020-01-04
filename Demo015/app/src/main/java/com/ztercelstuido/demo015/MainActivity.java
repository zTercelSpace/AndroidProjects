package com.ztercelstuido.demo015;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ztercelstuido.SerialPortUtils.SPHelper;
import com.ztercelstuido.SerialPortUtils.SerialPort;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SerialPort mSerialPort;

    private SPHelper mSPHelper = SPHelper.getInstance();

    private SPHelper.IDataReceiver mDataReceiver = new SPHelper.IDataReceiver() {
        @Override
        public void onReceive(byte[] data) {
            Log.d("zTercel", "SPHelper.IDataReceiver::onReceive");
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
            case R.id.tvOpen:
                mSPHelper.openSerialPort("/dev/ttyMT2", 19200);
                mSPHelper.addDataReceiver(mDataReceiver);
                break;
            case R.id.tvClose:
                mSPHelper.closeSerialPort();
                break;
        }
    }


}
