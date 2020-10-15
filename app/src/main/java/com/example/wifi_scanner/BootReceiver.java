package com.example.wifi_scanner;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    ArrayList<String> list;
    ArrayList<String> tlist;
    private static final String TAG = "BootReceiver";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(final Context context, final Intent intent) {
        //Log.e(TAG, "----BootReceiver---");
         list=new ArrayList<>();
        tlist=new ArrayList<>();

        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        //cur_cal.add(Calendar.SECOND, 50);
        Intent intentw = new Intent(context, MyService.class);
        PendingIntent pintent = PendingIntent.getService(context, 0, intentw, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(), 30*1000, pintent);
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            createNotificationChannel(context);

            final SharedPreferences pre=context.getSharedPreferences("details",Context.MODE_PRIVATE);

            final WifiApManager ap=new WifiApManager(context);
            final Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    handler.postDelayed(this,100);
                }
            },10);
        }

    }
    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.str);
            String description = context.getString(R.string.ch);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
