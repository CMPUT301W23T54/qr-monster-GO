package com.example.qr_monster_go;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ScanCodeButton = findViewById(R.id.scan_code_button);
        SearchButton = findViewById(R.id.search_users);
        MapButton = findViewById(R.id.map_location);
        AccountButton = findViewById(R.id.account_details);
        Leaderboards = findViewById(R.id.leaderboards);
        username = findViewById(R.id.username);
        sharedPreferences = getSharedPreferences(sharedPreference, MODE_PRIVATE);
        username.setText(sharedPreferences.getString(key, null));
        ScanCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, ScanCodeActivity.class));
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
}
