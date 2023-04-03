package com.example.qr_monster_go.scan;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
//import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.home.QRCode;
import com.example.qr_monster_go.R;
import com.example.qr_monster_go.database.CodeDataStorageController;
import com.example.qr_monster_go.database.QrMonsterGoDB;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

    private LocationRequest locationRequest;
    private String Glocation; // global location

    String GcodeFormat; // global code format
    String Gcontent; // global content

    byte[] GImageMap;

    /**
     * This function displays a Toast with the content of the code then creates
     * a new QRCode object with the SHA-256 hash of 'content'
     *
     * Called by setCurrentLocation to ensure that geolocation value is obtained
     * before sending data to database
     *
     */
    @Override
    public void scanResultData() {
        if (Gcontent != null) {
            // generate SHA-256 hash of code
            String hashValue = Hashing.sha256()
                    .hashString(Gcontent, StandardCharsets.UTF_8)
                    .toString();

            // create new QRCode object from the contents of the scanned code
            QRCode code = new QRCode(hashValue);
            CodeDataStorageController dc = new CodeDataStorageController(new QrMonsterGoDB());

            // check if the document is already in the database(code has already been scanned)
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference codeCollection = db.collection("CodeCollection");
            DocumentReference ref = codeCollection.document(code.getCode());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        // if this code has already been found by the user or another player
                        if (document.exists()) {
                            ArrayList<String> playerList = (ArrayList<String>) document.get("playerList");
                            // check if user has already scanned the code
                            if (playerList.contains(getIntent().getExtras().getString("username"))) {
                                Toast.makeText(getApplicationContext(), "You have already scanned this code", Toast.LENGTH_LONG).show();
                            }
                            else {
                                code.setPlayerList(playerList);

                                // add database values to code if none were added by this user
                                if (Glocation == null) {
                                    Glocation = (String) document.get("location");
                                }
                                if (GImageMap == null) {
                                    GImageMap =  Base64.decode((String) document.get("imageMap"), Base64.DEFAULT);
                                }

                                // update all data on the code and upload to database
                                code.addPlayer(getIntent().getExtras().getString("username"));
                                code.setGeolocation(Glocation);
                                code.setImageMap(GImageMap);
                                dc.addElement(code);

                                Toast.makeText(getApplicationContext(), "Code added!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            // update all data on the code and upload to database
                            code.addPlayer(getIntent().getExtras().getString("username"));
                            code.setGeolocation(Glocation);
                            code.setImageMap(GImageMap);
                            dc.addElement(code);

                            Toast.makeText(getApplicationContext(), "Code added!", Toast.LENGTH_SHORT).show();
                        }

                        // set all values back to null for next scan after scan has been processed
                        Gcontent = null;
                        GcodeFormat = null;
                        Glocation = null;
                        GImageMap = null;
                    }
                    else {
                        Log.d("Something went wrong loading the document", "onComplete: error");
                    }
                }
            });

        }
        else {
            // display toast with No Results message
            Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
        }

    }


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

    /**
     * This function is used by ScanFragment to update the QR information after scanning
     * the QR code. These updated variables are used in scanResultData()
     */
    public void setQRStrings(String codeFormat, String content) {
        GcodeFormat = codeFormat;
        Gcontent = content;
    }
    /*********************************************************************/
    // Location Methods (will be refactored later)
    // Credit for precise location method @TechnicalCoding
    // https://www.youtube.com/watch?v=mbQd6frpC3g
    /*********************************************************************/


    /**
     * This function generates the geolocation of the user and updates geolocation variable
     * It checks to see if GPS is enabled and also requests for location permissions if necessary
     *
     * Calls upon scanResultData() once geolocation has been determined
     *
     */
    @Override
    public void setCurrentLocation() {

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


                                Glocation = String.valueOf(latitude) +"X"+String.valueOf(longitude);

                                Log.d("location", Glocation);

                                DialogFragment confirmImageDialog = new ConfirmImageDialog();
                                confirmImageDialog.show(getSupportFragmentManager(), "image");
                            }
                        }
                    }, Looper.getMainLooper());
        }
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

    /**
     * Function launches the image capturing activity
     * called through ConfirmImageDialog
     */
    public void startImageActivity() {
        Intent intent = new Intent(ScanCodeActivity.this, ImageCaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if correct request code has been sent back
        if (requestCode == 0) {
            if (data != null) {
                GImageMap = data.getByteArrayExtra("imageMap");
            }
            else {
                GImageMap = null;
            }

            scanResultData();

        }
    }
}
