package com.group3.pcremote.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendDatagramObject {

	//send an object to other device
	public static void send(DatagramSocket datagramSocket,Object dataToSend, InetAddress addrToSend, int portToSend) {
	 	/* Create ByteArratOutputStream and ObjectOutput Stream
	 	 * then write dataToSend to ObjectOutputStream  
	 	 * finally, put it into DatagramSocket and send it
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
			datagramSocket.send(pkg);
		} catch (IOException e) {
			e.printStackTrace();
		}
}
}
