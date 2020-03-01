package com.ztercelstudio.demo023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.service.vehicle.VehicleMessageManager;
import android.service.vehicle.VehicleMessageListener;

public class MainActivity extends AppCompatActivity {

    private VehicleMessageManager mVehicleMessageManager    = null;
    private VehicleMessageHandler mVehicleMessageHandler    = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVehicleMessageManager = (VehicleMessageManager)getSystemService(Context.VEHICLE_MESSAGE_SERVICE);
        if (null != mVehicleMessageManager) {
            mVehicleMessageHandler = new VehicleMessageHandler();
            mVehicleMessageManager.registerListener(mVehicleMessageHandler);
            mVehicleMessageManager.sendMessage(new byte[]{0x01, 0x02}, 2);
            mVehicleMessageManager.unregisterListener(mVehicleMessageHandler);
        }
    }

    private class VehicleMessageHandler implements VehicleMessageListener {

        @Override
        public void onHandleMessage(int id, byte[] data, int dataSize) {

        }
    }
}
