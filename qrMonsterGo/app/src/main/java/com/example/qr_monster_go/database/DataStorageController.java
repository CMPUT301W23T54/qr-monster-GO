package com.example.qr_monster_go.database;

import java.util.ArrayList;

/**
 * @param <T> the generic object that is stored in database
 *           This is an interface of generic database management classes.
 *           It helps manage the data from the database.
 *           It is implemented by concrete data concroller classes.
 */
public interface DataStorageController<T> {


    /**
     * This is a method that adds an object to the database
     * @param object the generic object that needs to be added to the database
     */
    void addElement(T object) ;


    /**
     * This is a method that removes an object from the database
     * @param objectId - the id of the document that needs to be removed
     */
    void removeElement(String objectId);


    /**
     * @param value
     * @return
     */
    T getElementOfId( String value);

    T getElementOfKey( String key, String value);

    ArrayList<T> getSearchResultList(String searchKeywords);

    void sortElement();






}
