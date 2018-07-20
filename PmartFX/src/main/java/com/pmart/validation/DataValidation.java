package com.pmart.validation;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class DataValidation {
	
	public static boolean isTextFieldNotEmpty(TextField tf) {
		
		boolean b = false;
		
		if(tf.getText().length() != 0 || !tf.getText().isEmpty()) {
			b = true;
		}
			
		return b;
	}
	
	public static boolean isTextFieldNotEmpty(TextField tf, Label lb, String errorMessage) {
		
		boolean b = true;
		String msg = null;
		tf.getStyleClass().remove("error");
		
		if(!isTextFieldNotEmpty(tf)) {
			b = false;
			msg = errorMessage;
			tf.getStyleClass().add("error");
		}
		lb.setText(msg);
			
		return b;
	}
	
	
	
	public static boolean isTextFieldTypeNumber(TextField tf) {
		
		boolean b = false;
		
		if(tf.getText().matches("([0-9]+(\\.[0-9]+)?)+")) {
			b = true;
		}
			
		return b;
	}
	
	public static boolean isTextFieldTypeNumber(TextField tf, Label lb, String errorMessage) {
		
		boolean b = true;
		String msg = null;
		tf.getStyleClass().remove("error");
		
		if(!isTextFieldTypeNumber(tf)) {
			b = false;
			msg = errorMessage;
			tf.getStyleClass().add("error");
		}
		lb.setText(msg);	
		return b;
	}
	
	
	public static boolean isValidEmail(TextField tf) {
		
		boolean b = false;
		String pattern = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		
		if(tf.getText().matches(pattern)) {
			b = true;
			
		}
		return b;
	}
	
	
	public static boolean isValidEmail(TextField tf, Label lb, String errorMessage) {
		
		boolean b = true;
		String msg = null;
		
		tf.getStyleClass().remove("error");
		
		if(!isValidEmail(tf)) {
			
			b = false;
			msg = errorMessage;
			tf.getStyleClass().add("error");
			
		}
		lb.setText(msg);
		return b;
		
	}
	
	
	public static boolean isPasswordMatched(PasswordField pf1, PasswordField pf2) {
		
		boolean b = false;
		
		if(pf1.getText().equals(pf2.getText())) {
			b = true;
			
		}
		return b;
	}
	
	
	public static boolean isPasswordMatched(PasswordField pf1, PasswordField pf2, Label lb, String errorMessage) {
		
		boolean b = true;
		String msg = null;
		
		pf2.getStyleClass().remove("error");
		
		if(!isPasswordMatched(pf1,pf2)) {
			
			b = false;
			msg = errorMessage;
			pf2.getStyleClass().add("error");
			
		}
		lb.setText(msg);
		return b;
		
	}
	
	
	
	

}
