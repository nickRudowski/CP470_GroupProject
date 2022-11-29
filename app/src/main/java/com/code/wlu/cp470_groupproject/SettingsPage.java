package com.code.wlu.cp470_groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class SettingsPage extends AppCompatActivity {

    public String loginUsername;
    public String ACTIVITY_NAME = "SettingsPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        Bundle bundleFromLogin = getIntent().getExtras();
        if(bundleFromLogin!=null){
            loginUsername = bundleFromLogin.getString("UserName").replaceAll("[.]", "");
            Log.i(ACTIVITY_NAME, "LoginUsername: " + loginUsername);
        }

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