package com.ztercelstudio.demo025;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationApps();
        //getPacageName();
    }

    private void getApplicationApps() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> appInfos = packageManager.getInstalledApplications(0);
        for (ApplicationInfo appInfo : appInfos) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\tapplication: " + packageManager.getApplicationLabel(appInfo));
            stringBuilder.append("\tpackageName: " + appInfo.packageName);

            Log.d("zTercel",  stringBuilder.toString());
        }
    }

    private void getPacageName() {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appList = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo app : appList) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(app.activityInfo.packageName, 0);
                stringBuilder.append("\tapplication: " + packageManager.getApplicationLabel(appInfo));
                stringBuilder.append("\tprocessName: " + app.activityInfo.processName);
                stringBuilder.append("\tpackageName: " + app.activityInfo.packageName);
                stringBuilder.append("\tname: " + app.activityInfo.name);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Log.d("zTercel",  stringBuilder.toString());
        }
    }

}
