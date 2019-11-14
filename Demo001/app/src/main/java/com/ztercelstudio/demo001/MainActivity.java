package com.ztercelstudio.demo001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String TAG = "zTercel";

    private class ClickOnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.textView) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new ClickOnListener());
        textView.setText("Click me - observe activity life period");

        Log.d(TAG, "MainActivity::onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "MainActivity::onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(TAG, "MainActivity::onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "MainActivity::onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "MainActivity::onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "MainActivity::onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "MainActivity::onDestroy");
    }
}
