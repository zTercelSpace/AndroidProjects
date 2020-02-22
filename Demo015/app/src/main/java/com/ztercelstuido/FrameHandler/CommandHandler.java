package com.ztercelstuido.FrameHandler;

import com.ztercelstuido.SerialPortUtils.IMessageHandler;
import com.ztercelstuido.SmartClassUtils.SmartClassUtils;

import android.util.Log;

import java.nio.ByteBuffer;


public class CommandHandler implements IMessageHandler {

    boolean gitBit(int num, int index) {
        return (0 != (num & (1 << index)));
    }
    
    int byte2Int(byte num) {
        return (num & 0XFF);
    }

    @Override
    public void handleMessage(int cmdID, byte[] cmdData, int length) {
        ByteBuffer data = ByteBuffer.wrap(cmdData, 0, length);

        if ((byte)0xD0 == cmdID && data.hasRemaining()) {
            int subCmdID = data.get();
            switch (subCmdID) {
                case (byte)0x56: { // 空调状态
                    int status  = byte2Int(data.get());   // '1' = open  '0' = close;
                    int mode    = byte2Int(data.get());   // bit 0: 自动 1: 左右温度同步 2: 内外循环 3.
                    int acmode  = byte2Int(data.get());
                    int wind    = byte2Int(data.get());
                    int frontLTmp = byte2Int(data.get());
                    int fronteRTmp = byte2Int(data.get());
                    int backTmp = byte2Int(data.get());
                    int hotSeat = byte2Int(data.get());
                    int windOrg = byte2Int(data.get());
                    int windWgt = byte2Int(data.get());
                    Log.d("zTercel", "airplane: " + status);
                    break;
                }
                case (byte)0x43: { // 车辆信息
                    int status = data.getShort(); // 5C
                   // Log.d("zTercel", "carInfo: " + status);
                    break;
                }
                case (byte)0x4C: { // 雷达
                    int action = data.get();
                    switch (action) {
                        case (byte)0x41: { // 雷达状态信息
                            int bleft   = byte2Int(data.get());
                            int blmid   = byte2Int(data.get());
                            int bmright = byte2Int(data.get());
                            int bright  = byte2Int(data.get());
                            int aleft   = byte2Int(data.get());
                            int almid   = byte2Int(data.get());
                            int amright = byte2Int(data.get());
                            int aright  = byte2Int(data.get());
                            break;
                        }
                        case (byte)0x42: { // 雷达声音播放
                            int interval = byte2Int(data.get());
                            Log.d("zTercel", "" + interval);
                            break;
                        }
                        case (byte)0x43: { // 雷达报警音开关状态
                            int status = byte2Int(data.get());
                            Log.d("zTercel", "" + status);
                            break;
                        }
                        case (byte)0x44: { // 雷达预览状态
                            int status = byte2Int(data.get());
                            Log.d("zTercel", "" + status);
                            break;
                        }
                    }
                    Log.d("zTercel", "radar: " + action);
                    break;
                }
                case (byte)0x4B: { // 按键
                    int action      = byte2Int(data.get());
                    int keyType     = byte2Int(data.get());
                    int keyValue    = byte2Int(data.get());
                    int keyStatus   = byte2Int(data.get());

                    String type = "";
                    switch (keyType) {
                        case (byte)0x00: type = "keyboard"; break;
                        case (byte)0x01: type = "rotating"; break;
                    }

                    String status = "";
                    switch (keyStatus) {
                        case (byte)0x31: status = "keyDown";      break;
                        case (byte)0x30: status = "keyUp";        break;
                    }

                    String info = String.format("type: %s value: 0X%x status: %s", type, keyValue, status);
                    Log.d("zTercel", "KeyInfo: " + info);
                    break;
                }
                case (byte)0x11: { // 方向盘转角
                    int turnRange = data.getShort(); // 0 left, 540 middle, 1080 right
                    Log.d("zTercel", "" + turnRange);
                    Log.d("zTercel", "direct: " + turnRange);
                    break;
                }
                default: {
                    Log.d("zTercel", "events: " + SmartClassUtils.BytePrinter.byteArrayToHex(cmdData, length));
                }

            }
        }
    }
}