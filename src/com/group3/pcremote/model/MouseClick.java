package com.group3.pcremote.model;

import com.group3.pcremote.constant.MouseConstants;

public class MouseClick {
	private int buttonIndex;
	private int press;
	public MouseClick() {
		// TODO Auto-generated constructor stub
		buttonIndex = 1;
		press = MouseConstants.CLICK;
	}   
	public int getPress() {
		return press;
	}

	public void setPress(int press) {
		this.press = press;
	}

	public int getButtonIndex() {
		return buttonIndex;
	}

	public void setButtonIndex(int buttonIndex) {
		this.buttonIndex = buttonIndex;
	}
	

}
