package com.google.controller;

import com.google.domain.Authentication;
import com.google.domain.UserData;
import com.google.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Console;

@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @RequestMapping(value = "/")
    public String starter(){
        return "Version: " + System.getProperty("java.version")
                + " OS: " + System.getProperty("os.name")
                + " User: " + System.getProperty("user.name");
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.GET)
    public UserData home(@RequestHeader("authorization-code") String authCode){
        return authenticationService.saveAuthentication(authCode);
    }

}
