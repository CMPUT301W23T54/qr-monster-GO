package com.example.qr_monster_go;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CodeDataStorageController implements DataStorageController<ScannableCode>{

    QrMonsterGoDB db;
    public CodeDataStorageController(QrMonsterGoDB establishedDB) {
        this.db = establishedDB;
    }

    @Override
    public void addElement( ScannableCode code) {
        CollectionReference codeCollectionReference = db.getCollectionReference("CodeCollectionReference");

        Map<String, Object> curCodeMap = new HashMap<>();
        curCodeMap.put("codeStringKey", code.getCode());
        curCodeMap.put("codeScoreKey", code.getScore());
        curCodeMap.put("codeNameKey", code.getName());

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
    public void removeElement( ScannableCode object) {
    }

    @Override
    public void editElement( ScannableCode object) {
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
