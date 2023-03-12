package com.example.qr_monster_go;


import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

public class QrMonsterGoDB {

    private FirebaseFirestore db;

    public QrMonsterGoDB() {

        db = FirebaseFirestore.getInstance();
    }

    public CollectionReference getCollectionReference(String collectionName){

        final CollectionReference collectionReference = db.collection(collectionName);
        return collectionReference;


    }

    public DocumentReference getDocumentReference(String documentId, String collectionName){

        final DocumentReference documentReference = db.collection(collectionName).document(documentId);
        return documentReference;
    }



}
