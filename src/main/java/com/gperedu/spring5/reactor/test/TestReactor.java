package com.gperedu.spring5.reactor.test;

import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Date 2019/4/2 12:57
 * @Created by rogan.luo
 */
public class TestReactor {

    public static void main(String[] args) throws InterruptedException {

        Thread server = new Thread(new CtmServerHandler(8765),"serverThread");
        Thread client = new Thread(new CtmClientHandler(8765,"127.0.0.1" ),"clientThread");
        server.start();
        TimeUnit.MILLISECONDS.sleep(10l);
        client.start();
        TimeUnit.MINUTES.sleep(5l);
    }
}
