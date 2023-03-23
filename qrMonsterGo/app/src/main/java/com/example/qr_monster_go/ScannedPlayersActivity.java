package com.example.qr_monster_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScannedPlayersActivity extends AppCompatActivity {
    ArrayList<String> data;
    ListView users;
    ArrayAdapter<String> usersAdapter;
    TextView review;
    TextView visual;
    ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_players);
        Intent intent = getIntent();
        String getName = intent.getStringExtra("code");
        QrMonsterGoDB db = new QrMonsterGoDB();
        returnButton = returnButton.findViewById(R.id.return_button);
        users = findViewById(R.id.users);
        data = new ArrayList<>();
        usersAdapter = new ArrayAdapter<>(this, R.layout.display, data);
        users.setAdapter(usersAdapter);
        review = review.findViewById(R.id.review);
        review.setText("Players who have scanned the same code");
        visual = visual.findViewById(R.id.visual);
        CollectionReference codesReference = db.getCollectionReference("CodeCollection");
        codesReference.whereEqualTo("code", getName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String players = document.get("playerList").toString();

                        ArrayList<String> person = new ArrayList<String>(Arrays.asList(players.replace("[", "").replace("]", "").split(", ")));
                        data.addAll(person);
                        users.setAdapter(usersAdapter);
                    }
                    Log.d(String.valueOf(data), "onComplete: dataList");
                }
                else {
                    Log.d("fail", "getCodes: fail");
                }
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScannedPlayersActivity.this, PlayerActivity.class));
            }
        });
    }
}