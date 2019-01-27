package com.google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.domain.CommentMessage;
import com.google.domain.Event;
import com.google.domain.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.googlecode.objectify.ObjectifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class SpringScreen extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(SpringScreen.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        ObjectifyService.init();
        ObjectifyService.register(User.class);
        ObjectifyService.register(Event.class);
        ObjectifyService.register(CommentMessage.class);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setDatabaseUrl("https://training-222106.firebaseio.com")
                    .build();

            if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
                FirebaseApp.initializeApp(options);
            }

            System.out.println("Loaded");  // "[DEFAULT]"

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return application.sources(SpringScreen.class);
    }

}
