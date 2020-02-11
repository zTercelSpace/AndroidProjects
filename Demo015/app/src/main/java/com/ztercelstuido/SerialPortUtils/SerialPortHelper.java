package com.ztercelstuido.SerialPortUtils;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerialPortHelper {

    private FileDescriptor      mFd;    // mFd变量名不得修改（变量名在so库中被引用）
    private FileInputStream     mFileInputStream    = null;
    private FileOutputStream    mFileOutputStream   = null;

    ////////////////////////////////////////////////////////////////////////////
    public SerialPortHelper(String serialPort, int baudRate) throws SecurityException, IOException {
        mFd = open(serialPort, baudRate);
        if (null == mFd) {
            throw new IOException();
        }

        mFileInputStream    = new FileInputStream(mFd);
        mFileOutputStream   = new FileOutputStream(mFd);
    }

    public FileInputStream getFileIntputStream() {
        return mFileInputStream;
    }

    public FileOutputStream getFileOutputStream() {
        return mFileOutputStream;
    }

    public void closeSerialPort() {
        if (null != mFd) {
            close();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Native methods
    static {
        System.loadLibrary("SerialPort");
    }

    private native FileDescriptor open(String serialPort, int baudRate);
    private native int close();
}
