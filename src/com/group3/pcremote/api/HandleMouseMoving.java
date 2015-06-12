package com.group3.pcremote.api;

import java.awt.Point;
import java.awt.Robot;

import javax.swing.SwingWorker;

import com.group3.pcremote.model.Coordinates;

public class HandleMouseMoving extends SwingWorker<String,String> {

	Robot robot = null;
	Coordinates mousePosition = null;
	
	public HandleMouseMoving(Robot robot, Coordinates mousePosition) {
		super();
		this.robot = robot;
		this.mousePosition = mousePosition;
	}

	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		if(mousePosition!=null && robot!=null) {
			robot.mouseMove(mousePosition.getX(), mousePosition.getY());
		}
		return null;
	}

}
