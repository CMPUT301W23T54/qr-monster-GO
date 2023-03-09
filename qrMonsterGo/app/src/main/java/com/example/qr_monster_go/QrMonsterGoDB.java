package com.example.qr_monster_go;



import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

public class QrMonsterGoDB {

    FirebaseFirestore db;

    public QrMonsterGoDB() {
        db = FirebaseFirestore.getInstance();
    }

    public CollectionReference getCollectionReference(String str){
        final CollectionReference collectionReference = db.collection(str);
        return collectionReference;
    }


}
