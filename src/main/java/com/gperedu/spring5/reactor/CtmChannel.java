package com.gperedu.spring5.reactor;

import com.gperedu.spring5.reactor.event.Event;

/**
 * @Description 通道
 * @Date 2019/3/29 17:42
 * @Created by rogan.luo
 */
public abstract class CtmChannel {

    protected CtmChannel() {
    }

    public abstract Event register(CtmSelector sel, int ops);

}
