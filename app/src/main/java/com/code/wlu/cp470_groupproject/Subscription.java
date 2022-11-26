package com.code.wlu.cp470_groupproject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscription {
    private static int count = 0;
    //ID
    public int keyID;
    //Name of subscription
    public String name;
    //Cost of subscription
    public double amount;
    //T/F on automatic renewal
    public boolean automatic_renewal = true;
    //Date obj for when it needs to be renewed.
    public Date renewal_date = new Date();

    //Work in progress, Don't think String is the right type.
    public String payment_plan;

    public Subscription(){
        keyID = count;
        count++;
        Log.i("Constructor of Sub", dateToString(renewal_date));
    }

    public static String dateToString(Date obj){
        // Returns YYYY-MM-DD
        String out = obj.getYear() + "-" + (obj.getMonth()+1) + "-" + obj.getDate();
        return out;
    }

    public String toString(){
        return ("Subscription: " + name + "\namt: " + amount + "\nauto renew: " + automatic_renewal + "\nRenewal date: " + dateToString(renewal_date));
    }

}

