package com.group3.pcremote.model;

import java.io.Serializable;

public class SenderDataForServerInfo implements Serializable {
	private String command = "";
	private ServerInfo serverInfo;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public ServerInfo getServerInfo() {
		return serverInfo;
	}
	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}
	
	
}