package com.example.wifi_scanner;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class MyService extends Service {

    private static String TAG = "MyService";
    private Handler handler;
    private Runnable runnable;
    private final int runTime = 5000;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        createNotificationChannel(getApplicationContext());
        handler = new Handler();
        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                final ArrayList<String> list;
                final SharedPreferences pre=getSharedPreferences("details",MODE_PRIVATE);
                final WifiApManager ap=new WifiApManager(getApplicationContext());
                String values=pre.getString("values","[]");
                System.out.println(values);
                list=new ArrayList<>();
                //String values=pre.getString("values","[]");

                try {
                    JSONArray ar=new JSONArray(values);
                    for (int i=0;i<ar.length();i++){
                        System.out.println(ar.get(i));
                        list.add(ar.get(i).toString());
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                ap.getClientList(true, new FinishScanListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onFinishScan(ArrayList<ClientScanResult> clients) {

                        for (int i=0;i<clients.size();i++){
if (!list.contains(clients.get(i).getHWAddr())){
   list.add(clients.get(i).getHWAddr());
    Intent intent1 = new Intent();
    intent1.setClassName("com.android.settings", "com.android.settings.TetherSettings");
    intent1.addCategory(Intent.CATEGORY_LAUNCHER);
    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    PendingIntent snoozePendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, intent1, 0);

    Intent intentr = new Intent(getApplicationContext(), splash_screen.class);
    intentr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent snoozePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentr, 0);

    Notification newMessageNotification = new Notification.Builder(getApplicationContext(), "1")
            .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
            .setPriority(Notification.PRIORITY_MAX)
            .setContentTitle("New Connection")
            .setContentText("New Device Connected  "+clients.get(i).getHWAddr())
            .addAction(R.drawable.ic_notifications_black_24dp,"Allow",
                    snoozePendingIntent).setPriority(Notification.PRIORITY_HIGH)
            .addAction(R.drawable.ic_notifications_black_24dp,"Block",snoozePendingIntent1
            ).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build();

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
    notificationManager.notify(1, newMessageNotification);
    Log.i(TAG, "onStart");
}
SharedPreferences.Editor editor=pre.edit();
                            Gson j=new Gson();
editor.putString("values",j.toJson(list));
editor.apply();

                        }



                    }
                });

                //Toast.makeText(getApplicationContext(),"new connection",Toast.LENGTH_LONG).show();
                handler.postDelayed(runnable, runTime);
            }
        };
        handler.post(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message = "RunAfterBootService onStartCommand() method.";

        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
      return START_STICKY;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }
    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.str);
            String description = context.getString(R.string.ch);
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel channel = new NotificationChannel("1", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
