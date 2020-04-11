package com.ztercelstudio.demo024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.andrich.AUDIO_FOCUS_ACTION");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int focus = intent.getIntExtra("focus", 1000);
                String action = (0 == focus ? "loss " : "fetch");
                String packageName = intent.getStringExtra("package");
                Log.d("zTercel", "action: " + action + " app name: " + packageName);


            }
        }, filter);
       // addWindow();
    }

    private View layout;
    private Button mButton;
    private LayoutParams mLayoutParams;
    private WindowManager mWindowManager;
    private LayoutInflater mLayoutInflater;

    private void addWindow() {
        mButton = new Button(this);
        mButton.setText("在基材");
        // 配置window的参数
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_APPLICATION_OVERLAY,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.RGBA_8888);
        mLayoutParams.gravity = Gravity.CENTER;

        // 获得WindowManager
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (mWindowManager != null) {
            // 通过WindowManager添加Window
            mLayoutInflater = LayoutInflater.from(this);
            layout = (View)mLayoutInflater.inflate(R.layout.overlay_layout, null);
            layout.findViewById(R.id.textView).setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(MainActivity.this, "Read the fucking source code.  ~_~", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
            mWindowManager.addView(layout, mLayoutParams);
        }
    }
}
