/**
 * Project:A01166101Assignment1
 * File: Validator.java
 * Date: Sep 17, 2019
 * Time: 6:12:15 PM
 */
package a01166101.data.customer.util;

import java.time.LocalDate;

import a01166101.book.ApplicationException;

/**
 * This class is a validator for customer
 * 
 * @author Sz En Kao, A01166101
 *
 */
public class ValidatorCustomer {
	/**
	 * validates email
	 * 
	 * @param email
	 * @return
	 * @throws ApplicationException
	 */
	public static String validateEmail(String email) throws ApplicationException {
		if (email.contains("@")) {
			return email;
		} else {
			throw new ApplicationException(email + "is an invalid email address");
		}
	}

	/**
	 * validates date
	 * 
	 * @param date
	 * @return
	 * @throws ApplicationException
	 */
	public static LocalDate validateDate(String date) throws ApplicationException {
		if (date.length() != 8) {
			throw new ApplicationException("length is " + date.length());
		}
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		if (year > 0) {
			if (0 < month && month <= 12) {
				if (0 < day && day <= 31) {
					LocalDate localDate = LocalDate.of(year, month, day);
					return localDate;
				} else {
					throw new ApplicationException("day is " + day + " valid values 1 - 28/31");
				}
			} else {
				throw new ApplicationException("month is " + month + " valid values 1 - 12");
			}
		} else {
			throw new ApplicationException("year is " + year + "valid values 1 - 9999");
		}
	}

	/**
	 * validates whether if customer has the correct element or not
	 * 
	 * @param blockInput
	 * @throws ApplicationException
	 */
	public static void validateElements(String[] blockInput) throws ApplicationException {
		if (blockInput.length != 9) {
			throw new ApplicationException("Expected 9 elements but got " + blockInput.length + " " + blockInput);
		}
	}
}
