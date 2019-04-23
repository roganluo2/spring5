package com.gperedu.meframework.reactor;

import java.io.IOException;

/**
 * @Description client
 * @Date 2019/3/29 17:57
 * @Created by rogan.luo
 */
public abstract class CtmClientChannel extends CtmSelectableChannel {



    public static CtmClientChannel open() throws IOException {
        //需要一个实现类
        return new CtmClientChannelImpl();
    }

    //连接
    public abstract boolean connect(String ip, int port);

    public abstract int read(byte[] bytes);

    public abstract int write(String src);


}
