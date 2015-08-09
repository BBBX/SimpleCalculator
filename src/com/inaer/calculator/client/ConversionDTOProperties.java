package com.inaer.calculator.client;

import java.util.Date;

import com.inaer.calculator.shared.ConversionDTO;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ConversionDTOProperties extends PropertyAccess<ConversionDTO> {
	ValueProvider<ConversionDTO, String> key();
	
	ValueProvider<ConversionDTO, Date> timeOfConversion();

	ValueProvider<ConversionDTO, String> decimalNumber();
	
	ValueProvider<ConversionDTO, String> binaryNumber();
}
