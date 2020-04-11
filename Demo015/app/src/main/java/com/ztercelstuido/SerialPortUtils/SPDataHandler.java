package com.ztercelstuido.SerialPortUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class SPDataHandler {

    public interface IDataHandler {
        void onDataReceived(byte[] data, int dataSize);
    }

    // 此线程用于读取串口数据并分发
    private class SPReadThread extends Thread {
        private long mSleepTime     = 100;
        private boolean mStopped    = false;

       int index = 0;
        int getTestData(byte[] data) {
            byte[][] testData = {
                {(byte)0xAA, (byte)0x55, (byte)0x00, (byte)0x06, (byte)0xD0, (byte)0x43, (byte)0x5C, (byte)0x0A, (byte)0xEE, (byte)0x93,
                 (byte)0xAA, (byte)0x55, (byte)0x00, (byte)0x05, (byte)0xD0, (byte)0x43, (byte)0x72, (byte)0x00, (byte)0x76}
            };

            int dataSize = 0;

            if (index < testData.length) {
                dataSize = testData[index].length;
                System.arraycopy(testData[index], 0, data, 0, dataSize);
            } else {
                index = 0;
            }
            return dataSize;
        }

        @Override
        public void run() {
            Log.d("SPDataHandler", "The thread start to read data.");

            byte[] data = new byte[1024];
            while (!mStopped && !isInterrupted()) {
                try {
                    if (null == mFileInputStream) throw new Exception();
                    int dataSize = mFileInputStream.read(data);

                    Log.d("zTercel", "size: " + dataSize);
                    if (0 < dataSize) {
                        // 数据分发
                        for (IDataHandler dataHandler : mDataHandlers) {
                            if (null != dataHandler) {
                                dataHandler.onDataReceived(data, dataSize);
                            }
                        }
                    }
                    Thread.sleep(mSleepTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.d("SPDataHandler", "The thread has been exited!");
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
    private static SPDataHandler mDataReceiver = new SPDataHandler();

    private SerialPortHelper mSerialPort        = null;
    private SPReadThread mSPReadThread          = null;
    private FileInputStream mFileInputStream    = null;
    private FileOutputStream mFileOutputStream  = null;
    private List<IDataHandler> mDataHandlers  = new ArrayList<IDataHandler>();

    //////////////////////////////////////////////////////////////////////
    private SPDataHandler() {}

    public static SPDataHandler getInstance() {
        return  mDataReceiver;
    }

    public void openSerialPort(String serialPort, int baudRate) {
        if (null != mSerialPort)  {
            Log.d("SPDataHandler", "The serial port has been opened.");
            return;
        }

        try {
            mSerialPort         = new SerialPortHelper(serialPort, baudRate);
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

    public void addDataHandler(IDataHandler dataHandler) {
        if (null != dataHandler) {
            mDataHandlers.add(dataHandler);
        }
    }

    public void removeDataHandler(IDataHandler dataHandler) {
        if (null != dataHandler) {
            mDataHandlers.remove(dataHandler);
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