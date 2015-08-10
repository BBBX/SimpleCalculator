package com.inaer.calculator.server.dao;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.inaer.calculator.server.EMF;
import com.inaer.calculator.server.domain.Conversion;

public class ConversionJpaDAO implements IConversionDAO {

	/**
	 * Convert a decimal number to its binary representation.
	 * 
	 * @param num
	 *            decimal number to convert
	 * @param binaryNum
	 *            binary representation of the decimal number
	 */
	@Override
	public void addConversion(String num, String binaryNum) {

		Calendar cal = Calendar.getInstance();
		EntityManager em = EMF.get().createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			Conversion conversion = new Conversion();
			conversion.setTimeOfConversion(cal.getTime());
			conversion.setDecimalNumber(num);
			conversion.setBinaryNumber(binaryNum);

			em.persist(conversion);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
	}

	/**
	 * Retrieve the accesses to conversion operations as a list.
	 * 
	 * @return the list of conversion operations
	 */
	@Override
	public List<Conversion> listConversion() {
		EntityManager em = EMF.get().createEntityManager();
		List<Conversion> results = null;
		try {
			Query query = em.createQuery("SELECT c FROM Conversion c ORDER BY c.timeOfConversion DESC");
			results = (List<Conversion>) query.getResultList();
		} finally {
			em.close();
		}

		return results;
	}

}
