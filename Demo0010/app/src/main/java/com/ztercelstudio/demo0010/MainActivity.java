package com.ztercelstudio.demo0010;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            if (null != packageInfo && null != packageInfo.applicationInfo) {
                TextView textView = (TextView)findViewById(R.id.textView);
                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                imageView.setImageDrawable(packageInfo.applicationInfo.loadIcon(packageManager));

                ApplicationInfo appInfo = packageInfo.applicationInfo;

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\nlabel: " + appInfo.loadLabel(packageManager));
                stringBuilder.append("\nuid: " + appInfo.uid);
                stringBuilder.append("\nname: " + appInfo.name);
                stringBuilder.append("\nversionCode: " + packageInfo.versionCode);
                stringBuilder.append("\nversionCode: " + packageInfo.versionName);
                stringBuilder.append("\ngids: " + packageInfo.gids);
                textView.setText(stringBuilder.toString());

                Log.d("TAG", packageInfo.applicationInfo.toString());
            }

            Log.d("TAG", "stop point");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
