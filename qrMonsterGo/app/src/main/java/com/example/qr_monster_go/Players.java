package com.example.qr_monster_go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

public class Players extends ArrayAdapter<Player> {
    private ArrayList<Player> users;
    private Context context;
    public Players(Context context, ArrayList<Player> users){
        super(context, 0, users);
        this.users = users;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
// return super.getView(position, convertView, parent);
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.leaderboards_display, parent,false);
        }
        Player player = users.get(position);
        String score = player.getTotalScore().toString();
        String name = player.getUsername();
        TextView uName = view.findViewById(R.id.name);
        TextView scores = view.findViewById(R.id.scores);
        uName.setText(name);
        scores.setText(score);
        return view;
    }

}
