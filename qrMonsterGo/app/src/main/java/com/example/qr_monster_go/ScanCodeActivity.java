package com.example.qr_monster_go;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;


public class ScanCodeActivity extends AppCompatActivity implements ScanResultReceiver {
    Button scanButton;
    boolean addLocation = false;
    boolean addImage = false;

    @Override
    public void scanResultData(String codeFormat, String content) {
        if (content != null) {
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
            new ConfirmLocationDialog().show(getSupportFragmentManager(), "hfsdfs");
        }
        else {
            Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void setLocationChoice() {
        this.addLocation = true;
    }


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
    }

    private void scanCode() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        ScanFragment scanFragment = new ScanFragment();
        fragmentTransaction.add(R.id.scanFragment,scanFragment);
        fragmentTransaction.commit();
    }
}
