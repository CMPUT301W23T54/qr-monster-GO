package com.example.qr_monster_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    TextView username;
    ImageButton returnButton;
    ImageButton deleteButton;
    ListView qrCodes;
    ArrayAdapter<String> codesAdapter;
    ArrayList<String> data;
    private int location = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        String getName = intent.getStringExtra("username");
        username = findViewById(R.id.username);
        returnButton = findViewById(R.id.home_return);
        deleteButton = findViewById(R.id.delete_button);
        qrCodes = findViewById(R.id.codes);
        username.setText(getName);
        QrMonsterGoDB db = new QrMonsterGoDB();
        data = new ArrayList<>();
        codesAdapter = new ArrayAdapter<>(this, R.layout.display, data);
        qrCodes.setAdapter(codesAdapter);
        CollectionReference usersReference = db.getCollectionReference("users");
        CollectionReference codesReference = db.getCollectionReference("CodeCollection");
        codesReference.whereArrayContains("playerList", getName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.get("name").toString();
                        data.add(name);
                    }
                    qrCodes.setAdapter(codesAdapter);
                }
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlayerActivity.this, HomePageActivity.class));
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location == -1){
                    Toast.makeText(PlayerActivity.this, "Code not selected to remove", Toast.LENGTH_LONG).show();
                }
                else{
                codesReference.whereEqualTo("name", data.get(location)).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                ArrayList<String> update = new ArrayList<>();
                                DocumentReference code = db.getDocumentReference(id, "CodeCollection");
                                code.update("playerList", FieldValue.arrayRemove(getName));
                                data.remove(location);
                                location = -1;
                            }
                            qrCodes.setAdapter(codesAdapter);
                        }
                    }
                });
                }
            }
        });
        qrCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                location = index;
            }
        });
    }
}