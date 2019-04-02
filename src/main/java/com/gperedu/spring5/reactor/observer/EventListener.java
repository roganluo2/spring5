package com.gperedu.spring5.reactor.observer;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.gperedu.spring5.reactor.event.Event;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by 召君王 on 2019/3/25.
 */
public class EventListener {

    Set<Event> events = new HashSet<>();

    public Set<Event> getEvents() {
        return events;
    }

    @Subscribe
    public void subscribe(Event event)
    {
        events.add(event);
    }

}
