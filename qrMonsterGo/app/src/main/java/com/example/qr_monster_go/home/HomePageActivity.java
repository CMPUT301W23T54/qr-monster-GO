package com.example.qr_monster_go.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.qr_monster_go.player.PlayerActivity;
import com.example.qr_monster_go.R;
import com.example.qr_monster_go.database.QrMonsterGoDB;
import com.example.qr_monster_go.maps.MapsActivity;
import com.example.qr_monster_go.scan.ScanCodeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

//Home page of the application. Allows user to navigate through different
// functionalities and activities of the application through buttons and shows the user various player stats
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
    TextView totalScore;
    TextView scannedCodes;
    TextView visual;
    ArrayList<QRCode> codes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().setTitle("QR Monster GO"); // action bar title
        //All textviews
        totalScore = findViewById(R.id.total_score);
        scannedCodes = findViewById(R.id.scanned_codes);
        username = findViewById(R.id.username);
        // Setting textviews with default "loading..."
        totalScore.setText("loading...");
        scannedCodes.setText("loading...");
        //All buttons
        ScanCodeButton = findViewById(R.id.scan_code_button);
        SearchButton = findViewById(R.id.search_users);
        MapButton = findViewById(R.id.map_location);
        AccountButton = findViewById(R.id.account_details);
        Leaderboards = findViewById(R.id.leaderboards);
        visual = findViewById(R.id.image);
        visual.setText("loading..."); // default value
        //Gets player stats and displays
        ArrayList<Integer> stats = new ArrayList<>();
        sharedPreferences = getSharedPreferences(sharedPreference, MODE_PRIVATE);
        username.setText(sharedPreferences.getString(key, null));
        QrMonsterGoDB db = new QrMonsterGoDB();
        codes = new ArrayList<>();
        CollectionReference codesReference = db.getCollectionReference("CodeCollection");
        getCodes(sharedPreferences.getString(key, null), new dataCallback() {
            @Override
            public void onCallBack(ArrayList<QRCode> list) {
                codes.addAll(list);
                Handler handler = new Handler();
                handler.post(new Runnable(){
                    int x = 0;
                    int y = 0;
                    @Override
                    public void run() {
                        ArrayList<Integer> stats = new ArrayList<>();
                        stats = playerStats(sharedPreferences.getString(key, ""), x);
                        if(codes.size() == 0){
                            visual.setText("No codes scanned");
                        }
                        else{
                            visual.setText(codes.get(y).getName() + "\n\n\n\n" + codes.get(y).generateVisualRep(codes.get(y).code));
                        }
                        handler.postDelayed(this,2500); // set time here to refresh textView
                        x += 1;
                        y += 1;
                        if(y == codes.size()){
                            y = 0;
                        }
                    }
                });
            }
        });
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
                Intent intent = new Intent(HomePageActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        AccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("username", sharedPreferences.getString(key, null));
                startActivity(intent);
            }
        });

        Leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, LeaderboardsActivity.class));
            }
        });
    }
    public ArrayList<Integer> playerStats(String username, int x){
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
                    if(x % 2 == 0){
                        totalScore.setText("Highest Code Score: " + data.get(0));
                        scannedCodes.setText("Lowest Code Score: " + data.get(1));
                    }
                    else{
                        totalScore.setText("Total Score: " + data.get(2));
                        scannedCodes.setText("Number of Codes Scanned: " + data.get(3));
                    }
                }
            }
        });
        return data;
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
