package com.example.qr_monster_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ScannedPlayersActivity extends AppCompatActivity {
    ArrayList<String> data;
    ListView users;
    ArrayAdapter<String> usersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_players);
        Intent intent = getIntent();
        String getName = intent.getStringExtra("code");
        QrMonsterGoDB db = new QrMonsterGoDB();
        users = findViewById(R.id.users);
        data = new ArrayList<>();
        usersAdapter = new ArrayAdapter<>(this, R.layout.display, data);
        users.setAdapter(usersAdapter);
        CollectionReference codesReference = db.getCollectionReference("CodeCollection");
        codesReference.whereEqualTo("code", getName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String players = document.get("playerList").toString();
                        data.add(players);
                    }
                    Log.d(String.valueOf(data), "onComplete: dataList");
                }
                else {
                    Log.d("fail", "getCodes: fail");
                }
            }
        });
    }
}