package com.group3.pcremote.model;

import com.group3.pcremote.constant.MouseConstants;

public class MouseClick {
	private int buttonNum;
	private int press;
	public MouseClick() {
		// TODO Auto-generated constructor stub
		buttonNum = 1;
		press = MouseConstants.CLICK;
	}   
	public int getPress() {
		return press;
	}

	public void setPress(int press) {
		this.press = press;
	}

	public int getButtonNum() {
		return buttonNum;
	}

	public void setButtonNum(int buttonNum) {
		this.buttonNum = buttonNum;
	}
	

}
