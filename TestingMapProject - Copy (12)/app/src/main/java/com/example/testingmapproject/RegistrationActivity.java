package com.example.testingmapproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail; //username not used, email acts as username
    private Button regButton;
    private TextView userLogin;

    //File base authenticator
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener (new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                if (validate()) {

                    //upload to database

                    String user_email = userEmail.getText().toString().trim();

                   // String user_username = userName.getText().toString().trim(); //?

                    String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    //firebaseAuth.createUserWithEmailAndPassword(user_username, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(RegistrationActivity.this, "Registration Successful~!", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));

                            }

                            else {

                                Toast.makeText(RegistrationActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }



            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick (View view){


                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));

            }

        });

    }


    private void setupUIViews() {

     //   userName = findViewById(R.id.etUsername); //et

        userPassword = findViewById(R.id.etPassword); //et
        userEmail = findViewById(R.id.etEmail); //et
        regButton = findViewById(R.id.buttonRegister); //but
        userLogin = findViewById(R.id.tvUserLogin); //textview

    }

    private Boolean validate() {

        boolean result = false;

       // String name = userName.getText().toString();

        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if(password.isEmpty() || email.isEmpty()) {

      //  if(name.isEmpty() || password.isEmpty() || email.isEmpty()) {

            Toast.makeText(this, "Please enter all the details required.", Toast.LENGTH_SHORT).show(); //this works

        }

        else {

            result = true;

        }

        return result;

    }




}