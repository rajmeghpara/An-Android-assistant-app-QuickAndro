package com.rarity.apps.quickandro.Modules;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import com.rarity.apps.quickandro.MainActivity;
import com.rarity.apps.quickandro.RunBackground;
import com.rarity.apps.quickandro.RunBot;

public class Message implements RecognitionListener{

    private Context context;
    private RunBot bot;
    private SpeechToText stt;
    private Contacts contacts;
    private boolean isMessage;
    private String message, receiver;

    public Message(Context context){
        this.context = context;
        try {
            this.bot = (MainActivity) context;
        }catch(ClassCastException e){
            this.bot = (RunBackground) context;
        }

        stt = new SpeechToText(context, this);
        contacts = new Contacts(context);
    }

    private void sendSMS(String message, String number) {
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null, message, null, null);
    }

    public void sendMessage(){
        if(!checkPermission())
            return;

        isMessage = false;
        bot.updateLayout("Please give me the name of receiver.");
        sleep();
        stt.listen();
    }

    public void sendMessage(String name){
        if(!checkPermission())
            return;

        isMessage = true;
        receiver = contacts.findNumber(name);

        if (receiver == null) {
            bot.updateLayout("Sorry could not find the number of " + name);
            return;
        }

        bot.updateLayout("Speak your message now");
        sleep();
        stt.listen();
    }

    @Override
    public void onResults(Bundle results) {
        String result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        bot.updateLayout(" " + result);
        if(isMessage){
            message = result;
            sendSMS(message, receiver);
            bot.updateLayout("Message sent.");
        }
        else{
            receiver = contacts.findNumber(result);

            if (receiver == null) {
                bot.updateLayout("Sorry could not find the number of " + result);
                return;
            }

            isMessage = true;
            bot.updateLayout("Speak your message now");
            sleep();
            stt.listen();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {}
    @Override
    public void onBeginningOfSpeech() {}
    @Override
    public void onRmsChanged(float rmsdB) {}
    @Override
    public void onBufferReceived(byte[] buffer) {}
    @Override
    public void onEndOfSpeech() {}
    @Override
    public void onError(int error) {}
    @Override
    public void onPartialResults(Bundle partialResults) {}
    @Override
    public void onEvent(int eventType, Bundle params) {}

    private void sleep(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
    }

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            bot.updateLayout("Sorry, I don't have permission to send a message.");
            return false;
        }
        return true;
    }
}
