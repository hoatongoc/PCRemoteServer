package com.group3.pcremote.model;

import java.io.Serializable;

/**
 * Created by HoaDT on 6/1/2015.
 */
public class ServerInfo implements Serializable {
    private String serverIP;
    private String serverName;

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

}

