package com.gperedu.spring5.reactor;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.gperedu.spring5.reactor.observer.EventListener;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description TODO
 * @Date 2019/4/1 18:02
 * @Created by rogan.luo
 */
public class SystemRegister {

    private Map<String, CtmSelectableChannel> bindChannelMap;

    private static volatile SystemRegister instance;

    private ArrayBlockingQueue<byte[]> shareDataSpaces = new ArrayBlockingQueue<>(100);

    public ArrayBlockingQueue<byte[]> getShareDataSpaces() {
        return shareDataSpaces;
    }

    private SystemRegister(){
        eventListener = new EventListener();
        bindChannelMap = Maps.newHashMap();
    }

    public Map<String, CtmSelectableChannel> getBindChannelMap() {
        return bindChannelMap;
    }

    public static final SystemRegister getInstance(){
        if(instance == null)
        {
            synchronized (SystemRegister.class) {
                if(null == instance) {
                    instance = new SystemRegister();
                }
            }
        }
        return instance;
    }

    private EventListener eventListener;

    public EventListener getEventListener(){
        return eventListener;
    }

}
