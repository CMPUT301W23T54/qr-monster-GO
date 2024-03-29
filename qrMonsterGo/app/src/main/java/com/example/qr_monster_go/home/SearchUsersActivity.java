package com.example.qr_monster_go.home;

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

import com.example.qr_monster_go.R;
import com.example.qr_monster_go.database.QrMonsterGoDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Allows users to search for others in thee application and view other players profiles
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
        getSupportActionBar().setTitle("Search For Users"); // action bar title
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
                    CollectionReference usersReference = db.getCollectionReference("PlayerCollection");
                    usersReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String name = document.get("username").toString();
                                    if (name.contains(user)){
                                        data.add(name);
                                    }
                                }
                                users.setAdapter(usersAdapter);
                                if(data.size() == 0){
                                    Toast.makeText(SearchUsersActivity.this, "No results", Toast.LENGTH_LONG).show();
                                }
                            }

                            //return null;

                        }
                    });
                    usersAdapter.notifyDataSetChanged();
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