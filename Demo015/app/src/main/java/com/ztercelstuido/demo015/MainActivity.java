package com.ztercelstuido.demo015;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.ztercelstuido.SerialPortUtils.SPHelper;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SPHelper mSPHelper = SPHelper.getInstance();

    private SPHelper.IDataReceiver mDataReceiver = new SPHelper.IDataReceiver() {
        @Override
        public void onReceive(byte[] data) {
            Log.d("zTercel", "SPHelper.IDataReceiver::onReceive dataSize = " + data.length);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        byte d = (byte)0xAA;

        byte[] data = new byte[]{(byte)0xAA, (byte)0x55, (byte)0x00, (byte)0x03, (byte)0x50, (byte)0x01, (byte)0xAC};

        ByteBuffer dataBuffer = ByteBuffer.wrap(data);

        byte head1  = (byte)(dataBuffer.get() & 0xFF);
        byte head2  = (byte)(dataBuffer.get() & 0xFF);
        short length = (short)(dataBuffer.getShort());

        if (((byte)(0xAA)) == head1) {
            Log.d("zTercel", "" + length);
        }

        String shead1 = Integer.toHexString(head1).toUpperCase();
        String shead2 = Integer.toHexString(head2).toUpperCase();
        Log.d("zTercel", shead1 + " " + shead2 + " " + length);
        return;
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
