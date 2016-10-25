package com.ctrip.implus;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.Tcp.*;
import akka.io.TcpMessage;

import java.net.InetSocketAddress;

/**
 * Created by chengyq on 2016/10/25.
 */
public class ChatInterfaceListener extends UntypedActor {

    @Override
    public void preStart() {
        bind();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof Bound) {
            final Bound bound = (Bound) message;
            System.out.println("bound at " + bound.localAddress().toString());
        } else if (message instanceof CommandFailed) {
            getContext().stop(getSelf());
            System.out.println("stopped");
        } else if (message instanceof Connected) {
            final Connected conn = (Connected) message;
            final ActorRef handler = getContext().actorOf(Props.create(ChatInterfaceConnHandler.class));
            getSender().tell(TcpMessage.register(handler), getSelf());
            System.out.println("new income connection " + conn.toString());
        }
    }

    private void bind() {
        final ActorRef tcp = Tcp.get(getContext().system()).manager();
        final InetSocketAddress addr = new InetSocketAddress("localhost", 7777);
        tcp.tell(TcpMessage.bind(getSelf(), addr, 100), getSelf());
    }
}
