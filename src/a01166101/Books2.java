/**
 * Project:Books2
 * File: Books2.java
 * Date: Nov. 9, 2019
 * Time: 1:50:53 a.m.
 */
package a01166101;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import a01166101.book.ApplicationException;
import a01166101.data.book.BookDao;
import a01166101.data.customer.CustomerDao;
import a01166101.data.purchase.PurchaseDao;
import a01166101.db.Database;
import a01166101.ui.Mainframe;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class Books2 {
	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	private static CustomerDao customerDao;
	private static BookDao bookDao;
	private static PurchaseDao purchaseDao;

	public static void main(String[] args) {
		customerMain();
		bookMain();
		purchaseMain();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Mainframe frame = new Mainframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void customerMain() {
		File file = new File(CustomerDao.CUSTOMERS_DATA_FILENAME);
		if (!file.exists()) {
			System.out.format("Required '%s' is missing.", CustomerDao.CUSTOMERS_DATA_FILENAME);
			System.exit(-1);
		}
		try {
			Database db = connect();
			customerDao = new CustomerDao(db);
			System.out.println(customerDao.getCustomerIds());
		} catch (Exception e) {
		}
	}

	/**
	 * @return the customerDao
	 */
	public static CustomerDao getCustomerDao() {
		return customerDao;
	}

	public static void bookMain() {
		File file = new File(BookDao.BOOKS_DATA_FILENAME);
		if (!file.exists()) {
			System.out.format("Required '%s' is missing.", BookDao.BOOKS_DATA_FILENAME);
			System.exit(-1);
		}
		try {
			Database db = connect();
			bookDao = new BookDao(db);
			System.out.println(bookDao.getBookIds());
		} catch (Exception e) {
		}
	}

	/**
	 * @return the bookDao
	 */
	public static BookDao getBookDao() {
		return bookDao;
	}

	public static void purchaseMain() {
		File file = new File(PurchaseDao.PURCHASES_DATA_FILENAME);
		if (!file.exists()) {
			System.out.format("Required '%s' is missing.", PurchaseDao.PURCHASES_DATA_FILENAME);
			System.exit(-1);
		}
		try {
			Database db = connect();
			purchaseDao = new PurchaseDao(db);
			System.out.println(purchaseDao.getPurchaseIds());
		} catch (Exception e) {
		}
	}

	/**
	 * @return the purchaseDao
	 */
	public static PurchaseDao getPurchaseDao() {
		return purchaseDao;
	}

	public static Database connect() throws IOException, SQLException, ApplicationException {
		Database db = new Database();
		return db;
	}

}
