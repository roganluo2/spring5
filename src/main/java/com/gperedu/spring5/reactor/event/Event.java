package com.gperedu.spring5.reactor.event;


import com.gperedu.spring5.reactor.CtmChannel;
import com.gperedu.spring5.reactor.CtmSelector;

/**
 * @Description 事件接口
 * @Date 2019/3/29 17:20
 * @Created by rogan.luo
 */
public abstract class Event  {

    protected Event(){}

    public abstract CtmChannel channel();

    public abstract CtmSelector selector();


    public static final int OP_CONNECT = 1;

    public static final int OP_WRITE = 2;

    public static final int OP_READ = 3;

    public static final int OP_ACCEPT = 4;

    public final boolean isReadable() {
        return getEventType() == OP_READ;
    }

    public final boolean isWritable() {
        return getEventType() == OP_WRITE;
    }

    public final boolean isConnectable() {
        return getEventType() == OP_CONNECT;
    }

    public final boolean isAcceptable() {
        return getEventType() == OP_ACCEPT;
    }

    protected abstract int getEventType();

}
