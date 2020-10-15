package com.example.wifi_scanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
Context context;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
public void Service(Context context){
    this.context=context;
}
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
           // createNotificationChannel(context);
            // run on another thread
            Intent i=new Intent();
            mHandler.post(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    // display toast
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
                            .setContentTitle("New Connection")
                            .setContentText(" has connected to your network")
                            .addAction(R.drawable.ic_notifications_black_24dp,"Allow",
                                    snoozePendingIntent).setPriority(Notification.PRIORITY_HIGH)
                            .addAction(R.drawable.ic_notifications_black_24dp,"Block",snoozePendingIntent1
                            )
                            .build();
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    notificationManager.notify(1, newMessageNotification);
                    //Log.i(TAG, "onStart");
//                    Toast.makeText(getApplicationContext(), getDateTime(),
//                            Toast.LENGTH_SHORT).show();
                }

            });
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
        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

    }}