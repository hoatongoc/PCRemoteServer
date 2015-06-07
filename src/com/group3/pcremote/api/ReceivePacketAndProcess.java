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

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.SocketConstant;
import com.group3.pcremote.model.MouseClick;
import com.group3.pcremote.model.SenderData;
import com.group3.pcremote.model.ServerInfo;

public class ReceivePacketAndProcess extends SwingWorker<String, String>{

	MainForm sForm;
	DatagramSocket sDatagramSocket;
	ReceivePacketAndProcess(MainForm f, DatagramSocket d) {
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
			// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            System.out.println("Client: " + pk.getAddress() + ":" + pk.getPort());
	            JLabel addrAddrOutput = (JLabel) sForm.getComponentByName("dAddrOutput");
	            addrAddrOutput.setText(pk.getAddress().toString());
	            ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
	            ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(baos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            try {
					SenderData data = (SenderData)ois.readObject();
					if(data.getCommand().equals(SocketConstant.REQUEST_SERVER_INFO)) {
						SenderData sendData = new SenderData();
						sendData.setCommand(SocketConstant.RESPONSE_SERVER_INFO);
		                ServerInfo obj = new ServerInfo();
		                obj.setServerName(InetAddress.getLocalHost().getHostName());
		                obj.setServerIP(InetAddress.getLocalHost().toString());
		                sendData.setData(obj);
		                ByteArrayOutputStream bao = new ByteArrayOutputStream(6000);
		                ObjectOutputStream oos = new ObjectOutputStream(bao);
		                oos.writeObject(sendData);
		                byte[] buf= bao.toByteArray();
		                DatagramPacket pkg = new DatagramPacket(buf,buf.length,pk.getAddress(),pk.getPort());
		                sDatagramSocket.send(pkg);
		            }
					else if(data.getCommand().equals(SocketConstant.MOUSE_CLICK)) {
						if(((MouseClick)data.getData()).isPress()) {
							int btnNum = ((MouseClick)data.getData()).getButtonNum();
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
						else {
							int btnNum = ((MouseClick)data.getData()).getButtonNum();
							if(btnNum == 1) {
								r.mousePress(InputEvent.BUTTON1_MASK);
								try {
									Thread.sleep(20);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								r.mouseRelease(InputEvent.BUTTON1_MASK);
							}
							else if(btnNum == 2) {
								r.mousePress(InputEvent.BUTTON2_MASK);
								try {
									Thread.sleep(20);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								r.mouseRelease(InputEvent.BUTTON2_MASK);
							}
							else if(btnNum == 3) {
								r.mousePress(InputEvent.BUTTON3_MASK);
								try {
									Thread.sleep(20);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								r.mouseRelease(InputEvent.BUTTON3_MASK);
							}
						}
					}
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
		//sDatagramSocket.close();
		return null;
	}
}
