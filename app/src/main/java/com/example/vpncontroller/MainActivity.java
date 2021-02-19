package com.example.vpncontroller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    // 0-pptp 1-l2tp
    final private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.textView);

        VpnService.init(MainActivity.this);

        Button btn_connect = (Button)findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Object VpnProfile;
                if(0 == type) {
                    VpnProfile = VpnService.createVpnProfile(
                            "Vpn0",
                            "sxya1.xfip.vip",
                            "pyx123",
                            "123",
                            type,
                            false,
                            "");
                } else {
                    VpnProfile = VpnService.createVpnProfile(
                            "Vpn0",
                            "jsnj4.xfip.vip",
                            "pyx123",
                            "123",
                            type,
                            false,
                            "123");
                }
                //连接
                boolean result = VpnService.connect(MainActivity.this, VpnProfile);
                if(false == result)
                    textView.setText("connect failed!");
            }
        });

        Button btn_checkconnect = (Button)findViewById(R.id.btn_checkconnect);
        btn_checkconnect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                int result = VpnService.checkConnectting();
                if(0 == result) {
                    textView.setText("disconnected");
                } else if(1 == result) {
                    textView.setText("initializing");
                } else if(2 == result) {
                    textView.setText("connecting");
                } else if(3 == result) {
                    textView.setText("connected");
                } else if(4 == result) {
                    textView.setText("timeout");
                } else if(5 == result) {
                    textView.setText("failed");
                } else if(6 == result) {
                    textView.setText("no connection");
                }
            }
        });

        Button btn_disconnect = (Button)findViewById(R.id.btn_disconnect);
        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                boolean result = VpnService.disconnect(MainActivity.this);
                if(false == result)
                    textView.setText("disconnect failed!");
            }
        });
    }
}