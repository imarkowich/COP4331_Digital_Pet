package com.example.testingmapproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiplayerMenu extends AppCompatActivity {

    Spinner my_spinner;
    ImageButton my_menu;
    Button my_start;

    UserObject myUser;
    ArrayList<WalkObject> myWalks;
    String breed2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_menu);

        // assign buttons n such
        my_spinner = findViewById(R.id.multi_spinner);
        my_menu = findViewById(R.id.button_menu_from_multi);
        my_start = findViewById(R.id.button_start);

        // breed ids
        String[] myPetIds =
                {"lab", "tabby", "chi", "russian",
                        "maltese", "persian", "shep", "sphinx"};

        // get a list of breeds
        List<String> breeds = new ArrayList<>(
                Arrays.asList("Labrador", "Tabby", "Chihuahua", "Russian Blue",
                        "Maltese", "Persian", "German Sheppard", "Sphinx"));

        // get user
        myUser = getIntent().getParcelableExtra("theUser");
        // get walks
        Bundle myBundle = getIntent().getExtras();
        myWalks = (ArrayList<WalkObject>) myBundle.getSerializable("theWalks");


        // set array dropdown functionality
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, breeds);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        my_spinner.setAdapter(arrayAdapter);

        // select a breed
        my_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // breeds set
                String item = parent.getItemAtPosition(position).toString();
                breed2 = myPetIds[position];
                Toast.makeText(parent.getContext(),"BREED: " + breeds.get(position), Toast.LENGTH_SHORT).show();

                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // set start button
        my_start.setOnClickListener(v -> {
            openMultiWalk();
        });


        // set menu button
        my_menu.setOnClickListener(v -> {
            openMainMenu();
        });
    }

    // multi walk
    private void openMultiWalk() {
        Intent myIntent = new Intent(this, MapsActivityMulti.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        myIntent.putExtra("theBreed2", breed2);
        startActivity(myIntent);
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