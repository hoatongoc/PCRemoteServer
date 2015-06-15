package com.group3.pcremote.model;

import java.io.Serializable;

public class ClientInfo implements Serializable {
	private String clientName;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}