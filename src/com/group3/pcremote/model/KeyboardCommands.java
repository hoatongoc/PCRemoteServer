package com.group3.pcremote.model;

public class KeyboardCommands {
	private int keyboardCode;
	private int press;
	public int getPress() {
		return press;
	}
	public void setPress(int press) {
		this.press = press;
	}
	public int getKeyboardCode() {
		return keyboardCode;
	}
	public void setKeyboardCode(int keyboardCode) {
		//keyboard code meaning in KeyboardConstants.java file.
		this.keyboardCode = keyboardCode; 
	}
	

}
