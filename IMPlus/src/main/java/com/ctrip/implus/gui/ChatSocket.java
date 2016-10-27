package com.ctrip.implus.gui;

import com.ctrip.implus.ChatCommand;
import com.ctrip.implus.ChatCommandBuilder;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatSocket implements Runnable {

    public ChatSocket(String serverAddress, int serverPort, ChatSocketListener listener) {
        this.socketAddress = new InetSocketAddress(serverAddress, serverPort);
        this.listener = listener;
        this.cb = new ChatCommandBuilder();
    }

    public void login(String nickname) {
        try {
            socket = new Socket();
            socket.connect(this.socketAddress);
        } catch (java.io.IOException ioe) {
            this.close();
            this.listener.onLogin("网络连接失败");
        } catch (Exception ex) {
            this.close();
            this.listener.onLogin("登录异常");
        }

        ChatCommand reqCmd = new ChatCommand(ChatCommand.LOGIN, nickname, "");
        byte[] bytes = reqCmd.toBytes();
        if (bytes == null) {
            this.close();
            this.listener.onLogin("数据异常");
        }


        try {
            socket.getOutputStream().write(bytes);
        } catch (Exception e) {
            this.close();
            this.listener.onLogin("发送网络数据失败");
        }

        try {
            List<ChatCommand> cmds = read();
            if (cmds.size() == 0) {
                this.listener.onLogin("解析登录响应失败");
            }

            Iterator<ChatCommand> itr = cmds.iterator();
            ChatCommand firstCmd = itr.next();

            if (firstCmd.getCommand().equals(ChatCommand.LOGIN) && firstCmd.getMessage().equals("ok")) {
                loopRead();
                this.listener.onLogin("");
            } else {
                this.listener.onLogin(firstCmd.getMessage());
            }

            while(itr.hasNext()){
                this.listener.onReceive(itr.next());
            }

        } catch (Exception ex) {
            this.listener.onLogin(ex.getMessage());
        }
    }

    public String send(ChatCommand chatCommand) {
        byte[] bytes = chatCommand.toBytes();
        if (bytes == null) {
            this.close();
            return "数据异常";
        }


        try {
            socket.getOutputStream().write(bytes);
            return "";
        } catch (Exception e) {
            this.close();
            return "发送网络数据失败";
        }
    }

    private void loopRead() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (true) {
            try {
                List<ChatCommand> resCmd = read();
                Iterator<ChatCommand> itr = resCmd.iterator();
                while(itr.hasNext()) {
                    this.listener.onReceive(itr.next());
                }
            } catch (Exception ex) {
                this.close();
                this.listener.onException(ex.getMessage());

                break;
            }
        }
    }

    private List<ChatCommand> read() throws Exception {
        byte[] buffer = new byte[1024];
        int index = 0;
        int len = 1024;
        try {
            while (len > 0) {
                int n = socket.getInputStream().read(buffer, index, len);
                if (n == 0) {
                    this.close();
                    throw new Exception("读取网络数据失败");
                }
                index += n;
                len -= n;


                synchronized (this) {
                    String s = new String(buffer, 0, index, "UTF8");
                    List<ChatCommand> cmds = cb.append(s);
                    if (cmds.size() > 0) {
                        return cmds;
                    }
                }
            }

            this.close();
            throw new Exception("读取网络数据异常:命令无结束");

        } catch (Exception e) {
            throw new Exception("读取网络数据失败");
        }
    }

    private void close() {
        try {
            socket.close();
        } catch (Exception e) {
        }
    }


    private InetSocketAddress socketAddress;
    private Socket socket;
    private ChatSocketListener listener;

    private ChatCommandBuilder cb;
}
