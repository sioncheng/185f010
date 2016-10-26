package com.ctrip.implus;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.dsl.Creators;
import akka.io.Tcp;

import java.util.HashMap;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatManager extends UntypedActor {

    @Override
    public void preStart() {
        this.clients = new HashMap<String, ActorRef>();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof Tcp.Connected) {

        } else if (message instanceof ChatCommand) {
            ChatCommand cmd = (ChatCommand) message;
            if (ChatCommand.LOGIN.equals(cmd.getCommand())) {
                ChatCommand resCmd = new ChatCommand();
                resCmd.setCommand(ChatCommand.LOGIN);
                if (this.clients.containsKey(cmd.getFrom())) {
                    resCmd.setMessage("昵称已经存在");
                } else {
                    this.clients.put(cmd.getFrom(), getSender());
                    resCmd.setMessage("ok");
                }

                getSender().tell(resCmd, getSelf());
            } else {
                for(int i=0; i < clients.size(); i++) {
                    ChatCommand resCmd = new ChatCommand(ChatCommand.SEND,"",cmd.getFrom() + " 说 " + cmd.getMessage());
                    getSender().tell(resCmd, getSelf());
                }
            }
        }
    }


    private HashMap<String, ActorRef> clients;

}
