/**
 * Project:Books2
 * File: PurchaseListDialog.java
 * Date: Nov. 9, 2019
 * Time: 1:55:39 a.m.
 */
package a01166101.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a01166101.Books2;
import a01166101.data.book.Book;
import a01166101.data.book.BookDao;
import a01166101.data.customer.Customer;
import a01166101.data.customer.CustomerDao;
import a01166101.data.purchase.Purchase;
import a01166101.data.purchase.PurchaseDao;
import net.miginfocom.swing.MigLayout;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class PurchaseListDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private PurchaseDao purchaseDao;
	private CustomerDao customerDao;
	private BookDao bookDao;
	private static boolean sortByLastName;
	private static boolean sortByTitle;
	private static boolean sortByDescending;
	private JTextField idField;
	private JTextField nameField;
	private JTextField titleField;

	/**
	 * Create the dialog.
	 */
	public PurchaseListDialog(boolean isFiltered, long customerId) {
		purchaseDao = Books2.getPurchaseDao();
		bookDao = Books2.getBookDao();
		customerDao = Books2.getCustomerDao();

		ArrayList<Purchase> purchases = new ArrayList<>();
		HashMap<Long, Book> books = new HashMap<>();
		HashMap<Long, Customer> customers = new HashMap<>();

		try {
			for (Long i : purchaseDao.getPurchaseIds()) {
				Purchase purchase = purchaseDao.getPurchase(i);
				purchases.add(purchase);
			}
			for (Long i : bookDao.getBookIds()) {
				Book book = bookDao.getBook(i);
				books.put(book.getId(), book);
			}

			for (Long i : customerDao.getCustomerIds()) {
				Customer customer = customerDao.getCustomer(i);
				customers.put(customer.getId(), customer);
			}
		} catch (Exception e) {
		}

		setBounds(100, 100, 450, 300);
		final JScrollPane scroll = new JScrollPane(contentPanel);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(scroll, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow][grow]", "[]".repeat(purchases.size())));
		{
			if (sortByLastName) {
				purchases.sort(new Comparator<Purchase>() {
					@Override
					public int compare(Purchase o1, Purchase o2) {
						String customer1 = customers.get(o1.getCustomerId()).getLastName();
						String customer2 = customers.get(o2.getCustomerId()).getLastName();
						if (!sortByDescending) {
							return customer1.compareTo(customer2);
						}
						return customer2.compareTo(customer1);
					}
				});
			}

			if (sortByTitle) {
				purchases.sort(new Comparator<Purchase>() {
					@Override
					public int compare(Purchase o1, Purchase o2) {
						String book1 = books.get(o1.getBookId()).getTitle();
						String book2 = books.get(o2.getBookId()).getTitle();
						if (!sortByDescending) {
							return book1.compareTo(book2);
						}
						return book2.compareTo(book1);
					}
				});
			}
			int count = 0;
			for (Purchase purchase : purchases) {
				Customer customer = customers.get(purchase.getCustomerId());
				Book book = books.get(purchase.getBookId());
				String lastName = customer.getLastName();
				if (!isFiltered || customerId == customer.getId()) {
					idField = new JTextField();
					idField.setEnabled(false);
					idField.setEditable(false);
					contentPanel.add(idField, "cell 0 " + count + ",alignx trailing");
					idField.setColumns(10);
					idField.setText(Long.toString(purchase.getId()));
					nameField = new JTextField();
					nameField.setEnabled(false);
					nameField.setEditable(false);
					contentPanel.add(nameField, "cell 1 " + count + ",growx");
					titleField = new JTextField();
					titleField.setEnabled(false);
					titleField.setEditable(false);
					contentPanel.add(titleField, "cell 2 " + count + ",growx");
					nameField.setText(customers.get(purchase.getCustomerId()).getLastName());
					titleField.setText(books.get(purchase.getBookId()).getTitle());
					count++;
				}
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
	}

	/**
	 * @param sortByLastName
	 *            the sortByLastName to set
	 */
	public static void setSortByLastName(boolean sortByLastName) {
		PurchaseListDialog.sortByLastName = sortByLastName;
	}

	/**
	 * @param sortByTitle
	 *            the sortByTitle to set
	 */
	public static void setSortByTitle(boolean sortByTitle) {
		PurchaseListDialog.sortByTitle = sortByTitle;
	}

	/**
	 * @param sortByDescending
	 *            the sortByDescending to set
	 */
	public static void setSortByDescending(boolean sortByDescending) {
		PurchaseListDialog.sortByDescending = sortByDescending;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}

}
