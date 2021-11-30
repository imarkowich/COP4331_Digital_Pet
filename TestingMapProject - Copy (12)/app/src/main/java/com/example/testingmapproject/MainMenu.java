package com.example.testingmapproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainMenu extends AppCompatActivity {

    // buttons and such
    Button my_walk_solo;
    Button my_pet_choose;
    Button my_pet_mood;
    Button my_walkdata;
    Button my_walk_multi;

    UserObject myUser;
    ArrayList<WalkObject> myWalks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        // set buttons
        my_walk_solo = findViewById(R.id.button_start_solo);
        my_pet_choose = findViewById(R.id.button_pet_choose);
        my_pet_mood = findViewById(R.id.button_pet_mood);
        my_walkdata = findViewById(R.id.button_walkdata);
        my_walk_multi = findViewById(R.id.button_start_multi);


        // get user
        myUser = getIntent().getParcelableExtra("theUser");
        // get walks
        Bundle myBundle = getIntent().getExtras();
        myWalks = (ArrayList<WalkObject>) myBundle.getSerializable("theWalks");


        // go to fragments
        my_walk_solo.setOnClickListener(v -> {
            openMapsActivity();
        });

        my_walk_multi.setOnClickListener(v -> {
            openMultipayer();
        });

        my_pet_choose.setOnClickListener(v -> {
            openPetChoose();
        });

        my_pet_mood.setOnClickListener(v -> {
            openPetMood();
        });

        my_walkdata.setOnClickListener(v -> {
            openWalkdata();
        });
    }



    // go to fragment methods
    private void openMapsActivity() {
        Intent myIntent = new Intent(this, MapsActivity.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        startActivity(myIntent);
    }

    private void openMultipayer() {
        Intent myIntent = new Intent(this, MultiplayerMenu.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        startActivity(myIntent);
    }

    private void openPetChoose() {
        Intent myIntent = new Intent(this, PetChoose.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        startActivity(myIntent);
    }

    private void openPetMood() {
        Intent myIntent = new Intent(this, PetMood.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        startActivity(myIntent);
    }

    private void openWalkdata() {
        Intent myIntent = new Intent(this, WalkDataView.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        startActivity(myIntent);
    }
}