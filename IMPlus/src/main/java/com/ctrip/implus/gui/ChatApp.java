package com.ctrip.implus.gui;

import com.ctrip.implus.ChatCommand;

import javax.swing.*;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatApp implements LoginListener, ChatListener, ChatSocketListener {

    public static void main(String[] args) {
        app = new ChatApp();
        app.start();
    }

    public ChatApp() {
        lw = new LoginWindow(this);
        cw = new ChatWindow(this);
    }

    private void start() {
        lw.setVisible(true);
    }

    public void login(String nickname) {
        if (null == nickname || "".equals(nickname)) {
            lw.warn("昵称不能为空");
            return;
        }

        if(nickname.length() > 20){
            lw.warn("昵称字数不要多于20");
            return;
        }

        this.from = nickname;
        cs = new ChatSocket(ServerAddress, ServerPort, this);
        cs.login(nickname);
    }

    public void onLogin(String logon) {
        if ("".equals(logon)) {
            lw.setVisible(false);
            cw.setVisible(true);
        } else {
            lw.warn(logon);
        }
    }

    public void onReceive(ChatCommand cmd) {
        if (ChatCommand.SEND.equals(cmd.getCommand())) {
            cw.appendMessage(cmd.getFrom(), cmd.getMessage());
        } else if (ChatCommand.JOIN.equals(cmd.getCommand())) {
            cw.addMember(cmd.getMessage());
        } else if(ChatCommand.LEAVE.equals(cmd.getCommand())) {
            cw.removeMember(cmd.getMessage());
        }
    }

    public void onException(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void onExit() {
        ChatCommand logoutCommand = new ChatCommand(ChatCommand.LOGOUT, this.from, "");
        cs.send(logoutCommand);

        System.out.println("exit");

        System.exit(0);
    }

    public void onClosing() {
        System.exit(0);
    }

    public void sendMessage(String message){
        if(message.length() > 500) {
            cw.warn("消息字数不要多于500");
            return;
        }

        ChatCommand sendCommand = new ChatCommand(ChatCommand.SEND, this.from, message);
        cs.send(sendCommand);
    }

    private LoginWindow lw;
    private ChatWindow cw;
    private ChatSocket cs;

    private String from;

    private static ChatApp app;

    private final static String ServerAddress = "10.32.24.29";
    private final static int ServerPort = 7778;
}
