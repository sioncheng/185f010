package com.ctrip.implus;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

import java.util.List;
import java.util.Iterator;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatHandler extends UntypedActor {

    public ChatHandler(ActorRef manager) {
        this.manager = manager;
        this.cb = new ChatCommandBuilder();
        this.status = ChatHandler.STATUS_INIT;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof Tcp.Received) {
            this.tcpSender = getSender();

            final ByteString data = ((Tcp.Received) message).data();
            if (status == ChatHandler.STATUS_INIT) {
                processLogin(data, getSelf());
            } else {
                processMessage(data, getSelf());
            }
        } else if (message instanceof Tcp.ConnectionClosed) {
            final Tcp.ConnectionClosed cc = (Tcp.ConnectionClosed) message;
            System.out.println("connection closed " + cc.toString());

            ChatCommand logoutCmd = new ChatCommand(ChatCommand.LOGOUT, this.from, cc.getErrorCause());
            this.manager.tell(logoutCmd, getSelf());

            getContext().stop(getSelf());
        } else if (message instanceof ChatCommand) {
            this.status = ChatHandler.STATUS_HANDSHAKED;

            ChatCommand resCmd = (ChatCommand) message;
            tcpSender.tell(TcpMessage.write(ByteString.fromArray(resCmd.toBytes())), getSelf());

            System.out.println(resCmd.toString());
        }
    }

    private void processLogin(ByteString data, ActorRef self) {
        List<ChatCommand> cmds = cb.append(data.decodeString("UTF8"));
        if (cmds.size() > 0) {
            System.out.println("login");
            ChatCommand loginCmd = cmds.get(0);
            this.manager.tell(loginCmd, self);
            this.from = loginCmd.getFrom();
        } else {
            //todo
        }
    }

    private void processMessage(ByteString data, ActorRef self) {
        List<ChatCommand> cmds = cb.append(data.decodeString("UTF8"));
        Iterator<ChatCommand> itr = cmds.iterator();

        while (itr.hasNext()) {
            System.out.println("message");
            this.manager.tell(itr.next(), self);
        }
    }

    private ActorRef manager;
    private ActorRef tcpSender;
    private ChatCommandBuilder cb;
    private int status;
    private String from;

    private final static int STATUS_INIT = 1;
    private final static int STATUS_HANDSHAKED = 2;
}
