package com.ztercelstudio.demo018;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ztercelstudio.vehicle.IVehicleService;
import com.ztercelstudio.vehicle.IEventListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VehicleService extends Service {

    private Binder mBinder = new Binder();
    private Map<String, IEventListener> mEventListeners = null;
    private Handler mEventDeliveryHandler = null;

    public VehicleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("zTercel", "demo018::vehicleservice - onCreate");

        mEventDeliveryHandler = new EventDeliveryHandler(this.getMainLooper());

        mEventListeners = new ConcurrentHashMap<String, IEventListener>();
        new AutoSendThread().start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder.asBinder();
    }

    private class EventDeliveryHandler extends Handler {
        public EventDeliveryHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (100 == msg.what) {
                Bundle data = msg.getData();
                int cmdID = data.getInt("cmdID");
                byte[] command = data.getByteArray("command");
                for (IEventListener listener : mEventListeners.values()) {
                    try {
                        if (null != listener) {
                            listener.handle(cmdID, command);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    private class AutoSendThread extends Thread {

        @Override
        public void run() {

            int time = 0;
            do {
                time++;

                Message message = Message.obtain();
                message.what = 100;
                Bundle data = new Bundle();
                data.putInt("cmdID", time);
                data.putByteArray("command", new byte[]{0x11, 0x22});
                message.setData(data);

                mEventDeliveryHandler.sendMessage(message);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (time < 1000);
        }
    }
    private class Binder extends IVehicleService.Stub {

        @Override
        public void registerListener(String tag, IEventListener listener) throws RemoteException {
            if (!tag.isEmpty() && null != listener) {
                if (!mEventListeners.containsKey(tag)) {
                    mEventListeners.put(tag, listener);
                }
                Log.d("zTercel", "demo018::vehicleservice::registerListener tag: " + tag);
            }
        }

        @Override
        public void unregisterListener(String tag, IEventListener listener) throws RemoteException {
            if (!tag.isEmpty() && null != listener) {
                if (!mEventListeners.containsKey(tag)) {
                    mEventListeners.remove(tag);
                }
            }
        }

        @Override
        public void sendData(byte[] data) throws RemoteException {

            Log.d("zTercel", "demo018::vehicleservice::sendData receive");
        }
    }
}