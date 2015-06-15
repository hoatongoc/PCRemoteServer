package com.group3.pcremote.api;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.SocketConstant;
import com.group3.pcremote.model.SenderData;
import com.group3.pcremote.model.ServerInfo;


//Handle when an Android device want to check if PC is ready to connect
public class HandleRequestServerInfo extends SwingWorker<String, String> {
	MainForm mainForm;
	DatagramSocket datagramSocket = null;
	InetAddress sendAddress;
	int sendPort=0;
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}

	public HandleRequestServerInfo(DatagramSocket datagramSocket,
			InetAddress sendAddress, int sendPort) {
		super();
		this.datagramSocket = datagramSocket;
		this.sendAddress = sendAddress;
		this.sendPort = sendPort;
	}

	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}

	public InetAddress getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(InetAddress sendAddress) {
		this.sendAddress = sendAddress;
	}

	public int getSendPort() {
		return sendPort;
	}

	public void setSendPort(int sendPort) {
		this.sendPort = sendPort;
	}

	
	
	@Override
	protected String doInBackground() throws Exception {
		// Create a new SendData object
				SenderData senderData = new SenderData();
				senderData.setCommand(SocketConstant.RESPONSE_SERVER_INFO);
				//create a new ServerInfo object
		        ServerInfo serverInfo = new ServerInfo();
		        try {
					serverInfo.setServerName(InetAddress.getLocalHost().getHostName());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
		        //bring ServerInfo object into SendData Object, then send it to Android device
		        senderData.setData(serverInfo);
		        SendDatagramObject.send(datagramSocket,senderData, sendAddress, sendPort);
		// TODO Auto-generated method stub
		return null;
	}
}
