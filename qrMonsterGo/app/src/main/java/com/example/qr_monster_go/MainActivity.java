package com.example.qr_monster_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
// First time sign up activity. If user has not used application before, they will be prompted to
// sign up and if hey have signed up before, pushes to home page
public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText phoneNumber;
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
        email = findViewById(R.id.email_entry);
        phoneNumber = findViewById(R.id.phone_entry);
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
                String emailEntry = email.getText().toString();
                String phoneNumberEntry = phoneNumber.getText().toString();

                if (isValidUsername(userNameEntry) && isValidPhoneNumber(phoneNumberEntry) && isValidEmail(emailEntry)) {
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
                                            // Get the bottom of the email prompt
                                            int emailBottom = email.getBottom();

                                            // Get the top of the sign up button
                                            int buttonTop = signUp.getTop();

                                            // Calculate the position of the toast in between the email prompt and sign up button
                                            int toastY = emailBottom + (buttonTop - emailBottom) / 2;

                                            // Show the toast
                                            Toast toast = Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, toastY + 60);
                                            toast.show();
                                        } else {
                                            // entry does not exist, will add into firebase
                                            HashMap<String, String> data = new HashMap<>();

                                            // create key-value pairs
                                            data.put(key, userNameEntry);
                                            data.put("email", emailEntry);
                                            data.put("phone number", phoneNumberEntry);

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
                                            Toast toast = Toast.makeText(MainActivity.this, "Successful sign up", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
                                    } else {
                                        Log.d(TAG, "Failed with: ", task.getException());
                                    }

                                }
                            }); // queryUsersByName.get().addOnCompleteListener
                }   else {

                    // Check for errors and display the error message if necessary
                    ArrayList<String> errorMessages = new ArrayList<String>();
                    if (!isValidUsername(username.getText().toString())) {
                        errorMessages.add("Invalid username");
                    }
                    if (!isValidPhoneNumber(phoneNumber.getText().toString())) {
                        errorMessages.add("Invalid phone number");
                    }
                    if (!isValidEmail(email.getText().toString())) {
                        errorMessages.add("Invalid email");
                    }
                    if (errorMessages.size() > 0) {
                        StringBuilder errorMessageBuilder = new StringBuilder();
                        for (String message : errorMessages) {
                            errorMessageBuilder.append(message).append("\n");
                        }
                        String errorMessage = errorMessageBuilder.toString().trim();

                        // Create a new text view to measure the height of the error message
                        TextView errorTextView = new TextView(MainActivity.this);
                        errorTextView.setText(errorMessage);
                        errorTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                        int errorMessageHeight = errorTextView.getMeasuredHeight();

                        // Calculate the height of the sign up button
                        int buttonHeight = signUp.getHeight();

                        // Calculate the extra offset for the toast position
                        int extraOffset = Math.max(buttonHeight - errorMessageHeight, 0);

                        // Create and show the toast
                        Toast errorToast = Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG);
                        errorToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, email.getBottom() + buttonHeight + extraOffset);
                        errorToast.show();
                    }
                }
            }       // signUp.setOnClickListener
        });               // onCreate
    }
    // username validation method
    public boolean isValidUsername(String username) {
        // Check if username is alphanumeric
        if (!username.matches("[a-zA-Z0-9]+")) {
            return false;
        }

        // Check if username is of a certain length
        if (username.length() < 3 || username.length() > 20) {
            return false;
        }

        // Add any other checks as required

        return true;
    }

    // phone number validation method
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    // email validation method
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    // if user presses back arrow to get to login page redirect to home page
    @Override
    protected void onResume() {
        super.onResume();

        String existing = sharedPreferences.getString(key, null);
        //Checks if the user has signed up already and redirect to home page if they have
        if (existing != null) {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        }
    }
}