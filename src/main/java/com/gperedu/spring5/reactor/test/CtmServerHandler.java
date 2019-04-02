package com.gperedu.spring5.reactor.test;

import com.gperedu.spring5.reactor.CtmClientChannel;
import com.gperedu.spring5.reactor.CtmSelector;
import com.gperedu.spring5.reactor.CtmServerChannel;
import com.gperedu.spring5.reactor.event.Event;

import java.io.IOException;
import java.util.Iterator;

/**
 * @Description TODO
 * @Date 2019/4/2 12:58
 * @Created by rogan.luo
 */
public class CtmServerHandler implements Runnable {

    private int port ;

    private CtmServerChannel ssc;


    //1 多路复用器（管理所有的通道）
    private CtmSelector selector;

    public CtmServerHandler(int port)  {
        this.port = port;

        try {
            this.ssc = CtmServerChannel.open();
            this.selector = CtmSelector.open();
            ssc.bind(port);
            ssc.register(selector, Event.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("服务端启动成功！port:" + port );
    }

    @Override
    public void run() {
        while(true){
            try {
                //1 必须要让多路复用器开始监听
                this.selector.select();
                //2 返回多路复用器已经选择的结果集
                Iterator<Event> events = this.selector.selectEvents().iterator();
                //3 进行遍历
                while(events.hasNext()){
                    //4 获取一个选择的元素
                    Event e = events.next();
                    //5 直接从容器中移除就可以了
                    events.remove();
                    //6 如果是有效的
                    //7 如果为阻塞状态
                    if(e.isAcceptable()){
                        this.accept(e);
                    }
                    //8 如果为可读状态
                    if(e.isReadable()){
                        this.read(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(Event event) {
        try {
            //1 获取服务通道
            CtmServerChannel ssc =  (CtmServerChannel) event.channel();
            //2 执行阻塞方法
            CtmClientChannel sc = ssc.accept();
            //4 注册到多路复用器上，并设置读取标识
            sc.register(this.selector, Event.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(Event event) {
        try {
            CtmClientChannel sc = (CtmClientChannel) event.channel();
            byte[] bytes = new byte[1024];
            sc.read(bytes);
            String body = new String(bytes).trim();
            System.out.println("server端:读取数据:" + body);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
