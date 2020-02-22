package com.ztercelstuido.SerialPortUtils;

public interface IFrameParser {
    void parse(byte[] frame, int length);

    void addHandler(IMessageHandler handler);
    void removeHandler(IMessageHandler handler);
    void messageNotify(int id, byte[] message, int length);
}
