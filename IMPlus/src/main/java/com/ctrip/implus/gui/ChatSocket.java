package com.ctrip.implus.gui;

import com.ctrip.implus.ChatCommand;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatSocket implements Runnable {

    public ChatSocket(String serverAddr, int serverPort, ChatSocketListener listener) {
        this.socketAddr = new InetSocketAddress(serverAddr, serverPort);
        this.listener = listener;
    }

    public String login(String nickname) {
        try {
            socket = new Socket();
            socket.connect(this.socketAddr);
        } catch (java.io.IOException ioe) {
            this.close();
            return "网络连接失败";
        } catch (Exception ex) {
            this.close();
            return "登录异常";
        }

        ChatCommand reqCmd = new ChatCommand(ChatCommand.LOGIN, nickname, "");
        byte[] bytes = reqCmd.toBytes();
        if (bytes == null) {
            this.close();
            return "数据异常";
        }


        try {
            socket.getOutputStream().write(bytes);
        } catch (Exception e) {
            this.close();
            return "发送网络数据失败";
        }

        try {
            ChatCommand resCmd = read();
            if (resCmd == null) {
                return "解析登录响应失败";
            }
            if (resCmd.getCommand().equals(ChatCommand.LOGIN) && resCmd.getMessage().equals("ok")) {
                loopRead();
                return "";
            } else {
                return resCmd.getMessage();
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String send(String from, String message) {
        ChatCommand reqCmd = new ChatCommand(ChatCommand.SEND, from, message);
        byte[] bytes = reqCmd.toBytes();
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
                ChatCommand resCmd = read();
                this.listener.onReceive(resCmd);
            } catch (Exception ex) {
                this.close();
                this.listener.onException(ex.getMessage());

                break;
            }
        }
    }

    private ChatCommand read() throws Exception {
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

                String commandLine = new String(buffer, 0, index, "UTF8");
                int endIndex = commandLine.indexOf("\r\n");
                if (endIndex > 0) {
                    return ChatCommand.parse(commandLine.substring(0, endIndex));
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


    private InetSocketAddress socketAddr;
    private Socket socket;
    private ChatSocketListener listener;
}
