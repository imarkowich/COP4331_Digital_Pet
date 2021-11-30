package com.example.testingmapproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;

public class SecondActivity extends AppCompatActivity {



    private EditText PetName;
    private EditText userName;



    private Button btSubmitPetInfo;
    private Spinner Breed;



    private FirebaseAuth firebaseAuth;

    DatabaseReference petDbRef;


    Timer timer;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        firebaseAuth = FirebaseAuth.getInstance();

        //Get UI elements
        PetName = findViewById(R.id.PetName); //textview works, leave alone

        userName = findViewById(R.id.userName);


        //dropdown list for breed
        Breed = findViewById(R.id.Breed);
        String[] breeds = {"Chihuahua", "Labrador Retriever", "German Sheppard", "Corgi", "Tabby", "Sphinx", "Persian", "Russian Blue"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, breeds);
        Breed.setAdapter(arrayAdapter);

        //The UI itself is fine

        petDbRef = FirebaseDatabase.getInstance().getReference().child("Pet");


        btSubmitPetInfo = findViewById(R.id.btSubmitPetInfo);


        //timer below

       /* timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

             //   Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
              //  startActivity(intent);
              //  finish();
                //above three lines refer to moving on to the next page, don't use it. Just an example



            }
        }, 5000); */


        //timer above


        btSubmitPetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertPetInfo();

            }

        });

    }

    //helper function for putting pet info in database

    private void insertPetInfo() {

        String username = userName.getText().toString();
        String petName = PetName.getText().toString();
        String breed = Breed.getSelectedItem().toString();

        PetInfo petInfo = new PetInfo(username, petName, breed);

        petDbRef.push().setValue(petInfo);

        Toast.makeText(SecondActivity.this, "Let's get moving!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(SecondActivity.this, MainMenu.class)); //go to third screen

    }





//Logout functions below

    private void Logout() {

        firebaseAuth.signOut();

        finish();

        startActivity(new Intent(SecondActivity.this,MainActivity.class));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {

            case R.id.logoutMenu: {

                Logout(); //at top right of screen, works

            }


        }

        return super.onOptionsItemSelected(item);
    }


}