package com.code.wlu.cp470_groupproject;

import static android.content.Intent.getIntent;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class myBackgroundProcess extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        ArrayList<Subscription> subscriptions = (ArrayList<Subscription>) intent.getSerializableExtra("subArray");

        Log.i("Background process", "received, array length: " + subscriptions.size());

        Date currentDate = new Date();

        for( Subscription sub : subscriptions){
            int daysApart = (int)((currentDate.getTime() - sub.renewal_date.getTime()) / (1000*60*60*24l));
            if(daysApart == -2 || daysApart == -1){
                Log.i("Background process", "2 days or 1 day before");
                //Notification;
            }
        }


    }
}
