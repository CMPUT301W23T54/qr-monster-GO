package com.example.qr_monster_go.player;

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

/**
 * Player class, allows ot create a player object and gets various information about the player
 */
public class Player {
    private String username;
    private String email;
    private String phone;
    private Integer totalScore;
    private Integer totalScannedCodes;
    private List<String> savedCodeList;

    FirebaseFirestore db;

    /**
     * constructor with a player's username
     *
     * @param username
     */
    public Player(String username) {
        this.username = username;
    }

    /**
     * Getter method to get players username
     * @return
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Setter method to set players username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method to get players email
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Setter method to set players email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter method to get players phone
     * @return
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Setter method to set players phone
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Getter method to get players score
     * @return
     */
    public Integer getTotalScore() {
        return this.totalScore;
    }

    /**
     * Setter method to set players score
     * @param totalScore
     */
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Getter method to get players scanned codes
     * @return
     */
    public Integer getTotalScannedCodes() {
        return this.totalScannedCodes;
    }

    /**
     * Setter method to set players scanned codes
     * @param totalScannedCodes
     */
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
     * populate Player class properties from Firebase PlayersAdapter collection queried by username
    public void populateFromFirebase() {
        // Access a Cloud Firestore instance
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference usersRef = db.collection("PlayersAdapter");
        // query Firebase by username
        Query queryByUserName = usersRef.whereEqualTo("username", this.username);

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


     */

}

