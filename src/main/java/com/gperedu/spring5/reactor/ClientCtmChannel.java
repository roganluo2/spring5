package com.gperedu.spring5.reactor;

import java.io.IOException;

/**
 * @Description client
 * @Date 2019/3/29 17:57
 * @Created by rogan.luo
 */
public abstract class ClientCtmChannel {

    public static ClientCtmChannel open() throws IOException {
        //需要一个实现类
        return new ClientCtmChannelImpl();
    }

    //连接
    public abstract boolean connect(String ip, int port);

    public abstract int read(String src);

    public abstract int write(String src);


}
