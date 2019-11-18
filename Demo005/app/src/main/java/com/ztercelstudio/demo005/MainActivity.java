package com.ztercelstudio.demo005;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

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
        recycleFilter.setPriority(1000);   // 优先级高于 orderReceiver, 先收到过广播
        registerReceiver(mRecycleReceiver, recycleFilter);

        mLocalReceiver = new LocalReceiver();
        mLocalManager = LocalBroadcastManager.getInstance(this);
        IntentFilter localFilter = new IntentFilter();
        localFilter.addAction(LocalReceiver.LOCAL_ACTION);
        mLocalManager.registerReceiver(mLocalReceiver, localFilter);
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
