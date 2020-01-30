/**
 * Project:Books2
 * File: CustomerList.java
 * Date: Nov. 9, 2019
 * Time: 1:55:16 a.m.
 */
package a01166101.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a01166101.Books2;
import a01166101.data.customer.Customer;
import a01166101.data.customer.CustomerDao;
import a01166101.data.util.CompareByJoinDate;
import net.miginfocom.swing.MigLayout;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class CustomerListDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private CustomerDao customerDao;
	private static boolean sortByJoinDate;
	private JTextField idField;
	private JTextField dateField;
	private JButton nameField;

	/**
	 * Create the dialog.
	 * 
	 * @throws Exception
	 */
	public CustomerListDialog() throws Exception {
		customerDao = Books2.getCustomerDao();
		int length = customerDao.countAllCustomers();
		setBounds(100, 100, 450, 300);
		final JScrollPane scroll = new JScrollPane(contentPanel);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(scroll, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow]", "[]".repeat(length)));
		{
			int count = 0;
			ArrayList<Customer> customers = new ArrayList<>();
			for (Long i : customerDao.getCustomerIds()) {
				customers.add(customerDao.getCustomer(i));
			}
			if (sortByJoinDate) {
				Collections.sort(customers, new CompareByJoinDate());
			}
			for (Customer customer : customers) {
				idField = new JTextField();
				idField.setEnabled(false);
				idField.setEditable(false);
				contentPanel.add(idField, "cell 0 " + count + ",alignx trailing");
				idField.setColumns(10);
				idField.setText(Long.toString(customer.getId()));
				dateField = new JTextField();
				dateField.setEnabled(false);
				dateField.setEditable(false);
				contentPanel.add(dateField, "cell 1 " + count + ",alignx trailing");
				dateField.setColumns(10);
				dateField.setText(customer.getJoinedDate().toString());
				nameField = new JButton();
				contentPanel.add(nameField, "cell 2 " + count + ",growx");
				nameField.setText(customer.getFirstName() + " " + customer.getLastName());
				nameField.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						customerDialog(customer);
					}
				});
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
		// add(scroll, BorderLayout.CENTER);
	}

	/**
	 * @param sortByJoinDate
	 *            the sortByJoinDate to set
	 */
	public static void setSortByJoinDate(boolean joinDate) {
		sortByJoinDate = joinDate;
	}

	private void customerDialog(Customer customer) {
		try {
			CustomerDialog dialog = new CustomerDialog(customer);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}

}
