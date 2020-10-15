package com.example.wifi_scanner.ui.dashboard;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.example.wifi_scanner.ClientScanResult;
import com.example.wifi_scanner.FinishScanListener;
import com.example.wifi_scanner.R;
import com.example.wifi_scanner.WifiApManager;
import com.example.wifi_scanner.items;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private Button button;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";
    private DashboardViewModel dashboardViewModel;
    SharedPreferences pre;
    Button bts;
ArrayList<items> list;
    ArrayList<items> items;
    item item;
    WifiManager wifiManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        bts=root.findViewById(R.id.scan);
        final WifiApManager ap=new WifiApManager(getContext().getApplicationContext());
list=new ArrayList<>();
        pre=getContext().getApplicationContext().getSharedPreferences("details",Context.MODE_PRIVATE);

        final String s=pre.getString("connections","[]");

        try {
            JSONArray ar=new JSONArray(s);
            Toast.makeText(getContext(),String.valueOf(ar.length()),Toast.LENGTH_LONG).show();
            for (int i=0;i<ar.length();i++){

                JSONObject ob=ar.getJSONObject(i);
                items itemw=new items(ob.getString("macadress"),"connection live",ob.getString("ipaddress"));
                list.add(itemw);
                item.add(itemw);
            }
        } catch (JSONException e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        ap.getClientList(true, new FinishScanListener() {
            @Override
            public void onFinishScan(ArrayList<ClientScanResult> clients) {
                for (int i=0;i<clients.size();i++){
                    items itemw=new items(clients.get(i).getIpAddr(),"connection live",clients.get(i).getHWAddr());
                    if(!list.contains(clients.get(i).getHWAddr())){
                        item.add(itemw);
//ap.diable_network(clients.get(i).getHWAddr());

                        SharedPreferences.Editor edit=pre.edit();
                        Gson j=new Gson();
                        list.add(itemw);
                        String s=j.toJson(list);
                        edit.putString("connections",s);
                        edit.commit();
                    }

                }

            }
        });

        items=new ArrayList<>();
        item=new item(getActivity(),items);
        ListView listView=root.findViewById(R.id.activ_list);
        listView.setAdapter(item);
        bts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);

            }
        });


        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    class item extends ArrayAdapter<items>{
        Activity context;
        Button block;
        TextView date,state,act,des,mobile;

        ArrayList<items> item;
        public item(@NonNull Activity context,ArrayList<items> item) {
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

}
