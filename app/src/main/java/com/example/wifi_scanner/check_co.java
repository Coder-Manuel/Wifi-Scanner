package com.example.wifi_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class check_co extends AppCompatActivity {
Button  wifib,hotspot;
    WifiManager wifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_co);
        showWritePermissionSettings();
        enableautostart();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()){
            //TODO: Code to execute if wifi is enabled.
            Intent i=new Intent(getApplicationContext(),wifi_activity.class);
            startActivity(i);
            finish();
        }
        wifib=findViewById(R.id.wifi);
        hotspot=findViewById(R.id.hotspot);
        wifib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),wifi_activity.class);
                startActivity(i);
                finish();
enableWifi();
            }
        });
        hotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // enable hotspot manually to avoid setting up configurations every time
                Method method = null;
                try {
                    method = wifi.getClass().getDeclaredMethod("getWifiApState");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                method.setAccessible(true);
                try {
                    int actualState = (Integer) method.invoke(wifi, (Object[]) null);
                    if(actualState!=13){
                     Toast.makeText(getApplicationContext(),"Enable Hotspot ",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent i=new Intent(getApplicationContext(),hotspot.class);
                        startActivity(i);
                        finish();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        });

        Method method = null;
        try {
            method = wifi.getClass().getDeclaredMethod("getWifiApState");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            int actualState = (Integer) method.invoke(wifi, (Object[]) null);
            if(actualState==13){
                Intent i=new Intent(getApplicationContext(),hotspot.class);
                startActivity(i);
                finish();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private boolean showWritePermissionSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (!Settings.System.canWrite(this)) {
                Log.v("DANG", " " + !Settings.System.canWrite(this));
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                return false;
            }
        }
        return true; //Permission already given
    }
    public void enableWifi(){
        wifiManager.setWifiEnabled(true);
        Toast.makeText(this, "Wifi enabled", Toast.LENGTH_SHORT).show();
    }
    public void disableWifi(){
        wifiManager.setWifiEnabled(false);
        Toast.makeText(this, "Wifi Disabled", Toast.LENGTH_SHORT).show();
    }
    public boolean setWifiEnabled(WifiConfiguration wifiConfig, boolean enabled) {

        try {
            if (enabled) { //disables wifi hotspot if it's already enabled
                wifiManager.setWifiEnabled(false);
            }


            Method method = wifiManager.getClass()
                    .getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (Boolean) method.invoke(wifiManager, wifiConfig, enabled);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            Log.e(this.getClass().toString(), "", e);
            return false;
        }
    }
    public void enableautostart(){
        String manufacturer = android.os.Build.MANUFACTURER;
        try {
            Intent intent = new Intent();
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}