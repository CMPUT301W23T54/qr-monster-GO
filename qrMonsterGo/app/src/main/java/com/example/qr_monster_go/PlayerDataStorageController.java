package com.example.qr_monster_go;


import java.util.ArrayList;

public class PlayerDataStorageController implements DataStorageController<Player>{

    QrMonsterGoDB db;

    public PlayerDataStorageController(QrMonsterGoDB establishedDatabase) {
        this.db = establishedDatabase;
    }

    @Override
    public void addElement(QrMonsterGoDB db, Player object) {
        DataStorageController.super.addElement(db, object);
    }

    @Override
    public void removeElement(QrMonsterGoDB db, Player object) {
        DataStorageController.super.removeElement(db, object);
    }

    @Override
    public void editElement(QrMonsterGoDB db, Player object) {
        DataStorageController.super.editElement(db, object);
    }

    @Override
    public Player getElementOfId(QrMonsterGoDB db, String username) {
        return DataStorageController.super.getElementOfId(db, username);
    }

    @Override
    public ArrayList<Player> getSearchResultList(QrMonsterGoDB db, String searchKeywords) {
        return DataStorageController.super.getSearchResultList(db, searchKeywords);
    }

    @Override
    public void sortElement() {
        DataStorageController.super.sortElement();
    }
}
