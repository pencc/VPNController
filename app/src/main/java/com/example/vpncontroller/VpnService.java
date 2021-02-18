package com.example.vpncontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class VpnService {
        private static Class VpnProfileClz;
        private static Class credentialsClz;
        private static Class keyStoreClz;
        private static Class iConManagerClz;
        private static Object iConManagerObj;

        final private static String TAG = "VpnService";

        /**
         * 使用其他方法前先调用该方法
         * 初始化Vpn相关的类
         * @param context
         */
        public static void init(Context context){
            try {
                VpnProfileClz = Class.forName("com.android.internal.net.VpnProfile");
                keyStoreClz = Class.forName("android.security.KeyStore");

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                Field fieldIConManager = null;

                fieldIConManager = cm.getClass().getDeclaredField("mService");
                fieldIConManager.setAccessible(true);
                iConManagerObj = fieldIConManager.get(cm);
                iConManagerClz = Class.forName(iConManagerObj.getClass().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * @param name     Vpn连接名，自定义
         * @param server   服务器地址
         * @param username 用户名
         * @param password 用户密码
         * @return 返回一个com.android.internal.net.VpnProfile的实例
         */
        public static Object createVpnProfile(String name,
                                              String server,
                                              String username,
                                              String password,
                                              int type,
                                              boolean mppe,
                                              String ipsecSecret) {
            Object VpnProfileObj = null;
            try {
                //获得构造函数
                Constructor constructor = VpnProfileClz.getConstructor(String.class);
                VpnProfileObj = constructor.newInstance(name);
                //设置参数
                setParams(VpnProfileObj,
                        name,
                        server,
                        username,
                        password,
                        type,
                        mppe,
                        ipsecSecret);
                //插入Vpn数据
                //insertVpn(VpnProfileObj, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return VpnProfileObj;
        }

        /**
         * @param name      Vpn连接名，自定义
         * @param server    服务器地址
         * @param username  用户名
         * @param password  用户密码
         * @param type      0-pptp 1-l2tp
         * @param mppe      mpp加密
         * @return 返回一个com.android.internal.net.VpnProfile的实例
         */
        public static Object setParams(Object VpnProfileObj,
                                       String name,
                                       String server,
                                       String username,
                                       String password,
                                       int type,
                                       boolean mppe,
                                       String ipsecSecret) {
            try {
                if(0 == type) { // pptp
                    Field field_username = VpnProfileClz.getDeclaredField("username");
                    Field field_password = VpnProfileClz.getDeclaredField("password");
                    Field field_server = VpnProfileClz.getDeclaredField("server");
                    Field field_name = VpnProfileClz.getDeclaredField("name");
                    Field field_type = VpnProfileClz.getDeclaredField("type");
                    Field field_mppe = VpnProfileClz.getDeclaredField("mppe");
                    field_name.set(VpnProfileObj, name);
                    field_server.set(VpnProfileObj, server);
                    field_username.set(VpnProfileObj, username);
                    field_password.set(VpnProfileObj, password);
                    field_type.set(VpnProfileObj, type);
                    field_mppe.set(VpnProfileObj, mppe);
                } else if(1 == type) { // l2tp
                    Field field_username = VpnProfileClz.getDeclaredField("username");
                    Field field_password = VpnProfileClz.getDeclaredField("password");
                    Field field_server = VpnProfileClz.getDeclaredField("server");
                    Field field_name = VpnProfileClz.getDeclaredField("name");
                    Field field_type = VpnProfileClz.getDeclaredField("type");
                    Field field_ipsecSecret = VpnProfileClz.getDeclaredField("ipsecSecret");
                    field_name.set(VpnProfileObj, name);
                    field_server.set(VpnProfileObj, server);
                    field_username.set(VpnProfileObj, username);
                    field_password.set(VpnProfileObj, password);
                    field_type.set(VpnProfileObj, type);
                    field_ipsecSecret.set(VpnProfileObj, ipsecSecret);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            return VpnProfileObj;
        }

        /**
         * 连接Vpn
         * @param context
         * @param profile com.android.internal.net.VpnProfile的实例
         * @return true:连接成功，false:连接失败
         */
        public static boolean connect(Context context, Object profile) {
            boolean isConnected = true;
            try {
                Method metStartLegacyVpn = iConManagerClz.getDeclaredMethod("startLegacyVpn", VpnProfileClz);
                metStartLegacyVpn.setAccessible(true);
                //解锁KeyStore
                unlock(context);
                //开启Vpn连接
                metStartLegacyVpn.invoke(iConManagerObj, profile);
            } catch (Exception e) {
                isConnected = false;
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            return isConnected;
        }

        /**
         * 断开Vpn连接
         * @param context
         * @return true:已断开，false:断开失败
         */
        public static boolean disconnect(Context context) {
            boolean disconnected = true;
            try {
                Method metPrepare = iConManagerClz.getDeclaredMethod("prepareVpn", String.class, String.class, int.class);
                //断开连接
                metPrepare.invoke(iConManagerObj, null, "[Legacy Vpn]", 1000);
            } catch (Exception e) {
                disconnected = false;
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            return disconnected;
        }

        /**
         * @return 返回一个已存在的Vpn实例
         */
        public static Object getVpnProfile(String vpnName) {
            try {
                Object keyStoreObj = getKeyStoreInstance();

                Method VpnProfile_decode = VpnProfileClz.getDeclaredMethod("decode", String.class, byte[].class);
                VpnProfile_decode.setAccessible(true);

                Method keyStore_get = keyStoreClz.getDeclaredMethod("get", String.class);
                keyStore_get.setAccessible(true);
                //获得第一个Vpn
                Object byteArrayValue = keyStore_get.invoke(keyStoreObj,vpnName);
                if(null == byteArrayValue)
                    return null;
                Log.i("getVpnProfile", byteArrayValue.toString());
                //反序列化返回VpnProfile实例
                Object VpnProfileObj = VpnProfile_decode.invoke(null, vpnName, byteArrayValue);

                return VpnProfileObj;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        private static void insertVpn(Object profieObj,String vpnName)throws Exception{
            Method keyStore_put = keyStoreClz.getDeclaredMethod("put", String.class, byte[].class, int.class, int.class);
            Object keyStoreObj = getKeyStoreInstance();
            Class VpnProfileClz = Class.forName("com.android.internal.net.VpnProfile");
            @SuppressLint("SoonBlockedPrivateApi")
            Method VpnProfile_encode = VpnProfileClz.getDeclaredMethod("encode");
            byte[] bytes = (byte[]) VpnProfile_encode.invoke(profieObj);
            keyStore_put.invoke(keyStoreObj,vpnName,bytes,-1,1);
        }

        private static Object getKeyStoreInstance() throws Exception {
            Method keyStore_getInstance = keyStoreClz.getMethod("getInstance");
            keyStore_getInstance.setAccessible(true);
            Object keyStoreObj = keyStore_getInstance.invoke(null);
            return keyStoreObj;
        }
        private static void unlock(Context mContext) throws Exception {
            credentialsClz = Class.forName("android.security.Credentials");

            Method credentials_getInstance = credentialsClz.getDeclaredMethod("getInstance");
            Object credentialsObj = credentials_getInstance.invoke(null);

            Method credentials_unlock = credentialsClz.getDeclaredMethod("unlock",Context.class);
            credentials_unlock.invoke(credentialsObj,mContext);
        }


    }
