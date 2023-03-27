package com.example.qr_monster_go;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This dialog appears when user finishes scanning a code.
 * It prompts the user to record location or not
 * If yes to recording location, dialog calls upon setCurrentLocation() in ScanCodeActivity
 * If no to recording location, dialog calls scanResultData(), skipping the location
 */
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
                .setNegativeButton("No", (dialog, which) -> {
                    ScanResultReceiver parent = (ScanResultReceiver) this.getActivity();
                    assert parent != null;
                    parent.scanResultData();

                    DialogFragment confirmImageDialog = new ConfirmImageDialog();
                    confirmImageDialog.show(((ScanCodeActivity) getActivity()).getSupportFragmentManager(), "image");
                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    ScanResultReceiver parent = (ScanResultReceiver) this.getActivity();
                    assert parent != null;
                    parent.setCurrentLocation();

                    DialogFragment confirmImageDialog = new ConfirmImageDialog();
                    confirmImageDialog.show(((ScanCodeActivity) getActivity()).getSupportFragmentManager(), "image");
                })
                .create();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ScanResultReceiver parent = (ScanResultReceiver) this.getActivity();
//        parent.scanResultData();
//    }
}
