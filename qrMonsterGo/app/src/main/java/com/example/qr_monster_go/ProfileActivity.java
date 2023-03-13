package com.example.qr_monster_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileActivity extends AppCompatActivity {
    TextView name;
    ImageButton backButton;
    ImageButton homeButton;

    private codeListArrayAdapter adapter;
    private ArrayList<QRCode> codeDataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = findViewById(R.id.user_name);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home_button);
        Intent intent = getIntent();
        String getName = intent.getStringExtra("username");
        name.setText(getName);


        ListView codeList = findViewById(R.id.code_list);
        adapter = new codeListArrayAdapter(this, codeDataList);
        codeList.setAdapter(adapter);
        getCodes(getName, new dataCallback() {
            @Override
            public void onCallBack(ArrayList<QRCode> list) {
                codeDataList.addAll(list);
                adapter.notifyDataSetChanged();
                Log.d(String.valueOf(codeDataList), "onCallBack: callback");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, SearchUsersActivity.class));
            }
        });


        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomePageActivity.class));
            }
        });
    }

    private void getCodes(String username, dataCallback callback) {
        //Code collection database instance
        QrMonsterGoDB dbCodes = new QrMonsterGoDB();
        CollectionReference codes = dbCodes.getCollectionReference("CodeCollection");

        codes.whereArrayContains("playerList", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<QRCode> data = new ArrayList<>();
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d((String) document.get("code"), "onComplete: codeFound");
                        data.add(new QRCode((String) document.get("code")));
                    }
                    Log.d(String.valueOf(data), "onComplete: dataList");
                }
                else {
                    Log.d("fail", "getCodes: fail");
                }

                callback.onCallBack(data);
            }
        });

    }



}