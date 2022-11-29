package com.code.wlu.cp470_groupproject;

import static android.content.Intent.getIntent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class myBackgroundProcess extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        ArrayList<Subscription> subscriptions = (ArrayList<Subscription>) intent.getSerializableExtra("subArray");

        Log.i("Background process", "received, array length: " + subscriptions.size());

        Date currentDate = new Date();
        currentDate.setYear(Calendar.getInstance().get(Calendar.YEAR));


        for( Subscription sub : subscriptions){
            //int daysApart = (sub.renewal_date.getDate() - currentDate.getDate());
            int daysApart = (int)((currentDate.getTime() - sub.renewal_date.getTime()) / (1000*60*60*24l));
            Log.i("Background process", "Days apart from current date, and " + sub.name + " due date is: " + daysApart + "\nCurrent date: " + Subscription.dateToString(currentDate) + "\tdue date: " + Subscription.dateToString(sub.renewal_date) + "\nCurrent year: " + currentDate.getYear() + " month: " + currentDate.getMonth() + " day: " + currentDate.getDate() + " time: " + currentDate.getTime() + " time of sub: " + sub.renewal_date.getTime());
            if(daysApart >= -2 && daysApart <=0){
                Log.i("Background process", "2 days or 1 day before");
                //Notification;

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Sub Notification");
                builder.setSmallIcon(R.drawable.cash_icon);
                builder.setContentTitle("Subscription Renewal Date Coming Up!");
                builder.setContentText("Your Subscription for: " + sub.name + " is due on: " + sub.dateToString(sub.renewal_date));
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                managerCompat.notify(sub.keyID, builder.build());

            }
        }


    }
}
