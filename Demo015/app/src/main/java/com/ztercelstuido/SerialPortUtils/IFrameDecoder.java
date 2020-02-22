package com.ztercelstuido.SerialPortUtils;

import java.nio.ByteBuffer;

public interface IFrameDecoder {
    final class FrameInfo {
        public int     headPos;    // 帧的起始位置
        public int     tailPos;    // 帧的结束位置
        public int     length;     // 帧的长度
    }

    boolean decode(ByteBuffer data, FrameInfo frameInfo);
}


