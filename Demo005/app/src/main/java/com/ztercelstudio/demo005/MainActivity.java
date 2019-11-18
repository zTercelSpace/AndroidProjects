package com.ztercelstudio.demo005;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    StandardReceiver mStandardReceiver;// = new StandardReceiver(this);
    OrderReceiver mOrderReceiver    = new OrderReceiver();
    LocalReceiver mLocalReceiver;
    LocalBroadcastManager mLocalManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mStandardReceiver.registerBroadcast(StandardReceiver.STARDAND_ACTION);
        mStandardReceiver = new StandardReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(StandardReceiver.STARDAND_ACTION);
        registerReceiver(mStandardReceiver, filter);

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
                break;
            }
            case R.id.btLocal: {
                mLocalManager.sendBroadcast(new Intent(LocalReceiver.LOCAL_ACTION));
                break;
            }
        }

    }
}
