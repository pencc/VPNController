package com.example.vpncontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            VpnService.init(MainActivity.this);
            //查询检查是否已经存在Vpn
            Object VpnProfile = VpnService.getVpnProfile("Vpn0");
            if (VpnProfile == null) {
                VpnProfile = VpnService.createVpnProfile("Vpn0", "192.168.191.1", "Vpntest", "123456");
            } else {
                VpnService.setParams(VpnProfile, "Vpn0", "192.168.191.1", "Vpntest", "123456");
            }
            //连接
            VpnService.connect(MainActivity.this, VpnProfile);

            //断开连接
            //VpnService.disconnect(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}