package com.example.qr_monster_go.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_monster_go.R;

import java.util.ArrayList;

/**
 * This class provides a custom adapter to use to display a list of codes
 */
public class codeListArrayAdapter extends ArrayAdapter<QRCode> {
    /**
     * Constructor for the code list adapter to make
     * @param context
     * @param codes
     */
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
        score.setText("Score " + String.valueOf(code.getScore()));
        //visual.setScaleX(0.5F);
        //visual.setScaleY(0.2F);
        return view;
    }
}
