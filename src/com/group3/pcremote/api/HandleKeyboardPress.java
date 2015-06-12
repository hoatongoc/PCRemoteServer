package com.group3.pcremote.api;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.SwingWorker;

import com.group3.pcremote.constant.KeyboardConstant;
import com.group3.pcremote.model.KeyboardCommand;


public class HandleKeyboardPress extends SwingWorker<String, String> {

	KeyboardCommand keyboardCommand;
	Robot robot;
	
	
	public HandleKeyboardPress(KeyboardCommand keyboardCommand, Robot robot) {
		super();
		this.keyboardCommand = keyboardCommand;
		this.robot = robot;
	}


	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		
		if(keyboardCommand !=null && robot!=null) {
			int keyboardCode = keyboardCommand.getKeyboardCode();
				if(keyboardCode<=KeyEvent.KEY_LAST && keyboardCode>-1) {
				if(keyboardCommand.getPress() == KeyboardConstant.PRESS) {
					robot.keyPress(keyboardCommand.getKeyboardCode());
					}
				else if(keyboardCommand.getPress() == KeyboardConstant.RELEASE) {
					robot.keyRelease(keyboardCommand.getKeyboardCode());
				}
			}
		}
		
		return null;
	}

}
