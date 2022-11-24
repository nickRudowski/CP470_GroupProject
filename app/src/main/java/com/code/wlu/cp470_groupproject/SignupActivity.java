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

public class SignupActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "SignupActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        Button register = findViewById(R.id.signup_button);
        TextView edit_email = findViewById(R.id.edit_register_email);
        TextView edit_password = findViewById(R.id.edit_register_password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_string = edit_email.getText().toString();
                String password_string = edit_password.getText().toString();
                createAccount(email_string, password_string);
            }
        });
    }

    private void createAccount(String email, String password) {
//        if (!validateForm()) {
//            return;
//        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
//                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ACTIVITY_NAME, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
//                    updateUI(null);
                }
            }
        });
    }
}

