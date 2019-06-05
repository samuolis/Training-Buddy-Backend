package com.google.controller

import com.google.domain.User
import com.google.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

    @Autowired
    internal var userService: UserService? = null

    @RequestMapping(value = ["/user"], method = arrayOf(RequestMethod.POST))
    fun postUser(@RequestBody user: User): User {
        return userService!!.saveUser(user)
    }

    @RequestMapping(value = ["/user/{userId}"], method = arrayOf(RequestMethod.GET))
    fun getUser(@PathVariable userId: String): User {
        return userService!!.getUser(userId)
    }

    @RequestMapping(value = ["/users"], method = arrayOf(RequestMethod.POST))
    fun getUsersByIds(@RequestBody userIds: List<String>): List<User> {
        return userService!!.getUsersById(userIds)
    }

}
