package com.rarity.apps.quickandro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartOnBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().compareToIgnoreCase(Intent.ACTION_BOOT_COMPLETED) == 0){
            context.startService(new Intent(context, RunBackground.class));
        }
    }

}
