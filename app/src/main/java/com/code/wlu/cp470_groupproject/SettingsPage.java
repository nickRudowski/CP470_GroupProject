package com.code.wlu.cp470_groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SettingsPage extends AppCompatActivity {

    public String loginUsername;
    public String ACTIVITY_NAME = "SettingsPage";
    public TextView UsernameTitle;
    public TextView NotifsTitle;
    public int pref = 2;
    public Button EditNotifsButton;
    public FirebaseDatabase Database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        UsernameTitle = findViewById(R.id.UsernameTitle);
        NotifsTitle = findViewById(R.id.NotifsTitle);
        EditNotifsButton = findViewById(R.id.EditNotifsButton);

        Bundle bundleFromLogin = getIntent().getExtras();
        if(bundleFromLogin!=null){
            loginUsername = bundleFromLogin.getString("UserName").replaceAll("[.]", "");
            pref = bundleFromLogin.getInt("pref");
            Log.i(ACTIVITY_NAME, "LoginUsername: " + loginUsername);
//            DatabaseReference DatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(loginUsername).child("UserDetails").child("Notification pref");
//            Log.i(ACTIVITY_NAME, "Notifs Pref Test "+DatabaseRef.getKey());
        }
        NotifsTitle.setText("Notification Preferences: " + pref);
        UsernameTitle.setText("User Login: "+ loginUsername);
        // Start Edit Notification Fragment

        // start fragment for the Edit form when user clicks the button
        EditNotifsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NotificationFragment fragment = (NotificationFragment) fragmentManager.findFragmentByTag("tag");
                if(fragment == null) {
                    Log.i(ACTIVITY_NAME,"Adding Fragment");
                    Bundle message = new Bundle();
                    message.putString("user", loginUsername);
                    fragment = new NotificationFragment();
                    fragment.setArguments(message);
                    fragmentTransaction.add(R.id.EditNotifFragment, fragment, "tag");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                } else {
                    Log.i(ACTIVITY_NAME,"Removing Fragment");
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                }
                fragmentTransaction.commit();
            }
        });
        // get the result back from the Fragment to Update the UI
        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("updateUI");
                NotifsTitle.setText("Notification Preferences: "+ result);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        switch(mi.getItemId()){
            case R.id.Choice1:
                Log.d("Toolbar", "Option 1 selected");
                Intent intent = new Intent(SettingsPage.this, SubscriptionList.class);
                intent.putExtra("UserName", loginUsername);
                startActivity(intent);

                break;
            case R.id.Choice2:
                Log.d("Toolbar", "Option 2 selected");
                Snackbar.make(findViewById(R.id.Choice1), "Settings View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                break;

            case R.id.Choice3:
                Log.d("Toolbar", "Option 3 selected");
                Snackbar.make(findViewById(R.id.Choice3), "Log out", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Replace with Settings Activity
                Intent logout_intent = new Intent(SettingsPage.this, LoginActivity.class);
                startActivity(logout_intent);


                break;

            case R.id.settings:
                Log.d("Toolbar", "Settings selected");
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(this , "Creators: Nicholas Rudowski, Syed Raza, Mustafa Syed, Alysha Stone, Caleb Welcome", duration);
                toast.show(); //display your message box
                break;
        }
        return true;
    }
}