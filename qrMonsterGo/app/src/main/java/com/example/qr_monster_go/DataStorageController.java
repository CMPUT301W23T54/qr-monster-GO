package com.example.qr_monster_go;

import java.util.ArrayList;

public interface DataStorageController<T> {

    void addElement(T object) ;

    void removeElement(String objectId);

    void editElement(T object, String key);

    T getElementOfId(String username);

    ArrayList<T> getSearchResultList(String searchKeywords);

    void sortElement();






}
