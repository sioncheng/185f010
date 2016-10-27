package com.ctrip.implus.gui;

/**
 * Created by chengyq on 2016/10/27.
 */
public interface ChatListener {
    void onExit();
    void sendMessage(String message);
}
