package com.group3.pcremote.model;

public class MouseClick {
	private int buttonNum;
	private boolean press;
	public MouseClick() {
		// TODO Auto-generated constructor stub
		buttonNum = 1;
		press = false;
	}   
	public boolean isPress() {
		return press;
	}

	public void setPress(boolean press) {
		this.press = press;
	}

	public int getButtonNum() {
		return buttonNum;
	}

	public void setButtonNum(int buttonNum) {
		this.buttonNum = buttonNum;
	}
	

}
