package com.group3.pcremote.api;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.KeyboardConstant;
import com.group3.pcremote.constant.MouseConstant;
import com.group3.pcremote.constant.SocketConstant;
import com.group3.pcremote.model.ClientInfo;
import com.group3.pcremote.model.KeyboardCommand;
import com.group3.pcremote.model.MouseClick;
import com.group3.pcremote.model.SenderData;
import com.group3.pcremote.api.HandleConnectionRequest;

public class ReceivePacketAndProcess extends SwingWorker<String, String>{

	private MainForm mainForm;
	
//	private String connectedDeviceAdress = "";
//	private String connectedDeviceName = "";
	

	public MainForm getMainForm() {
		return mainForm;
	}


	public void setMainForm(MainForm mainForm) {
		this.mainForm = mainForm;
	}


	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}


	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}

	DatagramSocket datagramSocket;
	public ReceivePacketAndProcess(MainForm f, DatagramSocket d) {
		this.mainForm = f;
		this.datagramSocket = d;
	}
	
	
	@Override
	protected String doInBackground() throws Exception {
		
		Robot robot = null;
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			
			e.printStackTrace();
			
		}
        
        byte[] buffer= new byte[6000];
        DatagramPacket pk= new DatagramPacket(buffer, buffer.length);
        
		
        //loop for receiving packets and process them
		while (!isCancelled())
		{
	            try {
	            	datagramSocket.receive(pk);
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
								HandleRequestServerInfo handleRequestServerInfo = new HandleRequestServerInfo(datagramSocket,pk.getAddress(), pk.getPort());
								handleRequestServerInfo.execute();
				            }
							//if command is request connection for controlling PC, handle it
							else if(command.equals(SocketConstant.REQUEST_CONNECT)) {
								ClientInfo clientInfo  = (ClientInfo) data.getData();
								if(clientInfo!=null) {
									HandleConnectionRequest handleConnectionRequest = new HandleConnectionRequest(mainForm,datagramSocket,pk.getAddress(),clientInfo.getClientName());
									handleConnectionRequest.execute();
								}
							}
							//if command is peform mouse click actions, handle it
							else if(command.equals(MouseConstant.MOUSE_COMMAND)) {
								MouseClick mouseClick = (MouseClick)data.getData();
								if(mouseClick!=null) {
									HandleMouseClick handleMouseClick = new HandleMouseClick(mouseClick, robot);
									handleMouseClick.execute();
								}
							}
							else if(command.equals(KeyboardConstant.KEYBOARD_COMMAND)) {
								KeyboardCommand keyboardCommand = (KeyboardCommand)data.getData();
								if(keyboardCommand!=null) {
									HandleKeyboardPress handleKeyboardPress = new HandleKeyboardPress(keyboardCommand, robot);
									handleKeyboardPress.execute();
								}
							}
						}
					}
					
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				
			}
				
		}
		datagramSocket.close();
		return null;
	}
		
}
