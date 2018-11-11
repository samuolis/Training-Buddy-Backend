package com.google.service;

import com.google.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public User saveUser(User user){
        logger.info("user info " + user.toString());
        try {
            ofy().save().entity(user).now();
            return user;
        } catch (Exception e){
            throw e;
        }
    }

}
