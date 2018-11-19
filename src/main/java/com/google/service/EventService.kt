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
            var distanceBetween = distFrom(latitude, longitude, it.eventLocationLatitude.toFloat(), it.eventLocationLongitude.toFloat())/1000
            if (distanceBetween <= radius/1000 && it.userId != userId){
                it.eventDistance = distanceBetween
                filteredEventsList.add(it)
                filteredEventsList.sortBy { distanceBetween }
            }
        }

        return filteredEventsList.toList()
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