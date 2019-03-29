package com.gperedu.spring5.reactor;

import com.gperedu.spring5.reactor.event.Event;

import java.io.IOException;

/**
 * @Description 服务端通道
 * @Date 2019/3/29 17:46
 * @Created by rogan.luo
 */
public abstract class ServerCtmChannel extends CtmChannel {

    public static ServerCtmChannel open() throws IOException {
        //需要一个实现类
        return null;
    }

    @Override
    public Event register(CtmSelector sel, int ops) {
        return null;
    }

    public abstract ServerCtmChannel bind(int port);

    //需要拿到对应的客户端通道
    public abstract ClientCtmChannel accept() throws IOException;



}
