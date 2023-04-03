package com.example.qr_monster_go.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_monster_go.home.QRCode;
import com.example.qr_monster_go.R;
import com.example.qr_monster_go.database.QrMonsterGoDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Used in PlayerActivity
 */
public class ScannedPlayersActivity extends AppCompatActivity {
    ArrayList<String> data;
    ImageView location;
    ArrayList<String> commentsData;
    ListView users;
    ListView usersComments;
    ArrayAdapter<String> usersAdapter;
    ArrayAdapter<String> commentsAdapter;
    TextView review;
    TextView visual;
    ImageButton returnButton;
    EditText comments;
    Button addComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_players);
        Intent intent = getIntent();
        String getName = intent.getStringExtra("code");
        String uname = intent.getStringExtra("uname");
        QrMonsterGoDB db = new QrMonsterGoDB();
        returnButton = findViewById(R.id.return_button);
        usersComments = findViewById(R.id.userComments);
        users = findViewById(R.id.users);
        location = findViewById(R.id.location);
        data = new ArrayList<>();
        commentsData = new ArrayList<>();
        usersAdapter = new ArrayAdapter<>(this, R.layout.display, data);
        commentsAdapter = new ArrayAdapter<>(this, R.layout.display, commentsData);
        users.setAdapter(usersAdapter);
        review = findViewById(R.id.review);
        review.setText("Players who have scanned the same code");
        visual = findViewById(R.id.image);
        comments = findViewById(R.id.comments);
        addComments = findViewById(R.id.addComments);
        CollectionReference codesReference = db.getCollectionReference("CodeCollection");
        codesReference.whereEqualTo("code", getName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String players = document.get("playerList").toString();
                        String commentsArray = document.get("comments").toString();
                        ArrayList<String> person = new ArrayList<String>(Arrays.asList(players.replace("[", "").replace("]", "").split(", ")));
                        ArrayList<String> review = new ArrayList<String>(Arrays.asList(commentsArray.replace("[", "").replace("]", "").split("//")));
                        data.addAll(person);
                        commentsData.addAll(review);
                        users.setAdapter(usersAdapter);
                        usersComments.setAdapter(commentsAdapter);
                        QRCode code = new QRCode(document.get("code").toString());
                        visual.setText(code.generateVisualRep(document.get("code").toString()));
                        if(document.contains("imageMap") && document.get("imageMap") != null){
                            String base64String = document.getString("imageMap");
                            byte[] byteArray = Base64.decode(base64String, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            location.setImageBitmap(bitmap);
                        }
                        else {
                            location.setImageResource(android.R.color.transparent);
                        }
                    }
                    Log.d(String.valueOf(data), "onComplete: dataList");
                }
                else {
                    Log.d("fail", "getCodes: fail");
                }
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newComments = comments.getText().toString();
                if(newComments.length() == 0){
                    Toast.makeText(ScannedPlayersActivity.this, "No Comments", Toast.LENGTH_LONG).show();
                }
                else{
                    codesReference.whereEqualTo("code", getName).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String id = document.getId();
                                            ArrayList<String> update = new ArrayList<>();
                                            DocumentReference code = db.getDocumentReference(id, "CodeCollection");
                                            code.update("comments", FieldValue.arrayUnion(newComments + "//"));
                                            commentsData.add(newComments);
                                            commentsAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }
}