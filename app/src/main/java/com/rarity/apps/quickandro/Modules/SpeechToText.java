package com.rarity.apps.quickandro.Modules;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public class SpeechToText{

    private Context context;
    private Intent intent;
    private SpeechRecognizer recognizer;

    public SpeechToText(Context c, RecognitionListener listener){
        this.context = c;

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        recognizer = SpeechRecognizer.createSpeechRecognizer(context);
        recognizer.setRecognitionListener(listener);
    }

    public void listen(){
        recognizer.startListening(intent);
    }

    public void stopListening(){
        recognizer.stopListening();
    }

}
