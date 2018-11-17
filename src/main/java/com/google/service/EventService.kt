package com.google.service

import com.google.domain.Event
import com.googlecode.objectify.ObjectifyService.ofy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EventService {

    private val logger = LoggerFactory.getLogger(EventService::class.java)

    fun saveEvent(event: Event): Event {
        logger.info("Save event")
        try {
            ofy().save().entity(event).now()
            return event
        } catch (e: Exception) {
            throw e
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
}