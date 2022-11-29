package com.code.wlu.cp470_groupproject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SubscriptionList extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "SubscriptionList";
    public ArrayList<Subscription> subscriptions;
    public ListView subView;
    public Button AddSubButton;
    public String loginUsername;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference DatabaseRef = database.getReference();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcription_window);
        subView = findViewById(R.id.subView);
        AddSubButton  = findViewById(R.id.AddSubButton);
        Log.d("OnCreate", "SubList start");
//        Toolbar toolbar;
//        toolbar = findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);
        Bundle bundleFromLogin = getIntent().getExtras();
        if(bundleFromLogin!=null){
            loginUsername = bundleFromLogin.getString("UserName").replaceAll("[.]", "");
            Log.i(ACTIVITY_NAME, "LoginUsername: " + loginUsername);
        }

        //DB Creation
        DatabaseRef.child("Users");

        //List of sub objects
        subscriptions = new ArrayList<Subscription>();


        //in this case, “this” is the SubWindow, which is-A Context object
        SubAdapter subAdapter =new SubAdapter( this );
        subView.setAdapter (subAdapter);

        //Used for reading from DB This calls the subAdapter too.
        //So if the DB is modified somehow, this will get called and update the view
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(loginUsername).child("Subscriptions");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subscriptions.clear();
                for(DataSnapshot achild : snapshot.getChildren()){
                    Log.i(ACTIVITY_NAME, achild.getValue().toString());
                    subscriptions.add(achild.getValue(Subscription.class));
                    subAdapter.notifyDataSetChanged(); //this restarts the process of getCount()
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase application", "Failed to read value.", error.toException());
            }
        });

        Intent intent = new Intent(this, myBackgroundProcess.class);
        intent.setAction("BackgroundProcess");
        intent.putExtra("subArray", subscriptions);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 100, pendingIntent);

        //The add subscription button action starts here
        AddSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder customDialog = new AlertDialog.Builder(SubscriptionList.this);
                // Get the layout inflater
                LayoutInflater inflater = SubscriptionList.this.getLayoutInflater();
                //Starts custom_dialog which allows for creation of sub object.
                final View view2 = inflater.inflate(R.layout.custom_dialog, null);
                customDialog.setView(view2)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //Creates new sub object and fills it with the data in the dialog.
                                Subscription sub = new Subscription();
                                EditText edit = view2.findViewById(R.id.dialog_subscription_name);
                                String name = edit.getText().toString();
                                edit = view2.findViewById(R.id.dialog_payment_amount);
                                double amt = Double.parseDouble(edit.getText().toString());
                                edit = view2.findViewById(R.id.dialog_payment_plan);
                                String plan = edit.getText().toString();
                                CheckBox type = view2.findViewById(R.id.dialog_payment_type_checkBox);
                                sub.automatic_renewal = type.isChecked();
                                sub.name = name;
                                sub.amount = amt;
                                sub.payment_plan = plan;

                                //Start of DatePicker process
                                DatePicker dateEdt = view2.findViewById(R.id.dialog_renewal_date);
                                Date newDate = new Date(dateEdt.getYear(), dateEdt.getMonth(), dateEdt.getDayOfMonth());
                                sub.renewal_date = newDate;

                                database.getReference("Users").child(loginUsername).child("Subscriptions").child("Sub " + sub.keyID).setValue(sub);

                            }
                        })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                Dialog dialogChoice3 = customDialog.create();
                dialogChoice3.show();

            }
        });


        //Goes from sub list to sub details.
        subView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Subscription subFromList = subAdapter.getItem(position);
                Log.i(ACTIVITY_NAME, "SUB FROM LIST: " + subFromList);
                //Ch 9.9 Slide 41
                //use a Bundle to pass the message string, and the database id of the selected item to the fragment in the FragmentTransaction
                Bundle args = new Bundle();
                String[] subArray = new String[] {subFromList.name, String.valueOf(subFromList.keyID), String.valueOf(subFromList.amount), Subscription.dateToString(subFromList.renewal_date), String.valueOf(subFromList.automatic_renewal), subFromList.payment_plan};
                args.putStringArray("subArray", subArray);

                    Intent intent = new Intent(SubscriptionList.this, subscription_details.class);
                    intent.putExtras(args);

                    startActivityForResult(intent, 1);



            }
        });
    }
    //Delete function
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(ACTIVITY_NAME, "DELETE!!!");
        if (requestCode == 1 && resultCode != RESULT_CANCELED) {

            String deleteSubID = data.getStringExtra("subID");
            Log.i(ACTIVITY_NAME, "SUB ID TO DELETE: " + deleteSubID);
            FirebaseDatabase.getInstance().getReference("Users").child(loginUsername).child("Subscriptions").child("Sub " + deleteSubID).removeValue();

            //Delete message
            //database.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + " = " + id, null);


            //Restarts activity
            finish();
            startActivity(getIntent());

        }


    }



    //Shows the sub in the listview
    private class SubAdapter extends ArrayAdapter<Subscription>{
        public SubAdapter(Context ctx) {
            super(ctx, 0);
        }
        public int getCount(){
            Log.i(ACTIVITY_NAME, "Length of subs: " + subscriptions.size());
            return subscriptions.size();
        }
        public Subscription getItem(int position){
            return subscriptions.get(position);
        }



        //THIS SHOULD GET FLUSHED OUT MORE, Look nicer + more details.
        //Maybe make it so it can be clicked on and more info displayed.
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = SubscriptionList.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.sub_row, null);

            TextView subName = (TextView)result.findViewById(R.id.subscription_name);
            subName.setText(   getItem(position).name  ); // get the string at position
            TextView subAmount = (TextView)result.findViewById(R.id.subscription_amount);
            subAmount.setText(   String.valueOf(getItem(position).amount)  ); // get the string at position

            return result;
        }
    }


}