package com.company;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

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
import java.util.Timer;

import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import com.group3.pcremote.api.ReceivePacketAndProcess;
import com.group3.pcremote.api.UpdatePCNameAndIPs;
import com.group3.pcremote.constant.CommonConstant;
import com.group3.pcremote.constant.SocketConstant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextArea;


public class MainForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String connectedDeviceAdress = "";
	private String connectedDeviceName = "";
	private boolean deviceConnected = false;
	private static Timer timerConnectionAlive = null;
	private int countAlive = 0;
	/* if connection between android device and
	 * PC is alive, it's true, otherwise false,
	 * delete all data of connected android device 
	 * */
	private boolean connectionAlive = false;  
	static MainForm frame=null;
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
					timerConnectionAlive = new Timer();
					
					frame = new MainForm();
					frame.setResizable(false);
					frame.setVisible(false);
					frame.setIconImage(createImage("/images/trayicon.png", "tray icon"));
					// get PC name and IPs
					UpdatePCNameAndIPs updatePCNameAndIPs = new UpdatePCNameAndIPs(frame);
					updatePCNameAndIPs.execute();
					
					//Begin receive and handle connections
					ReceivePacketAndProcess receiveAndProcess = new ReceivePacketAndProcess(frame,dSocket);
					receiveAndProcess.execute();
					
					
					frame.addWindowListener(new WindowAdapter(){
				          public void windowIconified(WindowEvent e){
				                frame.setVisible(false);
				          }
				    });
					//create system tray icon
					CreateSystemTrayIcon();
					
					
					
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
		setBounds(100, 100, 498, 237);
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
		
		JLabel cNameOutput = new JLabel(CommonConstant.NOT_AVAIABLE);
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
				
//				SenderData mainTainConnectionData = new SenderData();
//				mainTainConnectionData.setCommand(SocketConstant.MAINTAIN_CONNECTION);
//				try { //if still connecting, send maintain connection command
//					SendDatagramObject.send(dSocket, mainTainConnectionData,
//							InetAddress.getByName(getConnectedDeviceAdress()), SocketConstant.PORT);
//					System.out.println("Sent maintain connect packet MainForm " + getCountAlive() + " to " + getConnectedDeviceAdress());
//				} catch (Exception e) {
//				}
//				
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
		
		JLabel dNameOutput = new JLabel(CommonConstant.NOT_AVAIABLE);
		dNameOutput.setName("dNameOutput");
		dNameOutput.setBounds(133, 7, 157, 14);
		panel_4.add(dNameOutput);
		
		JLabel dAddr = new JLabel("Device Address:");
		dAddr.setName("dAddr");
		dAddr.setBounds(10, 48, 91, 14);
		panel_4.add(dAddr);
		
		JLabel dAddrOutput = new JLabel(CommonConstant.NOT_AVAIABLE);
		dAddrOutput.setName("dAddrOutput");
		dAddrOutput.setBounds(133, 48, 145, 14);
		panel_4.add(dAddrOutput);
		
		JPanel aboutTab = new JPanel();
		tabbedPane.addTab("About", null, aboutTab, null);
		aboutTab.setLayout(new BorderLayout(0, 0));
		
		JTextArea txtrAsdkjashdkjAsdahsdkjahsd = new JTextArea();
		txtrAsdkjashdkjAsdahsdkjahsd.setLineWrap(true);
		txtrAsdkjashdkjAsdahsdkjahsd.setText("PC Remote Control\r\nVersion 1.0\r\nCopyright © 2015-2016 UIT Group\r\nAll rights reserved\r\nThis app helps you to control your PC remotely and easily\r\nContact us: hoatongoc@gmail.com");
		txtrAsdkjashdkjAsdahsdkjahsd.setEditable(false);
		aboutTab.add(txtrAsdkjashdkjAsdahsdkjahsd, BorderLayout.CENTER);
		
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

    public static void CreateSystemTrayIcon() {
    	if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
    	final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("/images/trayicon.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();
        // Create a popup menu components
        MenuItem displayItem = new MenuItem("Show");
        MenuItem exitItem = new MenuItem("Exit");
        
        popup.add(displayItem);
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
        displayItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.setVisible(true);
			}
		});
        trayIcon.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.setVisible(true);
			}
		});
        
    }
    
    
    protected static Image createImage(String path, String description) {
        URL imageURL = MainForm.class.getResource(path);
         
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
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
	
	public static DatagramSocket getdSocket() {
		return dSocket;
	}

	public boolean isConnectionAlive() {
		return connectionAlive;
	}

	public void setConnectionAlive(boolean connectionAlive) {
		this.connectionAlive = connectionAlive;
		if(this.connectionAlive == false) countAlive = 0;
	}

	public Timer getTimerConnectionAlive() {
		return timerConnectionAlive;
	}

	public void setTimerConnectionAlive(Timer timerConnectionAlive) {
		this.timerConnectionAlive = timerConnectionAlive;
	}

	public int getCountAlive() {
		return countAlive;
	}

	public void setCountAlive(int countAlive) {
		this.countAlive = countAlive;
	}

}


