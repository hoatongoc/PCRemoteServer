package com.group3.pcremote.api;

import java.awt.Robot;
import java.awt.event.InputEvent;

import javax.swing.SwingWorker;

import com.group3.pcremote.constant.MouseConstant;
import com.group3.pcremote.model.MouseClick;

public class HandleMouseClick extends SwingWorker<String, String> {
	MouseClick mouseClick;
	Robot robot;
	public MouseClick getMouseClick() {
		return mouseClick;
	}
	public void setMouseClick(MouseClick mouseClick) {
		this.mouseClick = mouseClick;
	}
	public Robot getRobot() {
		return robot;
	}
	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	public HandleMouseClick(MouseClick mouseClick, Robot robot) {
		super();
		this.mouseClick = mouseClick;
		this.robot = robot;
	}
	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		
		int btnNum = (mouseClick.getButtonIndex());
		//if the command is press the mouse button
		if(mouseClick.getPress() == MouseConstant.PRESS) {
			if(btnNum == MouseConstant.LEFT_MOUSE) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
			}
			else if(btnNum == MouseConstant.MIDDLE_MOUSE) {
				robot.mousePress(InputEvent.BUTTON2_MASK);
			}
			else if(btnNum == MouseConstant.RIGHT_MOUSE) {
				robot.mousePress(InputEvent.BUTTON3_MASK);
			}
		}
		
		//if the command is mouse click
		else if (mouseClick.getPress() == MouseConstant.CLICK) {
			
			if(btnNum == MouseConstant.LEFT_MOUSE) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			else if(btnNum == MouseConstant.MIDDLE_MOUSE) {
				robot.mousePress(InputEvent.BUTTON2_MASK);
				robot.mouseRelease(InputEvent.BUTTON2_MASK);
			}
			else if(btnNum == MouseConstant.RIGHT_MOUSE) {
				robot.mousePress(InputEvent.BUTTON3_MASK);
				robot.mouseRelease(InputEvent.BUTTON3_MASK);
			}
		}
		
		//if the command is release mouse button
		else if(mouseClick.getPress() == MouseConstant.RELEASE) {
			if(btnNum == 1) {
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			else if(btnNum == 2) {
				robot.mouseRelease(InputEvent.BUTTON2_MASK);
			}
			else if(btnNum == 3) {
				robot.mouseRelease(InputEvent.BUTTON3_MASK);
			}
		}
		
		return null;
	}
	

}
