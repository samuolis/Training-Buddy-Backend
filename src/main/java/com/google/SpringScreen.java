package com.google;

import com.google.domain.Authentication;
import com.google.domain.Event;
import com.google.domain.User;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringScreen extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        ObjectifyService.register(Authentication.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Event.class);
        return application.sources(SpringScreen.class);
    }
}
