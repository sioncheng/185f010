package com.ctrip.implus;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatHandler extends UntypedActor {

    public ChatHandler(ActorRef manager) {
        this.manager = manager;
        this.sb = new StringBuffer();
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
            getContext().stop(getSelf());
        } else if(message instanceof ChatCommand) {
            ChatCommand resCmd = (ChatCommand)message;
            tcpSender.tell(TcpMessage.write(ByteString.fromArray(resCmd.toBytes())), getSelf());
        }
    }

    private void processLogin(ByteString data, ActorRef self) {
        ChatCommand cmd = parseCmd(data);
        if(cmd != null) {
            System.out.println("login");
            this.manager.tell(cmd,self);
        }
    }

    private void processMessage(ByteString data, ActorRef self) {
        ChatCommand cmd = parseCmd(data);
        if(cmd != null) {
            System.out.println("message");
            this.manager.tell(cmd,self);
        }
    }

    private ChatCommand parseCmd(ByteString data) {
        sb.append(data.decodeString("UTF8"));
        int index = sb.indexOf("\r\n");
        if (index > 0) {
            ChatCommand cmd = ChatCommand.parse(sb.substring(0, index));
            if (sb.length() > index + 2) {
                sb = new StringBuffer(sb.substring(index + 2));
            } else {
                sb = new StringBuffer();
            }

            return cmd;
        } else {
            return null;
        }
    }

    private ActorRef manager;
    private ActorRef tcpSender;
    private StringBuffer sb;
    private int status;

    private final static int STATUS_INIT = 1;
    private final static int STATUS_HANDSHAKED = 2;
}
