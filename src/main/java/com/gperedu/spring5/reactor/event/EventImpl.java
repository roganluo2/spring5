package com.gperedu.spring5.reactor.event;

import com.gperedu.spring5.reactor.CtmSelectableChannel;
import com.gperedu.spring5.reactor.CtmSelector;

/**
 * @Description TODO
 * @Date 2019/3/29 18:09
 * @Created by rogan.luo
 */
public class EventImpl extends Event {

    private CtmSelectableChannel ctmSelectableChannel;
    private CtmSelector ctmSelector;

    private int eventType;

    public EventImpl(CtmSelectableChannel ctmSelectableChannel, CtmSelector ctmSelector) {
        this.ctmSelectableChannel = ctmSelectableChannel;
        this.ctmSelector = ctmSelector;
    }

    public EventImpl(CtmSelectableChannel ctmSelectableChannel, CtmSelector ctmSelector, int eventType) {
        this.ctmSelectableChannel = ctmSelectableChannel;
        this.ctmSelector = ctmSelector;
        this.eventType = eventType;
    }

    @Override
    public CtmSelectableChannel channel() {
        return this.ctmSelectableChannel;
    }

    @Override
    public CtmSelector selector() {
        return this.ctmSelector;
    }

    //设定事件的类型
    public void setEventType(int eventType)
    {
        this.eventType = eventType;
    }

    @Override
    public int getEventType() {
        return this.eventType;
    }
}
