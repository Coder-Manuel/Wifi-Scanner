package com.example.wifi_scanner;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

public class BootUpReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            Intent intent1 = new Intent();
//            intent1.setClassName("com.android.settings", "com.android.settings.TetherSettings");
//            intent1.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            PendingIntent snoozePendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
//
//            Intent intente = new Intent(context, splash_screen.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent snoozePendingIntent = PendingIntent.getActivity(context, 0, intente, 0);
//
//            Notification newMessageNotification = new Notification.Builder(context, "1")
//                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
//                    .setContentTitle("Connection Request")
//                    .setContentText("New Connection Request has been made")
//                    .addAction(R.drawable.ic_notifications_black_24dp,"Allow",
//                            snoozePendingIntent).setPriority(Notification.PRIORITY_HIGH)
//                    .addAction(R.drawable.ic_notifications_black_24dp,"Block",snoozePendingIntent1
//                    )
//                    .build();
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            notificationManager.notify(1, newMessageNotification);
}}

}