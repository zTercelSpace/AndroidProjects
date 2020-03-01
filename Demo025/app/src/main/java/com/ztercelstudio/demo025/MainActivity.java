package com.ztercelstudio.demo025;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.service.vehicle.VehicleMessageManager;
import android.service.vehicle.VehicleMessageListener;

public class MainActivity extends AppCompatActivity {

    private VehicleMessageManager mVehicleMessageManager = null;
    private OnVehicleMessageListener mVehicleMessageListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVehicleMessageManager = (VehicleMessageManager)getSystemService(Context.VEHICLE_MESSAGE_SERVICE);


        if (null != mVehicleMessageManager) {
            mVehicleMessageListener = new OnVehicleMessageListener();
            mVehicleMessageManager.registerListener(mVehicleMessageListener);
            mVehicleMessageManager.unregisterListener(mVehicleMessageListener);
            mVehicleMessageManager.sendMessage(new byte[]{0x01, 0x02}, 2);
        }
    }

    private class OnVehicleMessageListener implements VehicleMessageListener {

        @Override
        public void onHandleMessage(int id, byte[] data, int dataSize) {

        }
    }
}
