package com.ztercelstuido.SerialPortUtils;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SPHelper {

    public interface IDataReceiver {
        void onReceive(byte[] data);
    }

    // 此线程用于读取串口数据并分发
    private class SPReadThread extends Thread {
        private long mSleepTime     = 100;
        private boolean mStopped    = false;

        @Override
        public void run() {
            Log.d("SPHelper", "The thread start to read data.");

            byte[] data = new byte[1024];
            while (!mStopped && !isInterrupted()) {
                try {
                    if (null == mFileInputStream) throw new Exception();
                    int dataSize = mFileInputStream.read(data);
                    if (0 < dataSize) {
                        // 数据分发
                        for (IDataReceiver dataReceiver : mDataReceivers) {
                            if (null != dataReceiver) {
                                dataReceiver.onReceive(data);
                            }
                        }
                    } else {
                        Log.d("SPHelper", "wait data.....");
                    }
                    Thread.sleep(mSleepTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

            Log.d("SPHelper", "The thread has been exited!");
        }

        public void stopThread() {
            mStopped = true;

            try {
                Thread.sleep(3 * mSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////
    // 内部变量
    private static SPHelper mSPHelper   = new SPHelper();
    private SerialPort mSerialPort      = null;
    private SPReadThread mSPReadThread  = null;
    private FileInputStream mFileInputStream    = null;
    private FileOutputStream mFileOutputStream  = null;
    private List<IDataReceiver> mDataReceivers  = new ArrayList<IDataReceiver>();

    //////////////////////////////////////////////////////////////////////
    private SPHelper() {}

    public static SPHelper getInstance() {
        return  mSPHelper;
    }

    public void openSerialPort(String serialPort, int baudRate) {
        if (null != mSerialPort)  {
            Log.d("SPHelper", "The serial port has been opened.");
            return;
        }

        try {
            mSerialPort         = new SerialPort(serialPort, baudRate);
            mFileInputStream    = mSerialPort.getFileIntputStream();
            mFileOutputStream   = mSerialPort.getFileOutputStream();

            mSPReadThread       = new SPReadThread();
            mSPReadThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendData(byte[] data) {
        boolean isSent = false;
        try {
            if (null != mFileOutputStream) {
                mFileOutputStream.write(data);
                isSent = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSent;
    }

    public void addDataReceiver(IDataReceiver dataReceiver) {
        if (null != dataReceiver) {
            mDataReceivers.add(dataReceiver);
        }
    }

    public void removeDataReceiver(IDataReceiver dataReceiver) {
        if (null != dataReceiver) {
            mDataReceivers.remove(dataReceiver);
        }
    }

    public void closeSerialPort() {
        if (null != mSPReadThread) {
            mSPReadThread.stopThread();
            mSPReadThread.interrupt();
            mSPReadThread = null;
        }

        if (null != mSerialPort) {
            mSerialPort.closeSerialPort();
            mSerialPort = null;
        }

        try {
            if (null != mFileOutputStream) {
                mFileOutputStream.close();
                mFileOutputStream = null;
            }

            if (null != mFileInputStream) {
                mFileInputStream.close();
                mFileInputStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
