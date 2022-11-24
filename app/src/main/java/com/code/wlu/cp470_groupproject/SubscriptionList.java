package com.code.wlu.cp470_groupproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SubscriptionList extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "SubscriptionList";
    public ArrayList<Subscription> subscriptions;
    public ListView subView;
    public Button AddSubButton;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public SubDatabaseHelper db_helper;
    public static SQLiteDatabase database;
    public Cursor cursor;
    private String[] cols = {SubDatabaseHelper.KEY_MESSAGE, SubDatabaseHelper.KEY_ID};

    public Subscription read(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            Subscription dataobj = (Subscription ) ois.readObject();
            return dataobj ;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] makebyte(Subscription modeldata) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(modeldata);
            byte[] subAsBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(subAsBytes);
            return subAsBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcription_window);
        subView = findViewById(R.id.subView);
        AddSubButton  = findViewById(R.id.AddSubButton);
        Log.d("OnCreate", "SubList start");

        //DB Creation
        db_helper = new SubDatabaseHelper(this);
        database = db_helper.getWritableDatabase();
        Log.i(ACTIVITY_NAME, "Created DB -- " + database + "\nDB Open: " + database.isOpen());
        cursor = database.query(db_helper.TABLE_NAME, cols, null, null, null, null, null);


        //List of sub objects
        subscriptions = new ArrayList<Subscription>();

        //in this case, “this” is the SubWindow, which is-A Context object
        SubAdapter messageAdapter =new SubAdapter( this );
        subView.setAdapter (messageAdapter);


        cursor.moveToFirst();
        //Iterate through cursor to get messages
        while (!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + read(cursor.getBlob(cursor.getColumnIndex(SubDatabaseHelper.KEY_MESSAGE))).name);

            //Also, print an information message about the Cursor:
            Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());
            Log.i(ACTIVITY_NAME, "Sub name =" + read(cursor.getBlob(cursor.getColumnIndex(SubDatabaseHelper.KEY_MESSAGE))).name);
            subscriptions.add(read(cursor.getBlob(cursor.getColumnIndex(SubDatabaseHelper.KEY_MESSAGE))));
            cursor.moveToNext();
        }

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "COLUMN: " + cursor.getColumnName(i));
            cursor.moveToNext();
        }
        cursor.close();


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
                                //edit = view2.findViewById(R.id.dialog_renewal_date);
                                CheckBox type = view2.findViewById(R.id.dialog_payment_type_checkBox);
                                sub.automatic_renewal = type.isChecked();
                                sub.name = name;
                                sub.amount = amt;
                                sub.payment_plan = plan;

                                //Start of DatePicker process
                                DatePicker dateEdt = view2.findViewById(R.id.dialog_renewal_date);
                                ///String date_in = dateEdt.getText().toString();
                                // on below line we are adding click listener
                                // for our pick date button
                                dateEdt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // on below line we are getting
                                        // the instance of our calendar.
                                        Calendar c = Calendar.getInstance();
                                        // on below line we are getting
                                        // our day, month and year.
                                        int year = c.get(Calendar.YEAR);
                                        int month = c.get(Calendar.MONTH);
                                        int day = c.get(Calendar.DAY_OF_MONTH);
                                        // on below line we are creating a variable for date picker dialog.
                                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                                // on below line we are passing context.
                                                SubscriptionList.this,
                                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                                mDateSetListener,
                                                // on below line we are passing year,
                                                // month and day for selected date in our date picker.
                                                year, month, day);
                                        // at last we are calling show to
                                        // display our date picker dialog.
                                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        datePickerDialog.show();
                                    }
                                });
                                //Once datepicker is set, create date object and update sub.
                                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // on below line we are setting date to our edit text.
                                        //dateEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        sub.renewal_date = new Date(year, monthOfYear, dayOfMonth);
                                    }
                                };
                                //Once the sub object is fully created and customized add it to list of subscriptions.

                                subscriptions.add(sub);

                                ContentValues values = new ContentValues();
                                values.put(SubDatabaseHelper.KEY_MESSAGE, makebyte(sub));
                                long insertId = database.insert(SubDatabaseHelper.TABLE_NAME, null, values);
                                String cmd = SubDatabaseHelper.KEY_ID + " = " + insertId;
                                Cursor cursor = database.query(SubDatabaseHelper.TABLE_NAME, cols, cmd, null, null, null, null);
                                cursor.moveToFirst();
                                cursor.close();

                                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/

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

                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/


            }
        });
    }

    //Item doesn't show up until button is pressed again. NEED TO FIX
    private class SubAdapter extends ArrayAdapter<Subscription>{
        public SubAdapter(Context ctx) {
            super(ctx, 0);
        }
        public int getCount(){
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