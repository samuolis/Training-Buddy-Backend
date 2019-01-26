package com.google.service

import com.google.domain.CommentMessage
import com.google.domain.Event
import com.google.domain.Notification
import com.google.domain.NotificationObject
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.Message
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*
import com.google.firebase.messaging.FirebaseMessaging



@Service
class NotificationService {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var eventService: EventService

    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun sendEventSignNotification(event: Event){
        logger.info("In Notification")
        var signedUser = userService.getUser(event.signedUserId!!)
        var eventHolderUser = userService.getUser(event.userId!!)

        var message = Message.builder()
                .setToken(eventHolderUser.userFcmToken)
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 24 * 1000)
                        .setPriority(AndroidConfig.Priority.NORMAL)
                        .setNotification(AndroidNotification.builder()
                                .setTitle(event.eventName)
                                .setBody("Signed by " + signedUser.fullName)
                                .setColor("#f45342")
                                .setClickAction(null)
                                .build())
                        .build())
                .build()
        FirebaseMessaging.getInstance().send(message)
    }

    fun sendCommentNotification(commentMessage: CommentMessage){
        var event = eventService.getEventByEventId(commentMessage.eventId)
        val map = HashMap<String, String>()
        map["eventId"] = event.eventId.toString()
        var message = Message.builder()
                .setTopic(commentMessage.eventId.toString())
                .putAllData(map)
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 24 * 1000)
                        .setPriority(AndroidConfig.Priority.NORMAL)
                        .setNotification(AndroidNotification.builder()
                                .setTitle(event.eventName)
                                .setBody("Commented by " + commentMessage.messageUserName)
                                .setColor("#f45342")
                                .setTag(event.eventId.toString())
                                .build())
                        .build())
                .build()
        FirebaseMessaging.getInstance().send(message)

    }

}