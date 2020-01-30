/**
 * Project:A01166101Assignment1
 * File: CompareByJoinDateDescending.java
 * Date: Oct. 20, 2019
 * Time: 11:34:49 p.m.
 */
package a01166101.data.util;

import java.util.Comparator;

import a01166101.data.customer.Customer;

/**
 * This class is a comparator for collection of customers
 * 
 * @author Sz En Kao, A01166101
 *
 */
public class CompareByJoinDate implements Comparator<Customer> {
	/*
	 * Compares customers using their join date
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Customer o1, Customer o2) {

		String[] s1 = o1.getJoinedDate().toString().split("-");
		String[] s2 = o2.getJoinedDate().toString().split("-");
		return Integer.parseInt(s1[0] + s1[1] + s1[2]) - Integer.parseInt(s2[0] + s2[1] + s2[2]);
	}
}
