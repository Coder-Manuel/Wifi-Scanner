package com.example.wifi_scanner.ui.notifications;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wifi_scanner.R;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    Handler handler;
    private NotificationsViewModel notificationsViewModel;
TextView ssid,hssid,ip,mac,li,rx,tx,fr;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
         ssid= root.findViewById(R.id.ussd);
        handler=new Handler();
        ip= root.findViewById(R.id.ipaddress);
        mac= root.findViewById(R.id.mcaddress);
        li= root.findViewById(R.id.linkspeed);
        tx= root.findViewById(R.id.tx);
        rx= root.findViewById(R.id.rx);
        fr= root.findViewById(R.id.frequency);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChanged(@Nullable String s) {

                zer();
            }
        });
        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ArrayList<String> getCurrentSsid(Context context) {
        ArrayList<String> ar=new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return ar;
        }

        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
      try{
          if (connectionInfo != null) {
              ar.add(connectionInfo.getSSID());
              String ipAddress = Formatter.formatIpAddress(connectionInfo.getIpAddress());

              ar.add(ipAddress);
              //ar.add(String.format("%d",(connectionInfo.getFrequency())));
             // Toast.makeText(context, String.valueOf(connectionInfo.getFrequency()), Toast.LENGTH_SHORT).show();
              ar.add(connectionInfo.getMacAddress());
              ar.add(String.valueOf((connectionInfo.getLinkSpeed())));
              ar.add(String.valueOf(connectionInfo.getFrequency()));
              //System.out.println(String.valueOf(connectionInfo.getTxLinkSpeedMbps()));
              //ar.add(String.valueOf(connectionInfo.getRxLinkSpeedMbps()));
              ar.add(String.valueOf(connectionInfo.getNetworkId()));
              ar.add(String.valueOf(connectionInfo.getSupplicantState()));
try{
    System.out.println();
}
catch (Exception p){
    Toast.makeText(context,p.getMessage(),Toast.LENGTH_LONG).show();
}

          }
      }catch (Exception p){}
        }

        return ar;
    }
    public void  zer(){

        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                // textView.setText(getCurrentSsid(getContext().getApplicationContext()).get(0));
                if(getCurrentSsid(getContext().getApplicationContext()).size()!=0){
                    ssid.setText(getCurrentSsid(getContext().getApplicationContext()).get(0));
//                ;
                    ip.setText(getCurrentSsid(getContext().getApplicationContext()).get(1));
                    mac.setText(getCurrentSsid(getContext().getApplicationContext()).get(2));
//                fr.setText(getCurrentSsid(getContext().getApplicationContext()).get(1));
                    li.setText(getCurrentSsid(getContext().getApplicationContext()).get(3)+" Mbps");
                    fr.setText(getCurrentSsid(getContext().getApplicationContext()).get(4)+" Mhz");
                    rx.setText(getCurrentSsid(getContext().getApplicationContext()).get(5));
                    tx.setText(getCurrentSsid(getContext().getApplicationContext()).get(6));
                }
                handler.postDelayed(this,1000);
            }
        },10);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

}