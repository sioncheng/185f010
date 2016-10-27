package com.ctrip.implus;

import java.util.Comparator;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatCommand {

    public final static String LOGIN = "login";
    public final static String LOGOUT = "logut";
    public final static String SEND = "sendd";
    public final static String JOIN = "joinn";
    public final static String LEAVE = "leave";

    public static ChatCommand parse(String commandText) {
        String command = commandText.substring(0, 5);
        String from = commandText.substring(5, 25).trim();
        String message = commandText.substring(25);

        return new ChatCommand(command,from, message);
    }

    public ChatCommand(){
        this.setCommand("");
        this.setFrom("");
        this.setMessage("");
    }

    public ChatCommand(String cmd, String from, String msg) {
        this.setCommand(cmd);
        this.setFrom(from);
        this.setMessage(msg);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] toBytes() {
        try {
            return this.toString().getBytes("UTF8");
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.getCommand() + this.paddingFrom() + this.getMessage() + "\r\n";
    }

    private String paddingFrom() {
        if(this.getFrom().length() == 20) {
            return this.getFrom();
        } else {
            int space = 20 - this.getFrom().length();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < space; i++) {
                sb.append(" ");
            }
            sb.append(this.getFrom());

            return sb.toString();
        }
    }

    private String command;

    private String from;

    private String message;
}
