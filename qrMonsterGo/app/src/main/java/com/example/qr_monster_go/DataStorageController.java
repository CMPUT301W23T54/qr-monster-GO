package com.example.qr_monster_go;

import java.util.ArrayList;

public interface DataStorageController<T> {

    public default void addElement(QrMonsterGoDB db, T object) {}

    public default void removeElement(QrMonsterGoDB db, T object) {}

    public default void editElement(QrMonsterGoDB db, T object){}

    public default T getElementOfId(QrMonsterGoDB db, String username){ return null; }

    public default ArrayList<T> getSearchResultList(QrMonsterGoDB db, String searchKeywords){ return null; }

    public default void sortElement(){}






}
