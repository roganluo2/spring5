package com.gperedu.spring5.reactor;

import com.gperedu.spring5.reactor.event.AcceptEvent;
import com.gperedu.spring5.reactor.event.Event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Date 2019/3/29 18:06
 * @Created by rogan.luo
 */
public class ClientCtmChannelImpl extends ClientCtmChannel{

    Map<String,Object> map = new HashMap<>();

    Set<Event> events = new HashSet<>();

    //连接的时候需要给监听的port注册一个accept事件
    @Override
    public boolean connect(String ip, int port) {
        String key = "client:connect"  + ":" + ip + ":" + port;
        Event event = new AcceptEvent();
        //默认连接成功
        map.put(key,this);
        //如何把当前事件推送给服务端??,把事件放入到对方的selector中
        events.add(event);
        return false;
    }

    @Override
    public int read(String src) {
        return 0;
    }

    @Override
    public int write(String src) {
        return 0;
    }

}
