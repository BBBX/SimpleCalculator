package com.inaer.calculator.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.inaer.calculator.client.CalculatorService;
import com.inaer.calculator.server.dao.FactoryDAO;
import com.inaer.calculator.server.dao.IConversionDAO;
import com.inaer.calculator.server.domain.Conversion;
import com.inaer.calculator.shared.ConversionDTO;
import com.inaer.calculator.shared.FieldVerifier;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CalculatorServiceImpl extends RemoteServiceServlet implements CalculatorService {

	/**
	 * Convert the input number to its binary representation.
	 * 
	 * @return a string binary representation of the input number
	 */
	public String convertToBinary(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidNumber(input)) {
			return "NaN";
		}

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);

		// Convert the input number to its binary representation.
		Double inputNumber = Double.valueOf(input);
		String inputBinaryNumber = Long.toBinaryString(Double.doubleToRawLongBits(inputNumber));

		// Store the conversion
		storeConversionOperation(inputNumber, inputBinaryNumber);

		return inputBinaryNumber;
	}

	/**
	 * Store a new conversion operation.
	 * 
	 * @param inputNumber
	 *            the decimal number to convert
	 * @param inputBinaryNumber
	 *            the binary number result of the conversion
	 * 
	 */
	private void storeConversionOperation(Double inputNumber, String inputBinaryNumber) {
		IConversionDAO conv = FactoryDAO.getFactory(1).getConversionDAO();
		conv.addConversion(String.valueOf(inputNumber), inputBinaryNumber);
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	/**
	 * Retrieve the accesses to conversion operations as a list.
	 * 
	 * @return the list of conversion operations
	 */
	@Override
	public List<ConversionDTO> retrieveAccesList() {
		IConversionDAO convDAO = FactoryDAO.getFactory(1).getConversionDAO();
		List<Conversion> result = convDAO.listConversion();

		// Creating Transfer Data Objects
		List<ConversionDTO> resultDTO = new ArrayList<ConversionDTO>();
		for (int i = 0; i < result.size(); i++) {
			ConversionDTO con = new ConversionDTO();
			Date m = result.get(i).getTimeOfConversion();
			con.setKey(String.valueOf(result.get(i).getKey()));
			con.setTimeOfConversion(new Date(m.getTime()));
			con.setDecimalNumber(result.get(i).getDecimalNumber());
			con.setBinaryNumber(result.get(i).getBinaryNumber());
			resultDTO.add(con);
		}

		return resultDTO;
	}
}
