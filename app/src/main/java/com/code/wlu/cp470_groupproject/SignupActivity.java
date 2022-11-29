package com.code.wlu.cp470_groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "SignupActivity";

    private FirebaseAuth mAuth;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference DatabaseRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        Button register = findViewById(R.id.signup_button);
        TextView edit_email = findViewById(R.id.edit_register_email);
        TextView edit_password = findViewById(R.id.edit_register_password);
        TextView edit_name = findViewById(R.id.edit_register_name);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_string = edit_email.getText().toString();
                String password_string = edit_password.getText().toString();
                String name = edit_name.getText().toString();
                createAccount(email_string, password_string, name);
            }
        });
    }

    private void createAccount(String email, String password, String name) {
//        if (!validateForm()) {
//            return;
//        }
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                DatabaseRef.child(email.replaceAll("[.]", "")).child("UserDetails").child("Name").setValue(name);
                                DatabaseRef.child(email.replaceAll("[.]", "")).child("UserDetails").child("Notification Pref").setValue("2");
                                Intent intent = new Intent(SignupActivity.this, SubscriptionList.class);
                                intent.putExtra("UserName", email);
                                startActivity(intent);
//                    updateUI(user);
                            }
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(ACTIVITY_NAME, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(SignupActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
////                    updateUI(null);
//                            }
                        }
                    });
        }catch (Exception e){
            Toast.makeText(SignupActivity.this, "Please Provide Valid Arguments", Toast.LENGTH_LONG).show();
        }
    }
}

