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

import java.nio.ByteBuffer;

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

        byte[] data = {(byte) 0x03, (byte) 0x50, (byte) 0xAA, (byte) 0x55};
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.get();
        byteBuffer.get();
        ByteBuffer temp = byteBuffer.slice();

        byte[][] testData = {
                {(byte) 0x03, (byte) 0x50, (byte) 0xAA, (byte) 0x55, (byte) 0x00, (byte) 0x03, (byte) 0x50, (byte) 0x01, (byte) 0xAC,
                (byte) 0xAA, (byte) 0x55, (byte) 0x00, (byte) 0x03, (byte) 0x50, (byte) 0x01, (byte) 0xAC,
                (byte) 0xAA, (byte) 0xAA, (byte) 0x00, (byte) 0x55, (byte) 0x00, (byte) 0x03, (byte) 0x50, (byte) 0x01,
                },
                {(byte)0xAC},
        };
        SPFrameHandler frameHandler = new SPFrameHandler(frameDecoder, frameParser);

        for (int ii = 0; ii < testData.length; ii++)
            frameHandler.handle(testData[ii]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOpen:
                mSPDataHandler.openSerialPort("/dev/ttyMT2", 19200);
                mSPDataHandler.sendData(new byte[]{(byte)0x11, (byte)0x88});
                break;
            case R.id.tvClose:
                mSPDataHandler.closeSerialPort();
                break;
        }
    }
}
