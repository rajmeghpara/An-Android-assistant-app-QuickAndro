package com.rarity.apps.quickandro.Modules;


import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import java.util.Calendar;

/**
 * Created by Hardik on 02-08-2016.
 */
public class SetAlarm {

    Context context;
    public SetAlarm(Context context){
        this.context = context;
    }

    public String setAlarm(String input) {

        int hour,minutes = 0;

        if(input.split(" ")[0].length()>2) {
            minutes = Integer.parseInt((input.split(" ")[0]).split(":")[1]);
        }

        if(input.split(" ")[0].length()==2)
            minutes =0;

        hour = check(input);


        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);


        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }

        Calendar cal = Calendar.getInstance();

        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);


        int Duration = calculateDuration(hour,minutes,currentHour,currentMinute);

        return  "Alarm set for " + Duration/60 + " hour and " + (Duration - (Duration/60)*60) +" minutes.";
    }

    private int check(String input) {
        int hour = 0;
        String str[] = (input.split(" ")[0]).split(":");
        String am_pm = input.split(" ")[1];

        if (str.length == 2) {
            hour = Integer.parseInt((input.split(" ")[0]).split(":")[0]);

            if ((am_pm.equals("p.m.")||(am_pm.equals("pm"))) && (hour != 12))
                hour += 12;
            if ((am_pm.equals("a.m.")||(am_pm.equals("am"))) && (hour == 12))
                hour = 0;
            if ((am_pm.equals("p.m.")||(am_pm.equals("pm"))) && (hour == 12))
                hour = 12;
            return hour;
        }

        if((input.split(" ")[0].length()==2)||(input.split(" ")[0].length()==1)){
            hour = Integer.parseInt(str[0]);

            if ((am_pm.equals("p.m.")||(am_pm.equals("pm"))) && (hour != 12))
                hour += 12;
            if ((am_pm.equals("a.m.")||(am_pm.equals("am"))) && (hour == 12))
                hour = 0;
            if ((am_pm.equals("p.m.")||(am_pm.equals("pm"))) && (hour == 12))
                hour = 12;
            return hour;
        }

        return hour;
    }

    private int calculateDuration(int hour, int minutes, int currentHour, int currentMinute) {

        if(minutes<currentMinute){
            minutes+=60;
            hour-=1;
        }
        if(hour<currentHour){
            hour+=24;
        }
        return  ((hour-currentHour)*60 + (minutes-currentMinute));


    }
}
