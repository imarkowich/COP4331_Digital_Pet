package com.example.testingmapproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PetMood extends AppCompatActivity {

    // UI elements
    ImageButton my_menu;
    ImageView myPet;
    ImageView myHeart5;
    ImageView myHeart4;
    ImageView myHeart3;
    ImageView myHeart2;
    ImageView myHeart1;
    TextView my_mood;

    UserObject myUser;
    ArrayList<WalkObject> myWalks;

    // arrays
    int [] images = {R.drawable.heart, R.drawable.heart_black};
    String [] messages = {"Your pet is happy!", "Your pet is alright.", "Your pet could use a walk."};
    String [] animations = {"_happy", "_sit", "_sad"};

    // values
    public Integer currentMood;
    public String currentBreed;
    // image values
    public String pet_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_mood);

        // get user
        myUser = getIntent().getParcelableExtra("theUser");
        // get walks
        Bundle myBundle = getIntent().getExtras();
        myWalks = (ArrayList<WalkObject>) myBundle.getSerializable("theWalks");

        // get breed
        currentBreed = myUser.getBreed();

        // get mood based on time since previous walk
        // if no walk is present, set hearts 2
        if (myWalks.size() == 0) {
            currentMood = 2;
        } else {
            // get most recent walk date
            String my_date2 = myWalks.get(myWalks.size() - 1).getDate();
            // get current date
            Calendar my_calendar = Calendar.getInstance();
            String my_date = DateFormat.getInstance().format(my_calendar.getTime());

            // SimpleDateFormat shenanigans
            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yy h:mm a");
            try {
                // get hours between dates
                Date date1 = dates.parse(my_date); // parse dates
                Date date2 = dates.parse(my_date2);
                long diff = Math.abs(date1.getTime() - date2.getTime()); // diff
                long diffHours = diff / (60 * 60 * 1000); // hours
                //String hourDiff = Long.toString(diffHours);
                //Toast.makeText(this, "HourDiff: " + hourDiff, Toast.LENGTH_LONG).show();

                // set mood based on hours
                if (diffHours <= 6) {
                    currentMood = 5;
                } else if (diffHours > 6 && diffHours < 12) {
                    currentMood = 4;
                } else if (diffHours >= 12 && diffHours < 18) {
                    currentMood = 3;
                } else if (diffHours >= 18 && diffHours < 24) {
                    currentMood = 2;
                } else if (diffHours >= 24 && diffHours < 30) {
                    currentMood = 1;
                } else if (diffHours >= 36) {
                    currentMood = 0;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        // set hearts, txtV, imgV, etc.
        myHeart5 = findViewById(R.id.heartDisplay5);
        myHeart4 = findViewById(R.id.heartDisplay4);
        myHeart3 = findViewById(R.id.heartDisplay3);
        myHeart2 = findViewById(R.id.heartDisplay2);
        myHeart1 = findViewById(R.id.heartDisplay1);
        my_mood = findViewById(R.id.tv_mood_state);
        myPet = findViewById(R.id.img_pet);
        my_menu = findViewById(R.id.button_menu_from_mood);

        // set healthy message
        my_mood.setText(messages[0]);

        // set healthy image dynamically
        Resources res = getResources();
        pet_string = String.format(currentBreed + animations[0]); // breed + animation
        myPet.setImageResource(res.getIdentifier(pet_string, "drawable", getPackageName()));


        // set heart images, mood text, and pet image
        if (currentMood < 5)
            myHeart5.setImageResource(images[1]); // bad
        else
            myHeart5.setImageResource(images[0]); // good
        if (currentMood < 4) {
            myHeart4.setImageResource(images[1]); // bad
            my_mood.setText(messages[1]); // set alright message & image
            // set sit pet image
            res = getResources();
            pet_string = String.format(currentBreed + animations[1]); // breed + animation
            myPet.setImageResource(res.getIdentifier(pet_string, "drawable", getPackageName()));
        }
        else
            myHeart4.setImageResource(images[0]); // good
        if (currentMood < 3) {
            myHeart3.setImageResource(images[1]); // bad
            my_mood.setText(messages[2]); // set bad message & image
            // set sad pet image
            res = getResources();
            pet_string = String.format(currentBreed + animations[2]); // breed + animation
            myPet.setImageResource(res.getIdentifier(pet_string, "drawable", getPackageName()));
        }
        else
            myHeart3.setImageResource(images[0]); // good
        if (currentMood < 2)
            myHeart2.setImageResource(images[1]); // bad
        else
            myHeart2.setImageResource(images[0]); // good
        if (currentMood < 1)
            myHeart1.setImageResource(images[1]); // bad
        else
            myHeart1.setImageResource(images[0]); // good

        // make sure mood value is in bounds
        if(currentMood < 0)
            currentMood = 0;
        if (currentMood > 5)
            currentMood = 5;

        // set button
        my_menu.setOnClickListener(v -> {
            openMainMenu();
        });
    }

    // fragment go to method
    private void openMainMenu() {
        Intent myIntent = new Intent(this, MainMenu.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        startActivity(myIntent);
    }
}