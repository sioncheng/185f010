package com.ctrip.implus.gui;

import com.ctrip.implus.ChatCommand;

/**
 * Created by chengyq on 2016/10/26.
 */
public interface ChatSocketListener {
    void onLogin(String message);
    void onReceive(ChatCommand cmd);
    void onException(String message);
}
