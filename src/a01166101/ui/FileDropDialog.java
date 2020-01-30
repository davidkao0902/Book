/**
 * Project:Books2
 * File: FileDropDialog.java
 * Date: Nov. 9, 2019
 * Time: 1:59:45 a.m.
 */
package a01166101.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a01166101.Books2;
import a01166101.data.book.BookDao;
import a01166101.data.customer.CustomerDao;
import a01166101.data.purchase.PurchaseDao;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class FileDropDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public FileDropDialog() {
		CustomerDao customerDao = Books2.getCustomerDao();
		BookDao bookDao = Books2.getBookDao();
		PurchaseDao purchaseDao = Books2.getPurchaseDao();

		setBounds(100, 100, 250, 100);
		final JScrollPane scroll = new JScrollPane(contentPanel);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(scroll, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JTextField text = new JTextField();
				text.setEnabled(false);
				text.setEditable(false);
				contentPanel.add(text);
				text.setText("Do you want to drop all the tables?");
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							customerDao.drop();
							bookDao.drop();
							purchaseDao.drop();
							System.exit(-1);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

					}
				});
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

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}
}
