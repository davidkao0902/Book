/**
 * Project:Books2
 * File: PurchaseDao.java
 * Date: Nov. 11, 2019
 * Time: 8:12:55 p.m.
 */
package a01166101.data.purchase;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01166101.book.ApplicationException;
import a01166101.db.Dao;
import a01166101.db.Database;
import a01166101.db.DbConstants;
import a01166101.io.PurchaseReader;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class PurchaseDao extends Dao {
	public static final String TABLE_NAME = DbConstants.TABLE_ROOT + "Purchases";
	public static final String PURCHASES_DATA_FILENAME = "purchases.csv";
	public PurchaseDao(Database database) throws ApplicationException {
		super(database, TABLE_NAME);

		load();
	}

	/**
	 * @param customerDataFile
	 * @throws ApplicationException
	 * @throws SQLException
	 */
	public void load() {
		File purchaseDataFile = new File(PURCHASES_DATA_FILENAME);
		try {
			if (!Database.tableExists(PurchaseDao.TABLE_NAME)) {
				try {
					create();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!purchaseDataFile.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", PURCHASES_DATA_FILENAME));
				}
				// ****************************
				PurchaseReader.read(purchaseDataFile, this);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void create() throws SQLException {
		// With MS SQL Server, JOINED_DATE needs to be a DATETIME type.
		String sqlString = String.format("CREATE TABLE %s(" //
				+ "%s BIGINT, " // ID
				+ "%s VARCHAR(%d), " // CUSTOMER_ID
				+ "%s VARCHAR(%d), " // BOOK_ID
				+ "%s FLOAT(%d), " // PRICE
				+ "PRIMARY KEY (%s))", // ID
				TABLE_NAME, //
				Column.ID.name, //
				Column.CUSTOMER_ID.name, Column.CUSTOMER_ID.length, //
				Column.BOOK_ID.name, Column.BOOK_ID.length, //
				Column.PRICE.name, Column.PRICE.length, //
				Column.ID.name);

		super.create(sqlString);
	}

	public void add(Purchase purchase) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				purchase.getId(), //
				purchase.getCustomerId(), //
				purchase.getBookId(), //
				purchase.getPrice());
	}

	/**
	 * Update the customer.
	 * 
	 * @param customer
	 * @throws SQLException
	 */
	public void update(Purchase purchase) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'", tableName, //
					Column.ID.getName(), purchase.getId(), //
					Column.CUSTOMER_ID.getName(), purchase.getCustomerId(), //
					Column.BOOK_ID.getName(), purchase.getBookId(), //
					Column.PRICE.getName(), purchase.getPrice(), //
					Column.ID.getName(), purchase.getId());

			int rowcount = statement.executeUpdate(sql);
			System.out.println(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Delete the customer from the database.
	 * 
	 * @param customer
	 * @throws SQLException
	 */
	public void delete(Purchase purchase) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, purchase.getId());
			int rowcount = statement.executeUpdate(sqlString);
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the customer IDs from the database
	 * 
	 * @return the list of customer IDs
	 * @throws SQLException
	 */
	public List<Long> getPurchaseIds() throws SQLException {
		List<Long> ids = new ArrayList<>();

		String selectString = String.format("SELECT %s FROM %s", Column.ID.name, TABLE_NAME);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				ids.add(resultSet.getLong(Column.ID.name));
			}

		} finally {
			close(statement);
		}

		return ids;
	}

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public Purchase getPurchase(Long purchaseId) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, purchaseId);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);

			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}

				Purchase purchase = new Purchase.Builder(resultSet.getInt(Column.ID.name), resultSet.getInt(Column.CUSTOMER_ID.name),
						resultSet.getInt(Column.BOOK_ID.name), resultSet.getFloat(Column.PRICE.name)).build();

				return purchase;
			}
		} finally {
			close(statement);
		}

		return null;
	}

	public float getTotal() throws Exception {
		float total = 0;
		for (Long i : this.getPurchaseIds()) {
			total += this.getPurchase(i).getPrice();
		}

		return total;
	}

	public int countAllPurchases() throws Exception {
		Statement statement = null;
		int count = 0;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT COUNT(*) AS total FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} finally {
			close(statement);
		}
		return count;
	}

	public enum Column {
		ID("id", 16), //
		CUSTOMER_ID("customerId", 20), //
		BOOK_ID("bookId", 20), //
		PRICE("price", 40);

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}

		private String getName() {
			return name;
		}

	}
}
