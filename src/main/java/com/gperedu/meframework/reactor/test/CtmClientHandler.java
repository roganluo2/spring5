package com.gperedu.meframework.reactor.test;

import com.gperedu.meframework.reactor.CtmClientChannel;
import com.gperedu.meframework.reactor.event.Event;

import java.io.IOException;

/**
 * @Description TODO
 * @Date 2019/4/2 13:12
 * @Created by rogan.luo
 */
public class CtmClientHandler implements Runnable {

    private String ip;
    private int port;
    private CtmClientChannel sc;

    public CtmClientHandler(int port, String ip)  {
        this.port = port;
        this.ip = ip;
        try {
            sc = CtmClientChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            if(sc.connect(ip,port)) {
                while (true)
                {
                    this.write(sc);
                }

            }else {
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("客户端已经启动");
    }

    private void read(Event event) {
        try {
            CtmClientChannel sc = (CtmClientChannel) event.channel();
            byte[] bytes = new byte[1024];
            sc.read(bytes);
            System.out.println("client端:读取返回数据:" + new String(bytes).trim());
            new Thread(() -> {
                write(sc);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void write( CtmClientChannel sc){
        try {
            byte [] bytes = new byte[1024];
            System.out.println("请输入！");
            System.in.read(bytes);
            System.out.println("client端:输入了 --> " + new String(bytes));
            sc.write(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
