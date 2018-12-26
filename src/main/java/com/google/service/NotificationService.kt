package com.google.service

import com.google.domain.CommentMessage
import com.google.domain.Event
import com.google.domain.Notification
import com.google.domain.NotificationObject
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class NotificationService {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var eventService: EventService

    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    val URL = "https://fcm.googleapis.com/fcm/send"
    val FCM_KEY = "key=AAAANibJ03I:APA91bHPLIhuYMMgHUoUzhAMeR1MYkYJrMZHt1Eo3T1dbvzjjyEUygLevTpHIbuuiYkOLbC_dodMrJlxu1b_DwEO9cb9PMjmJyZdE6fJLM7C1P6-JyMu_b6i1J90UeFizm7nfIMOcYCe"

    fun sendEventSignNotification(event: Event){
        logger.info("In Notification")
        var signedUser = userService.getUser(event.signedUserId!!)
        var eventHolderUser = userService.getUser(event.userId!!)
        var restTemplate = RestTemplate()
        val headers = HttpHeaders()
        var notification = Notification(event.eventName,"Signed by " + signedUser.fullName, null)
        var notificationObject = NotificationObject(eventHolderUser.userFcmToken, notification)
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
        headers.add(HttpHeaders.AUTHORIZATION, FCM_KEY)
        var entity = HttpEntity<NotificationObject>(notificationObject, headers)
        restTemplate.exchange(URL, HttpMethod.POST, entity ,String::class.java)
    }

    fun sendCommentNotification(commentMessage: CommentMessage){
        var event = eventService.getEventByEventId(commentMessage.eventId)
        var signedUsers = event.eventSignedPlayers
        var restTemplate = RestTemplate()
        val headers = HttpHeaders()
        var notification = Notification(event.eventName, "Commented by " + commentMessage.messageUserName, null)
        signedUsers.forEach { userId ->
            logger.info("Sending notification to " + userId)
            if (commentMessage.userId != userId) {
                var user = userService.getUser(userId)
                var notificationObject = NotificationObject(user.userFcmToken, notification)
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
                headers.add(HttpHeaders.AUTHORIZATION, FCM_KEY)
                var entity = HttpEntity<NotificationObject>(notificationObject, headers)
                restTemplate.exchange(URL, HttpMethod.POST, entity, String::class.java)
            }
        }
    }

}