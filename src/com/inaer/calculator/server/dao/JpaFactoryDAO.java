package com.inaer.calculator.server.dao;

public class JpaFactoryDAO extends FactoryDAO {

	@Override
	public IConversionDAO getConversionDAO() {
		return new ConversionJpaDAO();
	}
}
