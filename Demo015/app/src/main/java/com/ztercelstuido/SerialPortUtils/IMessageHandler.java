package com.ztercelstuido.SerialPortUtils;

public interface IMessageHandler {
    void handle(final int ID, byte[] message);
}
