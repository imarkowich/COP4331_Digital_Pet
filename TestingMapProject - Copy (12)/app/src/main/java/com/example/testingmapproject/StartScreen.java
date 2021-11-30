package com.example.testingmapproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StartScreen extends AppCompatActivity {

    UserObject myUser;
    ArrayList<WalkObject> myWalks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Toast.makeText(this, "START SCREEN", Toast.LENGTH_LONG).show();

        // make user
        myUser = new UserObject(null, "lab", 4);
        myWalks = new ArrayList<>();


        // go to main menu
        Intent myIntent = new Intent(this, MainMenu.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        // go
        startActivity(myIntent);


    }
}