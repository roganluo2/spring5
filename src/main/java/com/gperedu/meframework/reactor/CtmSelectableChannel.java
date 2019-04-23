package com.gperedu.meframework.reactor;

import com.gperedu.meframework.reactor.event.Event;

/**
 * @Description 通道
 * @Date 2019/3/29 17:42
 * @Created by rogan.luo
 */
public abstract class CtmSelectableChannel {

    protected CtmSelectableChannel() {
    }

    /**
     * 完成通过的注册,主要是把通道,通道感兴趣的事件注册到多路服用去上
     * @param sel
     * @param ops
     * @return
     */
    public Event register(CtmSelector sel, int ops){
        return sel.register(this,ops);
    }

}
