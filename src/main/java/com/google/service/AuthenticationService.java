package com.google.service;

import com.google.domain.Authentication;
import com.google.domain.UserData;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Date;

import static com.googlecode.objectify.ObjectifyService.ofy;


@Service
public class AuthenticationService {

    final String ACCESS_TOKEN_URL = "https://graph.accountkit.com/v1.3/access_token?grant_type=authorization_code&code=";
    final String ACCOUNT_KIT_URL = "https://graph.accountkit.com/v1.3/me/?access_token=";

    final String CLIENT_ID = "357099475220262";
    final String SECRET = "84876895f9ed23f08ec4976daf608320";
    final String REDIRECT_URI = "https://trainingbuddy-221215.appspot.com/user/";

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    public UserData saveAuthentication(String authCode){
        logger.info("Save authentication called");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resultForUser;
        UserData finalResult = new UserData();
        String finalUrlAccess = ACCESS_TOKEN_URL + authCode + "&access_token=AA|" + CLIENT_ID + "|" + SECRET;
        ResponseEntity<String> resultForAuthentication;
        resultForAuthentication = restTemplate.getForEntity(finalUrlAccess, String.class);
        Gson gson = new Gson();
        Authentication authenticationResult = gson.fromJson(resultForAuthentication.getBody(), Authentication.class);
        authenticationResult.setCreatedAt(new Date());
        long timeInMil = authenticationResult.getCreatedAt().getTime();
        long expirationTimeInMil = timeInMil + authenticationResult.getTokenRefreshTime()*1000;
        authenticationResult.setExpirationDate(new Date(expirationTimeInMil));
        if (authenticationResult.getAccessToken() != null) {
            try {
                ofy().save().entity(authenticationResult).now();
            } catch (Exception e){
                throw e;
            }
            String finalUrlUser = ACCOUNT_KIT_URL + authenticationResult.getAccessToken();
            try{
                resultForUser = restTemplate.getForEntity(finalUrlUser, String.class);
                finalResult = gson.fromJson(resultForUser.getBody(), UserData.class);
            }catch (Exception e){
                throw e;
            }
        }
        return finalResult;
    }
}
