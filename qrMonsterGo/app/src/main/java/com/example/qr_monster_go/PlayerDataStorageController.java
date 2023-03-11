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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataStorageController implements DataStorageController<Player>{

    QrMonsterGoDB db;

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



    @Override
    public void editElement( Player object, String key) {
    }

    @Override
    public Player getElementOfId( String username) {
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
