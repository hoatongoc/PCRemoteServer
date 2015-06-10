package com.group3.pcremote.api;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.company.MainForm;
import com.group3.pcremote.constant.SocketConstant;
import com.group3.pcremote.model.SenderData;

//Handle a connection request of Android device
public class HandleConnectionRequest extends SwingWorker<String, String> {

	MainForm mainForm;
	DatagramSocket datagramSocket = null;
	InetAddress offerAdress;
	String offerName="";
	
	
	
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}
	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}
	
	public MainForm getMainForm() {
		return mainForm;
	}
	public void setMainForm(MainForm mainForm) {
		this.mainForm = mainForm;
	}
	public InetAddress getOfferAdress() {
		return offerAdress;
	}
	public void setOfferAdress(InetAddress offerAdress) {
		this.offerAdress = offerAdress;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	
	public HandleConnectionRequest(MainForm mainForm,
			DatagramSocket datagramSocket, InetAddress offerAdress, String offerName) {
		super();
		this.mainForm = mainForm;
		this.datagramSocket = datagramSocket;
		this.offerAdress = offerAdress;
		this.offerName = offerName;
	}
	@Override
	protected String doInBackground() throws Exception {
		//create new thread for not stopping receiving broadcast packets from android devices
		//if timeout after 4 secs, dismiss the confirm dialog
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
			String message = "Device blah blah " + offerName + " want to connect. Confirm?\n Yes to accept, No to refuse the connect";
			String title = "Connect Confirmation";
			
			int connectConfirm = JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
			
			//If user choose yes option (accept connect) then update UI and send back the confirm packet to android decvice
			if(connectConfirm == JOptionPane.YES_OPTION) {
				//mainForm.setDeviceConnected(true);
				JLabel offerAdressLabel = (JLabel) mainForm.getComponentByName("dAddrOutput");
				JLabel offerNameLabel = (JLabel) mainForm.getComponentByName("dNameOutput");
	            mainForm.setConnectedDeviceAdress(offerAdress.getHostAddress());
	            mainForm.setConnectedDeviceName(offerName);
				offerAdressLabel.setText(mainForm.getConnectedDeviceAdress());
	            offerNameLabel.setText(mainForm.getConnectedDeviceName());
	            
	            //Send confirm packet back to Android device
	            SenderData confirmData = new SenderData();
	            confirmData.setCommand(SocketConstant.CONNECT_ACCEPT);
	            try {
					SendDatagramObject.send(datagramSocket,confirmData, InetAddress.getByName(mainForm.getConnectedDeviceAdress()), SocketConstant.PORT);
					System.out.println("Sent back to " + offerAdress.getHostAddress());
					timer.cancel();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//else send back to android device the refuse connection packet
			else if(connectConfirm == JOptionPane.NO_OPTION) {
				 SenderData confirmData = new SenderData();
		            confirmData.setCommand(SocketConstant.CONNECT_REFUSE);
		            SendDatagramObject.send(datagramSocket,confirmData, offerAdress, SocketConstant.PORT);
		            System.out.println("Sent back to " + offerAdress.getHostAddress());
					timer.cancel();
			}
		return null;
	}
	
}
