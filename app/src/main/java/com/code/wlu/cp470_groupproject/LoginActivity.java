package com.code.wlu.cp470_groupproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;



public class LoginActivity extends AppCompatActivity {



    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    public void print(String in_text){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this , in_text, duration); //this is the ListActivity
        toast.show(); //display your message box
        return;
    }
    protected static final String ACTIVITY_NAME = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }

        Log.i(ACTIVITY_NAME, "In onCreate()");

        //Login button object
        Button login_button = findViewById(R.id.login_button);
        //Signup button object
        Button signup_button = findViewById(R.id.signup_button);
        //Creates preferences
        SharedPreferences prefs = getSharedPreferences("DefaultEmail", MODE_PRIVATE);
        //Email string
        String default_email = getResources().getString(R.string.default_email);
        //Text for login
        String email = prefs.getString(String.valueOf(R.string.shared_email),default_email);
        //Set that text to the shared email string
        EditText login = findViewById(R.id.login_name);
        login.setText(email);
        //Password
        EditText password = findViewById(R.id.password_name);

        //On click of button
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update the shared reference with new string input
                SharedPreferences setPrefs = getSharedPreferences("DefaultEmail", MODE_PRIVATE);
                SharedPreferences.Editor myEditor = setPrefs.edit();
                //myEditor.clear();
                myEditor.putString(String.valueOf(R.string.shared_email), login.getText().toString());
                myEditor.commit();

                Log.i(ACTIVITY_NAME, "Username in login: " + login.getText().toString());

                if(password.length() > 0 && Patterns.EMAIL_ADDRESS.matcher(login.getText()).matches()){
                    //Intent to launch main activity

                    signIn(login.getText().toString(), password.getText().toString());
                    Log.i(ACTIVITY_NAME, "Username in login: " + login.getText().toString());
                    Log.i(ACTIVITY_NAME, "Username in login: " + login.getText().toString());

                }
            }
        });
        //On click of button
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to launch main activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }


    public void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    public void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    private void signIn(String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, SubscriptionList.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(LoginActivity.this, "Credentials Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch (Exception e){
            Toast.makeText(LoginActivity.this, "Please Provide Valid Arguments", Toast.LENGTH_LONG).show();
        }
    }
}






