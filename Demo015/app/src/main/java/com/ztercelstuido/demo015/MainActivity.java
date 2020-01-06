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

        byte[] data = new byte[]{(byte)0x00, (byte)0x03, (byte)0x50, (byte)0xAA, (byte)0x55, (byte)0x03, (byte)0x00, (byte)0x50, (byte)0x01, (byte)0xAC, (byte)0xDD};

        ByteBuffer dataBuffer = ByteBuffer.wrap(data);

        byte[] head = new byte[]{(byte)0xAA, (byte)0x55};

        //dataBuffer.flip();
        /*
        byte tmp1 = (byte) (dataBuffer.get() & 0xFF);
        dataBuffer.mark();
        byte tmp2 = (byte) (dataBuffer.get() & 0xFF);
        byte tmp3 = (byte) (dataBuffer.get() & 0xFF);
        dataBuffer.reset();
        byte tmp4 = (byte) (dataBuffer.get() & 0xFF); */
        //return;

        for (int ii = 0; ii < dataBuffer.limit() - head.length; ii++) {
            byte tmp = (byte) (dataBuffer.get() & 0xFF);
            dataBuffer.mark();
            // find head
            boolean isFound = true;
            for (int jj = 0; jj < head.length; jj++) {
                if (tmp == head[jj]) {
                    tmp = (byte)(dataBuffer.get() & 0xFF);
                } else {
                    dataBuffer.reset();
                    isFound = false;
                    break;
                }
            }

            if (isFound) {
                  Log.d("zTercel", "pos " + ii);

                  //dataBuffer.reset();
                  short frameLength = dataBuffer.getShort();
                  byte[] content = new byte[frameLength];
                  dataBuffer.get(content);
                //  dataBuffer.compact();
            }
        }

        return;
        /*
        byte head1  = (byte)(dataBuffer.get() & 0xFF);
        byte head2  = (byte)(dataBuffer.get() & 0xFF);
        short length = (short)(dataBuffer.getShort());

        if (((byte)(0xAA)) == head1) {
            Log.d("zTercel", "" + length);
        }

        String shead1 = Integer.toHexString(head1).toUpperCase();
        String shead2 = Integer.toHexString(head2).toUpperCase();
        Log.d("zTercel", shead1 + " " + shead2 + " " + length);*/
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
