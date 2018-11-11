package com.google.controller;

import com.google.domain.User;
import com.google.domain.UserData;
import com.google.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User postUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId){
        return userService.getUser(userId);
    }

}
