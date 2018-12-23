package com.google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.domain.CommentMessage;
import com.google.domain.Event;
import com.google.domain.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class SpringScreen extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Event.class);
        ObjectifyService.register(CommentMessage.class);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setDatabaseUrl("https://training-222106.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

            System.out.println("Loaded");  // "[DEFAULT]"

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return application.sources(SpringScreen.class);
    }
}
