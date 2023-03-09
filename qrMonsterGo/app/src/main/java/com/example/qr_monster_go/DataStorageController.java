package com.example.qr_monster_go;

import java.util.ArrayList;

public interface DataStorageController<T> {

    void addElement(QrMonsterGoDB db, T object) ;

    void removeElement(QrMonsterGoDB db, T object);

    void editElement(QrMonsterGoDB db, T object);

    T getElementOfId(QrMonsterGoDB db, String username);

    ArrayList<T> getSearchResultList(QrMonsterGoDB db, String searchKeywords);

    void sortElement();






}
