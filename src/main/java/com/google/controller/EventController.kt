package com.google.controller

import com.google.domain.CommentMessage
import com.google.domain.Event
import com.google.service.EventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class EventController {

    @Autowired
    lateinit var eventService: EventService


    @RequestMapping(value = "/event", method = arrayOf(RequestMethod.POST))
    fun postEvent(@RequestBody event: Event, @RequestHeader("authorization-code") authCode: String): Event {
        return eventService.createEvent(event, authCode)
    }

    @RequestMapping(value = "/event/{userId}", method = arrayOf(RequestMethod.GET))
    fun getEvent(@PathVariable userId: String): List<Event> {
        return eventService.getAllEventsByUser(userId = userId)
    }

    @RequestMapping(value = "/event/{userId}/{radius}/{countryCode}/{latitude}/{longitude}", method = arrayOf(RequestMethod.GET))
    fun getEventsByLocation(@PathVariable("userId") userId: String,
                            @PathVariable("radius") radius: Float,
                            @PathVariable("countryCode") countryCode: String,
                            @PathVariable("latitude") latitude: Float,
                            @PathVariable("longitude") longitude: Float): List<Event> {
        return eventService.getAllEventsByLocation(userId, radius, countryCode, latitude, longitude)
    }

    @RequestMapping(value = "/event/{userId}/{eventId}", method = arrayOf(RequestMethod.POST))
    fun signEventsForUsers(@PathVariable("userId") userId: String,
                           @PathVariable("eventId") eventId: Long,
                           @RequestHeader("authorization-code") authCode: String){
        return eventService.setSignInEventAndUser(userId, eventId, authCode)
    }

    @RequestMapping(value = "/event/delete/{userId}/{eventId}", method = arrayOf(RequestMethod.POST))
    fun unsignEventsForUsers(@PathVariable("userId") userId: String,
                             @PathVariable("eventId") eventId: Long,
                             @RequestHeader("authorization-code") authCode: String){
        return eventService.unsignEvent(userId, eventId, authCode)
    }

    @RequestMapping(value = "/events", method = arrayOf(RequestMethod.POST))
    fun getEventsByIds(@RequestBody listOfIds: List<Long>): List<Event> {
        return eventService.getEventsByEventIds(listOfIds)
    }

    @RequestMapping(value = "/event/delete/{eventId}", method = arrayOf(RequestMethod.DELETE))
    fun deleteEventById(@PathVariable("eventId") eventId: Long,
                        @RequestHeader("authorization-code") authCode: String) {
        return eventService.removeEvent(eventId, authCode)
    }

    @RequestMapping(value = "/event/one/{eventId}", method = arrayOf(RequestMethod.GET))
    fun getEventByItsId(@PathVariable("eventId") eventId: Long): Event {
        return eventService.getEventByEventId(eventId)
    }

    @RequestMapping(value = "/event/comment", method = arrayOf(RequestMethod.POST))
    fun createComment(@RequestBody commentMessage: CommentMessage,
                      @RequestHeader("authorization-code") authCode: String): CommentMessage? {
        return eventService.createCommentMessage(commentMessage, authCode)
    }

}
