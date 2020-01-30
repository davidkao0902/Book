/**
 * Project:A01166101Lab9
 * File: CustomerDialog.java
 * Date: Nov. 7, 2019
 * Time: 10:20:32 p.m.
 */
package a01166101.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a01166101.Books2;
import a01166101.book.ApplicationException;
import a01166101.data.customer.Customer;
import a01166101.data.customer.CustomerDao;
import a01166101.data.util.Validator;
import net.miginfocom.swing.MigLayout;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class CustomerDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private CustomerDao customerDao;
	private Customer customer;
	private JTextField idField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField streetField;
	private JTextField cityField;
	private JTextField postalCodeField;
	private JTextField phoneField;
	private JTextField emailField;
	private JTextField joinDateField;

	/**
	 * Create the dialog.
	 */
	public CustomerDialog(Customer customer) {
		customerDao = Books2.getCustomerDao();
		this.customer = customer;
		setBounds(400, 100, 350, 350);
		final JScrollPane scroll = new JScrollPane(contentPanel);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(scroll, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));
		{
			JLabel lblId = new JLabel("ID");
			contentPanel.add(lblId, "cell 0 0,alignx trailing");

			idField = new JTextField();
			idField.setEnabled(false);
			idField.setEditable(false);
			contentPanel.add(idField, "cell 1 0,growx");
			idField.setColumns(10);

			JLabel lblFirstName = new JLabel("First Name");
			contentPanel.add(lblFirstName, "cell 0 1,alignx trailing");

			firstNameField = new JTextField();
			contentPanel.add(firstNameField, "cell 1 1,growx");
			firstNameField.setColumns(10);

			JLabel lblLastName = new JLabel("Last Name");
			contentPanel.add(lblLastName, "cell 0 2,alignx trailing");

			lastNameField = new JTextField();
			contentPanel.add(lastNameField, "cell 1 2,growx");
			lastNameField.setColumns(10);

			JLabel lblStreet = new JLabel("Street");
			contentPanel.add(lblStreet, "cell 0 3,alignx trailing");

			streetField = new JTextField();
			contentPanel.add(streetField, "cell 1 3,growx");
			streetField.setColumns(10);

			JLabel lblCity = new JLabel("City");
			contentPanel.add(lblCity, "cell 0 4,alignx trailing");

			cityField = new JTextField();
			contentPanel.add(cityField, "cell 1 4,growx");
			cityField.setColumns(10);

			JLabel lblPostalCode = new JLabel("PostalCode");
			contentPanel.add(lblPostalCode, "cell 0 5,alignx trailing");

			postalCodeField = new JTextField();
			contentPanel.add(postalCodeField, "cell 1 5,growx");
			postalCodeField.setColumns(10);

			JLabel lblPhone = new JLabel("Phone");
			contentPanel.add(lblPhone, "cell 0 6,alignx trailing");

			phoneField = new JTextField();
			contentPanel.add(phoneField, "cell 1 6,growx");
			phoneField.setColumns(10);

			JLabel lblEmail = new JLabel("Phone");
			contentPanel.add(lblEmail, "cell 0 7,alignx trailing");

			emailField = new JTextField();
			contentPanel.add(emailField, "cell 1 7,growx");
			emailField.setColumns(10);

			JLabel lblJoinDate = new JLabel("Phone");
			contentPanel.add(lblJoinDate, "cell 0 8,alignx trailing");

			joinDateField = new JTextField();
			contentPanel.add(joinDateField, "cell 1 8,growx");
			joinDateField.setColumns(10);
			setData();
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							saveCustomer();
						} catch (ApplicationException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
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

	private void setData() {
		idField.setText(Long.toString(customer.getId()));
		firstNameField.setText(customer.getFirstName());
		lastNameField.setText(customer.getLastName());
		streetField.setText(customer.getStreet());
		cityField.setText(customer.getCity());
		postalCodeField.setText(customer.getPostalCode());
		phoneField.setText(customer.getPhone());
		emailField.setText(customer.getEmailAddress());
		joinDateField.setText(customer.getJoinedDate().toString());
	}

	private void saveCustomer() throws ApplicationException, SQLException {
		long id = Integer.parseInt(idField.getText());
		String phone = phoneField.getText();
		String firstName = firstNameField.getText();
		String lastName = lastNameField.getText();
		String street = streetField.getText();
		String city = cityField.getText();
		String postalCode = postalCodeField.getText();
		String email = emailField.getText();

		String yyyymmdd = joinDateField.getText().split("-")[0] + joinDateField.getText().split("-")[1] + joinDateField.getText().split("-")[2];
		if (!Validator.validateJoinedDate(yyyymmdd)) {
			throw new ApplicationException(String.format("Invalid joined date: %s for customer %d", yyyymmdd, id));
		}
		int year = Integer.parseInt(yyyymmdd.substring(0, 4));
		int month = Integer.parseInt(yyyymmdd.substring(4, 6));
		int day = Integer.parseInt(yyyymmdd.substring(6, 8));
		LocalDate joinDate = LocalDate.of(year, month, day);
		System.out.println(id + phone + firstName + lastName + street + city + postalCode + year + month + day);
		Customer customer = new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName).setStreet(street).setCity(city)
				.setPostalCode(postalCode).setEmailAddress(email).setJoinedDate(joinDate).build();

		customerDao.update(customer);
		System.exit(-1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}

}
