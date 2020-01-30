/**
 * Project: A00123456Lab3
 * File: CustomerReader.java
 */

package a01166101.io;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01166101.book.ApplicationException;
import a01166101.data.customer.Customer;
import a01166101.data.customer.CustomerDao;
import a01166101.data.customer.util.ValidatorCustomer;

/**
 * Read the customer input.
 * 
 * @author Sam Cirka, A00123456
 *
 */
public class CustomerReader {

	public static final String RECORD_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";

	/**
	 * private constructor to prevent instantiation
	 */
	private CustomerReader() {
	}

	/**
	 * 
	 * @return a hash map containing customer id with value as customer
	 * @throws ApplicationException
	 */
	public static void read(File customerDataFile, CustomerDao dao) throws ApplicationException {
		Scanner scanner = null;
		ArrayList<String> input = new ArrayList<>();
		try {
			scanner = new Scanner(customerDataFile);
			int i = 0;
			while (scanner.hasNext()) {
				i++;
				String row = scanner.nextLine();
				if (i > 1) {
					input.add(row.trim());
				}
			}
		} catch (Exception e) {
			System.exit(-1);
		} finally {
			if (scanner != null) {
				try {
					scanner.close();
				} catch (Exception e) {
				}
			}
		}

		int count = 0;
		int amountPeople = input.size();
		String[] ids = new String[amountPeople];
		String[] firstNames = new String[amountPeople];
		String[] lastNames = new String[amountPeople];
		String[] streetNames = new String[amountPeople];
		String[] cities = new String[amountPeople];
		String[] postalCodes = new String[amountPeople];
		String[] phones = new String[amountPeople];
		String[] emailAddresses = new String[amountPeople];
		LocalDate[] joinDates = new LocalDate[amountPeople];

		for (String s : input) {
			String[] blockInput = s.split("\\|");
			for (int k = 0; k < blockInput.length; k++) {
				String modifiedBlock = blockInput[k];
				switch (k) {
				case (0):
					ids[count] = (modifiedBlock);
					break;
				case (1):
					firstNames[count] = (modifiedBlock);
					break;
				case (2):
					lastNames[count] = (modifiedBlock);
					break;
				case (3):
					streetNames[count] = (modifiedBlock);
					break;
				case (4):
					cities[count] = (modifiedBlock);
					break;
				case (5):
					postalCodes[count] = (modifiedBlock);
					break;
				case (6):
					phones[count] = (modifiedBlock);
					break;
				case (7):
					emailAddresses[count] = ValidatorCustomer.validateEmail(modifiedBlock);
					break;
				case (8):
					joinDates[count] = ValidatorCustomer.validateDate(modifiedBlock);
					break;
				default:
					break;
				}
			}

			Customer customer = new Customer.Builder(Long.parseLong(ids[count]), phones[count]).setFirstName(firstNames[count])
					.setLastName(lastNames[count]).setStreet(streetNames[count]).setCity(cities[count]).setPostalCode(postalCodes[count])
					.setEmailAddress(emailAddresses[count]).setJoinedDate(joinDates[count]).build();
			try {
				if (!dao.getCustomerIds().contains(Long.parseLong(ids[count]))) {
					dao.add(customer);

				} else {
				}
			} catch (NumberFormatException e1) {

			} catch (SQLException e1) {

			} catch (Exception e1) {

			}
			count++;
		}
	}

}
