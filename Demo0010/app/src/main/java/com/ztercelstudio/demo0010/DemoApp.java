package com.ztercelstudio.demo0010;

import android.app.Application;
import android.util.Log;

public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("TAG", "DemoApp::onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.d("TAG", "DemoApp::onTerminate");
    }
}
