package com.ctrip.implus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        //System.out.println( "Hello World!" );

        //final ActorSystem actorSystemInterface = ActorSystem.create("implus_interface");
        //actorSystemInterface.actorOf(Props.create(ChatInterfaceListener.class), "listener");

        final ActorSystem actorSystemServer = ActorSystem.create("implus_server");
        ActorRef manager = actorSystemServer.actorOf(Props.create(ChatManager.class), "manager");
        actorSystemServer.actorOf(Props.create(ChatServer.class, manager), "listener");


        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("enter q to exit");
            String s = bf.readLine();

            if ("q".equals(s)) {
                break;
            }
        }

        System.out.println("exit");

        //actorSystemInterface.shutdown();

        actorSystemServer.shutdown();
    }
}
