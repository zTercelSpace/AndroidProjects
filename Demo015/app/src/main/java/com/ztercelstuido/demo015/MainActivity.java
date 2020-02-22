package com.ztercelstuido.demo015;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ztercelstuido.FrameHandler.CommandHandler;
import com.ztercelstuido.FrameHandler.FixedHeadDecoder;
import com.ztercelstuido.FrameHandler.VehicleFrameParser;
import com.ztercelstuido.SerialPortUtils.IFrameDecoder;
import com.ztercelstuido.SerialPortUtils.IFrameParser;
import com.ztercelstuido.SerialPortUtils.SPDataHandler;
import com.ztercelstuido.SerialPortUtils.SPFrameHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SPDataHandler mSPDataHandler = SPDataHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        byte[] HEAD_TAG = {(byte)0xAA, (byte)0x55};
        IFrameParser  frameParser  = new VehicleFrameParser(new CommandHandler());
        IFrameDecoder frameDecoder = new FixedHeadDecoder(HEAD_TAG, 2, 2);
        mSPDataHandler.addDataHandler(new SPFrameHandler(frameDecoder, frameParser));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOpen:
                mSPDataHandler.openSerialPort("/dev/ttyHSL2", 115200);
                //mSPDataHandler.sendData(new byte[]{(byte)0x11, (byte)0x88});
                break;
            case R.id.tvClose:
                mSPDataHandler.closeSerialPort();
                break;
        }
    }
}
