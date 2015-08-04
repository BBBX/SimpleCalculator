package com.inaer.calculator.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>CalculatorService</code>.
 */
public interface CalculatorServiceAsync {
	void convertToBinary(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
}
