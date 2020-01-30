/**
 * Project: Books
 * File: BookReader.java
 */

package a01166101.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01166101.book.ApplicationException;
import a01166101.data.book.Book;
import a01166101.data.book.BookDao;

/**
 * @author Sz En Kao, 21505466
 *
 */
public class BookReader {

	/**
	 * private constructor to prevent instantiation
	 */
	private BookReader() {
	}

	/**
	 * 
	 * @return the hash map with key as book id and value as book
	 * @throws ApplicationException
	 */
	public static void read(File bookDataFile, BookDao dao) throws ApplicationException {
		FileReader in;
		Iterable<CSVRecord> records;
		try {
			in = new FileReader(bookDataFile);
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		for (CSVRecord record : records) {
			long id = Long.parseLong(record.get("book_id"));
			String isbn = record.get("isbn");
			String author = record.get("authors");
			int publicationYear = Integer.parseInt(record.get("original_publication_year"));
			String title = record.get("original_title");
			Float averageRating = Float.parseFloat(record.get("average_rating"));
			int ratingCount = Integer.parseInt(record.get("ratings_count"));
			String url = record.get("image_url");
			Book book = new Book.Builder(id).setIsbn(isbn).setAuthors(author).setYear(publicationYear).setTitle(title).setRating(averageRating)
					.setRatingsCount(ratingCount).setImageUrl(url).build();
			try {
				if (!dao.getBookIds().contains(id)) {
					dao.add(book);
				} else {
				}
			} catch (NumberFormatException e1) {
			} catch (SQLException e1) {
			} catch (Exception e1) {
			}

		}
	}
}