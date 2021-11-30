package com.example.testingmapproject;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;

    private TextView userRegistration;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Username = findViewById(R.id.Username); //et
        Password = findViewById(R.id.Password); //et
        Info = findViewById(R.id.NoAttempts); //tv
        Login = findViewById(R.id.ButtonLogin); //button

        //userRegistration = findViewById(R.id.tvUserLogin); //tv
        userRegistration = findViewById(R.id.Register); //the correct one


       // Info.setText("Number of Attempts Remaining: 5"); //initial attempts displayed

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser(); //check that user is authenticated

       /* if (user != null) { //check if user is already logged in

            finish();
            //startActivity(new Intent(MainActivity.this, SecondActivity.class));

        } */



        Login.setOnClickListener (new View.OnClickListener() {

            @Override

            public void onClick (View view) {

                validate (Username.getText().toString(), Password.getText().toString());

            }

        });

        userRegistration.setOnClickListener (new View.OnClickListener() {

            @Override

            public void onClick (View view) {

                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));

            }


        });

    }


    private void validate(String userName, String userPassword) { //user and pass must match



        progressDialog.setMessage("Have fun with your pet!");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    progressDialog.dismiss();

                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainMenu.class));

                }

                else {

                    Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();

                    counter--;

                    Info.setText("Number of Attempts Remaining: " + counter); //initial attempts displayed

                    progressDialog.dismiss();

                    if (counter == 0) { //login button disabled

                        Login.setEnabled(false);

                    }

                }

            }
        });



    }


}