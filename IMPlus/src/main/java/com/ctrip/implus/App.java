package com.ctrip.implus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        //System.out.println( "Hello World!" );

        final ActorSystem actorSystemInterface = ActorSystem.create("implus_interface");
        actorSystemInterface.actorOf(Props.create(ChatInterfaceListener.class), "listener");

        final ActorSystem actorSystemServer = ActorSystem.create("implus_server");
        ActorRef manager = actorSystemServer.actorOf(Props.create(ChatManager.class), "manager");
        actorSystemServer.actorOf(Props.create(ChatListener.class, manager), "listener");

        Thread.sleep(30 * 60 * 1000);

        actorSystemInterface.shutdown();

        actorSystemInterface.shutdown();
    }
}
