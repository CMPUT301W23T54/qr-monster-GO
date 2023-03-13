package com.example.qr_monster_go;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
//import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.SharedPreferencesKt;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * This activity is where the user can scan a code
 * and add it to their account
 *
 * Implements ScanResultReceiver to easily retrieve
 * data from the scanning fragment
 *
 * Triggers the getLocation dialog and function (will be refactored into own class soon)
 */
public class ScanCodeActivity extends AppCompatActivity implements ScanResultReceiver {
    Button scanButton;
    ImageButton returnButton;

    boolean addLocation = false;
    private LocationRequest locationRequest;
    private String location;
    private QRCode code;
    boolean addImage = false;

    /**
     * This function reads the results of the code scanning fragment
     * and displays a Toast with the content of the code then creates
     * a new QRCode object with the SHA-256 hash of 'content'
     *
     * @param codeFormat
     *      this is the type of code that was scanned(String)
     * @param content
     *      this is the contents contained in the code that was scanned(String)
     */
    @Override
    public void scanResultData(String codeFormat, String content) {
        if (content != null) {
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();

            // generate SHA-256 hash of code
            String hashValue = Hashing.sha256()
                    .hashString(content, StandardCharsets.UTF_8)
                    .toString();

            // create new QRCode object from the contents of the scanned code
            code = new QRCode(hashValue);

            CodeDataStorageController dc = new CodeDataStorageController(new QrMonsterGoDB());

            if (dc.isCodeAlreadyScanned(code.getCode())) {
                // 1. check if player has already scanned code
                // 2. if not: add player to codes player list here
            }
            else {
                // add username to codes player list then add code to the database
                code.addPlayer(getIntent().getExtras().getString("username"));
                dc.addElement(code);
            }
        }
        else {
            // display toast with No Results message
            Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
        }
    }

//    @Override --implement later--
//    public void setLocationChoice() {
//        this.addLocation = true;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        scanButton = findViewById(R.id.scan_code_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode(); // Assumes new code is scanned each time

                // Create location dialog fragment
                DialogFragment confirmlocationdialog = new ConfirmLocationDialog();
                confirmlocationdialog.show(getSupportFragmentManager(), "location");
                // Bug: if GPS is not enabled, user has to re-scan code

            }
        });

        // return to home page if back button is pressed
        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScanCodeActivity.this, HomePageActivity.class));
            }
        });
    }


    private void scanCode() {
        // create new ScanFragment to scan a code
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        ScanFragment scanFragment = new ScanFragment();
        fragmentTransaction.add(R.id.scanFragment,scanFragment);
        fragmentTransaction.commit();
    }


    /*********************************************************************/
    // Location Methods (will be refactored later)
    // Credit for precise location method @TechnicalCoding
    // https://www.youtube.com/watch?v=mbQd6frpC3g
    /*********************************************************************/

    /**
     * This function adds the geolocation to the database
     */
    public void setCurrentLocation() {
        String gpslocation = getLocation();

        code.geolocation = gpslocation;

        // Add
    }
    /**
     * This function generates the geolocation of the user.
     * It checks to see if GPS is enabled and also requests for location permissions if necessary
     *
     * @return A string formatted as "latitudeXXlongitude"
     */
    private String getLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Request location permissions
            if (ActivityCompat.checkSelfPermission(ScanCodeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            // Turn on GPS if not already enabled
            if (!isGPSEnabled()) {
                turnOnGPS();
            }


            LocationServices.getFusedLocationProviderClient(ScanCodeActivity.this)
                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);

                            LocationServices.getFusedLocationProviderClient(ScanCodeActivity.this)
                                    .removeLocationUpdates(this);

                            if (locationResult != null && locationResult.getLocations().size() >0){

                                int index = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();


                                location = String.valueOf(latitude) +"XX"+String.valueOf(longitude);
                                Log.d("locationtag", location);
                                Toast toast = Toast.makeText(getApplicationContext(), location, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    }, Looper.getMainLooper());
        }
        return location;
    }

    /**
     * Function turns on GPS
     */
    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(ScanCodeActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ScanCodeActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    /**
     * Function checks to see if GPS is enabled
     *
     * @return A boolean representing whether or not the GPS is enabled
     */
    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

}
