package com.inaer.calculator.server.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Conversion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	private Date timeOfConversion;
	private String decimalNumber;
	private String binaryNumber;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Date getTimeOfConversion() {
		return timeOfConversion;
	}

	public void setTimeOfConversion(Date timeOfConversion) {
		this.timeOfConversion = timeOfConversion;
	}

	public String getDecimalNumber() {
		return decimalNumber;
	}

	public void setDecimalNumber(String decimalNumber) {
		this.decimalNumber = decimalNumber;
	}

	public String getBinaryNumber() {
		return binaryNumber;
	}

	public void setBinaryNumber(String binaryNumber) {
		this.binaryNumber = binaryNumber;
	}

}
