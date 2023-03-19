package com.example.qr_monster_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Leaderboards activity that shows user top scores in the application
public class LeaderboardsActivity extends AppCompatActivity {
    ListView players;
    ArrayAdapter<Player> playerArrayAdapter;
    ArrayList<Player> playersinfo;
    QrMonsterGoDB db = new QrMonsterGoDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        players = findViewById(R.id.players);
        playersinfo = new ArrayList<>();
        playerArrayAdapter = new PlayersAdapter(this, playersinfo);
        players.setAdapter(playerArrayAdapter);
        CollectionReference users = db.getCollectionReference("PlayerCollection");
        users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Player player = new Player(document.get("username").toString());
                        CollectionReference codesReference = db.getCollectionReference("CodeCollection");
                        codesReference.whereArrayContains("playerList", document.get("username")
                                .toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    Integer sum = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        sum += Integer.parseInt(document.get("score").toString());
                                    }
                                    player.setTotalScore(sum);
                                    playerArrayAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                        if(player.getTotalScore() == null){
                            player.setTotalScore(0);
                        }
                        playersinfo.add(player);
                    }
                    players.setAdapter(playerArrayAdapter);
                }
            }
        });
    }
}