package com.google.service

import com.google.domain.CommentMessage
import com.google.domain.Event
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class NotificationService {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var eventService: EventService

    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    private val NOTIFICATION_EVENT_KEY = "notification_event"

    private val EVENT_ID_KEY = "event_id"

    fun sendEventSignNotification(event: Event) {
        logger.info("In Notification")
        sendRefreshNotification(event)
        var signedUser = userService.getUser(event.signedUserId!!)

        var message = Message.builder()
                .setTopic("subscribeEventSignIn-" + event.eventId)
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

    fun sendCommentNotification(commentMessage: CommentMessage) {
        logger.info("send Comment notification " + commentMessage.eventId)
        var event = eventService.getEventByEventId(commentMessage.eventId)
        val map = HashMap<String, String>()
        map[NOTIFICATION_EVENT_KEY] = "comment"
        map[EVENT_ID_KEY] = event.eventId.toString()
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
                                .build())
                        .build())
                .build()
        FirebaseMessaging.getInstance().send(message)

    }

    fun sendRefreshNotification(event: Event) {
        logger.info("send refresh notification : " + event.toString())
        val map = HashMap<String, String>()
        map[NOTIFICATION_EVENT_KEY] = "refresh"
        if (event.eventId != null) {
            map[EVENT_ID_KEY] = event.eventId.toString()
        }
        val message = Message.builder()
                .setTopic("all")
                .putAllData(map)
                .build()
        FirebaseMessaging.getInstance().send(message)

    }

}
