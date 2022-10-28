package com.code.wlu.cp470_groupproject;

import java.util.Date;

public class Subscription {
    //Name of subscription
    public String name;
    //Cost of subscription
    public double amount;
    //T/F on automatic renewal
    public boolean automatic_renewal = true;
    //Date obj for when it needs to be renewed.
    public Date renewal_date;

    //Work in progress, Don't think String is the right type.
    public String payment_plan;
}
