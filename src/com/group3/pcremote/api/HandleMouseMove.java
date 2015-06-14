package com.group3.pcremote.api;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import javax.swing.SwingWorker;

import com.group3.pcremote.model.Coordinates;

public class HandleMouseMove extends SwingWorker<String,String> {

	Robot robot = null;
	Coordinates mousePosition = null;
	
	public HandleMouseMove(Robot robot, Coordinates mousePosition) {
		super();
		this.robot = robot;
		this.mousePosition = mousePosition;
	}

	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		Coordinates curMousePosition = new Coordinates();
		curMousePosition.setX((int)MouseInfo.getPointerInfo().getLocation().getX());
		curMousePosition.setY((int)MouseInfo.getPointerInfo().getLocation().getY());
		
		Coordinates newMousePosition = new Coordinates();
		int newMousePosX = (mousePosition.getX() + curMousePosition.getX())>0?(mousePosition.getX() + curMousePosition.getX()):0;
		int newMousePosY = (mousePosition.getY() + curMousePosition.getY())>0?(mousePosition.getY() + curMousePosition.getY()):0;
		
		newMousePosition.setX(newMousePosX);
		newMousePosition.setY(newMousePosY);
		if(mousePosition!=null && robot!=null) {
			robot.mouseMove(newMousePosition.getX(), newMousePosition.getY());
		}
		System.out.print("Before Mouse Pos: " + curMousePosition.getX() + " " + curMousePosition.getY()+"\n");
		System.out.print("Delta Mouse Pos: " + mousePosition.getX() + " " + mousePosition.getY()+"\n");
		System.out.print("Current Mouse Pos: " + newMousePosX + " " + newMousePosY+"\n");
		return null;
	}

}
