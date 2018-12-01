package com.google.service

import com.google.domain.Event
import com.google.domain.Notification
import com.google.domain.NotificationObject
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class NotificationService {

    @Autowired
    lateinit var userService: UserService

    val URL = "https://fcm.googleapis.com/fcm/send"
    val FCM_KEY = "key=AAAA4BV3ACE:APA91bEM8POgLbKRegHvC---F5X6YN9l8LuoFrThW8CwRMa2oXWLA3dDLu_g3rI8UWc-UekEhWO4nT1L4tNbPPxcxvy9Rb1aIUnNUtnH2l5ua21VO0i8i0l-v95AXThyS0EmX4iAPhGy"

    fun sendEventSignNotification(signedUserId: String?, event: Event){
        var signedUser = userService.getUser(signedUserId!!)
        var eventHolderUser = userService.getUser(event.userId!!)
        var restTemplate = RestTemplate()
        val headers = HttpHeaders()
        var notification = Notification(event.eventName + " signed by", signedUser.fullName, null)
        var notificationObject = NotificationObject(eventHolderUser.userFcmToken, notification)
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
        headers.add(HttpHeaders.AUTHORIZATION, FCM_KEY)
        var entity = HttpEntity<NotificationObject>(notificationObject, headers)
        restTemplate.exchange(URL, HttpMethod.POST, entity ,String::class.java)
    }

}