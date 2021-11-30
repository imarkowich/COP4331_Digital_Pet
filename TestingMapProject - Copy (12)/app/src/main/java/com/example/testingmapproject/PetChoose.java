package com.example.testingmapproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class PetChoose extends AppCompatActivity {

    // list of animals
    private String[] myPetNames =
            {"Labrador", "Tabby", "Chihuahua", "Russian Blue",
                    "Maltese", "Persian", "German Sheppard", "Sphinx"};
    private String[] myPetIds =
            {"lab", "tabby", "chi", "russian",
                    "maltese", "persian", "shep", "sphinx"};

    private Integer BreedIdx;
    private String pet_string;

    // buttons n such
    ImageButton myLeft;
    ImageButton myRight;
    ImageButton my_choose;
    ImageButton my_menu;
    TextView myBreed;
    ImageView myPet;
    EditText myEdit;

    UserObject myUser;
    ArrayList<WalkObject> myWalks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_choose);

        // get user
        myUser = getIntent().getParcelableExtra("theUser");
        // get walks
        Bundle myBundle = getIntent().getExtras();
        myWalks = (ArrayList<WalkObject>) myBundle.getSerializable("theWalks");

        // get buttons and such
        myLeft = (ImageButton) findViewById(R.id.img_button_L);
        myRight = (ImageButton) findViewById(R.id.img_button_R);
        myBreed = (TextView) findViewById(R.id.tv_breed);
        myPet = (ImageView) findViewById(R.id.img_pet);
        my_choose = (ImageButton) findViewById(R.id.img_btn_choose);
        myEdit = (EditText) findViewById(R.id.editPetName);


        // display pet name
        if(myUser.getPetName() == null){
            myEdit.setText("Pet name here!");
        } else {
            myEdit.setText(myUser.getPetName());
        }
        //Toast.makeText(this, "ID EQUALS: " + myPetIds[0], Toast.LENGTH_LONG).show();
        BreedIdx = 0;

        // maybe display breed
        for(Integer i = 0; i < myPetIds.length; i ++) {
            if (myPetIds[i].equals(myUser.getBreed())) {
                //Toast.makeText(this, "ID EQUALS: " + i, Toast.LENGTH_LONG).show();
                BreedIdx = i;
                break;
            }
        }

        // set initial breed name and image
        myBreed.setText(myPetNames[BreedIdx]);
        Resources res = getResources();
        pet_string = String.format(myPetIds[BreedIdx] + "_sit"); // breed + animation
        myPet.setImageResource(res.getIdentifier(pet_string, "drawable", getPackageName()));

        // left button
        myLeft.setOnClickListener(v -> {
            BreedIdx -= 1;
            if (BreedIdx < 0)
                BreedIdx = myPetNames.length - 1;
            myBreed.setText(myPetNames[BreedIdx]);
            // Resources res = getResources();
            pet_string = String.format(myPetIds[BreedIdx] + "_sit"); // breed + animation
            myPet.setImageResource(res.getIdentifier(pet_string, "drawable", getPackageName()));
        });

        // right button
        myRight.setOnClickListener(v -> {
            BreedIdx = (BreedIdx + 1) % myPetNames.length;
            myBreed.setText(myPetNames[BreedIdx]);
            //Resources res2 = getResources();
            pet_string = String.format(myPetIds[BreedIdx] + "_sit"); // breed + animation
            myPet.setImageResource(res.getIdentifier(pet_string, "drawable", getPackageName()));
        });

        // Choose button
        my_choose.setOnClickListener(v -> {
            // get pet name
            String myPetName = String.valueOf(myEdit.getText());

            // store data
            myUser.setPetName(myPetName);
            myUser.setBreed(myPetIds[BreedIdx]);

            // send data
            String my_pet_data = String.format(myPetName + " " + myPetNames[BreedIdx]);
            Toast.makeText(this, my_pet_data, Toast.LENGTH_LONG).show();
        });


        // set menu button
        my_menu = findViewById(R.id.button_menu_from_choose);
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