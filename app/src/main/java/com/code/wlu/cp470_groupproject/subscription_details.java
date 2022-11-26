package com.code.wlu.cp470_groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class subscription_details extends AppCompatActivity {

    private String[] subArray;
    private TextView subscriptionNameTextView, subIDTextView, amountTextView, renewalTextView, renewalDateTextView, paymentPlanTextView;
    public String ACTIVITY_NAME = "SubscriptionDetails";
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);
        Bundle args = this.getIntent().getExtras();
        Log.i(ACTIVITY_NAME, "onCreate for MessageDetails.");

        //Take in args
        subArray = args.getStringArray("subArray");

        subscriptionNameTextView = (TextView) findViewById(R.id.subscriptionNameTextView);
        subIDTextView = (TextView) findViewById(R.id.subIDTextView);
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        renewalTextView = (TextView) findViewById(R.id.renewalTextView);
        renewalDateTextView = (TextView) findViewById(R.id.renewalDateTextView);
        paymentPlanTextView = (TextView) findViewById(R.id.paymentPlanTextView);

        deleteButton = (Button) (findViewById(R.id.deleteButton));

        //Update text views
        subscriptionNameTextView.setText("Sub name: " + subArray[0]);
        subIDTextView.setText("Sub ID: " + subArray[1]);
        amountTextView.setText("Amount: $" + subArray[2]);
        renewalDateTextView.setText("Renewal Date: " + subArray[3] );
        renewalTextView.setText("Automatic Renewal: " + subArray[4]);
        paymentPlanTextView.setText("Payment Plan: " + subArray[5]);


        //Delete Button listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "Delete button clicked");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("subID", subArray[1]);
                Log.i(ACTIVITY_NAME, "SubID to Delete: " + subArray[1]);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                //Go back to activity when message deleted.
            }
        });
    }
}