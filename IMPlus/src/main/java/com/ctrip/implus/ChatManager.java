package com.ctrip.implus;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.dsl.Creators;
import akka.io.Tcp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
                this.processLogin(cmd);
            } else if (ChatCommand.LOGOUT.equals(cmd.getCommand())) {
                this.processLogout(cmd);
            } else if (ChatCommand.SEND.equals(cmd.getCommand())) {
                this.processSend(cmd);
            }
        }
    }

    private void processLogin(ChatCommand cmd) {
        ChatCommand resCmd = new ChatCommand();
        resCmd.setCommand(ChatCommand.LOGIN);
        resCmd.setFrom("system");

        if ("system".equals(cmd.getFrom()) || this.clients.containsKey(cmd.getFrom())) {
            resCmd.setMessage("昵称已经存在");
            getSender().tell(resCmd, getSelf());
        } else {
            //
            resCmd.setMessage("ok");
            getSender().tell(resCmd, getSelf());

            //welcome
            ChatCommand welcomeCmd = new ChatCommand(ChatCommand.SEND, "system", "欢迎你，"cmd.getFrom() + "。");
            getSender().tell(welcomeCmd, getSelf());

            //tell members
            Iterator<String> itr = this.clients.keySet().iterator();
            while (itr.hasNext()) {
                ChatCommand joinCmd = new ChatCommand(ChatCommand.JOIN, "system", itr.next());
                getSender().tell(joinCmd, getSelf());
            }


            //broad cast join information
            ChatCommand joinCmd = new ChatCommand(ChatCommand.JOIN, "system", cmd.getFrom());
            ChatCommand sendCmd = new ChatCommand(ChatCommand.SEND, "system", cmd.getFrom() + " 进来了。");
            Iterator<Map.Entry<String, ActorRef>> itrb = this.clients.entrySet().iterator();
            while (itrb.hasNext()) {
                ActorRef client = itrb.next().getValue();
                client.tell(joinCmd, getSelf());
                client.tell(sendCmd, getSelf());
            }

            //
            this.clients.put(cmd.getFrom(), getSender());
        }
    }

    private void processLogout(ChatCommand cmd) {
        //broad cast leave information
        ActorRef actorRef = this.clients.remove(cmd.getFrom());
        if(actorRef == null) {
            return;
        }

        ChatCommand leaveCmd = new ChatCommand(ChatCommand.LEAVE, "system", cmd.getFrom());
        ChatCommand sendCmd = new ChatCommand(ChatCommand.SEND, "system", cmd.getFrom() + " 离开了。");
        Iterator<Map.Entry<String, ActorRef>> itr = this.clients.entrySet().iterator();
        while (itr.hasNext()) {
            ActorRef client = itr.next().getValue();
            client.tell(leaveCmd, getSelf());
            client.tell(sendCmd, getSelf());
        }
    }

    private void processSend(ChatCommand cmd) {
        Iterator<Map.Entry<String, ActorRef>> itr = this.clients.entrySet().iterator();
        while (itr.hasNext()) {
            ActorRef client = itr.next().getValue();
            client.tell(cmd, getSelf());
        }
    }

    private HashMap<String, ActorRef> clients;

}
