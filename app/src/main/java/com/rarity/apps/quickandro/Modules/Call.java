package com.rarity.apps.quickandro.Modules;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

public class Call {

    private Context context;

    public Call(Context context){
        this.context = context;
    }

    public String call(String phoneNumber) {
        if(!checkPermission())
            return "Sorry, I do not have permission to call";

        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            return "Sorry, I could not make call";
        }
        return "Calling";
    }

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}
