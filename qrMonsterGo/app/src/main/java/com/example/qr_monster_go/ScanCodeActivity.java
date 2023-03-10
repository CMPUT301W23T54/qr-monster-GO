package com.example.qr_monster_go;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;


public class ScanCodeActivity extends AppCompatActivity implements ScanResultReceiver {
    Button scanButton;
    ImageButton returnButton;
    boolean addLocation = false;
    boolean addImage = false;

    @Override
    public void scanResultData(String codeFormat, String content) {
        if (content != null) {
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();

            // generate SHA-256 hash of code
            String hashValue = Hashing.sha256()
                    .hashString(content, StandardCharsets.UTF_8)
                    .toString();

            // create new ScannableCode object from the contents of the scanned code
            ScannableCode code = new ScannableCode(hashValue);
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
