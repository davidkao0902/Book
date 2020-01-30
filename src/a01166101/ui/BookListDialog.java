/**
 * Project:Books2
 * File: BookListDialog.java
 * Date: Nov. 9, 2019
 * Time: 1:55:02 a.m.
 */
package a01166101.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a01166101.Books2;
import a01166101.data.book.Book;
import a01166101.data.book.BookDao;
import net.miginfocom.swing.MigLayout;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class BookListDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private BookDao bookDao;
	private static boolean sortByAuthor;
	private static boolean sortByDescending;
	private JTextField idField;
	private JTextField authorField;
	private JButton nameField;

	/**
	 * Create the dialog.
	 */
	public BookListDialog() {
		bookDao = Books2.getBookDao();
		int length = 0;
		try {
			length = bookDao.countAllBook();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setBounds(100, 100, 450, 300);
		final JScrollPane scroll = new JScrollPane(contentPanel);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(scroll, BorderLayout.CENTER);

		contentPanel.setLayout(new MigLayout("", "[][grow][grow]", "[]".repeat(length)));
		{
			int count = 0;
			ArrayList<Book> books = new ArrayList<>();
			try {
				for (Long i : bookDao.getBookIds()) {
					books.add(bookDao.getBook(i));
				}
			} catch (SQLException e) {
			} catch (Exception e) {
			}

			if (sortByAuthor) {
				books.sort(new Comparator<Book>() {
					@Override
					public int compare(Book o1, Book o2) {
						if (!sortByDescending) {
							return o1.getAuthors().compareTo(o2.getAuthors());
						}
						return o2.getAuthors().compareTo(o1.getAuthors());
					}
				});
			}

			for (Book book : books) {
				idField = new JTextField();
				idField.setEnabled(false);
				idField.setEditable(false);
				contentPanel.add(idField, "cell 0 " + count + ",alignx trailing");
				idField.setColumns(10);
				idField.setText(Long.toString(book.getId()));

				authorField = new JTextField();
				authorField.setEnabled(false);
				authorField.setEditable(false);
				contentPanel.add(authorField, "cell 1 " + count + ",growx");
				authorField.setText(book.getAuthors());

				nameField = new JButton();
				contentPanel.add(nameField, "cell 2 " + count + ",growx");
				nameField.setText(book.getTitle());
				/**
				 * nameField.addActionListener(new ActionListener() {
				 * 
				 * @Override
				 *           public void actionPerformed(ActionEvent e) {
				 *           customerDialog(customer);
				 *           }
				 *           });
				 **/
				count++;
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane);
	}

	/**
	 * @param sortByAuthor
	 *            the sortByAuthor to set
	 */
	public static void setSortByAuthor(boolean sortByAuthor) {
		BookListDialog.sortByAuthor = sortByAuthor;
	}

	/**
	 * @param sortByDescending
	 *            the sortByDescending to set
	 */
	public static void setSortByDescending(boolean sortByDescending) {
		BookListDialog.sortByDescending = sortByDescending;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}
}
