package com.gperedu.meframework.reactor;

import java.io.IOException;

/**
 * @Description 服务端通道
 * @Date 2019/3/29 17:46
 * @Created by rogan.luo
 */
public abstract class CtmServerChannel extends CtmSelectableChannel {

    public static CtmServerChannel open() throws IOException {
        //完成通道参数的初始化
        return new CtmServerChannelImpl();
    }

    public abstract CtmServerChannel bind(int port);

    //需要拿到对应的客户端通道
    public abstract CtmClientChannel accept() throws IOException;



}
