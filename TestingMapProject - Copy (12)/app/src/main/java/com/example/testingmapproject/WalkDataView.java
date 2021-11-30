package com.example.testingmapproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WalkDataView extends AppCompatActivity {

    ImageButton my_menu;
    Spinner my_spinner;
    TextView my_duration;
    TextView my_steps;
    TextView my_distance;

    UserObject myUser;
    ArrayList<WalkObject> myWalks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_data_view);


        // set buttons n such
        my_menu = findViewById(R.id.button_menu_from_walkdata);
        my_spinner = findViewById(R.id.walk_spinner);
        my_duration = findViewById(R.id.tv_duration_value);
        my_steps = findViewById(R.id.tv_steps_value);
        my_distance = findViewById(R.id.tv_distance_value);

        // get user
        myUser = getIntent().getParcelableExtra("theUser");
        // get walks
        Bundle myBundle = getIntent().getExtras();
        myWalks = (ArrayList<WalkObject>) myBundle.getSerializable("theWalks");


        // get a list of dates from walks
        List<String> walkDates = new ArrayList<>();
        walkDates.add(0, "Select a walk from the list");

        // get dates
        for (int i = 0; i < myWalks.size(); i ++) {
            walkDates.add(myWalks.get(i).getDate());
        }


        // set array dropdown functionality
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, walkDates);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        my_spinner.setAdapter(arrayAdapter);

        // select a date
        my_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // ignore the default text
                if (parent.getItemAtPosition(position).equals("Select a walk from the list")){ }
                // display chosen date's info
                else {
                    String item = parent.getItemAtPosition(position).toString();

                    // use position - 1 to access index of array (for walk data)
                    my_duration.setText(getTimeString(myWalks.get(position - 1).getDuration()));
                    my_steps.setText(String.valueOf(myWalks.get(position - 1).getSteps()));
                    my_distance.setText(String.valueOf(myWalks.get(position - 1).getDistance()));

                    //Toast.makeText(parent.getContext(),"Selected DURATION: " + myWalks.get(0).getDuration(), Toast.LENGTH_SHORT).show();

                    //Toast.makeText(parent.getContext(),"Selected: " + item + " - position " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // set menu button
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

    // convert seconds
    public static String getTimeString(long s) {
        // extract seconds, minutes, hours
        long sec = s % 60;
        long min = (s / 60) % 60;
        long hours = (s / 60) / 60;

        // hours, minutes, seconds
        @SuppressLint("DefaultLocale") String time_text = String.format("%d:%d:%d", hours, min, sec);
        return time_text;
    }
}