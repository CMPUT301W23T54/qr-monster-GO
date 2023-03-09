package com.example.qr_monster_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchUsersActivity extends AppCompatActivity {
    ImageButton returnButton;
    ImageButton searchButton;
    ImageButton viewProfileButton;
    EditText searchedUser;
    ListView users;
    ArrayAdapter<String> usersAdapter;
    ArrayList<String> data;
    private int location = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        returnButton = findViewById(R.id.return_button);
        searchButton = findViewById(R.id.search_users_button);
        searchedUser = findViewById(R.id.searched_user);
        viewProfileButton = findViewById(R.id.view_profile_button);
        users = findViewById(R.id.users);
        data = new ArrayList<>();
        usersAdapter = new ArrayAdapter<>(this, R.layout.display, data);
        users.setAdapter(usersAdapter);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchUsersActivity.this, HomePageActivity.class));
            }
        });
        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                location = index;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QrMonsterGoDB db = new QrMonsterGoDB();
                String user = searchedUser.getText().toString();
                String TAG = "DocSnippets";
                if(user.length() > 0){
                    data.clear();
                    users.setAdapter(usersAdapter);
                    CollectionReference usersReference = db.getCollectionReference("users");
                    usersReference.whereEqualTo("name", user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String name = document.get("name").toString();
                                    data.add(name);
                                }
                                users.setAdapter(usersAdapter);
                                if(data.size() == 0){
                                    Toast.makeText(SearchUsersActivity.this, "No results", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SearchUsersActivity.this, "Please enter a username to search", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location != -1){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("username", data.get(location));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SearchUsersActivity.this, "User has not been selected", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}