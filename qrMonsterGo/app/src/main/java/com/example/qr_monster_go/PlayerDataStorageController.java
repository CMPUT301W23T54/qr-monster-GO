package com.example.qr_monster_go;


import java.util.ArrayList;

public class PlayerDataStorageController implements DataStorageController<Player>{

    QrMonsterGoDB db;

    public PlayerDataStorageController(QrMonsterGoDB establishedDatabase) {
        this.db = establishedDatabase;
    }

    @Override
    public void addElement( Player object) {


    }

    @Override
    public void removeElement( String playerId) {
    }

    @Override
    public void editElement( Player object, String key) {
    }

    @Override
    public Player getElementOfId( String username) {
        return null;
    }

    @Override
    public ArrayList<Player> getSearchResultList( String searchKeywords) {
        return null;
    }

    @Override
    public void sortElement() {
    }
}
