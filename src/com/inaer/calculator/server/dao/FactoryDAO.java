package com.inaer.calculator.server.dao;

public abstract class FactoryDAO {

	public static final int JPA = 1;

	/**
	 * Return an interface implemented by the Conversion object.
	 * 
	 * @return the interface implemented by the Conversion object
	 */
	public abstract IConversionDAO getConversionDAO();

	/**
	 * Return a factory DAO used to build DAO objects with different
	 * implementations.
	 * 
	 * @param keyFactory
	 *            the type of the factory DAO
	 * @return a factory DAO
	 */
	public static FactoryDAO getFactory(int keyFactory) {
		switch (keyFactory) {
		case JPA:
			return new JpaFactoryDAO();
		default:
			throw new IllegalArgumentException();
		}
	}
}
