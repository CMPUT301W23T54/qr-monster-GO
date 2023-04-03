package com.example.qr_monster_go.player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_monster_go.home.QRCode;
import com.example.qr_monster_go.R;
import com.example.qr_monster_go.database.QrMonsterGoDB;
import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.home.codeListArrayAdapter;
import com.example.qr_monster_go.home.dataCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Player activity, allows user to look at their qr codes they have scanned and view/delete them
public class PlayerActivity extends AppCompatActivity {
    TextView username;
    TextView playerRanking;
    ImageButton returnButton;
    ImageButton scannedButton;
    ImageButton deleteButton;
    ListView qrCodes;
    private codeListArrayAdapter codesAdapter;
    ArrayList<QRCode> data;
    ArrayList<QRCode> codes;
    private int location = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setTitle("Profile"); // action bar title
        Intent intent = getIntent();
        String getName = intent.getStringExtra("username");
        username = findViewById(R.id.username);
        returnButton = findViewById(R.id.home_return);
        deleteButton = findViewById(R.id.delete_button);
        scannedButton = findViewById(R.id.players_scanned_button);
        qrCodes = findViewById(R.id.codes);
        username.setText(getName);
        playerRanking = findViewById(R.id.ranking);
        QrMonsterGoDB db = new QrMonsterGoDB();
        data = new ArrayList<>();
        codes = new ArrayList<>();
        codesAdapter = new codeListArrayAdapter(this, data);
        qrCodes.setAdapter(codesAdapter);
        getCodes(getName, new dataCallback() {
            @Override
            public void onCallBack(ArrayList<QRCode> list) {
                data.addAll(list);
                Collections.sort(data, new Comparator<QRCode>() {
                    @Override
                    public int compare(QRCode code, QRCode code1) {
                        return code1.score < code.score ? -1 : 1;
                    }
                });
                codesAdapter.notifyDataSetChanged();
                Log.d(String.valueOf(data), "onCallBack: callback");
            }
        });
        getAllCodes(new dataCallback() {
            @Override
            public void onCallBack(ArrayList<QRCode> list) {
                codes.addAll(list);
                Log.d(String.valueOf(data), "onCallBack: callback");
                Collections.sort(codes, new Comparator<QRCode>() {
                    @Override
                    public int compare(QRCode code, QRCode code1) {
                        return code1.score < code.score ? -1 : 1;
                    }
                });
                Integer ranking = 0;
                if(data.size() != 0){
                    for (Integer i = 0; i < codes.size(); i++){
                        if( data.get(0).code.equals(codes.get(i).code)){
                            ranking = i + 1;
                            playerRanking.setText("Ranked " + ranking + " for highest unique code!");
                        }
                    }
                }
            }
        });
        CollectionReference usersReference = db.getCollectionReference("PlayerCollection");
        CollectionReference codesReference = db.getCollectionReference("CodeCollection");
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
                codesReference.whereEqualTo("code", data.get(location).code).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                DocumentReference code = db.getDocumentReference(id, "CodeCollection");
                                code.update("playerList", FieldValue.arrayRemove(getName));
                                data.remove(location);
                                Integer ranking = 0;
                                if(data.size() != 0){
                                    for (Integer i = 0; i < codes.size(); i++){
                                        if( data.get(0).code.equals(codes.get(i).code)){
                                            ranking = i + 1;
                                        }
                                    }
                                }
                                if(ranking != 0){
                                    playerRanking.setText("Ranked approximately " + ranking + " for highest unique code in the world!");
                                }
                                else{
                                    playerRanking.setText("");
                                }
                                codesAdapter.notifyDataSetChanged();
                            }
                            location = -1;
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
        scannedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location == -1){
                    Toast.makeText(PlayerActivity.this, "Code not selected to remove", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(PlayerActivity.this, ScannedPlayersActivity.class);
                    intent.putExtra("code", data.get(location).code);
                    intent.putExtra("uname", getName);
                    startActivityForResult(intent, 1);
                }
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
    private void getAllCodes(dataCallback callback) {
        //Code collection database instance
        QrMonsterGoDB dbCodes = new QrMonsterGoDB();
        CollectionReference codes = dbCodes.getCollectionReference("CodeCollection");

        codes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}