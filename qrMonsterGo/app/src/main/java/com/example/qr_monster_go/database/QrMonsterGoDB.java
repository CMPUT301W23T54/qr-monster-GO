package com.example.qr_monster_go.database;


import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

/**
 * This is QRMonsterGoDB class.
 * It's designed to encapsulate the FirebaseFirestore object
 * and hide internal details of getting access to our database
 */
public class QrMonsterGoDB {

    private FirebaseFirestore db;

    /**
     * This is the constructor of the class.
     * It initializes the connection to database
     */
    public QrMonsterGoDB() {

        db = FirebaseFirestore.getInstance();
    }

    /**
     * @param collectionName - the string name of the collections that we are accessing
     * @return - the collection reference given by the string name
     *
     *  This method takes in String of collectionName and returns the a CollectionReference
     *  object corresponding to the collectionName.
     */
    public CollectionReference getCollectionReference(String collectionName){

        if (collectionName != "PlayerCollection" && collectionName != "CodeCollection"){
            Log.d(TAG, "collectionName is invalid. Please enter only 'PlayerCollection' or 'CodeCollection' " );
        }

        final CollectionReference collectionReference = db.collection(collectionName);
        return collectionReference;


    }

    /**
     * @param documentId - the id of the document sought after
     * @param collectionName - the name string of the collection that contains the document
     * @return - the DocumentReference object that provides interface to the document in the db
     *
     *  This method takes in String of collectionName and document Id,
     *  and returns the a DocumentReference object corresponding to the documentId.
     *  Beware: If the document of Id does not yet exist in the database, it will create a document
     *          of that Id automatically. Should check whether the document exists before
     *          executing the query methods.
     */
    public DocumentReference getDocumentReference(String documentId, String collectionName){

        if (collectionName != "PlayerCollection" || collectionName != "CodeCollection"){
            Log.d(TAG, "collectionName is invalid. Please enter only 'PlayerCollection' or 'CodeCollection' " );
        }

        final DocumentReference documentReference = db.collection(collectionName).document(documentId);
        return documentReference;

    }



}
