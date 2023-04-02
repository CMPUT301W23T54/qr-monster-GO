package com.example.qr_monster_go.scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This dialog appears after the ConfirmLocationDialog when user finishes scanning a code.
 * It prompts the user to record photo of the location or not
 * If yes to taking a photo, this dialog calls startImageActivity on the parent activity
 * If no to recording location, dialog calls scanResultData(), skipping the image
 */
public class ConfirmImageDialog extends DialogFragment {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Would you like to add an image of the location to this code?")
                .setNegativeButton("No", (dialog, which) -> {
                    // process scan
                    ScanResultReceiver parent = (ScanResultReceiver) this.getActivity();
                    assert parent != null;
                    parent.scanResultData();
                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    // start image capturing activity
                    ScanCodeActivity parent = (ScanCodeActivity) this.getActivity();
                    assert parent != null;
                    parent.startImageActivity();
                })
                .create();
    }
}
