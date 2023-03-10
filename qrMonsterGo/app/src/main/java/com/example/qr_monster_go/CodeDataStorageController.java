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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CodeDataStorageController implements DataStorageController<ScannableCode>{

    QrMonsterGoDB db;
    public CodeDataStorageController(QrMonsterGoDB establishedDB) {
        this.db = establishedDB;
    }

    @Override
    public void addElement( ScannableCode code) {
        CollectionReference codeCollectionReference = db.getCollectionReference("CodeCollection");

        Map<String, Object> curCodeMap = new HashMap<>();
        curCodeMap.put("codeStringKey", code.getCode());
        curCodeMap.put("codeScoreKey", code.getScore());
        curCodeMap.put("codeNameKey", code.getName());
        curCodeMap.put("codePlayerList", code.getPlayerList());

        codeCollectionReference
                .add(curCodeMap)
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
    public void removeElement( String code) {
        db.getDocumentReference(code, "CodeCollection")
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


    public void removePlayerFromPlayerList(String code, String username){
        DocumentReference codeRef = db.getDocumentReference("code","CodeCollection");
        codeRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot codeSnapshot) {
                if(codeSnapshot.getData().get("playerList").equals(new ArrayList<String>().add(username)))
                    removeElement(code);
                // delete the document if code playerList contains only the specified player
                else {
                    codeRef.update("playerList", FieldValue.arrayRemove("username"))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Player in PlayerList successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting player from the player list", e);
                                }
                            });

                }
                //else update the playerList in code document
            }//on success
        })//on success listener
        .addOnFailureListener(new OnFailureListener() {
            @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error getting snap", e);
                }
            });//on failure listener
    }

    @Override
    public void editElement( ScannableCode code, String key) {



    }

    @Override
    public ScannableCode getElementOfId( String username) {

        return null;
    }

    @Override
    public ArrayList<ScannableCode> getSearchResultList( String searchKeywords) {
        return null;
    }

    @Override
    public void sortElement() {
    }
}
