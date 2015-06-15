package com.group3.pcremote.api;

import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.CommonConstant;
import com.group3.pcremote.model.ClientInfo;

public class HandleDisconnectRequest extends SwingWorker<String, String> {
	private MainForm mainForm = null;
	ClientInfo clientInfo = null;
	InetAddress offerAddress = null;
	public HandleDisconnectRequest() {
		// TODO Auto-generated constructor stub
	}
	public HandleDisconnectRequest(MainForm mainForm, ClientInfo clientInfo, InetAddress offerAddress) {
		super();
		this.mainForm = mainForm;
		this.clientInfo = clientInfo;
		this.offerAddress = offerAddress;
	}
	
	public ClientInfo getClientInfo() {
		return clientInfo;
	}
	
	public MainForm getMainForm() {
		return mainForm;
	}
	
	public InetAddress getOfferAddress() {
		return offerAddress;
	}
	
	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	
	public void setMainForm(MainForm mainForm) {
		this.mainForm = mainForm;
	}
	
	public void setOfferAddress(InetAddress offerAddress) {
		this.offerAddress = offerAddress;
	}
	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		if(mainForm !=null && clientInfo !=null && offerAddress!=null) {
			if(clientInfo.getClientName().equals(mainForm.getConnectedDeviceName())
					&& offerAddress.getHostAddress().equals(mainForm.getConnectedDeviceAdress())) {
				clearConnectedDevice(mainForm);
			}
		}
		
		return null;
	}
	
	public static void clearConnectedDevice(MainForm mForm) {
		mForm.setConnectedDeviceAdress(CommonConstant.NOT_AVAIABLE);
		mForm.setConnectedDeviceName(CommonConstant.NOT_AVAIABLE);
		mForm.setDeviceConnected(false);
		JLabel offerAdressLabel = (JLabel) mForm.getComponentByName("dAddrOutput");
		JLabel offerNameLabel = (JLabel) mForm.getComponentByName("dNameOutput");
		offerAdressLabel.setText(mForm.getConnectedDeviceAdress());
		offerNameLabel.setText(mForm.getConnectedDeviceName());
		
		try {
			MainForm.getdSocket().setSoTimeout(0);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
