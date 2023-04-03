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

    /**
     * @return - the saved CodeList of a player
     *
     * This method returns the savedCodeList of a player.
     */
    public List<String> getSavedCodeList() {
        return this.savedCodeList;
    }


    /**
     * @param savedCodeList - the List<String> representing the code saved by the player
     *
     * This method takes in a list of code and set it to player's codeList.
     */
    public void setSavedCodeList(List<String> savedCodeList) {
        this.savedCodeList = savedCodeList;
    }


}

