package com.ztercelstuido.FrameHandler;


import com.ztercelstuido.SerialPortUtils.IFrameDecoder;

import java.nio.ByteBuffer;

public class FixedHeadDecoder implements IFrameDecoder {

    private byte[]  mHead;
    private int     mLengthFieldOffset;
    private int     mLengthFieldBytes;

    public FixedHeadDecoder(byte[] head, int lengthFieldOffset, int lengthFieldBytes) {
        if (head.length > 0) {
            mHead = new byte[head.length];
            System.arraycopy(head, 0, mHead, 0, head.length);
        }

        assert (lengthFieldOffset <= 0 || mLengthFieldBytes <= 0);

        mLengthFieldOffset   = lengthFieldOffset;
        mLengthFieldBytes    = lengthFieldBytes;
    }

    @Override
    public boolean decode(ByteBuffer data, FrameInfo frameInfo) {
        //-----------------------------------------------------------------
        // frame:　head flag + data length field + data
        //-----------------------------------------------------------------
        boolean isDecoded = false;
        if (data.limit() > (mLengthFieldOffset + mLengthFieldBytes)) {
            byte[] lengthBytes  = new byte[mLengthFieldBytes];
            int frameHeadPos    = findFrameHeadPos(data, mHead);
            data.position(frameHeadPos + mLengthFieldOffset);
            data.get(lengthBytes);
            int dataFiledBytes  = byteArrayToInt(lengthBytes);
            if ((-1 != frameHeadPos) && (0 < dataFiledBytes)) {
                int frameTailPos = frameHeadPos + mLengthFieldOffset + mLengthFieldBytes + dataFiledBytes;
                if (data.limit() >= frameTailPos) {
                    frameInfo.headPos   = frameHeadPos;
                    frameInfo.tailPos   = frameTailPos;
                    frameInfo.length    = frameTailPos - frameHeadPos;

                    isDecoded = true;
                }
            }
        }

        return isDecoded;
    }

    int findFrameHeadPos(ByteBuffer data, byte[] head) {
        int frameHeadPos = -1;
        boolean isFound  = false;

        for (int ii = 0; ii < data.limit() && !isFound; ii++) {
            frameHeadPos = ii;
            for (int jj = 0; jj < head.length; jj++) {
                if (data.get(ii + jj) == head[jj]) {
                    isFound = true;
                } else {
                    isFound = false;
                    break;
                }
            }
        }

        return isFound ? frameHeadPos : -1;
    }

    public boolean decode(byte[] data, int dataBytes, FrameInfo frameInfo) {
        //-----------------------------------------------------------------
        // frame:　head flag + lengthfield + data
        //-----------------------------------------------------------------
        boolean isDecoded = false;
        if (dataBytes > (mLengthFieldOffset + mLengthFieldBytes)) {
            int frameHeadPos    = findFrameHeadPos(data, mHead);
            int dataFiledBytes  = byteArrayToInt(data, frameHeadPos + mLengthFieldOffset, mLengthFieldBytes);
            if ((-1 != frameHeadPos) && (0 < dataFiledBytes)) {
                int frameTailPos = frameHeadPos + mLengthFieldOffset + mLengthFieldBytes + dataFiledBytes;
                if (dataBytes >= frameTailPos) {
                    frameInfo.headPos   = frameHeadPos;
                    frameInfo.tailPos   = frameTailPos;
                    frameInfo.length    = frameTailPos - frameHeadPos;

                    isDecoded = true;
                }
            }
        }

        return isDecoded;
    }

    int findFrameHeadPos(final byte[] data, byte[] head) {
        int frameHeadPos = -1;
        boolean isFound  = false;

        for (int ii = 0; ii < data.length && !isFound; ii++) {
            frameHeadPos = ii;
            for (int jj = 0; jj < head.length; jj++) {
                if (data[ii + jj] == head[jj]) {
                    isFound = true;
                } else {
                    isFound = false;
                    break;
                }
            }
        }

        return isFound ? frameHeadPos : -1;
    }

    int byteArrayToInt(final byte[] bytes, int offset, int length) {
        if ((bytes.length < (offset + length)) || length > 4 || 0 >= length) {
            return -1;
        }

        byte[] lengthFieldBytes = new byte[length];
        System.arraycopy(bytes, offset, lengthFieldBytes, 0, length);

        return byteArrayToInt(lengthFieldBytes);
    }

    int byteArrayToInt(byte[] bytes) {

        if (bytes.length > 4 || 0 == bytes.length) {
            return 0;
        }

        byte[] intBytes = new byte[4];
        int index = intBytes.length - 1;
        for (int pos = bytes.length - 1; pos >= 0; pos--, index--) {
            intBytes[index] = bytes[pos];
        }

        int val0 = (intBytes[0] & 0xFF) << 24;
        int val1 = (intBytes[1] & 0xFF) << 16;
        int val2 = (intBytes[2] & 0xFF) << 8;
        int val3 = (intBytes[3] & 0xFF) << 0;

        return (val0 + val1 + val2 + val3);
    }
}
