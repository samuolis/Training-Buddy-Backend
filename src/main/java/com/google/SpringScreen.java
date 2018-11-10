package com.google;

import com.google.domain.Authentication;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringScreen extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        ObjectifyService.begin();
        ObjectifyService.register(Authentication.class);
        return application.sources(SpringScreen.class);
    }
}
