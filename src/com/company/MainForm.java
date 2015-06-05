package com.company;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Robot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import java.awt.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import com.group3.pcremote.constant.SocketConstant;
import com.group3.pcremote.model.MouseClick;
import com.group3.pcremote.model.SenderData;
import com.group3.pcremote.model.ServerInfo;
import com.sun.glass.events.MouseEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;


public class MainForm extends JFrame {

	private JPanel mainPanel;
	private HashMap<String, Component> componentMap;
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
					final MainForm frame = new MainForm();
					frame.setVisible(true);
					update_PC_Device uDP = new update_PC_Device(frame);
					Thread t = new Thread(uDP);
					t.start();
					Thread t1 = new Thread(new ReceivePacketAndProcess(frame));
					t1.start();
					
				} catch (Exception e) {
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
		
		JLabel dAddrOutput = new JLabel("New label");
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

    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return componentMap.get(name);
        }
        else return null;
    }

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
    
    private static void updatePCNameAndAddr(MainForm f) {
    	InetAddress addr = null;
        InetAddress[] addrs = null;
        @SuppressWarnings("unchecked")
		JComboBox<String> cAddrOutput = (JComboBox<String>) f.getComponentByName("cAddrOutput");
    	cAddrOutput.removeAllItems();
        try {
            addr = InetAddress.getLocalHost();
            addrs = InetAddress.getAllByName(addr.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (addr != null) {
        	JLabel cNameOutput = (JLabel) f.getComponentByName("cNameOutput");
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
    
    private static class update_PC_Device implements Runnable {
    	
    	private MainForm f;
    	
    	public update_PC_Device(MainForm form) {
			// TODO Auto-generated constructor stub
    		f=form;
    		
		}
    	public MainForm getF() {
			return f;
		}
		public void setF(MainForm f) {
			this.f = f;
		}
		public void run() {
    		// TODO Auto-generated method stub
    		updatePCNameAndAddr(f);
    		
    	}
    	
    	
    };
    
    private static class ReceivePacketAndProcess implements Runnable  {

    	MainForm f;
    	
    	public ReceivePacketAndProcess(MainForm mform) {
			// TODO Auto-generated constructor stub
    		f = mform;
		}
		public void run() {
			Robot r = null;
			DatagramSocket dsk= null;
			try {
				r = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				dsk= new DatagramSocket(1234);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        byte[] buffer= new byte[6000];
	        DatagramPacket pk= new DatagramPacket(buffer, buffer.length);
			// TODO Auto-generated method stub
			while (true)
			{
		            try {
						dsk.receive(pk);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            System.out.println("Client: " + pk.getAddress() + ":" + pk.getPort());
		            JLabel addrAddrOutput = (JLabel) f.getComponentByName("dAddrOutput");
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
						if(data.getCommand().equals(SocketConstant.SERVER_INFO)) {
			                ServerInfo obj = new ServerInfo();
			                obj.setServerName(InetAddress.getLocalHost().getHostName());
			                obj.setServerIP(InetAddress.getLocalHost().toString());
			                data.setData(obj);
			                ByteArrayOutputStream bao = new ByteArrayOutputStream(6000);
			                ObjectOutputStream oos = new ObjectOutputStream(bao);
			                oos.writeObject(data);
			                byte[] buf= bao.toByteArray();
			                DatagramPacket pkg = new DatagramPacket(buf,buf.length,pk.getAddress(),pk.getPort());
			                dsk.send(pkg);
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
			
		}
    	
    }
}


