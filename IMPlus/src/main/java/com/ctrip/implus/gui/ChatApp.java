package com.ctrip.implus.gui;

import com.ctrip.implus.ChatCommand;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatApp implements LoginInterface, ChatSocketListener {

    public static void main(String[] args) {
        app = new ChatApp();
        app.start();
    }

    public ChatApp() {
        lw = new LoginWindow(this);
        cw = new ChatWindow();
    }

    private void start() {
        lw.setVisible(true);
    }

    public void login(String nickname) {
        if(null == nickname || "".equals(nickname)){
            lw.warn("昵称不能为空");
            return;
        }

        cs = new ChatSocket(ServerAddr, ServerPort, this);
        String logon = cs.login(nickname);
        if("".equals(logon)) {
            this.from = nickname;

            lw.setVisible(false);
            cw.setVisible(true);
        } else {
            lw.warn(logon);
        }
    }

    public void onReceive(ChatCommand cmd) {

    }

    public void onException(String message) {

    }

    private LoginWindow lw;
    private ChatWindow cw;
    private ChatSocket cs;

    private String from;

    private static ChatApp app;

    private final static String ServerAddr = "10.32.24.29";
    private final static int ServerPort = 7778;
}
