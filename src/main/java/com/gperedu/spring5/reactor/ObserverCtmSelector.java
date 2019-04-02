package com.gperedu.spring5.reactor;

import com.google.common.collect.Maps;
import com.gperedu.spring5.reactor.event.Event;
import com.gperedu.spring5.reactor.event.EventImpl;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description 基于观察者模式方式实现
 * @Date 2019/3/29 17:36
 * @Created by rogan.luo
 */
public class ObserverCtmSelector extends CtmSelector {

    //通道上的可读事件
    protected Set<Event> selectedEvents;

    //存储注册到selector的channel
    protected HashSet<Event> events;

    private Set<Event> publicEvents;

    private Set<Event> publicSelecteEvents;

    private Map<Integer,Event> eventMap = Maps.newHashMap();


    public ObserverCtmSelector() {
        //完成事件集合的初始化
        events = new HashSet<Event>();
        selectedEvents = new HashSet<Event>();

        publicEvents = events;
        publicSelecteEvents = selectedEvents;
    }

    @Override
    protected void implRegister(EventImpl eventImpl) {
        this.events.add(eventImpl);
        eventMap.put(eventImpl.getEventType(), eventImpl);
    }

    //实现获取所有的events
    @Override
    public Set<Event> selectEvents() {
        return this.publicSelecteEvents;
    }

    //实现阻塞,没有时间就一直阻塞,windows通过poll函数从内核中获取可写,或者可读事件
    @Override
    public int select() throws IOException {
        while (true)
        {
            //监听到数据
            Set<Event> events = SystemRegister.getInstance().getEventListener().getEvents();
            Iterator<Event> iterator = events.iterator();
            while (iterator.hasNext()) {
                Event e = iterator.next();
                //判断事件是否感兴趣
                iterator.remove();
                if(!eventMap.containsKey(e.getEventType()))
                {
                    continue;
                }
                Event event = eventMap.get(e.getEventType());
                Event eventSelect = new EventImpl(event.channel(), this, event.getEventType());
                selectedEvents.add(eventSelect);
            }
            //构造读取事件
            ArrayBlockingQueue<byte[]> shareDataSpaces = SystemRegister.getInstance().getShareDataSpaces();
            Event event = eventMap.get(Event.OP_READ);
            if(null != event)
            {
                for (int i = 0; i < shareDataSpaces.size(); i++) {
                    Event eventSelect = new EventImpl(event.channel(), this, Event.OP_READ);
                    selectedEvents.add(eventSelect);
                }
            }
            if(!selectedEvents.isEmpty())
            {
                break;
            }
        }
        return selectedEvents.size();
    }

    //克隆的方法，暂时没想到用处，先放着
    @Override
    public void close() throws IOException {
    }
}
