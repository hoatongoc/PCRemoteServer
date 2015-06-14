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
		
		if(keyboardCommand !=null && robot!=null) {			int keyboardCode = keyboardCommand.getKeyboardCode();			if(keyboardCode<1) return null;
			else if (keyboardCode == KeyboardConstant.VK_ENTER || keyboardCode == KeyboardConstant.VK_TAB || keyboardCode == KeyboardConstant.VK_BACK_SPACE){
				doType(keyboardCode);
			}
			else if (keyboardCode <= KeyboardConstant.VK_ALT){
				if(keyboardCommand.getPress() == KeyboardConstant.PRESS) {
					robot.keyPress(keyboardCode);
					}
				else if(keyboardCommand.getPress() == KeyboardConstant.RELEASE) {
					robot.keyRelease(keyboardCode);
				}
			}
			else if(keyboardCode>KeyboardConstant.VK_PAUSE && keyboardCode<=KeyboardConstant.VK_QUOTE) {
				if(keyboardCommand.getPress() == KeyboardConstant.PRESS) {
					doType(keyboardCode);
					}
				else if(keyboardCommand.getPress() == KeyboardConstant.RELEASE) {
					robot.keyRelease(keyboardCode);
				}
			}
			else if(keyboardCode <= KeyboardConstant.CHAR_UNDEFINED) {
				typeSpecialChar(keyboardCode);
			}
			
		}
		
		return null;
	}
	
	private void doType(int... keyCodeToType) {
		if(keyCodeToType.length == 0) return;
		else {
			for(int i =0;i<keyCodeToType.length;i++) {
				robot.keyPress(keyCodeToType[i]);
			}
			for(int i =0;i<keyCodeToType.length;i++) {
				robot.keyRelease(keyCodeToType[i]);
			}
			
		}
		
	}
	private void doType(int keyCodeToType) {
		if(keyCodeToType<1) return;
		else {
			robot.keyPress(keyCodeToType);
			robot.keyRelease(keyCodeToType);
		}
	}
	
	private void typeSpecialChar(int keyCodeToType) {
		if(keyCodeToType<1) return;
		else {
			switch(keyCodeToType) {
			// "~" key
			case KeyboardConstant.VK_TILDE: doType(KeyboardConstant.VK_SHIFT, KeyboardConstant.VK_BACK_QUOTE); break;
			// "!" key
	        case KeyboardConstant.VK_EXCLAMATION_MARK: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_1); break;
	        // "@" key
	        case KeyboardConstant.VK_AT: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_2); break;
	        // "#" key
	        case KeyboardConstant.VK_NUMBER_SIGN: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_3); break;
	        // "$" key
	        case KeyboardConstant.VK_DOLLAR: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_4); break;
	        // "%" key
	        case KeyboardConstant.VK_PERCENT: doType(KeyboardConstant.VK_SHIFT, KeyboardConstant.VK_5); break;
	        // "^" key
	        case KeyboardConstant.VK_CIRCUMFLEX: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_6); break;
	        // "&" key
	        case KeyboardConstant.VK_AMPERSAND: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_7); break;
	        // "(" key
	        case KeyboardConstant.VK_LEFT_PARENTHESIS: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_9); break;
	        // ")" key
	        case KeyboardConstant.VK_RIGHT_PARENTHESIS: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_0); break;
	        // "_" key
	        case KeyboardConstant.VK_UNDERSCORE: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_MINUS); break;
	        // "+" key
	        case KeyboardConstant.VK_PLUS: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_EQUALS); break;
	        // "{" key
	        case KeyboardConstant.VK_BRACELEFT: doType(KeyboardConstant.VK_SHIFT, KeyboardConstant.VK_OPEN_BRACKET); break;
	        // "}" key
	        case KeyboardConstant.VK_BRACERIGHT: doType(KeyboardConstant.VK_SHIFT, KeyboardConstant.VK_CLOSE_BRACKET); break;
	        // "|" key
	        case KeyboardConstant.VK_VERTICAL_BAR: doType(KeyboardConstant.VK_SHIFT, KeyboardConstant.VK_BACK_SLASH); break;
	        // ":" key
	        case KeyboardConstant.VK_COLON: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_SEMICOLON); break;
	        // ":" key
	        case KeyboardConstant.VK_LESS: doType(KeyboardConstant.VK_SHIFT, KeyboardConstant.VK_COMMA); break;
	        // ">" key
	        case KeyboardConstant.VK_GREATER: doType(KeyboardConstant.VK_SHIFT, KeyboardConstant.VK_PERIOD); break;
	        // "?" key
	        case KeyboardConstant.VK_QUESTION: doType(KeyboardConstant.VK_SHIFT, KeyboardConstant.VK_SLASH); break;
	        // """ key
	        case KeyboardConstant.VK_DOUBLE_QUOTE: doType(KeyboardConstant.VK_SHIFT,KeyboardConstant.VK_QUOTE); break;
	        // "Windows" key
	        //WARNING: THIS IS FOR SHOW AND HIDE START MENU ONLY, CAN'T COMBINE WITH OTHER KEYS!
	        case KeyboardConstant.VK_WINDOWS: doType(KeyboardConstant.VK_CONTROL,KeyboardConstant.VK_ESCAPE); break;
			}
		}
	}

}
