package com.ztercelstuido.demo015;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.ztercelstuido.SerialPortUtils.SPHelper;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SPHelper mSPHelper = SPHelper.getInstance();

    private SPHelper.IDataReceiver mDataReceiver = new SPHelper.IDataReceiver() {
        @Override
        public void onReceive(byte[] data) {
            Log.d("zTercel", "SPHelper.IDataReceiver::onReceive dataSize = " + data.length);
        }
    };

    class EventEntity {
        byte      mID;
        byte[]    mData;
    }

    interface IFrameDecoder {
        class FrameInfo {
            int     headPos;    // 帧的起始位置
            int     tailPos;    // 帧的结束位置
            int     length;     // 帧的长度
        }

        boolean decode(byte[] bytes, int dataBytes, FrameInfo frameInfo);
    }

    class FixedHeadLengthFrame {
        private byte[]  mHead;
        private int     mLengthFieldOffset;
        private int     mLengthFieldBytes;

        FixedHeadLengthFrame(byte[] head, int lengthFieldOffset, int lengthFieldBytes) {
            if (head.length > 0) {
                mHead = new byte[head.length];
                System.arraycopy(head, 0, mHead, 0, head.length);
            }

            assert (lengthFieldOffset <= 0 || mLengthFieldBytes <= 0);

            mLengthFieldOffset   = lengthFieldOffset;
            mLengthFieldBytes    = lengthFieldBytes;
        }
    }

    class FixedHeadLengthDecoder implements IFrameDecoder {

        FixedHeadLengthFrame mFrameDes;

        FixedHeadLengthDecoder(FixedHeadLengthFrame frameDes) {
            mFrameDes = frameDes;
        }

        @Override
        public boolean decode(byte[] data, int dataBytes, FrameInfo frameInfo) {
            //-----------------------------------------------------------------
            // frame:　head flag + lengthfield + data
            //-----------------------------------------------------------------
            boolean isDecoded = false;
            if (dataBytes > (mFrameDes.mLengthFieldOffset + mFrameDes.mLengthFieldBytes)) {
                int frameHeadPos    = findFrameHeadPos(data, mFrameDes.mHead);
                int dataFiledBytes  = byteArrayToInt(data, frameHeadPos + mFrameDes.mLengthFieldOffset,mFrameDes. mLengthFieldBytes);
                if ((-1 != frameHeadPos) && (0 < dataFiledBytes)) {
                    int frameTailPos = frameHeadPos + mFrameDes.mLengthFieldOffset + mFrameDes.mLengthFieldBytes + dataFiledBytes;
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

    interface IFrameParser {
        void parse(ByteBuffer frame);
        void addHandler(IHandler handler);
        void removeHandler(IHandler handler);
    }

    interface IHandler {
        void handle(final int cmdID, byte[] cmdData);
    }

    class CommandHandler implements IHandler {

        @Override
        public void handle(int cmdID, byte[] cmdData) {
            switch (cmdID) {
                case 0x50: {
                    Log.d("zTercel", "ok --- 0x50");
                    break;
                }

            }
        }
    }

    class VehicleFrameParser implements IFrameParser {

        private List<IHandler> mHandlers = new ArrayList<IHandler>();

        public void addHandler(IHandler handler) {
            mHandlers.add(handler);
        }

        public void removeHandler(IHandler handler) {
            for (IHandler object : mHandlers) {
                if (object == handler) {
                    mHandlers.remove(object);
                }
            }
        }

        public void notify(final int cmdID, byte[] cmdData) {
            for (IHandler handler : mHandlers) {
                if (null != handler) {
                    handler.handle(cmdID, cmdData);
                }
            }
        }

        public void parse(ByteBuffer frame) {
            if (frame.limit() > 6 && checkSum(frame)) {
                frame.position(2);
                int frameLength = frame.getShort();
                byte command    = frame.get();
                byte[] data     = new byte[frameLength - 2];
                frame.get(data);

                notify(command, data);
            }
        }

        boolean checkSum(ByteBuffer frame) {
            frame.position(2);

            byte sum = 0;
            byte[] bytes = frame.array();
            for (int ii = 2; ii < bytes.length - 1; ii++) {
                sum += (byte)(bytes[ii] & 0xFF);
            }
            sum ^= 0xff;
            sum += 1;

            int checkSum = (byte)(bytes[bytes.length - 1] & 0xFF);
            return (sum == checkSum);
        }
    }

    class DataHandler {

        private IFrameParser    mFrameParser;
        private IFrameDecoder  mFrameDecorder;
        private ByteBuffer      mByteBuffer;

        DataHandler(IFrameDecoder frameDecorder, IFrameParser frameParser) {
            mFrameParser    = frameParser;
            mFrameDecorder  = frameDecorder;

            mByteBuffer     = ByteBuffer.allocate(1024);
        }

        void onHandle(byte[] data) {
            mByteBuffer.put(data);

            IFrameDecoder.FrameInfo frameInfo = new IFrameDecoder.FrameInfo();

            mByteBuffer.flip();
            while (mFrameDecorder.decode(mByteBuffer.array(), mByteBuffer.limit(), frameInfo)) {
                mByteBuffer.position(frameInfo.headPos);
                byte[] frame = new byte[frameInfo.length];
                mByteBuffer.get(frame);

                if (null != mFrameParser) {
                    mFrameParser.parse(ByteBuffer.wrap(frame));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        byte[] HEAD_TAG = {(byte)0xAA, (byte)0x55};

        FixedHeadLengthFrame frameDes = new FixedHeadLengthFrame(HEAD_TAG, 2, 2);
        IFrameDecoder   decoder = new FixedHeadLengthDecoder(frameDes);
        IFrameParser    parser  = new VehicleFrameParser();
        IHandler        handler = new CommandHandler();

        DataHandler dataHandler = new DataHandler(decoder, parser);
        parser.addHandler(handler);

        byte[][] data = {
                {(byte)0x03, (byte)0x50, (byte)0xAA, (byte)0x55, (byte)0x00, (byte)0x03, (byte)0x50, (byte)0x01, (byte)0xAC}
        };
        for (int ii = 0; ii < data.length; ii++) {
            dataHandler.onHandle(data[ii]);
        }
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
