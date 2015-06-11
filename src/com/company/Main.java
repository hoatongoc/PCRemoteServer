package com.company;

import com.group3.pcremote.model.SenderData;
import com.group3.pcremote.model.ServerInfo;
import com.group3.pcremote.constant.SocketConstant;

import java.awt.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static void main(String[] args) throws AWTException, IOException,
			InterruptedException, ClassNotFoundException {
		
		DatagramSocket dsk = new DatagramSocket(SocketConstant.PORT);
		byte[] buffer = new byte[6000];
		DatagramPacket pk = new DatagramPacket(buffer, buffer.length);

		while (true) {
			dsk.receive(pk);
//			 LOGGER.info("Client: " + pk.getAddress() + ":" +
//			 pk.getAddress().getHostName() + ":" + pk.getPort());
			 
			ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
			ObjectInputStream ois = new ObjectInputStream(baos);
			SenderData senderData = (SenderData) ois.readObject();
			
			if (senderData.getCommand().equals(SocketConstant.REQUEST_SERVER_INFO)) {
				ServerInfo serverInfo = new ServerInfo();
				serverInfo.setServerName(InetAddress.getLocalHost().getHostName());
				serverInfo.setServerIP(InetAddress.getLocalHost().toString());
				senderData.setData(serverInfo);
				senderData.setCommand(SocketConstant.RESPONSE_SERVER_INFO);
				
				ByteArrayOutputStream bao = new ByteArrayOutputStream(6000);
				ObjectOutputStream oos = new ObjectOutputStream(bao);
				oos.writeObject(senderData);
				byte[] buf = bao.toByteArray();
				
				DatagramPacket pkg = new DatagramPacket(buf, buf.length,
						pk.getAddress(), pk.getPort());
				dsk.send(pkg);
			}
		}
	}

}
