package com.inaer.calculator.shared;

/**
 * FieldVerifier validates that the number the user enters is correct number.
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified number is a real number.
	 * 
	 * @param value
	 *            the value to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidNumber(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}
