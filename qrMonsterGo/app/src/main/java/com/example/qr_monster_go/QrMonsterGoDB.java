package com.example.qr_monster_go;


<<<<<<< HEAD

=======
>>>>>>> 050f122fe77ef280d1c4b08bacc99456b44b47e1
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
