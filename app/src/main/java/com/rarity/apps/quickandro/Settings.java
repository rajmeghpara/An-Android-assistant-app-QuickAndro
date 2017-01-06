package com.rarity.apps.quickandro;

import android.content.Context;

/**
 * Created by Hardik on 8/7/16.
 */
public class Settings {

    private PrefManager prefManager;

    public Settings(Context context){
        prefManager=new PrefManager(context);
    }

    public void shakeOn(){
        prefManager.setShakeOperation(true);
    }
    public void shakeOff(){
        prefManager.setShakeOperation(false);
    }
    public void changeName(String name){
        prefManager.setName(name);
    }
    public String getName(){
        return prefManager.getName();
    }

}
