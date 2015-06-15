package com.group3.pcremote.model;

import java.io.Serializable;

public class ServerInfo implements Serializable {
	private String serverName;
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
