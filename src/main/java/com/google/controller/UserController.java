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
    public User home(@RequestBody User user){
        return userService.saveUser(user);
    }

}
