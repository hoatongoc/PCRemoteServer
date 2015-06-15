package com.group3.pcremote.api;

import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.CommonConstant;
import com.group3.pcremote.constant.SocketConstant;
import com.group3.pcremote.model.SenderData;


public class MaintainConnection extends SwingWorker<String, String> {

	private DatagramSocket datagramSocket = null;
	private MainForm mainForm = null;
	private InetAddress inetAddress = null;
	private int port;
	public MaintainConnection(DatagramSocket datagramSocket, MainForm mainForm, InetAddress inetAddress, int port) {
		super();
		this.datagramSocket = datagramSocket;
		this.mainForm = mainForm;
		this.inetAddress = inetAddress;
		this.port = port;
	}
	
	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		if(datagramSocket !=null && mainForm!=null) {
			//send Maintain connection data back to android device
			SenderData senderData = new SenderData();
			senderData.setCommand(SocketConstant.CONNECT_MAINTAIN_CONNECTION);
			SendDatagramObject.send(datagramSocket, senderData, inetAddress, port);
		}
		return null;
	}
	
	public MainForm getMainForm() {
		return mainForm;
	}
	public InetAddress getInetAddress() {
		return inetAddress;
	}
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}
	public int getPort() {
		return port;
	}
	public void setMainForm(MainForm mainForm) {
		this.mainForm = mainForm;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}
	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}

}
