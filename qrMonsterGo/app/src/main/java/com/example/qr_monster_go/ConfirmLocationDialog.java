package com.example.qr_monster_go;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmLocationDialog extends DialogFragment {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Would you like to add your geo-location to this code?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", (dialog, which) -> {
                    ScanResultReceiver parent = (ScanResultReceiver) this.getActivity();
                    parent.setCurrentLocation();
                })
                .create();
    }
}
