package com.inaer.calculator.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.inaer.calculator.shared.ConversionDTO;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("calc")
public interface CalculatorService extends RemoteService {
	String convertToBinary(String value) throws IllegalArgumentException;

	List<ConversionDTO> retrieveAccesList();
}