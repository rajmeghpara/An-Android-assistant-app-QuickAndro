package com.rarity.apps.quickandro.Modules;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.speech.RecognizerIntent;

/**
 * Created by Premang on 06-Aug-16.
 */
public class Temp_SpeechToText {

    Activity activity;
    Service temp;

    public Temp_SpeechToText(Activity activity){
        this.activity = activity;
    }

    public void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        activity.startActivityForResult(i, 1010);
    }


}
