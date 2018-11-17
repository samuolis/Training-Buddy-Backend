package com.google.service;

import com.google.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User saveUser(User user){
        logger.info("Save user");
        try {
            ofy().save().entity(user).now();
            return user;
        } catch (Exception e){
            throw e;
        }
    }

    public User getUser(String id){
        logger.info("Get user");
        User user;
        try {
            user = ofy().cache(false).load().type(User.class).id(id).now();
            if (user == null){
                throw new NotFoundException();
            }
            else {
                return user;
            }
        } catch (Exception e){
            throw e;
        }
    }

}
