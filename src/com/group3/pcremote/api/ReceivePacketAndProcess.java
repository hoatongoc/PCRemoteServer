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
	

	DatagramSocket sDatagramSocket;
	public ReceivePacketAndProcess(MainForm f, DatagramSocket d) {
		this.sForm = f;
		this.sDatagramSocket = d;
	}
	
	
	public boolean isDeviceConnected() {
		return deviceConnected;
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
        //loop for receiving packets and proccess them
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
	            	//read received object
					SenderData data = (SenderData)ois.readObject();
					if(data!=null) {
						//get command of received object
						Object command = data.getCommand();
						if(command!=null) {
							//if command is request info of server, handle it
							if(command.equals(SocketConstant.REQUEST_SERVER_INFO)) {
								handleRequestServerInfo(pk.getAddress(), pk.getPort());
				            }
							//if command is request connection for controlling PC, handle it
							else if(command.equals(SocketConstant.REQUEST_CONNECT)) {
								ClientInfo clientInfo  = (ClientInfo) data.getData();
								if(clientInfo!=null)
								handleConnectRequest(clientInfo.getClientName(),clientInfo.getClientIP());
							}
							//if command is peform mouse click actions, handle it
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
		 	/* Create ByteArratOutputStream and ObjectOutput Stream
		 	 * then write dataToSend to ObjectOutputStream  
		 	 * finally, put it into DatagramSocket and send it
		 	 * 
		 	 * 
		 	 * */ 
		
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
		// Create a new SendData object
		SenderData sendData = new SenderData();
		sendData.setCommand(SocketConstant.RESPONSE_SERVER_INFO);
		//create a new ServerInfo object
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
        //bring ServerInfo object into SendData Object, then send it to Android device
        sendData.setData(obj);
        sendDatagramObject(sendData, sendAddr, sendPort);
	}
	
	private void handleMouseClick(MouseClick mouseData, Robot r) {
		//get index of mouse button that android device wants to control
		int btnNum = (mouseData.getButtonNum());
		
		//if the command is press the mouse button
		if(mouseData.getPress() == MouseConstants.PRESS) {
			if(btnNum == MouseConstants.LEFT_MOUSE) {
				r.mousePress(InputEvent.BUTTON1_MASK);
			}
			else if(btnNum == MouseConstants.MIDDLE_MOUSE) {
				r.mousePress(InputEvent.BUTTON2_MASK);
			}
			else if(btnNum == MouseConstants.RIGHT_MOUSE) {
				r.mousePress(InputEvent.BUTTON3_MASK);
			}
		}
		
		//if the command is mouse click
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
		
		//if the command is release mouse button
		else if(mouseData.getPress() == MouseConstants.RELEASE) {
			if(btnNum == 1) {
				r.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			else if(btnNum == 2) {
				r.mouseRelease(InputEvent.BUTTON2_MASK);
			}
			else if(btnNum == 3) {
				r.mouseRelease(InputEvent.BUTTON3_MASK);
			}
		}
	}
	
	//handle the request connect command
	private void handleConnectRequest(final String dName, final String dAddr) {
		//create new thread for not stopping receiving broadcast packets from android devices
		Thread t = new Thread(new Runnable() {
			
			//if timeout after 4 secs, dismiss the confirm dialog
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
				
				
				//show the confim dialog
				String message = "Device blah blah " + dName + " want to connect. Confirm?\n Yes to accept, No to refuse the connect";
				String title = "Connect Confirmation";
				
				int connectConfirm = JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
				
				//If user choose yes option (accept connect) then update UI and send back the confirm packet to android decvice
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
				//else send back to android device the refuse connection packet
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
