package com.google.service

import com.google.cloud.tasks.v2beta3.*
import com.google.domain.CommentMessage
import com.google.domain.Event
import com.google.domain.User
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.protobuf.ByteString
import com.googlecode.objectify.NotFoundException
import com.googlecode.objectify.ObjectifyService.ofy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.util.*


@Service
class EventService {

    private val logger = LoggerFactory.getLogger(EventService::class.java)

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var notificationService: NotificationService

    @Autowired
    lateinit var authenticationService: AuthenticationService

    fun createEvent(event: Event, authCode: String): Event {
        logger.info("Create event")
        if (authenticationService.validateUser(authCode)) {
            return saveEvent(event)
        } else {
            throw IllegalAccessException()
        }
    }

    fun saveEvent(event: Event): Event {
        logger.info("Save event")
        if (event.eventSignedPlayers == null) {
            val signedUserIdsList: MutableList<User>? = mutableListOf()
            val user = userService.getUser(event.userId)
            signedUserIdsList?.add(user)
            event.eventSignedPlayers = signedUserIdsList
        }
        try {
            ofy().save().entity(event).now()
            sendRefreshNotification(event)
        } catch (e: Exception) {
            throw e
        }
        return event
    }

    private fun sendRefreshNotification(event: Event) {
        if (event.signedUserId != null) {
            GlobalScope.launch {
                notificationService.sendEventSignNotification(event)
            }
        } else {
            GlobalScope.launch {
                notificationService.sendRefreshNotification(event)
            }
        }
    }

    fun removeEvent(eventId: Long, authCode: String) {
        logger.info("delete event : " + eventId)
        if (authenticationService.validateUser(authCode)) {
            try {
                val event = getEventByEventId(eventId)
                event.eventSignedPlayers.forEach { user ->
                    unsignEvent(user.id.toString(), eventId, authCode)
                }
                ofy().delete().type(Event::class.java).id(eventId)
            } catch (e: Exception) {
                logger.error("error " + e.toString())
            }
        } else {
            throw IllegalAccessException()
        }
    }

    fun getAllEventsByUser(userId: String): List<Event> {
        logger.info("Get all events : " + userId)
        val eventsList: List<Event>
        try {
            eventsList = ofy().load().type<Event>(Event::class.java).filter("userId", userId).toList()
            return eventsList
        } catch (e: Exception) {
            logger.error("error " + e.toString())
            throw e
        }
    }

    fun getAllEventsByLocation(userId: String, radius: Float, countryCode: String, latitude: Float, longitude: Float): List<Event> {
        logger.info("Get all events : " + countryCode + "lat : " + latitude + "longt : " + longitude + "radius : " + radius)

        val eventsList: List<Event>
        try {
            eventsList = ofy().load().type<Event>(Event::class.java)
                    .filter("eventLocationCountryCode", countryCode)
                    .list()
        } catch (e: Exception) {
            logger.error("error " + e.toString())
            throw e
        }
        val filteredEventsList = eventsList.filter { event ->
            val distanceBetween = distFrom(latitude, longitude, event.eventLocationLatitude.toFloat(),
                    event.eventLocationLongitude.toFloat()) / 1000
            event.eventDistance = distanceBetween

            val filteredSignedEvents = event.eventSignedPlayers.filter { user ->
                user.userId == userId
            }
            filteredSignedEvents.isEmpty() && event.eventSignedPlayers.size < event.eventPlayers &&
                    distanceBetween <= radius && event.userId != userId &&
                    event.eventDate.after(Date(System.currentTimeMillis() + 1000 * 3600 * 2))
        }.sortedBy {
            it.eventDistance
        }
        return filteredEventsList
    }

    fun getEventsByEventIds(userId: String): List<Event> {
        val user = userService.getUser(userId)
        if (user.signedEventsList == null) return emptyList()
        try {
            logger.info("eventids " + user.signedEventsList.toString())
            val listOfEvents = ofy()
                    .load()
                    .type(Event::class.java)
                    .ids(user.signedEventsList)
                    .values
                    .toList()
            logger.info("events " + listOfEvents.toString())
            return listOfEvents
        } catch (e: Exception) {
            logger.error("error " + e.toString())
            throw e
        }
    }

