package com.group3.pcremote.api;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.MouseConstants;
import com.group3.pcremote.constant.SocketConstant;
import com.group3.pcremote.model.ClientInfo;
import com.group3.pcremote.model.MouseClick;
import com.group3.pcremote.model.SenderData;
import com.group3.pcremote.model.ServerInfo;

public class ReceivePacketAndProcess extends SwingWorker<String, String>{

	private MainForm sForm;
	private boolean deviceConnected = false;
	private String connectedDeviceAdress = "";
	private String connectedDeviceName = "";
	public boolean isDeviceConnected() {
		return deviceConnected;
	}

	DatagramSocket sDatagramSocket;
	public ReceivePacketAndProcess(MainForm f, DatagramSocket d) {
		this.sForm = f;
		this.sDatagramSocket = d;
	}
	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		Robot r = null;
		
		try {
			r = new Robot();
		} catch (AWTException e) {
			
			e.printStackTrace();
			
		}
        
        byte[] buffer= new byte[6000];
        DatagramPacket pk= new DatagramPacket(buffer, buffer.length);
		// TODO Auto-generated method stub
		while (!isCancelled())
		{
	            try {
	            	sDatagramSocket.receive(pk);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
	            System.out.println("Client: " + pk.getAddress() + ":" + pk.getPort());
	            ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
	            ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(baos);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
	            try {
					SenderData data = (SenderData)ois.readObject();
					if(data!=null) {
						Object command = data.getCommand();
						if(command!=null) {
							if(command.equals(SocketConstant.REQUEST_SERVER_INFO)) {
								handleRequestServerInfo(pk.getAddress(), pk.getPort());
				            }
							else if(command.equals(SocketConstant.REQUEST_CONNECT)) {
								ClientInfo clientInfo  = (ClientInfo) data.getData();
								if(clientInfo!=null)
								handleConnectRequest(clientInfo.getClientName(),clientInfo.getClientIP());
							}
							else if(command.equals(SocketConstant.MOUSE_CLICK)) {
								MouseClick mouseClickData = (MouseClick)data.getData();
								if(mouseClickData!=null) handleMouseClick(mouseClickData,r);
							}
						}
					}
					
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				
			}
				
		}
		sDatagramSocket.close();
		return null;
	}
	
	//send datagram object
	private void sendDatagramObject(Object dataToSend, InetAddress addrToSend, int portToSend) {
		 ByteArrayOutputStream bao = new ByteArrayOutputStream(6000);
	        ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(bao);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	        try {
				oos.writeObject(dataToSend);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	        byte[] buf= bao.toByteArray();
	        DatagramPacket pkg = null;
			pkg = new DatagramPacket(buf,buf.length,addrToSend,portToSend);
	        try {
				sDatagramSocket.send(pkg);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	//handle when there's a android device want to connect
	private void handleRequestServerInfo(InetAddress sendAddr, int sendPort) {
		SenderData sendData = new SenderData();
		sendData.setCommand(SocketConstant.RESPONSE_SERVER_INFO);
        ServerInfo obj = new ServerInfo();
        try {
			obj.setServerName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
        try {
			obj.setServerIP(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
        sendData.setData(obj);
        sendDatagramObject(sendData, sendAddr, sendPort);
//        ByteArrayOutputStream bao = new ByteArrayOutputStream(6000);
//        ObjectOutputStream oos = null;
//		try {
//			oos = new ObjectOutputStream(bao);
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
//        try {
//			oos.writeObject(sendData);
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
//        byte[] buf= bao.toByteArray();
//        DatagramPacket pkg = new DatagramPacket(buf,buf.length,sendAddr,sendPort);
//        try {
//			sDatagramSocket.send(pkg);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	private void handleMouseClick(MouseClick mouseData, Robot r) {
		int btnNum = (mouseData.getButtonNum());
		if(mouseData.getPress() == MouseConstants.PRESS) {
			if(btnNum == 1) {
				r.mousePress(InputEvent.BUTTON1_MASK);
			}
			else if(btnNum == 2) {
				r.mousePress(InputEvent.BUTTON2_MASK);
			}
			else if(btnNum == 3) {
				r.mousePress(InputEvent.BUTTON3_MASK);
			}
		}
		else if (mouseData.getPress() == MouseConstants.CLICK) {
			
			if(btnNum == 1) {
				r.mousePress(InputEvent.BUTTON1_MASK);
				r.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			else if(btnNum == 2) {
				r.mousePress(InputEvent.BUTTON2_MASK);
				r.mouseRelease(InputEvent.BUTTON2_MASK);
			}
			else if(btnNum == 3) {
				r.mousePress(InputEvent.BUTTON3_MASK);
				r.mouseRelease(InputEvent.BUTTON3_MASK);
			}
		}
	}
	private void handleConnectRequest(final String dName, final String dAddr) {
		Thread t = new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						JOptionPane.getRootFrame().dispose();
					}
				}, 4000);
				
				
				String message = "Device blah blah " + dName + " want to connect. Confirm?\n Yes to accept, No to refuse the connect";
				String title = "Connect Confirmation";
				
				int connectConfirm = JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
				if(connectConfirm == JOptionPane.YES_OPTION) {
					//deviceConnected = true;
					JLabel dAddrLabel = (JLabel) sForm.getComponentByName("dAddrOutput");
					JLabel dNameLabel = (JLabel) sForm.getComponentByName("dNameOutput");
		            connectedDeviceAdress = dAddr;
		            connectedDeviceName = dName;
					dAddrLabel.setText(connectedDeviceAdress);
		            dNameLabel.setText(connectedDeviceName);
		            
		            //Send confirm packet back to Android device
		            SenderData confirmData = new SenderData();
		            confirmData.setCommand(SocketConstant.CONNECT_ACCEPT);
		            try {
						sendDatagramObject(confirmData, InetAddress.getByName(connectedDeviceAdress), SocketConstant.PORT);
						timer.cancel();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(connectConfirm == JOptionPane.NO_OPTION) {
					//do blah blah
					 SenderData confirmData = new SenderData();
			            confirmData.setCommand(SocketConstant.CONNECT_REFUSE);
			            try {
			            	timer.cancel();
							sendDatagramObject(confirmData, InetAddress.getByName(connectedDeviceAdress), SocketConstant.PORT);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		});
		
		t.start();
	}
	
	
}
