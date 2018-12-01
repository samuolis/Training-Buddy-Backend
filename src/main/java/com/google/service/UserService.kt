package com.google.service

import com.google.domain.User
import com.googlecode.objectify.Key
import com.googlecode.objectify.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import com.googlecode.objectify.ObjectifyService.ofy

@Service
class UserService {

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun saveUser(user: User): User {
        logger.info("Save user")
        try {
            ofy().save().entity(user).now()
            return user
        } catch (e: Exception) {
            throw e
        }

    }

    fun getUser(id: String): User {
        logger.info("Get user")
        val user: User?
        try {
            user = ofy().cache(false).load().type(User::class.java).id(id).now()
            return user ?: throw NotFoundException()
        } catch (e: Exception) {
            throw e
        }

    }

    fun getUsersById(userIdsList: List<String>): List<User>{
        logger.info("Get user list")
        val userList: List<User>?
        try {
            userList = ofy().load().type(User::class.java).ids(userIdsList).values.toList()
            return userList ?: throw NotFoundException()
        } catch (e: Exception) {
            throw e
        }
    }



}
