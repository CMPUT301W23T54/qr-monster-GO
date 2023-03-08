package com.example.qr_monster_go;


import java.util.ArrayList;

public class PlayerDataStorageController implements DataStorageController<Player>{

    QrMonsterGoDB db;

    public PlayerDataStorageController(QrMonsterGoDB establishedDatabase) {
        this.db = establishedDatabase;
    }

    @Override
    public void addElement(QrMonsterGoDB db, Player object) {
    }

    @Override
    public void removeElement(QrMonsterGoDB db, Player object) {
    }

    @Override
    public void editElement(QrMonsterGoDB db, Player object) {
    }

    @Override
    public Player getElementOfId(QrMonsterGoDB db, String username) {
        return null;
    }

    @Override
    public ArrayList<Player> getSearchResultList(QrMonsterGoDB db, String searchKeywords) {
        return null;
    }

    @Override
    public void sortElement() {
    }
}
