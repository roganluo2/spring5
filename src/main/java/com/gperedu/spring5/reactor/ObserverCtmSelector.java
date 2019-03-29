package com.gperedu.spring5.reactor;

import com.gperedu.spring5.reactor.event.Event;

import java.io.IOException;
import java.util.Set;

/**
 * @Description 观察者方式实现
 * @Date 2019/3/29 17:36
 * @Created by rogan.luo
 */
public class ObserverCtmSelector extends CtmSelector {

    //实现获取所有的events
    @Override
    public Set<Event> selectEvents() {
        return null;
    }

    //实现阻塞,没有时间就一直阻塞
    @Override
    public int select() throws IOException {
        return 0;
    }

    //克隆的方法，暂时没想到用处，先放着
    @Override
    public void close() throws IOException {
    }
}
