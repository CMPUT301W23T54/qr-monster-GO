package com.example.qr_monster_go;

import java.util.ArrayList;

public interface DataStorageController<T> {

    void addElement( T object) ;

    void removeElement( T object);

    void editElement( T object);

    T getElementOfId( String username);

    ArrayList<T> getSearchResultList( String searchKeywords);

    void sortElement();






}
