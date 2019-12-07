package com.ztercelstudio.demo006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ztercelstudio.aidl.IDataManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private IDataManager mDataManager = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDataManager = IDataManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDataManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_linear: {
                Intent intent = new Intent();
                intent.setAction("com.ztercelstudio.demo008.DATA_ACTION");
                Intent explicitIntent = new Intent(createExplicitFromImplicitIntent(this, intent));
                bindService(explicitIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                break;
            }
            case R.id.btn_contraint: {
                try {
                    if (mDataManager == null) return;
                    String command = mDataManager.getCommand();
                    Toast.makeText(this, command, Toast.LENGTH_LONG).show();
                } catch (RemoteException e) {
                    System.out.println(e);
                }
            }
        }
        Button button = (Button)v;
        Toast.makeText(this, button.getText(), Toast.LENGTH_SHORT).show();
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
