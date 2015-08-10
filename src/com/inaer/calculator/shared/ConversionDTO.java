package com.inaer.calculator.shared;

import java.io.Serializable;
import java.util.Date;

public class ConversionDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String key;
	private Date timeOfConversion;
	private String decimalNumber;
	private String binaryNumber;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the timeOfConversion
	 */
	public Date getTimeOfConversion() {
		return timeOfConversion;
	}

	/**
	 * @param timeOfConversion
	 *            the timeOfConversion to set
	 */
	public void setTimeOfConversion(Date timeOfConversion) {
		this.timeOfConversion = timeOfConversion;
	}

	/**
	 * @return the decimalNumber
	 */
	public String getDecimalNumber() {
		return decimalNumber;
	}

	/**
	 * @param decimalNumber
	 *            the decimalNumber to set
	 */
	public void setDecimalNumber(String decimalNumber) {
		this.decimalNumber = decimalNumber;
	}

	/**
	 * @return the binaryNumber
	 */
	public String getBinaryNumber() {
		return binaryNumber;
	}

	/**
	 * @param binaryNumber
	 *            the binaryNumber to set
	 */
	public void setBinaryNumber(String binaryNumber) {
		this.binaryNumber = binaryNumber;
	}
}
