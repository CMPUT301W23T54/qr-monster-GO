package com.example.qr_monster_go;

import java.util.ArrayList;

public class CodeDataStorageController implements DataStorageController<ScannableCode>{

    @Override
    public void addElement(QrMonsterGoDB db, ScannableCode object) {
    }

    @Override
    public void removeElement(QrMonsterGoDB db, ScannableCode object) {
    }

    @Override
    public void editElement(QrMonsterGoDB db, ScannableCode object) {
    }

    @Override
    public ScannableCode getElementOfId(QrMonsterGoDB db, String username) {
        return null;
    }

    @Override
    public ArrayList<ScannableCode> getSearchResultList(QrMonsterGoDB db, String searchKeywords) {
        return null;
    }

    @Override
    public void sortElement() {
    }
}
