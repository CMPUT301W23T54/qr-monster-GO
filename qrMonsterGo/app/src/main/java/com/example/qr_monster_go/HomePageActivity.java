package com.example.qr_monster_go;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String sharedPreference = "login";
    private static final String key = "username";
    ImageButton ScanCodeButton;
    ImageButton SearchButton;
    ImageButton MapButton;
    ImageButton AccountButton;
    ImageButton Leaderboards;
    TextView username;
    TextView high;
    TextView low;
    TextView totalScore;
    TextView scannedCodes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //All textviews
        high = findViewById(R.id.high);
        low = findViewById(R.id.low);
        totalScore = findViewById(R.id.total_score);
        scannedCodes = findViewById(R.id.scanned_codes);
        username = findViewById(R.id.username);

        //All buttons
        ScanCodeButton = findViewById(R.id.scan_code_button);
        SearchButton = findViewById(R.id.search_users);
        MapButton = findViewById(R.id.map_location);
        AccountButton = findViewById(R.id.account_details);
        Leaderboards = findViewById(R.id.leaderboards);
        //Gets player stats and displays
        ArrayList<Integer> stats = new ArrayList<>();
        sharedPreferences = getSharedPreferences(sharedPreference, MODE_PRIVATE);
        stats = playerStats(sharedPreferences.getString(key, ""));
//        high.setText("Highest Code " + stats.get(0));
//        low.setText("Lowest Code " + stats.get(1));
//        totalScore.setText("Total Score " + stats.get(2));
//        scannedCodes.setText("Scanned " + stats.get(3) + " codes");
        //sharedPreferences = getSharedPreferences(sharedPreference, MODE_PRIVATE);
        username.setText(sharedPreferences.getString(key, null));

        ScanCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ScanCodeActivity.class);
                intent.putExtra("username", sharedPreferences.getString(key, null));

                startActivity(intent);
            }
        });

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, SearchUsersActivity.class));
            }
        });

        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Map activity
            }
        });

        AccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Account details activity
            }
        });

        Leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Leaderboards activity
            }
        });
    }
    public ArrayList<Integer> playerStats(String username){
        //Code collection database instance
        QrMonsterGoDB dbCodes = new QrMonsterGoDB();
        CollectionReference codes = dbCodes.getCollectionReference("CodeCollection");
        ArrayList<Integer> data = new ArrayList<>();
        codes.whereArrayContains("playerList", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Integer key =Integer.parseInt(document.get("score").toString()) ;
                        data.add(key);
                    }
                    if(data.size() == 0){
                        data.clear();
                        data.add(0);
                        data.add(0);
                        data.add(0);
                        data.add(0);
                        //return data;
                    }
                    else {
                        Collections.sort(data);
                        int high = data.get(data.size() - 1);
                        int low = data.get(0);

                        int score = 0;
                        for (int i = 0; i < data.size(); i++) {
                            score += data.get(i);
                        }

                        int sum = data.size();
                        data.clear();
                        data.add(high);
                        data.add(low);
                        data.add(score);
                        data.add(sum);
                    }

                    high.setText("Highest Code " + data.get(0));
                    low.setText("Lowest Code " + data.get(1));
                    totalScore.setText("Total Score " + data.get(2));
                    scannedCodes.setText("Scanned " + data.get(3) + " codes");

                    //return data;

                }
            }
        });
//        if(data.size() == 0){
//            data.clear();
//            data.add(0);
//            data.add(0);
//            data.add(0);
//            data.add(0);
//            return data;
//        }
//        Collections.sort(data);
//        int high = data.get(data.size() - 1);
//        int low = data.get(0);
//        int score = 0;
//        int sum = data.size();
//        data.clear();
//        data.add(high);
//        data.add(low);
//        data.add(score);
//        data.add(sum);
//        return data;
        return data;
    }
}