    fun setSignInEventAndUser(userId: String, eventId: Long, authCode: String): Event {
        if (authenticationService.validateUser(authCode)) {
            val event = getEventByEventId(eventId)
            val user = userService.getUser(userId)
            if (event.userId == userId) {
                throw IllegalStateException()
            }
            if (event.eventSignedPlayers.size == event.eventPlayers) {
                throw IllegalStateException()
            }
            var eventSignedUsers: MutableList<User>? = event.eventSignedPlayers
            if (eventSignedUsers == null) {
                eventSignedUsers = mutableListOf()
            }
            if (eventSignedUsers.contains(user)) {
                throw IllegalStateException()
            }

            var userSignedEvents: MutableList<Long>? = user.signedEventsList
            if (userSignedEvents == null) {
                userSignedEvents = mutableListOf()
            }
            if (userSignedEvents.contains(eventId)) {
                throw IllegalStateException()
            }
            eventSignedUsers.add(user)
            event.eventSignedPlayers = eventSignedUsers
            event.signedUserId = userId
            saveEvent(event)

            userSignedEvents.add(eventId)
            user.signedEventsList = userSignedEvents
            userService.saveUser(user)
            return event
        } else {
            throw IllegalAccessException()
        }
    }

    fun unsignEvent(userId: String, eventId: Long, authCode: String): Event {
        if (authenticationService.validateUser(authCode)) {
            val event = getEventByEventId(eventId)
            val user = userService.getUser(userId)
            if (event.userId == userId) {
                logger.info("UserId is equal to : " + userId)
                throw IllegalStateException()
            }
            val eventSignedUsers = event.eventSignedPlayers
            if (eventSignedUsers == null) {
                logger.info("eventsigned users is null")
                throw IllegalStateException()
            }

            eventSignedUsers.removeIf { signedUser ->
                signedUser.userId == userId
            }
            event.eventSignedPlayers = eventSignedUsers
            saveEvent(event)

            val userSignedEvents: MutableList<Long> = user.signedEventsList ?: throw IllegalStateException()
            userSignedEvents.remove(eventId)
            user.signedEventsList = userSignedEvents
            userService.saveUser(user)
            return event
        } else {
            throw IllegalAccessException()
        }
    }

    fun getEventByEventId(eventId: Long): Event {
        logger.info("Get eventsby ids: " + eventId.toString())
        try {
            val event = ofy().cache(false).load().type(Event::class.java).id(eventId).now()
            return if (event == null) {
                throw NotFoundException()
            } else {
                event
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun createCommentMessage(commentMessage: CommentMessage, authCode: String): CommentMessage? {
        logger.info("create comment: " + commentMessage.eventId)
        if (authenticationService.validateUser(authCode)) {

            val userId = commentMessage.userId
            val user = userService.getUser(userId)
            commentMessage.messageUserName = user.fullName

            val event = getEventByEventId(commentMessage.eventId)
            var eventComments: MutableList<CommentMessage>? = event.eventComments
            if (eventComments == null) {
                eventComments = mutableListOf()
            }
            eventComments.add(commentMessage)
            event.eventComments = eventComments
            saveEvent(event)
            GlobalScope.launch {
                notificationService.sendCommentNotification(commentMessage)
            }
            logger.info("task created: " + commentMessage.eventId)
            return commentMessage
        } else {
            throw IllegalAccessException()
        }
    }

    //distance in meters
    fun distFrom(lat1: Float, lng1: Float, lat2: Float, lng2: Float): Float {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians((lat2 - lat1).toDouble())
        val dLng = Math.toRadians((lng2 - lng1).toDouble())
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1.toDouble())) * Math.cos(Math.toRadians(lat2.toDouble())) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return (earthRadius * c).toFloat()
    }
}
