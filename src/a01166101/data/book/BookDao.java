/**
 * Project:Books2
 * File: BookDao.java
 * Date: Nov. 11, 2019
 * Time: 8:13:04 p.m.
 */
package a01166101.data.book;

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
import a01166101.io.BookReader;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class BookDao extends Dao {
	public static final String TABLE_NAME = DbConstants.TABLE_ROOT + "Books";

	public static final String BOOKS_DATA_FILENAME = "books500.csv";

	public BookDao(Database database) throws ApplicationException {
		super(database, TABLE_NAME);
		load();
	}

	/**
	 * @param customerDataFile
	 * @throws ApplicationException
	 * @throws SQLException
	 */
	public void load() {
		File booksDataFile = new File(BOOKS_DATA_FILENAME);
		try {
			if (!Database.tableExists(BookDao.TABLE_NAME)) {
				try {
					create();
				} catch (SQLException e) {
				}

				if (!booksDataFile.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", BOOKS_DATA_FILENAME));
				}
				// ****************************
				BookReader.read(booksDataFile, this);
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
				+ "%s VARCHAR(%d), " // ISBN
				+ "%s VARCHAR(%d), " // AUTHORS
				+ "%s VARCHAR(%d), " // TITLE
				+ "%s VARCHAR(%d), " // YEAR
				+ "%s VARCHAR(%d), " // RATING
				+ "%s VARCHAR(%d), " // RAITING COUNT
				+ "%s VARCHAR(%d), " // IMAGE URL
				+ "PRIMARY KEY (%s))", // ID
				TABLE_NAME, //
				Column.ID.name, //
				Column.ISBN.name, Column.ISBN.length, //
				Column.AUTHORS.name, Column.AUTHORS.length, //
				Column.TITLE.name, Column.TITLE.length, //
				Column.YEAR.name, Column.YEAR.length, //
				Column.RATING.name, Column.RATING.length, //
				Column.RATING_COUNT.name, Column.RATING_COUNT.length, //
				Column.IMAGE_URL.name, Column.IMAGE_URL.length, //
				Column.ID.name);

		super.create(sqlString);
	}

	public void add(Book book) throws SQLException {
		System.out.println("ADDING");
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				book.getId(), //
				book.getIsbn(), //
				book.getAuthors(), //
				book.getTitle(), //
				book.getYear(), //
				book.getRating(), //
				book.getRatingsCount(), //
				book.getImageUrl());
	}

	/**
	 * Update the customer.
	 * 
	 * @param customer
	 * @throws SQLException
	 */
	public void update(Book book) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Column.ID.getName(), book.getId(), //
					Column.ISBN.getName(), book.getIsbn(), //
					Column.AUTHORS.getName(), book.getAuthors(), //
					Column.TITLE.getName(), book.getTitle(), //
					Column.YEAR.getName(), book.getYear(), //
					Column.RATING.getName(), book.getRating(), //
					Column.RATING_COUNT.getName(), book.getRatingsCount(), //
					Column.IMAGE_URL.getName(), book.getImageUrl(), //
					Column.ID.getName(), book.getId());

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
	public void delete(Book book) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, book.getId());
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
	public List<Long> getBookIds() throws SQLException {
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
	 * @param bookID
	 * @return
	 * @throws Exception
	 */
	public Book getBook(Long bookID) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, bookID);

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
				Book book = new Book.Builder(resultSet.getInt(Column.ID.name)) //
						.setIsbn(resultSet.getString(Column.ISBN.name)) //
						.setAuthors(resultSet.getString(Column.AUTHORS.name)) //
						.setTitle(resultSet.getString(Column.TITLE.name)) //
						.setYear(resultSet.getInt(Column.YEAR.name)) //
						.setRating(resultSet.getFloat(Column.RATING.name)) //
						.setRatingsCount(resultSet.getInt(Column.RATING_COUNT.name)) //
						.setImageUrl(resultSet.getString(Column.IMAGE_URL.name)).build();

				return book;
			}
		} finally {
			close(statement);
		}

		return null;
	}

	public int countAllBook() throws Exception {
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
		ISBN("isbn", 20), //
		AUTHORS("authors", 200), //
		TITLE("title", 200), //
		YEAR("year", 20), //
		RATING("rating", 6), //
		RATING_COUNT("ratingCount", 10), //
		IMAGE_URL("imageURL", 200); //

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
