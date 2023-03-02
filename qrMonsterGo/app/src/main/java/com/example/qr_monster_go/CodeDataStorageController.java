package com.example.qr_monster_go;

import java.util.ArrayList;

public class CodeDataStorageController implements DataStorageController<ScannableCode>{

    @Override
    public void addElement(QrMonsterGoDB db, ScannableCode object) {
        DataStorageController.super.addElement(db, object);
    }

    @Override
    public void removeElement(QrMonsterGoDB db, ScannableCode object) {
        DataStorageController.super.removeElement(db, object);
    }

    @Override
    public void editElement(QrMonsterGoDB db, ScannableCode object) {
        DataStorageController.super.editElement(db, object);
    }

    @Override
    public ScannableCode getElementOfId(QrMonsterGoDB db, String username) {
        return DataStorageController.super.getElementOfId(db, username);
    }

    @Override
    public ArrayList<ScannableCode> getSearchResultList(QrMonsterGoDB db, String searchKeywords) {
        return DataStorageController.super.getSearchResultList(db, searchKeywords);
    }

    @Override
    public void sortElement() {
        DataStorageController.super.sortElement();
    }
}
