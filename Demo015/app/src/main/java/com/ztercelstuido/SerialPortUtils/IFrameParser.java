package com.ztercelstuido.SerialPortUtils;

public interface IFrameParser {
    void parse(byte[] frame);

    void addHandler(IMessageHandler handler);
    void removeHandler(IMessageHandler handler);
    void messageNotify(final int id, byte[] message);
}
