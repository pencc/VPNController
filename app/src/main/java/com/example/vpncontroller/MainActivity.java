package com.example.vpncontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    // 0-pptp 1-l2tp
    final private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            VpnService.init(MainActivity.this);
//            //查询检查是否已经存在Vpn
//            Object VpnProfile;
//            if(0 == type) {
//                VpnProfile = VpnService.createVpnProfile(
//                        "Vpn0",
//                        "sxya1.xfip.vip",
//                        "pyx123",
//                        "123",
//                        type,
//                        false,
//                        "");
//            } else {
//                VpnProfile = VpnService.createVpnProfile(
//                        "Vpn0",
//                        "jsnj4.xfip.vip",
//                        "pyx123",
//                        "123",
//                        type,
//                        false,
//                        "123");
//            }
//            //连接
//            VpnService.connect(MainActivity.this, VpnProfile);

            //断开连接
            VpnService.disconnect(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}