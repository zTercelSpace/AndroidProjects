package com.ztercelstuido.SerialPortUtils;

import android.util.Log;

import com.ztercelstuido.SmartClassUtils.SmartClassUtils;

import java.nio.ByteBuffer;

public class SPFrameHandler implements SPDataHandler.IDataHandler {

    private ByteBuffer mByteBuffer;
    private IFrameParser mFrameParser;
    private IFrameDecoder mFrameDecoder;

    public SPFrameHandler(IFrameDecoder frameDecoder, IFrameParser frameParser) {
        mByteBuffer     = ByteBuffer.allocate(1024);
        mFrameParser    = frameParser;
        mFrameDecoder   = frameDecoder;
    }

    @Override
    public void onDataReceived(byte[] data, int dataSize) {
        //Log.d("zTercel", "source: " + SmartClassUtils.BytePrinter.byteArrayToHex(data, dataSize));

        mByteBuffer.put(data, 0, dataSize);

        IFrameDecoder.FrameInfo frameInfo = new IFrameDecoder.FrameInfo();

        mByteBuffer.flip();
        while (mFrameDecoder.decode(mByteBuffer.array(), mByteBuffer.limit(), frameInfo)) {
            mByteBuffer.position(frameInfo.headPos);
            byte[] frame = new byte[frameInfo.length];
            mByteBuffer.get(frame);

            if (null != mFrameParser) {
                mFrameParser.parse(frame, frameInfo.length);
            }
            mByteBuffer.compact();
            mByteBuffer.flip();
        }
        mByteBuffer.compact();

        if (mByteBuffer.position() > mByteBuffer.capacity() / 5 * 4) {
            mByteBuffer.clear();
        }
    }
}