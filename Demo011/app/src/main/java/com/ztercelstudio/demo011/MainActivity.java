package com.ztercelstudio.demo011;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("operator_lib");
    }

    public native String stringFromJNI();
    public native int add(int left, int right);
    public native int sub(int left, int right);
    public native void setName(String name);
    public native String getName();
    public native void setAge(int age);
    public native int getAge();
    ///////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.sample_text);

        setName("ztercel@163.com");
        setAge(23);

        StringBuilder sb = new StringBuilder();
        sb.append("\nstringFromJNI => " + stringFromJNI());
        sb.append("\nadd(left + right) => " + add(10, 100));
        sb.append("\nsub(left + right) => " + sub(100, 10));
        sb.append("\ngetName() => " + getName());
        sb.append("\ngetAge() => " + getAge());
        tv.setText(sb.toString());
    }

}
