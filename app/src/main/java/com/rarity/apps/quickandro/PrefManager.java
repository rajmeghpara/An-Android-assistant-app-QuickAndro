package com.rarity.apps.quickandro;

/**
 * Created by hardik on 8/7/16.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "welcome";
    private static final String NAME = "name";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String SHAKE_FEATURE_VALUE = "shake";
    private static final String MESSAGE="message";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public void setName(String name){
        editor.putString(NAME,name);
        editor.commit();
    }
    public String getName(){
        return pref.getString(NAME,"Anonymous");
    }
    public void setShakeOperation(boolean b){
        editor.putBoolean(SHAKE_FEATURE_VALUE,b);
        editor.commit();
    }
    public boolean getShakeVal(){
        return pref.getBoolean(SHAKE_FEATURE_VALUE,true);
    }
    public void setMessage(String message){
        editor.putString(MESSAGE,message);
        editor.commit();
    }
    public String getMessage(){
        return pref.getString(MESSAGE,null);
    }
}