package com.ztercelstuido.SerialPortUtils;

public interface IFrameDecoder {
    class FrameInfo {
        public int     headPos;    // 帧的起始位置
        public int     tailPos;    // 帧的结束位置
        public int     length;     // 帧的长度
    }

    boolean decode(byte[] bytes, int dataBytes, FrameInfo frameInfo);
}


