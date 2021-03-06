package com.group3.pcremote.api;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.KeyboardConstant;
import com.group3.pcremote.constant.MouseConstant;
import com.group3.pcremote.constant.PowerConstant;
import com.group3.pcremote.constant.SocketConstant;
import com.group3.pcremote.model.ClientInfo;
import com.group3.pcremote.model.Coordinates;
import com.group3.pcremote.model.KeyboardCommand;
import com.group3.pcremote.model.MouseClick;
import com.group3.pcremote.model.MouseScroll;
import com.group3.pcremote.model.PowerCommand;
import com.group3.pcremote.model.SenderData;
import com.group3.pcremote.api.HandleConnectionRequest;

public class ReceivePacketAndProcess extends SwingWorker<String, String>{

	private MainForm mainForm;
	
//	private String connectedDeviceAdress = "";
//	private String connectedDeviceName = "";
	

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
//        CheckConnectionAlive checkConnectionAlive = new CheckConnectionAlive(mainForm);
//        checkConnectionAlive.execute();
        
		
        //loop for receiving packets and process them
		while (!isCancelled())
		{
			
			try {
            	datagramSocket.receive(pk);
			} catch (Exception e) {
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
							System.out.println("Receive request sever info from " + pk.getAddress().getHostAddress());
							HandleRequestServerInfo handleRequestServerInfo = new HandleRequestServerInfo(datagramSocket,pk.getAddress(), pk.getPort());
							handleRequestServerInfo.execute();
			            }
						
						if(mainForm.isDeviceConnected() == false) {	
							
							//if command is request connection for controlling PC, handle it
							if(command.equals(SocketConstant.REQUEST_CONNECT)) {
								System.out.println("Receive request connect");
								ClientInfo clientInfo  = (ClientInfo) data.getData();
								if(clientInfo!=null) {
									HandleConnectionRequest handleConnectionRequest =
											new HandleConnectionRequest(mainForm,datagramSocket,pk.getAddress(),clientInfo.getClientName());
									handleConnectionRequest.execute();
								}
							}
						}
						else {

							//if command is perform mouse click actions, handle it
							if(command.equals(MouseConstant.MOUSE_CLICK_COMMAND)) {
								System.out.println("Receive mouse click command");
								MouseClick mouseClick = (MouseClick)data.getData();
								if(mouseClick!=null) {
									HandleMouseClick handleMouseClick = new HandleMouseClick(mouseClick, robot);
									handleMouseClick.execute();
								}
							}
							//if command is perform keyboard actions, handle it 
							else if(command.equals(KeyboardConstant.KEYBOARD_COMMAND)) {
								System.out.println("Receive keyboard command");
								KeyboardCommand keyboardCommand = (KeyboardCommand)data.getData();
								if(keyboardCommand!=null) {
									HandleKeyboardPress handleKeyboardPress = new HandleKeyboardPress(keyboardCommand, robot);
									handleKeyboardPress.execute();
								}
							}
							//if command is perform mouse move actions, handle it
							else if(command.equals(MouseConstant.MOUSE_MOVE_COMMAND)) {
								System.out.println("Receive mouse move command");
								Coordinates mousePosition = (Coordinates) data.getData();
								if(mousePosition!=null) {
									HandleMouseMove handleMouseMoving = new HandleMouseMove(robot, mousePosition);
									handleMouseMoving.execute();
								}
							}
							//if command is perform special power button (shutdown/restart etc.) handle it. 
							else if (command.equals(PowerConstant.POWER_COMMAND)) {
								System.out.println("Receive power command");
								PowerCommand powerCommand = (PowerCommand) data.getData();
								if(powerCommand !=null) {
									HandlePowerCommand handlePowerCommand = new HandlePowerCommand(powerCommand);
									handlePowerCommand.execute();
								}
							}
							//if command is request disconnect from server, handle it.
							else if (command.equals(SocketConstant.NOTIFY_DISCONNECT_FROM_CLIENT)) {
								System.out.println("Receive notify  disconnect");
								ClientInfo clientInfo = (ClientInfo) data.getData();
								if(clientInfo!=null) {
									HandleDisconnectRequest handleDisconnectRequest = 
											new HandleDisconnectRequest(mainForm,clientInfo,pk.getAddress(),robot);
									handleDisconnectRequest.execute();
								}
							}
							else if(command.equals(SocketConstant.MAINTAIN_CONNECTION)) {
								System.out.println("Receive maintain connection from " + pk.getAddress().getHostAddress());
								mainForm.setConnectionAlive(true);
								mainForm.setCountAlive(0);
							}
							else if(command.equals(MouseConstant.MOUSE_SCROLL)) {
								MouseScroll mouseScroll = (MouseScroll)data.getData();
								if(mouseScroll!=null) {
									System.out.println("Receive mouse scroll command " + mouseScroll.getScrollValue());
									HandleMouseScroll handleMouseScroll = new HandleMouseScroll(mouseScroll,robot);
									handleMouseScroll.execute();
								}
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


	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}

	public MainForm getMainForm() {
		return mainForm;
	}
	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}
	
	
	public void setMainForm(MainForm mainForm) {
		this.mainForm = mainForm;
	}
		
}
