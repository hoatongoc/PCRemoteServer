package com.company;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import java.awt.GridLayout;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import com.group3.pcremote.api.ReceivePacketAndProcess;
import com.group3.pcremote.api.UpdatePCNameAndIPs;
import com.group3.pcremote.constant.SocketConstant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@SuppressWarnings("serial")
public class MainForm extends JFrame {

	private String connectedDeviceAdress = "";
	private String connectedDeviceName = "";
	private boolean deviceConnected = false;
	private JPanel mainPanel;
	//Hash map stores Components of MainForm frame
	private HashMap<String, Component> componentMap;
	
	//DatagramSocket for send and receive packets
	private static DatagramSocket dSocket = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dSocket = new DatagramSocket(SocketConstant.PORT);
					final MainForm frame = new MainForm();
					frame.setVisible(true);
					
					// get PC name and IPs
					UpdatePCNameAndIPs updatePCNameAndIPs = new UpdatePCNameAndIPs(frame);
					updatePCNameAndIPs.execute();
					
					//Begin receive and handle connections
					ReceivePacketAndProcess receiveAndProcess = new ReceivePacketAndProcess(frame,dSocket);
					receiveAndProcess.execute();
					
					
				} catch (Exception e) {
					System.out.print(e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	/**
	 * Create the frame.
	 */
	public MainForm() {
		setTitle("PCRemote");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 432, 237);
		mainPanel = new JPanel();
		mainPanel.setName("mainPanel");
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPanel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setName("tabbedPane");
		
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel statusTab = new JPanel();
		statusTab.setName("statusTab");
		tabbedPane.addTab("Status", null, statusTab, null);
		statusTab.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		statusTab.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel cName = new JLabel("Computer Name:");
		cName.setName("cName");
		cName.setBounds(10, 11, 81, 14);
		panel_3.add(cName);
		
		JLabel cNameOutput = new JLabel("Computer Name Here");
		cNameOutput.setName("cNameOutput");
		cNameOutput.setBounds(131, 11, 140, 14);
		panel_3.add(cNameOutput);
		
		JLabel cAddrs = new JLabel("IP Addresses:");
		cAddrs.setName("cAddrs");
		cAddrs.setBounds(10, 39, 81, 14);
		panel_3.add(cAddrs);
		
		JComboBox<String> cAddrOutput = new JComboBox<String>();
		cAddrOutput.setName("cAddrOutput");
		cAddrOutput.setBounds(131, 36, 140, 20);
		panel_3.add(cAddrOutput);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				InetAddress addr = null;
		        InetAddress[] addrs = null;
		        @SuppressWarnings("unchecked")
				JComboBox<String> cAddrOutput = (JComboBox<String>) getComponentByName("cAddrOutput");
		    	cAddrOutput.removeAllItems();
		        try {
		            addr = InetAddress.getLocalHost();
		            addrs = InetAddress.getAllByName(addr.getHostName());
		        } catch (UnknownHostException e) {
		            e.printStackTrace();
		        }
		        if (addr != null) {
		        	JLabel cNameOutput = (JLabel) getComponentByName("cNameOutput");
		            cNameOutput.setText(addr.getHostName());
		        }
		        if (addrs != null) {
		            for (InetAddress addr1 : addrs) {
		                if (addr1 instanceof Inet4Address) {
		                    cAddrOutput.addItem(addr1.getHostAddress());
		                }
		            }
		        }
			}
		});
	
		btnRefresh.setBounds(281, 35, 71, 23);
		panel_3.add(btnRefresh);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		statusTab.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel dName = new JLabel("Device Name:");
		dName.setName("dName");
		dName.setBounds(10, 7, 91, 14);
		panel_4.add(dName);
		
		JLabel dNameOutput = new JLabel("Device Name Here");
		dNameOutput.setName("dNameOutput");
		dNameOutput.setBounds(133, 7, 157, 14);
		panel_4.add(dNameOutput);
		
		JLabel dAddr = new JLabel("Device Address:");
		dAddr.setName("dAddr");
		dAddr.setBounds(10, 48, 91, 14);
		panel_4.add(dAddr);
		
		JLabel dAddrOutput = new JLabel("Device Address Here");
		dAddrOutput.setName("dAddrOutput");
		dAddrOutput.setBounds(133, 48, 145, 14);
		panel_4.add(dAddrOutput);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Settings", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("About", null, panel_2, null);
		
		createComponentMap();
	}

	private void createComponentMap() {
        componentMap = new HashMap<String,Component>();
        List <Component> componentList = getAllComponents(getContentPane());
        Component[] components= componentList.toArray(new Component[componentList.size()]);
        for (Component component : components) {
            componentMap.put(component.getName(), component);
        }
    }

	//function that helps us get a Component of frame by its name
    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return componentMap.get(name);
        }
        else return null;
    }

    //Get all components of frame
    public List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
    }



	public String getConnectedDeviceAdress() {
		return connectedDeviceAdress;
	}



	public void setConnectedDeviceAdress(String connectedDeviceAdress) {
		this.connectedDeviceAdress = connectedDeviceAdress;
	}



	public String getConnectedDeviceName() {
		return connectedDeviceName;
	}



	public void setConnectedDeviceName(String connectedDeviceName) {
		this.connectedDeviceName = connectedDeviceName;
	}



	public boolean isDeviceConnected() {
		return deviceConnected;
	}



	public void setDeviceConnected(boolean deviceConnected) {
		this.deviceConnected = deviceConnected;
	}
}


