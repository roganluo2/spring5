package com.gperedu.spring5.reactor;

import com.google.common.eventbus.EventBus;
import com.gperedu.spring5.reactor.event.EventImpl;
import com.gperedu.spring5.reactor.event.Event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description
 * @Date 2019/3/29 18:06
 * @Created by rogan.luo
 */
public class CtmClientChannelImpl extends CtmClientChannel {

    Map<String,Object> map = new HashMap<>();

    Set<Event> events = new HashSet<>();

    //连接的时候需要给监听的port注册一个accept事件
    @Override
    public boolean connect(String ip, int port) {
        String key = "server:" + ip + ":" + port;
        //默认连接成功
        CtmSelectableChannel ctmSelectableChannel = SystemRegister.getInstance().getBindChannelMap().get(key);
        if(ctmSelectableChannel == null)
        {
            return false;
        }
//      发送一个连接事件给服务端
        Event event = new EventImpl(ctmSelectableChannel, null,Event.OP_ACCEPT);
        publish(event);
        return true;
    }

    private void publish(Event event) {
        EventBus eventBus = new EventBus();
        eventBus.register(SystemRegister.getInstance().getEventListener());
        eventBus.post(event);
    }

    @Override
    public int read(byte[] bytes) {
        ArrayBlockingQueue<byte[]> shareDataSpaces = SystemRegister.getInstance().getShareDataSpaces();
        try {
            byte[] take = shareDataSpaces.take();
            shareDataSpaces.remove(take);
            System.arraycopy(take, 0 ,bytes,0,take.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public int write(String src) {
        byte[] bytes = src.getBytes();
        ArrayBlockingQueue<byte[]> shareDataSpaces = SystemRegister.getInstance().getShareDataSpaces();
        shareDataSpaces.add(bytes);
        return 1;
    }

}
