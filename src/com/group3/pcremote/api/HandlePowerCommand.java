package com.group3.pcremote.api;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.group3.pcremote.constant.PowerConstant;
import com.group3.pcremote.model.PowerCommand;

public class HandlePowerCommand extends SwingWorker<String, String> {

	PowerCommand powerCommand = null;
	 
	public HandlePowerCommand() {
		// TODO Auto-generated constructor stub
	}
	public HandlePowerCommand(PowerCommand powerCommand) {
		super();
		this.powerCommand = powerCommand;
	}

	public PowerCommand getPowerCommand() {
		return powerCommand;
	}
	public void setPowerCommand(PowerCommand powerCommand) {
		this.powerCommand = powerCommand;
	}
	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		if(powerCommand!=null) {
			
				
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						doPowerOperation();
					}
				}, 5000);
				
				String message = "Windows will " + powerCommand.getPowerConstant() + " within 5 secs,Press OK to do the operation immediately, Cancel to cancel the operation";
				String title = powerCommand.getPowerConstant() + "Confirmation";
				int powerComfirm = JOptionPane.showConfirmDialog(null,message,title,JOptionPane.OK_CANCEL_OPTION);
				if(powerComfirm == JOptionPane.OK_OPTION) {
					timer.cancel();
					doPowerOperation();
				}
				else if (powerComfirm == JOptionPane.CANCEL_OPTION) {
					timer.cancel();
				}
		}
		
		return null;
	}

	
	
	private void doPowerOperation() {
		if(powerCommand.getPowerConstant() == PowerConstant.SHUTDOWN) {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("shutdown /s /f");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Operation Failed!");
				e.printStackTrace();
			}
			System.exit(0);
		}
		else if (powerCommand.getPowerConstant() == PowerConstant.SLEEP) {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("Rundll32.exe Powrprof.dll,SetSuspendState Sleep");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Operation Failed!");
				e.printStackTrace();
			}
			System.exit(0);
		}
		else if (powerCommand.getPowerConstant() == PowerConstant.RESTART) {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("shutdown -r -t 0 /f");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Operation Failed!");
				e.printStackTrace();
			}
			System.exit(0);
		}
		else if (powerCommand.getPowerConstant() == PowerConstant.HIBERNATE) {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("shutdown /h /f");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Operation Failed!");
				e.printStackTrace();
			}
			System.exit(0);
		}
		else if (powerCommand.getPowerConstant() == PowerConstant.LOG_OFF) {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("shutdown /l /f");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Operation Failed!");
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
}
