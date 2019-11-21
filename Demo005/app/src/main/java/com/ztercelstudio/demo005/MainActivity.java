package com.ztercelstudio.demo005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    StandardReceiver mStandardReceiver;
    OrderReceiver mOrderReceiver;
    LocalReceiver mLocalReceiver;
    RecycleReceiver mRecycleReceiver;
    LocalBroadcastManager mLocalManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStandardReceiver = new StandardReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(StandardReceiver.STARDAND_ACTION);
        registerReceiver(mStandardReceiver, filter);

        mOrderReceiver = new OrderReceiver();
        IntentFilter orderFilter = new IntentFilter();
        orderFilter.addAction(OrderReceiver.ORDER_ACTION);
        orderFilter.setPriority(10);
        registerReceiver(mOrderReceiver, orderFilter);

        mRecycleReceiver = new RecycleReceiver();
        IntentFilter recycleFilter = new IntentFilter();
        recycleFilter.addAction(OrderReceiver.ORDER_ACTION);
        recycleFilter.addAction(StandardReceiver.STARDAND_ACTION);
        // recycleFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        recycleFilter.setPriority(1000);   // 优先级高于 orderReceiver, 先收到过广播
        registerReceiver(mRecycleReceiver, recycleFilter);

        mLocalReceiver = new LocalReceiver();
        mLocalManager = LocalBroadcastManager.getInstance(this);
        IntentFilter localFilter = new IntentFilter();
        localFilter.addAction(LocalReceiver.LOCAL_ACTION);
        mLocalManager.registerReceiver(mLocalReceiver, localFilter);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addCapability(NetworkCapabilities.TRANSPORT_CELLULAR);
            builder.addCapability(NetworkCapabilities.TRANSPORT_WIFI);
            connectivityManager.registerNetworkCallback(builder.build(), new NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);

                    Toast.makeText(MainActivity.this, "onAvailable", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);

                    String info = "onAvailable " + networkCapabilities.getSignalStrength();
                    Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mStandardReceiver);
        unregisterReceiver(mOrderReceiver);
        unregisterReceiver(mRecycleReceiver);
        mLocalManager.unregisterReceiver(mLocalReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btStardand: {
                sendBroadcast(new Intent(StandardReceiver.STARDAND_ACTION));
                break;
            }
            case R.id.btOrder: {
                sendOrderedBroadcast(new Intent(OrderReceiver.ORDER_ACTION), null);
                break;
            }
            case R.id.btLocal: {
                mLocalManager.sendBroadcast(new Intent(LocalReceiver.LOCAL_ACTION));
                break;
            }
        }

    }
}
