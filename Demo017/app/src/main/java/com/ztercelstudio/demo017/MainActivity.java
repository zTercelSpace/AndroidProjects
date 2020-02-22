package com.ztercelstudio.demo017;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ztercelstudio.vehicle.IEventListener;
import com.ztercelstudio.vehicle.IVehicleService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private IVehicleService mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setAction("com.ztercelstudio.action.VEHICLE");
        intent.setPackage("com.ztercelstudio.demo018");
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private class EventHandler extends IEventListener.Stub {

        @Override
        public void handle(int event, byte[] data) throws RemoteException {
            //String.format("0x%0x", data[0]);
            Log.d("zTercel", "handle received : " + event);
            final int cmdID = event;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "event: " + cmdID, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private static final String TAG = "Demo017";
    private EventHandler mEventHandler = new EventHandler();
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IVehicleService.Stub.asInterface(service);
            TextView textView = findViewById(R.id.textView);
            textView.setText("hello");

            if (null != mService) {
                byte[] data = {0x01, 0x02, 0x03, 0x0A};
                try {
                    mService.registerListener(TAG, mEventHandler);

                    mService.sendData(data);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mService) {
            try {
                mService.unregisterListener(TAG, mEventHandler);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
