package com.example.qr_monster_go;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.SharedPreferencesKt;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * This activity is where the user can scan a code
 * and add it to their account
 *
 * implements ScanResultReceiver to easily retrieve
 * data from the scanning fragment
 *
 * Triggers the getLocation dialog and function (will be refactored into own class soon)
 */
public class ScanCodeActivity extends AppCompatActivity implements ScanResultReceiver {
    Button scanButton;
    ImageButton returnButton;

    boolean addLocation = false;
    private LocationRequest locationRequest;
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
            QRCode code = new QRCode(hashValue);

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

        scanButton = findViewById(R.id.scan_code_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
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
}
