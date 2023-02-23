package com.example.qr_monster_go;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username;
    Button signUp;
    SharedPreferences sharedPreferences;
    private static final String sharedPreference = "login";
    private static final String key = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username_entry);
        signUp = findViewById(R.id.sign_up);
        sharedPreferences = getSharedPreferences(sharedPreference, MODE_PRIVATE);

        String existing = sharedPreferences.getString(key, null);
        if(existing != null){
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        }
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences.Editor login = sharedPreferences.edit();
                login.putString(key, username.getText().toString());
                login.apply();
                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Successful Sign Up", Toast.LENGTH_LONG).show();
            }
        });
    }
}