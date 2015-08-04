package com.inaer.calculator.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.inaer.calculator.client.CalculatorService;
import com.inaer.calculator.shared.FieldVerifier;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CalculatorServiceImpl extends RemoteServiceServlet implements CalculatorService {

	/**
	 * Convert the input number to its binary representation.
	 * @return a string binary representation of the input number 
	 */
	public String convertToBinary(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidNumber(input)) {			
			return "NaN";
		}

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);		
		
		// Convert the input number to its binary representation.
		Double inputNumber = Double.valueOf(input);
		String inputBinaryNumber = Long.toBinaryString(Double.doubleToRawLongBits(inputNumber));
		
		return inputBinaryNumber;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
