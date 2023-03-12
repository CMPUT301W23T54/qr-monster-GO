package com.example.qr_monster_go;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

// progress so far
public class Player {
    private String userName;
    private String email;
    private String phone;
    private Integer totalScore;
    private Integer totalScannedCodes;
    private List<String> savedCodeList;

    FirebaseFirestore db;

    /**
     * empty constructor
     */
    public Player() {
    }

    /**
     * constructor with a player's username
     *
     * @param userName
     */
    public Player(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalScannedCodes() {
        return this.totalScannedCodes;
    }

    public void setTotalScannedCodes(Integer totalScannedCodes) {
        this.totalScannedCodes = totalScannedCodes;
    }

    // todo: to query all users data, get the current player's overall rank
    public Integer getRank() {
        // return a temporary value for now
        return 0;
    }

    public List<String> getSavedCodeList() {
        return this.savedCodeList;
    }

    public void setSavedCodeList(List<String> savedCodeList) {
        this.savedCodeList = savedCodeList;
    }

    /**
     * populate Player class properties from Firebase Players collection queried by username
     */
    public void populateFromFirebase() {
        // Access a Cloud Firestore instance
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference usersRef = db.collection("Players");
        // query Firebase by username
        Query queryByUserName = usersRef.whereEqualTo("username", this.userName);

        // add listener
        queryByUserName
                .limit(1)       // limit to only 1 player, as user sign up process guarantees username uniqueness
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && (!task.getResult().isEmpty())) {
                            // get the first element from the query result
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                            if (document.exists()) {
                                // start to set class field value

                                // read the string directly
                                setEmail(document.get("email").toString());
                                setPhone(document.get("phone").toString());

                                // lambda expression, if no such a field, then default to 0, otherwise cast to an integer
                                setTotalScore(document.get("totalScore") == null ? 0 : (Integer) document.get("totalScore"));
                                setTotalScannedCodes(document.get("totalScannedCodes") == null ? 0 : (Integer) document.get("totalScannedCodes"));

                                // todo: get the QR codes, need to get codes sub collection, need to clarify how it is defined
                                // document.get("codes").toString();


                                Log.d(TAG, "DocumentSnapshot data: ${document.data}");
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                }); //addOnCompleteListener
    }

}

