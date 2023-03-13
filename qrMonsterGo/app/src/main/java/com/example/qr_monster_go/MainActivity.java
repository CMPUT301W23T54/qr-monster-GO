package com.example.qr_monster_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText username;
    Button signUp;
    SharedPreferences sharedPreferences;
    private static final String sharedPreference = "login";
    private static final String key = "username";
    final String TAG = "tag";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username_entry);
        signUp = findViewById(R.id.sign_up);
        sharedPreferences = getSharedPreferences(sharedPreference, MODE_PRIVATE);

        String existing = sharedPreferences.getString(key, null);

        //Checks if the user has signed up already
        if (existing != null) {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // we do not add anything if the username entry is empty
                if (username.getText().toString().length() == 0) {
                    return;
                }

                String userNameEntry = username.getText().toString();

                // Access a Cloud Firestore instance
                db = FirebaseFirestore.getInstance();
                // Get a top level reference to the collection
                final CollectionReference usersRef = db.collection("PlayerCollection");
                // query Firebase if the entry exists
                Query queryUsersByName = usersRef.whereEqualTo(key.toLowerCase(), userNameEntry.toLowerCase());

                // add listener
                queryUsersByName
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() > 0) {
                                        // log the exist for now
                                        Log.d("TAG", "user name already exists");
                                    } else {
                                        // entry does not exist, will add into firebase
                                        HashMap<String, String> data = new HashMap<>();

                                        // create a new key-value pair
                                        data.put(key, userNameEntry);

                                        // get a new document's Id first
                                        String id = usersRef.document().getId();

                                        // attempt to add the data to Firebase by the new Id
                                        usersRef.document(id)
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG, "Data has been added successfully!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(TAG, "Data could not be added!" + e.toString());
                                                    }
                                                });

                                        // keep local operation
                                        SharedPreferences.Editor login = sharedPreferences.edit();
                                        login.putString(key, username.getText().toString());
                                        login.apply();
                                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(MainActivity.this, "Successful Sign Up", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    Log.d(TAG, "Failed with: ", task.getException());
                                }

                            }
                        }); // queryUsersByName.get().addOnCompleteListener
            }
        });         // signUp.setOnClickListener
    }               // onCreate


}