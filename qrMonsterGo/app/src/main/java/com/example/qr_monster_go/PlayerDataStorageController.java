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

    final private QrMonsterGoDB db; //the QRMonsterGoDB associated with the data controller.

    /**
     * @param establishedDB - the established database in the process
     *
     *  This is the constructor for CodeDataStorageController class.
     *  It takes in a QRMonsterGoDB object and construct a data controller
     *  using it to access the database created.
     */
    public PlayerDataStorageController(QrMonsterGoDB establishedDB) {
        this.db = establishedDB;
    }



    /**
     * @param username - the username of the Player that needs to check if already exist in DB
     * @return  -  whether player with the same username is already in the DB
     *
     *  This is  a method that takes in the username of a Player and checks if the
     *  document is already in the database.
     *  Note: It is designed to be used together with the other methods in the class
     *        to prevent carrying out operations on a non-existent document.
     */
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



    /**
     * @param player - the Player object to be added to the database
     *
     *  This is a method that takes in a Player object and Stores
     *  it in the PlayerCollection in the database.
     *  Beware: the method does not check whether the document already exists in the database.
     *          It will report an error if adding something with the same id.
     */
    @Override
    public void addElement( Player player) {
        CollectionReference playerCollectionReference = db.getCollectionReference("PlayerCollection");
        Map<String, Object> curPlayerMap = new HashMap<>();

        curPlayerMap.put("username", player.getUsername());
        curPlayerMap.put("email", player.getEmail());
        curPlayerMap.put("phone", player.getPhone());
        curPlayerMap.put("totalScore", player.getTotalScore());
        curPlayerMap.put("totalScannedCodes", player.getTotalScannedCodes());
        curPlayerMap.put("savedCodeList", player.getSavedCodeList());

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



     /**
     * @param username - the player username that is needed to objectify
     * @return - the Player object that is fetched from the database
     *
     *  This is a method that takes in the username of the Player
     *  and returns the Player object that corresponds to the username
     *  recorded in the database.
     *  Beware: the method does not check whether the document exists in the database.
     *          It will report an error if fetching something not yet recorded.
     */
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
     * @param username - the username of the Player who wants to delete a code from their account
     * @param code - the hashString of the code that player wants to delete from their account
     *
     *  This is a method that receives playerId and codeId and delete that code from the player's
     *  codeList in the Database.
     *  Beware: the method does not check whether the player or code exists in the database.
     *          It will report an error if deleting something not yet recorded.
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


    public void editUsername(){


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
