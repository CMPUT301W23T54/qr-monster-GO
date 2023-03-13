package com.example.qr_monster_go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class codeListArrayAdapter extends ArrayAdapter<QRCode> {

    public codeListArrayAdapter(Context context, ArrayList<QRCode> codes) {
        super(context, 0, codes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.code_list_content, parent, false);
        }
        else {
            view = convertView;
        }

        QRCode code = super.getItem(position);
        TextView name = view.findViewById(R.id.code_name);
        name.setText(code.getName());
        TextView score = view.findViewById(R.id.score_text);
        score.setText(String.valueOf(code.getScore()));

        return view;
    }
}
