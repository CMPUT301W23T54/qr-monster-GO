package com.example.qr_monster_go;


import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataStorageController implements DataStorageController<Player>{

    private QrMonsterGoDB db;

    public PlayerDataStorageController(QrMonsterGoDB establishedDatabase) {
        this.db = establishedDatabase;
    }



    @Override
    public void addElement( Player player) {
        CollectionReference playerCollectionReference = db.getCollectionReference("PlayerCollection");

        Map<String, Object> curPlayerMap = new HashMap<>();

        curPlayerMap.put("playerStringKey", null);
        curPlayerMap.put("playerKey", null);
        curPlayerMap.put("playerUserNameKey", null);
        curPlayerMap.put("playerFriendList", null);

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
    }

    @Override
    public void removeElement( String playerId) {
        db.getDocumentReference(playerId, "PlayerCollection")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }



    public boolean checkUserNameExists(String username){

        final boolean[] isAlreadyScanned = {false};

        db.getDocumentReference(username, "CodeCollection")
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

    public void getSearchedPlayerByUsername(){

    }


    @Override
    public void editElement( Player object, String key) {



    }

    @Override
    public Player getElementOfId( String username) {
        return null;
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
