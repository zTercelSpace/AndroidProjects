package com.ztercelstudio.demo020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private class Event {
        public int      id;
        public String   message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 0;
                while (time++ < 100) {
                    Log.d("zTercel", "fuck......" + time);
                    EventBus.getDefault().post("我射了" + time);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister( this );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hello (String event){
        Toast.makeText( this , event , Toast.LENGTH_SHORT).show();
    }
}
