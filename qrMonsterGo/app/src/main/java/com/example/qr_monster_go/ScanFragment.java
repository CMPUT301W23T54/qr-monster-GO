package com.example.qr_monster_go;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

/**
 * This class is a fragment that hosts zxing's code scanning
 * activity then passes the results to the parent activity
 */
public class ScanFragment extends Fragment {

    /**
     * When this fragment is created it initiates zxing code scanning
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
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

    /**
     * When the scan is completed, this function checks if a code was actually
     * scanned then calls setQRString from the parent activity to pass
     * the data back to the parent activity and then initializes ConfirmLocationDialog
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
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
            assert parent != null;
//            parent.scanResultData(format, content);
            parent.setQRStrings(format, content); // Modified to set QR strings instead
            Log.d("scanResultData", "Finished setting up strings");
            DialogFragment confirmlocationdialog = new ConfirmLocationDialog();
            confirmlocationdialog.show(((ScanCodeActivity)getActivity()).getSupportFragmentManager(), "location");
        }
        else {
            // call scanResultData from parent activity with null results
            // FIXME when refactoring
            assert parent != null;
//            parent.scanResultData();
        }
    }
}
