package com.rarity.apps.quickandro.Modules;

import android.content.Context;
import android.media.AudioManager;

public class ProfileManager {

    private AudioManager audioManager;

    public ProfileManager(Context context){
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public String changeProfile(String profile){
        switch (profile){
            case "silent":
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            case "general":
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            case "vibrate":
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            default:
                return "Invalid profile type.";
        }

        return "Profile changed to " + profile;
    }
}
