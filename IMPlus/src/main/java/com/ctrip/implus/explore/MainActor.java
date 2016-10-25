package com.ctrip.implus.explore;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.ActorSystem;

/**
 * Created by chengyq on 10/25/16.
 */
public class MainActor extends UntypedActor {

    public static void main(String[] args) throws Exception {


        System.out.println("main actor");

        ActorSystem actorSystem= ActorSystem.create("com_ctrip_implus_explore");

        final ActorRef mainActor = actorSystem.actorOf(Props.create(MainActor.class),"main");

        Thread.sleep(2000);

        actorSystem.shutdown();
    }

    @Override
    public void preStart() {
        final ActorRef helloActor = getContext().actorOf(Props.create(HelloActor.class), "hello");

        helloActor.tell("what are you doing",getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            System.out.println("replied from hello actor : " + message.toString());
        }
    }

}
