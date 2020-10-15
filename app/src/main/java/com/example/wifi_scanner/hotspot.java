package com.example.wifi_scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifi_scanner.ui.dashboard.DashboardFragment;
import com.example.wifi_scanner.ui.dashboard.DashboardViewModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class hotspot extends AppCompatActivity {
    private Button button;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";
    private DashboardViewModel dashboardViewModel;
   ArrayList<item> values;
    SharedPreferences pre;

    Button bts;
    ArrayList<String> list;

    ArrayList<items> items;
   item item;
    WifiManager wifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot);
        final SharedPreferences pre=getSharedPreferences("details",MODE_PRIVATE);
        System.out.println(pre.getString("values","[]"));
      list=new ArrayList<>();
        //createNotificationChannel();
        items=new ArrayList<>();
        item=new item(this,items);
        final ListView listView=findViewById(R.id.activ_list);
        listView.setAdapter(item);
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
        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        //cur_cal.add(Calendar.SECOND, 50);
        Intent intentw = new Intent(getApplicationContext(), MyService.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intentw, 0);
        AlarmManager alarm = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(), 30*1000, pintent);
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                list=new ArrayList<>();
                String values=pre.getString("values","[]");
                try {
                    JSONArray ar=new JSONArray(values);
                    for (int i=0;i<ar.length();i++){
                        list.add(ar.get(i).toString());
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                ap.getClientList(true, new FinishScanListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onFinishScan(ArrayList<ClientScanResult> clients) {
                        items.clear();
                        for (int i=0;i<clients.size();i++){

                               items itemw=new items(clients.get(i).getIpAddr(),"connection live",clients.get(i).getHWAddr());
                               item.add(itemw);
                        }



                    }
                });
                handler.postDelayed(this,500);
            }
        },10);
    }

    class item extends ArrayAdapter<items> {
        Activity context;
        Button block;
        TextView date,state,act,des,mobile;

        ArrayList<items> item;
        public item(@NonNull Activity context, ArrayList<items> item) {
            super(context, R.layout.custom,item);
            this.context=context;
            this.item=item;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            items items=getItem(position);

            if (convertView==null){
                LayoutInflater inflater=context.getLayoutInflater();
                convertView=inflater.inflate(R.layout.custom,null,true);

            }
            date=convertView.findViewById(R.id.ipaddres);
            state=convertView.findViewById(R.id.macadress);
            act=convertView.findViewById(R.id.con);
            block=convertView.findViewById(R.id.block);
            block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent();
                    intent1.setClassName("com.android.settings", "com.android.settings.TetherSettings");
                    intent1.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
            });
            act.setText(items.getConnection());
            date.setText("Ip Address "+items.getIpaddress());
            state.setText("Mac Address: "+items.getMacadress());


            return convertView;
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
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


}