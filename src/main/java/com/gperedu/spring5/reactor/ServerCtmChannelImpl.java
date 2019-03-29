package com.gperedu.spring5.reactor;

import java.io.IOException;

/**
 * @Description TODO
 * @Date 2019/3/29 18:03
 * @Created by rogan.luo
 */
public class ServerCtmChannelImpl extends ServerCtmChannel {

    @Override
    public ServerCtmChannel bind(int port) {
        return new ServerCtmChannelImpl();
    }

    @Override
    public ClientCtmChannel accept() throws IOException {
        return null;
    }

}
