package com.group3.pcremote.api;

import javax.swing.SwingWorker;

import com.company.MainForm;

public class CheckConnectionAlive extends SwingWorker<String, String> {

	private MainForm mainForm = null;
			
	public CheckConnectionAlive(MainForm mainForm) {
		super();
		this.mainForm = mainForm;
	}

	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		while(!isCancelled()){
			if(mainForm !=null) {
				if(mainForm.getCountAlive() >= 5)
					System.out.println("Disconnected");
					HandleDisconnectRequest.clearConnectedDevice(mainForm);
			}
		}
		return null;
	}

	public MainForm getMainForm() {
		return mainForm;
	}

	public void setMainForm(MainForm mainForm) {
		this.mainForm = mainForm;
	}

}
