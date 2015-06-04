package com.company;
import com.group3.pcremote.model.SenderData;
import com.group3.pcremote.model.ServerInfo;
import com.group3.pcremote.constant.SocketConstant;

import java.awt.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws AWTException, IOException, InterruptedException, ClassNotFoundException {
        Robot r = new Robot();
        DatagramSocket dsk= new DatagramSocket(1234);
        byte[] buffer= new byte[6000];
        DatagramPacket pk= new DatagramPacket(buffer, buffer.length);
        while(true) {
            dsk.receive(pk);
//            String command = new String(buffer, 0, pk.getLength());
            System.out.println("Client: " + pk.getAddress() + ":" +pk.getAddress().getHostName()+ ":" + pk.getPort());
//            System.out.println(command);
//            Point mousePos = MouseInfo.getPointerInfo().getLocation();
//            if (command.equals("up")) r.mouseMove(mousePos.x, mousePos.y - 20);
//            else if (command.equals("down")) r.mouseMove(mousePos.x, mousePos.y + 20);
//            if (command.equals("left")) r.mouseMove(mousePos.x - 20, mousePos.y);
//            if (command.equals("right")) r.mouseMove(mousePos.x + 20, mousePos.y);
//            if (command.equals("mouse_left")) {
//                r.mousePress(InputEvent.BUTTON1_MASK);
//                Thread.sleep(20);
//                r.mouseRelease(InputEvent.BUTTON1_MASK);
//            }
//            if (command.equals("mouse_right")){
//                r.mousePress(InputEvent.BUTTON3_MASK);
//                Thread.sleep(20);
//                r.mouseRelease(InputEvent.BUTTON3_MASK);
//            }


            ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(baos);
            SenderData c1 = (SenderData)ois.readObject();

            if(c1.getCommand().equals(SocketConstant.SERVER_INFO)) {
                ServerInfo obj = new ServerInfo();
                obj.setServerName(InetAddress.getLocalHost().getHostName());
                obj.setServerIP(InetAddress.getLocalHost().toString());
                c1.setData(obj);
                ByteArrayOutputStream bao = new ByteArrayOutputStream(6000);
                ObjectOutputStream oos = new ObjectOutputStream(bao);
                //String msg = "HoaDT-PC";
                oos.writeObject(c1);
                byte[] buf= bao.toByteArray();
                DatagramPacket pkg = new DatagramPacket(buf,buf.length,pk.getAddress(),pk.getPort());
                dsk.send(pkg);
            }
        }
    }

}
