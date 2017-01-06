package com.rarity.apps.quickandro.Modules;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class SpeakText {

    private TextToSpeech tts;
    private Context c;

    public SpeakText(Context context){
        c = context;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status){
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.UK);
                }
            }
        });
    }

    public String speak(String textToSpeak){
        tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
        return textToSpeak;
    }

    public void stopSpeaking(){
        tts.stop();
    }
}
