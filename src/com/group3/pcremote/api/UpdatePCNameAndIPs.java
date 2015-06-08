package com.group3.pcremote.api;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.company.MainForm;

public class UpdatePCNameAndIPs extends SwingWorker<String, String> {

	private MainForm form;
	
	public UpdatePCNameAndIPs(MainForm form) {
		super();
		this.form = form;
	}
	
	public MainForm getForm() {
		return form;
	}
	public void setForm(MainForm form) {
		this.form = form;
	}

	@Override
	protected String doInBackground() throws Exception {		
		InetAddress addr = null;
        InetAddress[] addrs = null;
        @SuppressWarnings("unchecked")
		JComboBox<String> cAddrOutput = (JComboBox<String>) form.getComponentByName("cAddrOutput");
    	cAddrOutput.removeAllItems();
        try {
        	//get all network address of this PC, include IP and MAC address
            addr = InetAddress.getLocalHost();
            addrs = InetAddress.getAllByName(addr.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (addr != null) {
        	//assign host name to cNameOutput label
        	JLabel cNameOutput = (JLabel) form.getComponentByName("cNameOutput");
            cNameOutput.setText(addr.getHostName());
        }
        if (addrs != null) {
        	//filter IP address out of ddress list then add to cAddrOutput combo box
            for (InetAddress addr1 : addrs) {
                if (addr1 instanceof Inet4Address) {
                    cAddrOutput.addItem(addr1.getHostAddress());
                }
            }
        }
		
		return null;
	}

}
