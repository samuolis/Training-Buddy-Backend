package com.google.service

import com.google.api.client.json.jackson2.JacksonFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class AuthenticationService {

    fun validateUser(authCode: String): Boolean {
        logger.info("validate authentication with token : $authCode")

        var decodedToken: FirebaseToken? = null
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(authCode)
        } catch (e: FirebaseAuthException) {
            println("error: $e")
        }

        if (decodedToken != null) {
            println("User id: " + decodedToken.uid)
            return true
        } else {
            println("Invalid ID token.")
            return false
        }
    }

    companion object {

        private val logger = LoggerFactory.getLogger(AuthenticationService::class.java)
        private val jacksonFactory = JacksonFactory()
    }
}
