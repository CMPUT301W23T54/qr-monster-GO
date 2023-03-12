package com.example.qr_monster_go;


import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is PlayerDataStorageController class
 * It builds an interface for the exchange of data
 * between the local machine and remote database
 * It has several methods either to add, delete, modify, query data from the
 * PlayerCollection in QrMonsterGo database
 */
public class PlayerDataStorageController implements DataStorageController<Player>{

    private QrMonsterGoDB db;

    public PlayerDataStorageController(QrMonsterGoDB establishedDatabase) {
        this.db = establishedDatabase;
    }



    @Override
    public void addElement( Player player) {
        CollectionReference playerCollectionReference = db.getCollectionReference("PlayerCollection");
        Map<String, Object> curPlayerMap = new HashMap<>();

        curPlayerMap.put("Username", player.getUserName());
        curPlayerMap.put("Email", player.getEmail());
        curPlayerMap.put("Phone", player.getPhone());
        curPlayerMap.put("TotalScore", player.getTotalScore());
        curPlayerMap.put("Total#Scan", player.getTotalScannedCodes());
        curPlayerMap.put("SavedCodes", player.getSavedCodeList());

        playerCollectionReference
                .add(curPlayerMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Log.d("RRG", "document is successfully added");
                        }else{
                            Log.d("RRG", "something went wrong");
                        }
                    }
                });
    }//addElement


    public boolean ifUserNameExists(String username){
        boolean[] isAlreadyScanned = {false};

        db.getDocumentReference(username, "PlayerCollection")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            isAlreadyScanned[0] = task.getResult().exists();
                            Log.d(TAG, "Cached document data" );
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });
        return isAlreadyScanned[0];
    }//checkUserNameExists


    @Override
    public Player getElementOfId(String username) {
        Player[] player = new Player[1];
        DocumentReference codeRef = db.getDocumentReference(username,"PlayerCollection");
        codeRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        player[0] = documentSnapshot.toObject(Player.class);
                    }
                })//OnSuccess
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting document", e);
                    }
                });//onFailure
        return player[0];
    }//getElementOfId


    /**
     * This is a method that receives playerId and codeId and delete that code from the player's
     * codeList in the Database.
     *
     * @param username - the username of the Player who wants to delete a code from their account
     * @param code - the hashString of the code that player wants to delete from their account
     */
    public void removeCodeFromPlayerAccount(String username, String code){
        DocumentReference playerRef = db.getDocumentReference(username,"PlayerCollection");
        playerRef.update("codeList", FieldValue.arrayRemove(code))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "this Code in player's codeList successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting code from the player's  codeList", e);
                    }
                });
    }


    @Override
    public void removeElement(String playerId) {

    }






    @Override
    public void editElement( Player object, String key) {


    }




    @Override
    public Player getElementOfKey(String key, String value) {
        return null;
    }

    @Override
    public ArrayList<Player> getSearchResultList( String searchKeywords) {
        return null;
    }

    @Override
    public void sortElement() {
    }




}
