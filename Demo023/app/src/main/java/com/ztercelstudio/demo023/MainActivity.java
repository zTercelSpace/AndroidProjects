package com.ztercelstudio.demo023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.util.Log;
import android.os.Bundle;
import android.os.Message;
import android.content.Context;
import android.service.vehicle.VehicleMessageManager;
import android.service.vehicle.VehicleMessageListener;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.acl.Permission;

import android.os.Build;

public class MainActivity extends AppCompatActivity {
    public static final byte[] FRAME_HEAD   = {(byte)0xAA, (byte)0x55};
    private VehicleMessageManager mVehicleMessageManager    = null;
    private VehicleMessageHandler mVehicleMessageHandler    = null;

    static final int REQUEST_PHONE_STATE = 1;
    private String serialNumber;
    public String getSerialNumber() {
        String serial = null;
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // 9.0 +
             //   serial = Build.getSerial();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) { // 8.0 +
                serial = Build.SERIAL;
            } else { // 8.0 -
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return serial;
    }

    public static String getDeviceSN2() {

        String serial = null;

        try {



            Class<?> c = Class.forName("android.os.Build");
            if (null != c) {
                //Method method = c.getMethod("getSerial", String.class);
                Method get = c.getMethod("getSerial");
                serial = (String) get.invoke(null);
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

        return serial;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("zTercel" , "sn " + androidId);
        mVehicleMessageManager = (VehicleMessageManager)getSystemService(Context.VEHICLE_MESSAGE_SERVICE);
        if (null != mVehicleMessageManager) {
            mVehicleMessageHandler = new VehicleMessageHandler();
            mVehicleMessageManager.registerListener(mVehicleMessageHandler);
            Message message = new Message();
            message.arg1 = 0x40;
            message.arg2 = 0x56;
            message.getData().putByteArray("data", new byte[]{0x01, 0x02});
            mVehicleMessageManager.sendMessage(message);
           // mVehicleMessageManager.unregisterListener(mVehicleMessageHandler);
            Log.d("zTercel", "hello vehicle_message_service ");
        }
    }

    private class VehicleMessageHandler implements VehicleMessageListener {

        @Override
        public void onHandleMessage(Message message) {

            byte[] data = message.getData().getByteArray("data");
            Log.d("zTercel", "Vehicle Message Handler: " + message.what + "  " + (byte)message.arg1 + " " + message.arg2 + " " + data.length);
            switch (message.arg2) {
                case 0x5C: {
                    int speed = message.getData().getInt("rotate_speed");
                    Log.d("zTercel", "Vehicle Message Handler: " + " rotate_speed: " + speed);
                    break;
                }
                case 0x72: {
                    int remaining = message.getData().getInt("remaining");
                    Log.d("zTercel", "Vehicle Message Handler: " + " remaining: " + remaining);
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mVehicleMessageManager) {
            mVehicleMessageManager.unregisterListener(mVehicleMessageHandler);
        }
    }
    /*

        int cmdID = 0x40;
        int subCmdID = 0x43;
        byte[] data = new byte[]{0x46};

        ByteBuffer frame = ByteBuffer.allocate(512);
        frame.put((byte)0xAA).put((byte)0x55);
        frame.putShort((short)(data.length + 3));
        frame.put((byte)cmdID).put((byte)subCmdID);
        frame.put(data);

        byte checkSum = 0;
        for (int ii = 2; ii < frame.position(); ii++) {
            checkSum += (byte)(frame.get(ii) & 0xFF);
        }
        checkSum ^= 0xff;
        checkSum += 1;
        frame.put(checkSum);

        frame.flip();
        byte[] data1 = new byte[frame.limit()];
        frame.get(data1);
        Log.d("ztercel", data1.toString());
     */


}
