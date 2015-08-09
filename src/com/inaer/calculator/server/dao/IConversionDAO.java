package com.inaer.calculator.server.dao;

import java.util.List;

import com.inaer.calculator.server.domain.Conversion;

public interface IConversionDAO {
	void addConversion(String num, String binaryNum);

	List<Conversion> listConversion();
}
