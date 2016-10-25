package com.ctrip.implus;

import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        //System.out.println( "Hello World!" );

        final ActorSystem actorSystem = ActorSystem.create("implus");


        actorSystem.actorOf(Props.create(ChatInterfaceListener.class), "listener");


        Thread.sleep(30 * 60 * 1000);

        actorSystem.shutdown();
    }
}
