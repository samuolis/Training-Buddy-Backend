package com.google.controller

import com.google.domain.CommentMessage
import com.google.domain.Event
import com.google.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
class NotificationController {

    @Autowired
    lateinit var notificationService: NotificationService


    @RequestMapping(value = "/notification/event", method = arrayOf(RequestMethod.POST))
    fun signEvent(@RequestBody event: Event) {
        return notificationService.sendEventSignNotification(event)
    }

    @RequestMapping(value = "/notification/comment", method = arrayOf(RequestMethod.POST))
    fun comment (@RequestBody commentMessage: CommentMessage) {
        return notificationService.sendCommentNotification(commentMessage)
    }

    @RequestMapping(value = "/notification/refresh", method = arrayOf(RequestMethod.POST))
    fun refresh (@RequestBody event: Event) {
        return notificationService.sendRefreshNotification(event)
    }
}