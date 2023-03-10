package com.example.qr_monster_go;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initiate code scanning
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // get results of code scanning
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // create parent activity object (ScanCodeActivity)
        ScanResultReceiver parent = (ScanResultReceiver) this.getActivity();

        if (result != null) {
            // get the code contents and format from results
            String content = result.getContents();
            String format = result.getFormatName();

            //call scanResultData from parent activity to set values for code scanning results
            parent.scanResultData(format, content);
        }
        else {
            // call scanResultData from parent activity with null results
            parent.scanResultData(null, null);
        }
    }
}
