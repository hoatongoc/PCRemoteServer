package com.group3.pcremote.api;

import java.awt.Robot;

import javax.swing.SwingWorker;

import com.group3.pcremote.model.MouseScroll;

public class HandleMouseScroll extends SwingWorker<String, String> {

	MouseScroll mouseScroll = null;
	Robot robot = null;
	public HandleMouseScroll(MouseScroll mouseScroll, Robot robot) {
	// TODO Auto-generated constructor stub
		this.mouseScroll = mouseScroll;
		this.robot = robot;
	}
	
	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		if(mouseScroll!=null && robot!=null) {
			robot.mouseWheel(mouseScroll.getScrollValue());
			System.out.println("Mouse scrolled" + mouseScroll.getScrollValue());
		}
		return null;
	}
}
