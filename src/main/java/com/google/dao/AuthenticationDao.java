package com.google.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.domain.Authentication;

public class AuthenticationDao {

    private DatastoreService datastore;
    private static final String AUTHENTICATION_KIND = "Authentication";

    public AuthenticationDao() {
        datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
    }

//    public Long createAuthentication(Authentication authentication) {
//        Entity incBookEntity = new Entity(AUTHENTICATION_KIND);  // Key will be assigned once written
//        incBookEntity.setProperty(Authentication.AUTHENTICATION_ID, authentication.getAuthenticationId());
//        incBookEntity.setProperty(Authentication.ACCESS_TOKEN, authentication.getAccessToken());
//        incBookEntity.setProperty(Authentication.TOKEN_REFRESH_TIME, authentication.getTokenRefreshTime());
//
//        Key bookKey = datastore.put(incBookEntity); // Save the Entity
//        return bookKey.getId();                     // The ID of the Key
//    }
}
