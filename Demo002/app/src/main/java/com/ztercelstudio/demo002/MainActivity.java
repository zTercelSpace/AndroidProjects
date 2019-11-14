package com.ztercelstudio.demo002;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String TAG    = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textView1: {
                Intent intent = new Intent(this, SecondActivity.class);
                intent.putExtra("content", "back button will return me");
                startActivity(intent);
                break;
            }
            case R.id.textView2:
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("content", "click the button will return with result");
                startActivityForResult(intent, 99);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, requestCode + "  " + resultCode);
        if (100 == resultCode) {
            String name = data.getStringExtra("name");
            Integer age = data.getIntExtra("age", 1);
            boolean sex = data.getBooleanExtra("sex", false);
            String info = String.format("data => name: %s age: %d  sex: %b", name, age, sex);
            setTitle(info);
            Log.d(TAG, info);
        }
    }
}
