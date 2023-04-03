package com.example.qr_monster_go.database;

import static android.content.ContentValues.TAG;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_monster_go.home.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  This is CodeDataStorageController class
 *  It builds an interface for the exchange of data
 *  between the local machine and remote database
 *  It has several methods either to add, delete, modify, query data from the
 *  CodeCollection in QrMonsterGo database
 */
public class CodeDataStorageController implements DataStorageController<QRCode> {

    final private QrMonsterGoDB db;   //the QRMonsterGoDB associated with the data controller.

    /**
     * @param establishedDB - the established database in the process
     *
     *  This is the constructor for CodeDataStorageController class.
     *  It takes in a QRMonsterGoDB object and construct a data controller
     *  using it to access the database created.
     */
    public CodeDataStorageController(QrMonsterGoDB establishedDB) {
        this.db = establishedDB;
    }


    /**
     * @param code - the hashString of the QRCode that needs to check if already exist in DB
     * @return  -  whether QRCode with the same hash is already in the DB
     *
     *  This is  a method that takes in the hashString of a QRCode and checks if the
     *  document is already in the database.
     *  Note: It is designed to be used together with the other methods in the class
     *        to prevent carrying out operations on a non-existent document.
     */
    public boolean isCodeAlreadyScanned(String code){

        boolean[] isAlreadyScanned = {false};

        db.getDocumentReference(code, "CodeCollection")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Document found in the offline cache
                         isAlreadyScanned[0] = task.getResult().exists();

                        Log.d(TAG, "Cached document data" );
                    } else {
                        Log.d(TAG, "Cached get failed: ", task.getException());
                    }
                });
        return isAlreadyScanned[0];
    }//isCodeAlreadyScanned

    /**
     * @param code - the QRCode object to be added to the database
     *
     *  This is a method that takes in a QRCode object and Stores
     *  it in the CodeCollection in the database.
     *  Beware: the method does not check whether the document already exists in the database.
     *          It will report an error if adding something with the same id.
     */
    @Override
    public void addElement( QRCode code) {
        CollectionReference codeCollectionReference = db.getCollectionReference("CodeCollection");

        String base64String = null;
        if (code.getImageMap() != null) {
            if (code.getImageMap().length != 0) {
                base64String = Base64.encodeToString(code.getImageMap(), Base64.DEFAULT);
            }
        }

        Map<String, Object> curCodeMap = new HashMap<>();
        curCodeMap.put("code", code.getCode());
        curCodeMap.put("score", code.getScore());
        curCodeMap.put("name", code.getName());
        curCodeMap.put("playerList", code.getPlayerList());
        curCodeMap.put("location", code.getGeolocation());
        curCodeMap.put("comments", code.getCommentList());
        curCodeMap.put("imageMap", base64String);

        DocumentReference doc = codeCollectionReference.document(code.getCode());

        doc.set(curCodeMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("RRG", "document is successfully added");
                        } else {
                            Log.d("RRG", "something went wrong");
                        }
                    }
                });

    }//addElement


    /**
     * @param code - the id of the document that needs to be removed
     *  This is a method that takes in a hashString of the QRCode
     *  and removes the corresponding document in the database.
     *  Beware: the method does not check whether the document exists in the database.
     *          It will report an error if deleting something not yet recorded.
     */
    @Override
    public void removeElement( String code) {
        db.getDocumentReference(code, "CodeCollection")
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }//removeElement


    /**
     * @param codeId - the code hashString that is needed to objectify
     * @return - the QRCode object that is fetched from the database
     *
     *  This is a method that takes in the hashString of the QRCode
     *  and returns the QRCode object that corresponds to the hashString
     *  recorded in the database.
     *  Beware: the method does not check whether the document exists in the database.
     *          It will report an error if fetching something not yet recorded.
     */
    @Override
    public QRCode getElementOfId(String codeId) {
        QRCode[] code = new QRCode[1];
        DocumentReference codeRef = db.getDocumentReference(codeId,"CodeCollection");
        codeRef.get()
                .addOnSuccessListener(documentSnapshot -> code[0] = documentSnapshot.toObject(QRCode.class))//onSuccess
                .addOnFailureListener(e -> Log.w(TAG, "Error getting document", e));//onFailure
        return code[0];
    }//getElementOfId

    /**
     * @param username - the username that we need query for
     * @return - the list of code hashString that is scanned by the user
     *
     *  This is a method that takes in a player username and return the list of
     *  code hashString that the
     */
    public ArrayList<String> getCodePlayerScanned(String username){
        ArrayList<String> codeList = new ArrayList<>();
        final CollectionReference codeRef = db.getCollectionReference("CodeReference");
        // query Firebase by username
        Query queryByUserName = codeRef.whereArrayContains("playerList", username);
        queryByUserName
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                Log.d(TAG, "No such document");
                            }
                            else{
                                //todo  query successful
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    codeList.add(document.get("code" ,String.class));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }//query result
                        }//if query result not empty
                        else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }//task failed
                    }//getSuccessful
                }); //addOnCompleteListener
        return codeList;
    }


    /**
     * @param code - the hashString of a QRCode
     * @return - the ArrayList<String> of players who has the QRCode scanned
     *
     * This method takes in the String of code object and return the playerList who
     * has collected the QRCode.
     */
    public ArrayList<String> getPlayerWhoHasCode(String code){
        return getElementOfId(code).getPlayerList();
    }//getPlayerWhoHasCode







}
