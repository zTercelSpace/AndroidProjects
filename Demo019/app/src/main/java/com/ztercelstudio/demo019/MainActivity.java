package com.ztercelstudio.demo019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ztercelstudio.vehicle.IEventListener;
import com.ztercelstudio.vehicle.IVehicleService;

import java.util.List;
import java.util.jar.Manifest;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private IVehicleService mService = null;
    private LocationHandler mLocationHandler = new LocationHandler();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setAction("com.ztercelstudio.action.VEHICLE");
        intent.setPackage("com.ztercelstudio.demo018");
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void showLocation(Location location) {
        if (null == location) return;

        String pattern = "latitude: %f longitude: %f";
        String content = String.format(pattern, location.getLatitude(), location.getLongitude());
        Log.d("zTercel", content);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (100 == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        || !ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this, "请前往设置界面打开权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        String provider = null;
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;

            final String LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION;
            final String[] PERMISSION_LIST = new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            };

           if (ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSION_LIST, 100);
            }
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        locationManager.requestLocationUpdates(provider, 1000, 10, mLocationHandler);
        new LocationUpdateThread(provider, locationManager).start();
    }

    private class LocationUpdateThread extends Thread {
        private String mProvider;
        private LocationManager mLocationManager;

        public LocationUpdateThread(String provider, LocationManager locationManager) {
            mProvider           = provider;
            mLocationManager    = locationManager;
        }

        @Override
        public void run() {
            while (true) {
                if (null != mProvider) {
                    Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    showLocation(location);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class LocationHandler implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private class EventHandler extends IEventListener.Stub {

        @Override
        public void handle(int event, byte[] data) throws RemoteException {
            //String.format("0x%0x", data[0]);
            Log.d("zTercel", "handle Received: " + event);
        }
    }

    private static final String TAG = "Demo019";
    private EventHandler mEventHandler = new EventHandler();
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IVehicleService.Stub.asInterface(service);

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
