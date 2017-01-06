package com.rarity.apps.quickandro.Modules;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import java.lang.reflect.Method;

public class Switch {

    Context context;

    public Switch(Context context){
        this.context = context;
    }

    public String utility(String argument){
        String state="", device="";

        try {
            state = argument.split(" ")[0];
            device = argument.split(" ")[1];
        }
        catch(Exception e){}

        switch (device){
            case "bluetooth":
                if(state.equals("on"))
                    return onBluetooth();
                else
                    return offBluetooth();
            case "wifi":
            case "wi-fi":
                if(state.equals("on"))
                    return onWifi();
                else
                    return offWifi();
            case "hotspot":
                if(state.equals("on"))
                    return onHotspot();
                else
                    return offHotspot();
            case "rotation":
                if(state.equals("on"))
                    return onRotation();
                else
                    return offRotation();
            case "location":
                if(state.equals("on"))
                    return onLocation();
                else
                    return offLocation();
            case "flash":
            case "flashlight":
                if(state.equals("on"))
                    return onFlash();
                else
                    return offFlash();
            default:
                return "Sorry, I could not catch you";
        }
    }

    private String onBluetooth(){
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if(bluetooth.isEnabled()) {
            return "Bluetooth is already on.";
        }
        else {
            bluetooth.enable();
            return "Bluetooth turned on";
        }
    }

    private String offBluetooth(){
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if(bluetooth.isEnabled()) {
            bluetooth.disable();
            return "Bluetooth turned off.";
        }
        else {
            return "Bluetooth is already off.";
        }
    }

    private String onWifi(){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifi.isWifiEnabled()){
            return "Wifi is already on.";
        }
        else{
            wifi.setWifiEnabled(true);
            return "Wifi turned on.";
        }
    }

    private String offWifi(){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifi.isWifiEnabled()){
            wifi.setWifiEnabled(false);
            return "Wifi turned off.";
        }
        else{
            return "Wifi is already off.";
        }
    }

    private String onHotspot(){
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            wifimanager.setWifiEnabled(false);
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, true);
            return "Hotspot started.";
        }
        catch (Exception e) {
            return "Sorry, I could not access hotspot settings.";
        }
    }

    private String offHotspot(){
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            wifimanager.setWifiEnabled(false);
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, false);
            return "Hotspot stopped.";
        }
        catch (Exception e) {
            return "Sorry, I could not access hotspot settings.";
        }
    }

    private String onRotation(){
        Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
        return "Auto rotation is on.";
    }

    private String offRotation(){
        Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        return "Auto rotation is off.";
    }

    private String onLocation(){
        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        return "I need your help to do that.";
    }

    private String offLocation(){
        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        return "I need your help to do that.";
    }

    private static Camera camera = null;
    private String onFlash(){
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            return "Flashlight started";
        }
        catch(Exception e){
            return "Flashlight is already on";
        }
    }

    private String offFlash(){
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            camera.release();
            return "Flashlight turned off";
        }
        catch (Exception e){
            return "Flashlight is already off";
        }
    }
}
