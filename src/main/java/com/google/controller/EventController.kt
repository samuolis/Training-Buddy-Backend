package com.google.controller

import com.google.domain.Event
import com.google.service.EventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class EventController {

    @Autowired
    lateinit var eventService: EventService


    @RequestMapping(value = "/event", method = arrayOf(RequestMethod.POST))
    fun postEvent(@RequestBody event: Event): Event {
        return eventService.saveEvent(event)
    }

    @RequestMapping(value = "/event/{userId}", method = arrayOf(RequestMethod.GET))
    fun getEvent(@PathVariable userId: String): List<Event> {
        return eventService.getAllEventsByUser(userId = userId)
    }
}