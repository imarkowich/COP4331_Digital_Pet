package com.example.testingmapproject;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.testingmapproject.databinding.ActivityMapsBinding;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    // default map stuff
    // go to user's location https://www.youtube.com/watch?v=boyyLhXAZAQ&t=3s
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    Location currentLocation;
    FusedLocationProviderClient myFused;
    private static final int REQUEST_CODE = 101;

    // add time, buttons, etc.
    SensorManager my_sensor_manager;
    Chronometer my_chronometer;
    Button my_end;
    TextView my_tv_steps;
    TextView my_tv_distance;
    TextView my_tv_date;
    ImageView myPet;

    UserObject myUser;
    ArrayList<WalkObject> myWalks;

    // vars
    boolean running = false;
    float total_steps = 0;
    float prev_total_steps;
    float current_distance;
    int current_steps;
    int iter = 0;
    long timer_time = Long.MIN_VALUE;
    public String pet_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // default map stuff up until ***
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        // get user
        myUser = getIntent().getParcelableExtra("theUser");
        // get walks
        Bundle myBundle = getIntent().getExtras();
        myWalks = (ArrayList<WalkObject>) myBundle.getSerializable("theWalks");


        // *** extra map stuff
        myFused = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        // assign buttons and such
        my_chronometer = findViewById(R.id.chronometer_solo); // assign values
        my_end = findViewById(R.id.button_end_walk_solo);
        myPet = findViewById(R.id.img_pet);

        // set pet image
        String currentBreed = myUser.getBreed();
        Resources res = getResources();
        pet_string = String.format(currentBreed + "_walk"); // breed + animation
        myPet.setImageResource(res.getIdentifier(pet_string, "drawable", getPackageName()));

        // load and reset steps
        loadData();
        //prev_total_steps = total_steps;
        //my_tv_steps.setText("0");
        //saveData();

        my_sensor_manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        my_tv_steps = (TextView) findViewById(R.id.tv_steps_solo);
        my_tv_distance = (TextView) findViewById(R.id.tv_duration_solo);
        my_tv_date = (TextView) findViewById(R.id.tv_walk_date);

        // clock
        my_chronometer.setBase(SystemClock.elapsedRealtime()); // reset clock
        my_chronometer.start(); // start clock
        timer_time = Long.MIN_VALUE; // reset timer_time

        // get permission from user to use physical data
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        // get date from when walk is started
        Calendar my_calendar = Calendar.getInstance();
        String my_date = DateFormat.getInstance().format(my_calendar.getTime());
        my_tv_date.setText(String.format("Walk Started %s", my_date));

        // when end button clicked,
        // stop clock, record walk data, transfer data into user account, go to main menu
        my_end.setOnClickListener(v -> {
            // clock
            my_chronometer.stop(); // stop clock
            timer_time = SystemClock.elapsedRealtime() - my_chronometer.getBase(); // get timer time
            timer_time /= 1000;

            // show walk data
            @SuppressLint("DefaultLocale") String my_walk_data = String.format("Date: %s, Duration: %s, Steps: %d, Distance: %f", my_date, getTimeString(timer_time), current_steps, current_distance);

            // store walk data
            WalkObject walk = new WalkObject(my_date, (int) timer_time, current_steps, current_distance);
            myWalks.add(walk);

            // temporary: display data
            for (int i=0; i < 1; i++)
            {
                Toast.makeText(this, my_walk_data, Toast.LENGTH_LONG).show();
            }

            // reset steps
            prev_total_steps = total_steps;
            my_tv_steps.setText("0");
            saveData();

            // go to main menu now
            openMainMenu();
        });


        // **** buttons for other fragments ***
        // or maybe not
    }

    // ger user's previous location

    private void fetchLastLocation() {
        //Toast.makeText(this, "Fetch!!!", Toast.LENGTH_LONG).show();
        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> myTask = myFused.getLastLocation();

        myTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    //getApplicationContext(), not this
                    //.makeText(getApplicationContext(), currentLocation.getLatitude() + " YO " + currentLocation.getLongitude(), Toast.LENGTH_LONG).show();

                    SupportMapFragment mySupMap = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.map);
                    mySupMap.getMapAsync(MapsActivity.this);

                }
            }
        });
    }


    // go to main menu fragment
    public void openMainMenu() {
        Intent myIntent = new Intent(this, MainMenu.class);
        // bundle
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("theWalks", myWalks);
        // add data
        myIntent.putExtras(myBundle);
        myIntent.putExtra("theUser", myUser);
        startActivity(myIntent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mMap = googleMap;

        LatLng myLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());


        googleMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 18));

        // marker testing
        /*
        iter ++;
        String myNote = String.format("iter: %d", iter);
        MarkerOptions myMarkerOptions = new MarkerOptions().position(myLatLng).title(myNote);
        googleMap.addMarker(myMarkerOptions);
        googleMap.addMarker(myMarkerOptions);
        */

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }


    // these are due to event listner
    @Override
    public void onResume() {
        super.onResume();
        running = true;

        //Toast.makeText(this, "Resumed Step Tracking.", Toast.LENGTH_LONG).show();

        Sensor my_count_sensor = my_sensor_manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (my_count_sensor != null) {
            my_sensor_manager.registerListener(this, my_count_sensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "No Step Sensor was found.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
        // reset total steps?
        my_sensor_manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Toast.makeText(this, "YO MAN, YO.", Toast.LENGTH_LONG).show();
        if (running) {
            // update steps
            total_steps = event.values[0];
            current_steps = (int) (total_steps - prev_total_steps);
            my_tv_steps.setText(String.valueOf(current_steps));

            // update distance
            current_distance = round2(getDistance(current_steps));
            my_tv_distance.setText(String.valueOf(current_distance));

            // update map
            fetchLastLocation();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("key1", prev_total_steps);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        float my_saved_number = sharedPreferences.getFloat("key1", 0f);
        Log.d("MainActivity", String.valueOf(my_saved_number));
        prev_total_steps = my_saved_number;

    }

    // distance functions
    public float getDistance(int steps){
        float distance = (float) (steps * 68) / (float) 100000; // kilometers
        distance *= 3280.8399; // km to feet     (0.62137119 m to mi)
        return distance;
    }

    public static float round2(float f) {
        final float factor = 1e2f;
        if (f < Integer.MIN_VALUE/factor || f > Integer.MAX_VALUE/factor)
            return f;
        return Math.round(f * factor) / factor;
    }

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

/*
    public void resetSteps() {
        my_tv_steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Long click to reset steps", Toast.LENGTH_LONG).show();
            }
        });
    }*/

