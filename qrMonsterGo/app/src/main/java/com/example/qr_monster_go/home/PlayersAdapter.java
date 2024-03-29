package com.example.qr_monster_go.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qr_monster_go.R;
import com.example.qr_monster_go.player.Player;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

/**
 * Used in LeaderboardsActivity, allows for a list of players to be stored and adapted to view
 */
public class PlayersAdapter extends ArrayAdapter<Player> {
    private ArrayList<Player> users;
    private Context context;
    public PlayersAdapter(Context context, ArrayList<Player> users){
        super(context, 0, users);
        this.users = users;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
// return super.getView(position, convertView, parent);
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.leaderboards_display, parent,false);
        }

        Player player = users.get(position);
        String score = player.getTotalScore().toString();
        String name = player.getUsername();
        int rank = position + 1; // calculate the rank of the player

        if (name.length() < 20){
            while(name.length() < 20){
                name += " ";
            }
        }

        TextView uName = view.findViewById(R.id.name);
        TextView scores = view.findViewById(R.id.scores);
        TextView uRank = view.findViewById(R.id.rank);

        uName.setText("Username: " + name);
        scores.setText("Score: " + score);
        uRank.setText("Rank: " + rank);

//        uRank.setText(String.valueOf(rank));
//
//        // Set the icon for the top three players
//        ImageView icon = view.findViewById(R.id.icon);
//        if (rank == 1) {
//            icon.setImageResource(R.drawable.gold_medal_icon);
//        } else if (rank == 2) {
//            icon.setImageResource(R.drawable.silver_medal_icon);
//        } else if (rank == 3) {
//            icon.setImageResource(R.drawable.bronze_medal_icon);
//        } else {
//            // Hide the icon for other players
//            icon.setVisibility(View.GONE);
//        }

        return view;
    }

}