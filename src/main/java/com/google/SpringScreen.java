package com.google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.domain.Authentication;
import com.google.domain.Event;
import com.google.domain.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class SpringScreen extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        ObjectifyService.register(Authentication.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Event.class);

        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("trainerapp-8409a-firebase-adminsdk-ghmz0-e5b9873ba3.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return application.sources(SpringScreen.class);
    }
}
