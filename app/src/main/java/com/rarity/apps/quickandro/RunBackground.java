package com.rarity.apps.quickandro;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Toast;
import android.os.Vibrator;

import com.rarity.apps.quickandro.Modules.Calculator;
import com.rarity.apps.quickandro.Modules.Call;
import com.rarity.apps.quickandro.Modules.Contacts;
import com.rarity.apps.quickandro.Modules.Message;
import com.rarity.apps.quickandro.Modules.OpenApp;
import com.rarity.apps.quickandro.Modules.ProfileManager;
import com.rarity.apps.quickandro.Modules.Search;
import com.rarity.apps.quickandro.Modules.SetAlarm;
import com.rarity.apps.quickandro.Modules.SpeakText;
import com.rarity.apps.quickandro.Modules.SpeechToText;
import com.rarity.apps.quickandro.Modules.Switch;

public class RunBackground extends Service implements RecognitionListener, RunBot {

    private ShakeDetector mShaker;
    private Vibrator vibrator;
    private SpeechToText stt;
    private SpeakText tts;

    @Override
    public void onCreate() {
        super.onCreate();

        /*this is to enable shake-detection while phone is locked*/
        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
        PowerManager.WakeLock lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SensorRead");
        lock.acquire();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mShaker = new ShakeDetector(this);
        mShaker.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                vibrator.vibrate(200);
                //run();
                Intent intent = new Intent(RunBackground.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        stt = new SpeechToText(this, this);
        tts = new SpeakText(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    /*method tu run the bot*/
    public void run(){
        Toast.makeText(this, "Say something...", Toast.LENGTH_SHORT).show();
        tts.stopSpeaking();
        stt.listen();
    }

    /*method to add new message in layout*/
    @Override
    public void updateLayout(String message){
        if(message.charAt(0)!=' ')    //this will speak the message aloud if that is written by the bot.
            tts.speak(message);
        else
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*this method will decide which function of app to call based on user's command*/
    private void callModule(String message){
        String temp[] = message.split(" ");
        String command = temp[0];         //stores the first word(ie. command)
        String argument = "";

        //this will store all user's words into argument except the first word which is command.
        for(int i=1; i<temp.length-1; i++)
            argument += temp[i] + " ";
        if(temp.length>1)
            argument += temp[ temp.length-1 ];

        /*call functions based on command*/
        if(command.equalsIgnoreCase("call")){
            commandCall(argument);
        }
        else if(command.equalsIgnoreCase("calculate")){
            commandCalculate(argument);
        }
        else if(command.equalsIgnoreCase("message")){
            commandMessage(argument);
        }
        else if(command.equalsIgnoreCase("open")){
            commandOpen(argument);
        }
        else if(command.equalsIgnoreCase("profile")){
            commandProfile(argument);
        }
        else if(command.equalsIgnoreCase("search")){
            commandSearch(argument);
        }
        else if(command.equalsIgnoreCase("turn")){
            commandTurn(argument);
        }
        else if(command.equalsIgnoreCase("alarm")){
            commandAlarm(argument);
            Toast.makeText(this, argument, Toast.LENGTH_LONG).show();
        }
        else{
            tts.speak("Invalid command, try again");
            updateLayout("Invalid command, try again");
        }
    }

    //this method will be called when the result of SpeechToText module got decleared
    @Override
    public void onResults(Bundle resultBundle) {
        String result = resultBundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        updateLayout(" " + result);
        callModule(result);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {}
    @Override
    public void onError(int error) {}
    @Override
    public void onBeginningOfSpeech() {}
    @Override
    public void onBufferReceived(byte[] buffer) {}
    @Override
    public void onEndOfSpeech() {}
    @Override
    public void onEvent(int eventType, Bundle params) {}
    @Override
    public void onPartialResults(Bundle partialResults) {}
    @Override
    public void onRmsChanged(float rmsdB) {}

    /*function to call a person*/
    private void commandCall(String argument){
        Call call = new Call(this);
        Contacts contacts = new Contacts(this);
        String reply;

        if(contacts.findNumber(argument) == null)
            reply = "Could not find the number of " + argument;
        else
            reply = call.call( contacts.findNumber(argument) );

        if(reply.compareToIgnoreCase("calling") == 0)
            reply += " " + argument;

        tts.speak(reply);
        updateLayout(reply);
    }

    /*function to calculate a mathematical expression*/
    private void commandCalculate(String argument){
        Calculator calculator = new Calculator(this);

        if(argument.equals(""))
            calculator.calculate();
        else
            updateLayout( calculator.calculate(argument) );
    }

    /*function to send a message*/
    private void commandMessage(String argument){
        Message msg = new Message(this);

        Toast.makeText(this, "."+argument+".", Toast.LENGTH_SHORT).show();

        if(argument.equals(""))
            msg.sendMessage();
        else
            msg.sendMessage(argument);
    }

    /*function to open a app*/
    private void commandOpen(String argument){
        OpenApp openApp = new OpenApp(this);
        updateLayout(openApp.openApp(argument));
    }

    /*function to change prpofile*/
    private void commandProfile(String argument){
        ProfileManager profileManager = new ProfileManager(this);
        updateLayout(profileManager.changeProfile(argument));
    }

    /*function for search*/
    private void commandSearch(String argument) {
        argument = argument.toLowerCase();
        Search search = new Search(this);
        if(argument.split(" ")[0].equalsIgnoreCase("wiki"))
            updateLayout(search.wikiSearch(argument.replaceFirst("wiki ", "")));
        else if(argument.split(" ")[0].equalsIgnoreCase("wikipedia"))
            updateLayout( search.wikiSearch(argument.replaceFirst("wikipedia ", "")) );
        else if(argument.split(" ")[0].equalsIgnoreCase("dictionary"))
            updateLayout( search.dictionarySearch(argument.replaceFirst("dictionary ", "")) );
        else if(argument.split(" ")[0].equalsIgnoreCase("youtube"))
            updateLayout( search.youtubeSearch(argument.replaceFirst("youtube ", "")) );
        else
            updateLayout( search.googleSearch(argument) );
    }

    /*function for extra utilities*/
    private void commandTurn(String argumrnt){
        Switch s = new Switch(this);
        argumrnt = argumrnt.toLowerCase();
        updateLayout( s.utility(argumrnt) );
    }

    /*function for alarm*/
    private void commandAlarm(String argument){
        SetAlarm alarm = new SetAlarm(this);
        updateLayout( alarm.setAlarm(argument) );
    }
}
