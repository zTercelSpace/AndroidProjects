package com.ztercelstuido.SerialPortUtils;

public interface IMessageHandler {
    void handleMessage(int ID, byte[] message, int length);
}
