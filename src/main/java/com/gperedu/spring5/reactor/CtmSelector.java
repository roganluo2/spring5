package com.gperedu.spring5.reactor;

import com.gperedu.spring5.reactor.event.Event;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

/**
 * @Description 时间轮询器
 * @Date 2019/3/29 17:31
 * @Created by rogan.luo
 */
public abstract class CtmSelector  implements Closeable {

    protected  CtmSelector(){}

    public static CtmSelector open() throws IOException {
        return new ObserverCtmSelector();
    }

    public abstract Set<Event> selectEvents();

    public abstract int select() throws IOException;



}
