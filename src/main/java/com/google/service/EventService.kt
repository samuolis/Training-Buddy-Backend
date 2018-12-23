package com.google.service

import com.google.domain.CommentMessage
import com.google.domain.Event
import com.google.domain.User
import com.googlecode.objectify.Key
import com.googlecode.objectify.NotFoundException
import com.googlecode.objectify.ObjectifyService.ofy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EventService {

    private val logger = LoggerFactory.getLogger(EventService::class.java)

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var notificationService: NotificationService

    fun createEvent(event: Event, authCode: String):Event {
        logger.info("Create event")
        if (authenticationService.validateUser(authCode)){
            return saveEvent(event)
        } else{
            throw IllegalAccessException()
        }
    }

    fun saveEvent(event: Event): Event {
        logger.info("Save event")
        if (event.signedUserId != null) {
            notificationService.sendEventSignNotification(event.signedUserId, event)
        } else if (event.eventSignedPlayers == null){
            var signedUserIdsList: MutableList<String>? = mutableListOf()
            signedUserIdsList?.add(event.userId)
            event.eventSignedPlayers = signedUserIdsList
        }
        try {
            ofy().save().entity(event).now()
            return event
        } catch (e: Exception) {
            throw e
        }
    }

    fun removeEvent(eventId: Long, authCode: String){
        logger.info("delete event : " + eventId)
        if (authenticationService.validateUser(authCode)){
            try {
                ofy().delete().type(Event::class.java).id(eventId)
            }catch (e: Exception){
                throw e
            }
        } else{
            throw IllegalAccessException()
        }
    }

    fun getAllEventsByUser(userId: String): List<Event> {
        logger.info("Get all events : " + userId)
        var eventsList: List<Event>
        try {
            eventsList = ofy().load().type<Event>(Event::class.java).filter("userId", userId).list()
            return eventsList
        } catch (e: Exception) {
            throw e
        }

    }

    fun getAllEventsByLocation(userId: String, radius: Float, countryCode: String, latitude: Float, longitude: Float): List<Event> {
        logger.info("Get all events : " + countryCode + "lat : " + latitude + "longt : " + longitude + "radius : " + radius)

        var eventsList: List<Event>
        try {
            eventsList = ofy().load().type<Event>(Event::class.java)
                    .filter("eventLocationCountryCode", countryCode)
                    .list()
        } catch (e: Exception) {
            throw e
        }
        var filteredEventsList: MutableList<Event> = mutableListOf<Event>()
        eventsList.forEach {
            if (it.eventSignedPlayers != null) {
                if (it.eventSignedPlayers.contains(userId) || it.eventSignedPlayers.size == it.eventPlayers) {
                    return@forEach
                }
            }
            var distanceBetween = distFrom(latitude, longitude, it.eventLocationLatitude.toFloat(), it.eventLocationLongitude.toFloat()) / 1000
            if (distanceBetween <= radius && it.userId != userId) {
                it.eventDistance = distanceBetween
                filteredEventsList.add(it)
                filteredEventsList.sortBy({ selector(it) })
            }
        }

        return filteredEventsList.toList()
    }

    fun getEventsByEventId(eventIds: List<Long>): List<Event>{
        try {
            var event = ofy().load().type(Event::class.java).ids(eventIds).values.toList()
            return if (event == null) {
                throw NotFoundException()
            } else {
                event
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun setSignInEventAndUser(userId: String, eventId: Long, authCode: String){
        if (authenticationService.validateUser(authCode)) {
            var event = getEventByEventId(eventId)
            var user = userService.getUser(userId)
            if (event.userId == userId) {
                throw IllegalStateException()
            }
            if (event.eventSignedPlayers.size == event.eventPlayers) {
                throw IllegalStateException()
            }
            var eventSignedUsers: MutableList<String>? = event.eventSignedPlayers
            if (eventSignedUsers == null) {
                eventSignedUsers = mutableListOf()
            }
            if (eventSignedUsers.contains(userId)) {
                throw IllegalStateException()
            }

            var userSignedEvents: MutableList<Long>? = user.signedEventsList
            if (userSignedEvents == null) {
                userSignedEvents = mutableListOf()
            }
            if (userSignedEvents.contains(eventId)) {
                throw IllegalStateException()
            }
            eventSignedUsers.add(userId)
            event.eventSignedPlayers = eventSignedUsers
            event.signedUserId = userId
            saveEvent(event)

            userSignedEvents.add(eventId)
            user.signedEventsList = userSignedEvents
            userService.saveUser(user)
        } else {
            throw IllegalAccessException()
        }
    }

    fun unsignEvent(userId: String, eventId: Long, authCode: String){
        if (authenticationService.validateUser(authCode)) {
            var event = getEventByEventId(eventId)
            var user = userService.getUser(userId)
            if (event.userId == userId) {
                logger.info("UserId is equal to : " + userId)
                throw IllegalStateException()
            }
            var eventSignedUsers: MutableList<String>? = event.eventSignedPlayers
            if (eventSignedUsers == null) {
                logger.info("eventsigned users is null")
                throw IllegalStateException()
            }

            var userSignedEvents: MutableList<Long>? = user.signedEventsList
            if (userSignedEvents == null) {
                throw IllegalStateException()
            }
            eventSignedUsers.remove(userId)
            event.eventSignedPlayers = eventSignedUsers
            saveEvent(event)

            userSignedEvents.remove(eventId)
            user.signedEventsList = userSignedEvents
            userService.saveUser(user)
        } else {
            throw IllegalAccessException()
        }
    }

    fun getEventByEventId(eventId: Long): Event{
        logger.info("Get eventsby ids: " + eventId.toString())
        try {
            var event = ofy().cache(false).load().type(Event::class.java).id(eventId).now()
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
        logger.info("create comment: " + commentMessage.messageId)
        if (authenticationService.validateUser(authCode)) {

            var userId = commentMessage.userId
            var user = userService.getUser(userId)
            commentMessage.messageUserName = user.fullName
            var message: Key<CommentMessage>? = null
            try {
                message = ofy().save().entity(commentMessage).now()
            } catch (e: Exception) {
                throw e
            }
            logger.info("message id : " + message.id)
            if (message.id != null) {
                var event = getEventByEventId(commentMessage.eventId)
                var eventComments: MutableList<Long>? = event.eventComments
                if (eventComments == null) {
                    eventComments = mutableListOf()
                }
                eventComments.add(message.id)
                event.eventComments = eventComments
                saveEvent(event)
                return commentMessage
            } else {
                return null
            }
        } else{
            throw IllegalAccessException()
        }
    }

    fun getAllMessagesByMessageId(listOfMessageIds: List<Long>): List<CommentMessage>{
        logger.info("load event comments")
        try {
            var commentMessages = ofy().load().type(CommentMessage::class.java).ids(listOfMessageIds).values.toList()
            return if (commentMessages == null) {
                throw NotFoundException()
            } else {
                commentMessages
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun selector(event: Event): Float = event.eventDistance

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