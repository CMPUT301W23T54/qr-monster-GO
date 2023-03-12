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

public class CodeDataStorageController implements DataStorageController<ScannableCode>{

    private QrMonsterGoDB db;

    public CodeDataStorageController(QrMonsterGoDB establishedDB) {
        this.db = establishedDB;
    }



    public boolean isCodeAlreadyScanned(String code){

        boolean[] isAlreadyScanned = {false};

        db.getDocumentReference(code, "CodeCollection")
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
    }//isCodeAlreadyScanned

    /**
     * @param code - the code object to be added to the database
     *
     */
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
    }//addElement


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
    }//removeElement



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
    }//method



    @Override
    public void editElement( ScannableCode code, String key) {
    }//editElement


    @Override
    public ScannableCode getElementOfId(String codeId) {
        ScannableCode[] code = new ScannableCode[1];
        DocumentReference codeRef = db.getDocumentReference(codeId,"CodeCollection");
        codeRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                code[0] = documentSnapshot.toObject(ScannableCode.class);
            }
        });
        return code[0];
    }//getElementOfId


    @Override
    public ScannableCode getElementOfKey(String key, String value) {

        return null;

    }


    public ArrayList<String> getPlayerWhoHasCode(String code){
        return getElementOfId(code).getPlayerList();
    }//getPlayerWhoHasCode

    @Override
    public ArrayList<ScannableCode> getSearchResultList( String searchKeywords) {
        return null;
    }


    @Override
    public void sortElement() {
    }








}
