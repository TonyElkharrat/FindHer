package com.example.zivug.Api;

import android.content.Context;
import android.net.wifi.WifiInfo;

public class WifiManager
{
    public static boolean checkWifiOnAndConnected(Context context) {
        android.net.wifi.WifiManager wifiMgr = (android.net.wifi.WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled())
        {
            // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 )
            {
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else
        {
            return false; // Wi-Fi adapter is OFF
        }
    }
}
