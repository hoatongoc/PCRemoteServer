package com.group3.pcremote.model;

import java.io.Serializable;

public class ClientInfo implements Serializable {
	private String clientIP;
	private String clientName;

	public String getclientIP() {
		return clientIP;
	}

	public void setclientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getclientName() {
		return clientName;
	}

	public void setclientName(String clientName) {
		this.clientName = clientName;
	}

}
