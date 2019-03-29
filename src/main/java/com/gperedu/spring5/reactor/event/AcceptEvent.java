package com.gperedu.spring5.reactor.event;

import com.gperedu.spring5.reactor.CtmChannel;
import com.gperedu.spring5.reactor.CtmSelector;

/**
 * @Description TODO
 * @Date 2019/3/29 18:09
 * @Created by rogan.luo
 */
public class AcceptEvent extends Event {
    @Override
    public CtmChannel channel() {
        return null;
    }

    @Override
    public CtmSelector selector() {
        return null;
    }

    @Override
    protected int getEventType() {
        return 0;
    }
}
