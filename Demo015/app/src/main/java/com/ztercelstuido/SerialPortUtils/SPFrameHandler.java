package com.ztercelstuido.SerialPortUtils;

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
    public void handle(byte[] data) {
        mByteBuffer.put(data);

        IFrameDecoder.FrameInfo frameInfo = new IFrameDecoder.FrameInfo();
        mByteBuffer.flip();
        while (mByteBuffer.hasRemaining() && mFrameDecoder.decode(mByteBuffer.slice(), frameInfo)) {
            mByteBuffer.position(frameInfo.headPos);
            byte[] frame = new byte[frameInfo.length];
            mByteBuffer.get(frame);

            if (null != mFrameParser) {
                mFrameParser.parse(frame);
            }
            mByteBuffer.compact();
            mByteBuffer.flip();
        }

        if (mByteBuffer.position() > mByteBuffer.capacity() / 5 * 4) {
            mByteBuffer.clear();
        }
    }

    /*
    @Override
    public void handle(byte[] data) {
        mByteBuffer.put(data);

        IFrameDecoder.FrameInfo frameInfo = new IFrameDecoder.FrameInfo();

        mByteBuffer.flip();
        while (mFrameDecoder.decode(mByteBuffer.array(), mByteBuffer.limit(), frameInfo)) {
            mByteBuffer.position(frameInfo.headPos);
            byte[] frame = new byte[frameInfo.length];
            mByteBuffer.get(frame);

            if (null != mFrameParser) {
                mFrameParser.parse(frame);
            }

            mByteBuffer.compact();
            mByteBuffer.flip();
        }
        mByteBuffer.compact();

        if (mByteBuffer.position() > mByteBuffer.capacity() / 5 * 4) {
            mByteBuffer.clear();
        }
    }*/


}