/**
 * Project: Books
 * File: PurchaseReader.java
 */

package a01166101.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01166101.Books2;
import a01166101.book.ApplicationException;
import a01166101.data.purchase.Purchase;
import a01166101.data.purchase.PurchaseDao;

/**
 * @author Sz En Kao, 21505466
 *
 */
public class PurchaseReader {


	private static List<Long> customerIds;
	private static Long[] customerIdArray;
	private static int customerIdCount;
	private static List<Long> bookIds;
	private static Long[] bookIdArray;
	private static int bookIdCount;

	/**
	 * private constructor to prevent instantiation
	 */
	private PurchaseReader() {
	}

	/**
	 * Read the inventory input data.
	 * 
	 * @return the inventory.
	 * @throws ApplicationException
	 * @throws SQLException
	 */
	public static void read(File purchaseDataFile, PurchaseDao dao) throws ApplicationException {
		try {
			customerIds = Books2.getCustomerDao().getCustomerIds();
			bookIds = Books2.getBookDao().getBookIds();
		} catch (SQLException e2) {

		}
		customerIdArray = customerIds.toArray(new Long[0]);
		customerIdCount = customerIdArray.length;
		bookIdArray = bookIds.toArray(new Long[0]);
		bookIdCount = bookIdArray.length;
		FileReader in;
		Iterable<CSVRecord> records;
		try {
			in = new FileReader(purchaseDataFile);
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		for (CSVRecord record : records) {
			long id = Long.parseLong(record.get("id"));
			long customerId = Long.parseLong(record.get("customer_id"));
			long bookId = Long.parseLong(record.get("book_id"));
			float price = Float.parseFloat(record.get("price"));

			Purchase purchase = new Purchase.Builder(id, customerId, bookId, price).build();
			try {
				if (!dao.getPurchaseIds().contains(id)) {
					dao.add(purchase);

				} else {
				}
			} catch (NumberFormatException e1) {

			} catch (SQLException e1) {

			} catch (Exception e1) {

			}

			if (!customerIds.contains(customerId)) {
				customerId = customerIdArray[(int) (Math.random() * customerIdCount)];
			}
			if (!bookIds.contains(bookId)) {
				bookId = bookIdArray[(int) (Math.random() * bookIdCount)];
			}
		}
	}

}
