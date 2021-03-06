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
import com.group3.pcremote.model.ServerInfo;

//Handle a connection request of Android device
public class HandleConnectionRequest extends SwingWorker<String, String> {

	private MainForm mainForm;
	private DatagramSocket datagramSocket = null;
	private InetAddress offerAdress;
	private String offerName="";
	
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
			String message = "Device " + offerName + " wants to connect.\n Yes to accept, No to refuse the connect";
			String title = "Connect Confirmation";
			
			int connectConfirm = JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
			
			//If user choose yes option (accept connect) then update UI and send back the confirm packet to android decvice
			if(connectConfirm == JOptionPane.YES_OPTION) {
				mainForm.setDeviceConnected(true);
				JLabel offerAdressLabel = (JLabel) mainForm.getComponentByName("dAddrOutput");
				JLabel offerNameLabel = (JLabel) mainForm.getComponentByName("dNameOutput");
	            mainForm.setConnectedDeviceAdress(offerAdress.getHostAddress());
	            mainForm.setConnectedDeviceName(offerName);
				offerAdressLabel.setText(mainForm.getConnectedDeviceAdress());
	            offerNameLabel.setText(mainForm.getConnectedDeviceName());
	            
	            //Send confirm packet back to Android device
	            SenderData confirmData = new SenderData();
	            confirmData.setCommand(SocketConstant.CONNECT_ACCEPT);
	            ServerInfo serverInfo = new ServerInfo();
	            serverInfo.setServerName(InetAddress.getLocalHost().getHostName());
	            confirmData.setData(serverInfo);
	            try {
					SendDatagramObject.send(datagramSocket,confirmData, InetAddress.getByName(mainForm.getConnectedDeviceAdress()), SocketConstant.PORT);
					System.out.println("Sent back to " + offerAdress.getHostAddress());
					timer.cancel();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
	            
	            mainForm.setConnectionAlive(true); //connected, set connection state to alive
	            
	            // create new and run timer to check the connection is alive or not
	            mainForm.getTimerConnectionAlive().cancel();
	            mainForm.setTimerConnectionAlive(new Timer());
	            mainForm.getTimerConnectionAlive().scheduleAtFixedRate(new TimerTask() {
					
					@Override
					public void run() {
						int count = mainForm.getCountAlive();
						mainForm.setCountAlive(count+1);
						SenderData mainTainConnectionData = new SenderData();
						mainTainConnectionData.setCommand(SocketConstant.MAINTAIN_CONNECTION);
						if(mainForm !=null) {
							if(mainForm.getCountAlive() >= 5){ // if count alive >=5 -> disconnect
								HandleDisconnectRequest.clearConnectedDevice(mainForm);
							}
							else {
								try { //if still connecting, send maintain connection command
									SendDatagramObject.send(datagramSocket, mainTainConnectionData,
											InetAddress.getByName(mainForm.getConnectedDeviceAdress()), SocketConstant.PORT);
									System.out.println("Sent maintain connect packet " + mainForm.getCountAlive() + " to " + mainForm.getConnectedDeviceAdress());
								} catch (Exception e) {
								}
							}
						}
						
 						
					}
				}, 1000, 1000);
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
