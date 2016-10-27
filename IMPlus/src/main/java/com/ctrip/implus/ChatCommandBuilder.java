package com.ctrip.implus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengyq on 2016/10/27.
 */
public class ChatCommandBuilder {

    public ChatCommandBuilder() {
        this.sb = new StringBuffer();
    }

    public List<ChatCommand> append(String s) {
        ArrayList<ChatCommand> cmds = new ArrayList<ChatCommand>();
        sb.append(s);

        while(true) {
            int index = sb.indexOf("\r\n");
            if (index > 0) {
                ChatCommand cmd = ChatCommand.parse(sb.substring(0, index));
                if (sb.length() > index + 2) {
                    sb = new StringBuffer(sb.substring(index + 2));
                } else {
                    sb = new StringBuffer();
                }
                cmds.add(cmd);
            } else {
                break;
            }
        }

        return cmds;
    }


    private StringBuffer sb;
}
