package com.inaer.calculator.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.inaer.calculator.shared.ConversionDTO;

/**
 * The async counterpart of <code>CalculatorService</code>.
 */
public interface CalculatorServiceAsync {
	void convertToBinary(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void retrieveAccesList(AsyncCallback<List<ConversionDTO>> callback);
}
