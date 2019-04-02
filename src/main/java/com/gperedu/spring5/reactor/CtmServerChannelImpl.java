package com.gperedu.spring5.reactor;

import java.io.IOException;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2019/3/29 18:03
 * @Created by rogan.luo
 */
public class CtmServerChannelImpl extends CtmServerChannel {
    @Override
    public CtmServerChannel bind(int port) {
        Map<String,CtmSelectableChannel> bindMap = SystemRegister.getInstance().getBindChannelMap();
        String localIp = "server:127.0.0.1";
        String key = localIp + ":" + port;
        bindMap.put(key, this);
        return this;
    }

    @Override
    public CtmClientChannel accept() throws IOException {
        CtmClientChannel ctmClientChannel = new CtmClientChannelImpl() ;
        return ctmClientChannel;
    }

}
