package com.ztercelstuido.FrameHandler;

import com.ztercelstuido.SerialPortUtils.IFrameParser;
import com.ztercelstuido.SerialPortUtils.IMessageHandler;
import com.ztercelstuido.SmartClassUtils.SmartClassUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import android.util.Log;

import android.os.Handler;

public class VehicleFrameParser implements IFrameParser {
    private final int FRAME_MIN_SIZE = 6;

    private List<IMessageHandler> mHandlers = new ArrayList<IMessageHandler>();


    private class DeliveryHandler extends Handler {


    };

    public VehicleFrameParser() {

    }

    public VehicleFrameParser(IMessageHandler handler) {
        addHandler(handler);
    }

    @Override
    public void addHandler(IMessageHandler handler) {
        mHandlers.add(handler);
    }

    @Override
    public void removeHandler(IMessageHandler messageHandler) {
        for (IMessageHandler handler : mHandlers) {
            if (handler == messageHandler) {
                mHandlers.remove(handler);
                break;
            }
        }
    }

    @Override
    public void messageNotify(int id, byte[] message, int length) {
        for (IMessageHandler messageHandler : mHandlers) {
            if (null != messageHandler) {
                messageHandler.handleMessage(id, message, length);
            }
        }
    }

    @Override
    public void parse(byte[] data, int dataSize) {
        //==========================================================================================
        // Frame: Head(2Bytes) + Length Field(2Bytes) + Channel(1Byte) + CMD(1Byte) + Data(nBytes) + CheckSum(1Byte)
        // CheckSum: BYTE(LENGTH FIELD + Channel(1Byte) + CMD + DATA) ^ 0XFF + 1
        //==========================================================================================
        //Log.d("zTercel", "frames: " + SmartClassUtils.BytePrinter.byteArrayToHex(data, dataSize));

        ByteBuffer frame = ByteBuffer.wrap(data);
        if (frame.limit() > FRAME_MIN_SIZE && checkSum(frame)) {
            frame.position(2);                          // Head Field
            int messageSize = frame.getShort();         // Length Field
            byte messageID  = frame.get();              // CMD
            byte[] message  = new byte[messageSize - 2];// Data (2 = CMD(1Byte) + CheckSum(1Byte))
            frame.get(message);



            messageNotify(messageID, message, message.length);
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



