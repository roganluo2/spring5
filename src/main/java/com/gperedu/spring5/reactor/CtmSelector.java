package com.gperedu.spring5.reactor;

import com.gperedu.spring5.reactor.event.Event;
import com.gperedu.spring5.reactor.event.EventImpl;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

/**
 * @Description
 * @Date 2019/3/29 17:31
 * @Created by rogan.luo
 */
public abstract class CtmSelector  implements Closeable {

    protected  CtmSelector(){}

    public static CtmSelector open() throws IOException {
        return new ObserverCtmSelector();
    }

    public Event register(CtmSelectableChannel ch,int ops){
        EventImpl eventImpl = new EventImpl(ch, this);
        eventImpl.setEventType(ops);
        implRegister(eventImpl);
         return eventImpl;

    }

    protected abstract void implRegister(EventImpl eventImpl);

    public abstract Set<Event> selectEvents();

    public abstract int select() throws IOException;



}
