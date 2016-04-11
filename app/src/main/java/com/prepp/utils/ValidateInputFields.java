package com.prepp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidateInputFields {
	
	private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");


	public static boolean isEmailValid(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public static boolean isNumeric(String field) {
		String expression = "^[0-9]";
		Pattern p = Pattern.compile(expression);
		Matcher m = p.matcher(field);
		if (m.matches()/* && !(field.trim().length() <5) */) {
			return true;
		}
		return false;
	}

	public static boolean isMobileNumberValid(String mobileNum) {
		String expression = "^[987]\\d{9}$";
		Pattern p = Pattern.compile(expression); // pattern=/^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;

		Matcher m = p.matcher(mobileNum);
		if (m.matches() && mobileNum.trim().length() > 0) {
			return true;
		}

		return false;
	}

	public static boolean isNameValid(String name) {
		
		if (name.trim().length() > 1) {
			return true;
		}
		return false;
	}

	public static boolean isFieldEmpty(String field) {
		if (field.trim().length() > 0) {
			return true;
		}
		return false;
	}

}
