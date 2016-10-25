package com.ctrip.implus.explore;

import akka.actor.UntypedActor;

/**
 * Created by chengyq on 2016/10/25.
 */
public class HelloActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            getSender().tell("i received your message " + message.toString(), getSelf());
        }
    }

}

