package com.group3.pcremote.api;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.CommonConstant;
import com.group3.pcremote.constant.KeyboardConstant;
import com.group3.pcremote.model.ClientInfo;
import com.sun.glass.events.MouseEvent;

public class HandleDisconnectRequest extends SwingWorker<String, String> {
	private MainForm mainForm = null;
	ClientInfo clientInfo = null;
	InetAddress offerAddress = null;
	static Robot robot = null;
	public HandleDisconnectRequest() {
		// TODO Auto-generated constructor stub
	}
	public HandleDisconnectRequest(MainForm mainForm, ClientInfo clientInfo, InetAddress offerAddress, Robot robot) {
		super();
		this.mainForm = mainForm;
		this.clientInfo = clientInfo;
		this.offerAddress = offerAddress;
		this.robot = robot;
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
	public Robot getRobot() {
		return robot;
	}
	public void setRobot(Robot robot) {
		this.robot = robot;
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
		System.out.println("Disconnected");
		mForm.setConnectedDeviceAdress(CommonConstant.NOT_AVAIABLE);
		mForm.setConnectedDeviceName(CommonConstant.NOT_AVAIABLE);
		mForm.setDeviceConnected(false);
		JLabel offerAdressLabel = (JLabel) mForm.getComponentByName("dAddrOutput");
		JLabel offerNameLabel = (JLabel) mForm.getComponentByName("dNameOutput");
		offerAdressLabel.setText(mForm.getConnectedDeviceAdress());
		offerNameLabel.setText(mForm.getConnectedDeviceName());
		mForm.setConnectionAlive(false); //connection not alive anymore, countAlive = 0;
		if(mForm.getTimerConnectionAlive()!=null)
			mForm.getTimerConnectionAlive().cancel();
		
		//Release all mouse pressed and key pressed
		if(robot!=null) {
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON2_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
			robot.keyRelease(KeyboardConstant.VK_SHIFT);
			robot.keyRelease(KeyboardConstant.VK_CONTROL);
			robot.keyRelease(KeyboardConstant.VK_ALT);
			
		}
		
		try {
			MainForm.getdSocket().setSoTimeout(0);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
