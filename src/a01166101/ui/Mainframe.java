/**
 * Project:Books2
 * File: Mainframe.java
 * Date: Nov. 9, 2019
 * Time: 1:39:24 a.m.
 */
package a01166101.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import a01166101.Books2;
import a01166101.data.book.BookDao;
import a01166101.data.customer.CustomerDao;
import a01166101.data.purchase.PurchaseDao;

/**
 * @author Sz En Kao, A01166101
 *
 */
public class Mainframe extends JFrame {

	private JPanel contentPane;
	private CustomerDao customerDao;
	private BookDao bookDao;
	private PurchaseDao purchaseDao;

	/**
	 * Create the frame.
	 */
	public Mainframe() {
		customerDao = Books2.getCustomerDao();
		bookDao = Books2.getBookDao();
		purchaseDao = Books2.getPurchaseDao();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 100);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmDrop = new JMenuItem("Drop");
		mntmDrop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileDropDialog();
			}
		});
		mnFile.add(mntmDrop);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmQuit);

		JMenu mnBooks = new JMenu("Books");
		menuBar.add(mnBooks);

		JMenuItem mntmCount = new JMenuItem("Count");
		mntmCount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					infoBox(Integer.toString(bookDao.countAllBook()), "Books Count");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnBooks.add(mntmCount);

		JCheckBoxMenuItem chckbxmntmByAuthor = new JCheckBoxMenuItem("By Author");
		chckbxmntmByAuthor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookListDialog.setSortByAuthor(chckbxmntmByAuthor.isSelected());
			}
		});
		mnBooks.add(chckbxmntmByAuthor);

		JCheckBoxMenuItem chckbxmntmDescending = new JCheckBoxMenuItem("Descending");
		chckbxmntmDescending.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookListDialog.setSortByDescending(chckbxmntmDescending.isSelected());
			}
		});
		mnBooks.add(chckbxmntmDescending);

		JMenuItem mntmList = new JMenuItem("List");
		mntmList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bookListDialog();
			}
		});
		mnBooks.add(mntmList);

		JMenu mnCustomers = new JMenu("Customers");
		menuBar.add(mnCustomers);

		JMenuItem mntmCount_1 = new JMenuItem("Count");
		mntmCount_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				try {
					infoBox(Integer.toString(customerDao.countAllCustomers()), "Customer Count");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnCustomers.add(mntmCount_1);

		JCheckBoxMenuItem chckbxmntmByJoinDate = new JCheckBoxMenuItem("By Join Date");
		chckbxmntmByJoinDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CustomerListDialog.setSortByJoinDate(chckbxmntmByJoinDate.isSelected());
			}
		});
		mnCustomers.add(chckbxmntmByJoinDate);

		JMenuItem mntmList_1 = new JMenuItem("List");
		mntmList_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				customerListDialog();
			}
		});
		mnCustomers.add(mntmList_1);

		JMenu mnPurchases = new JMenu("Purchases");
		menuBar.add(mnPurchases);

		JMenuItem mntmTotal = new JMenuItem("Total");
		mntmTotal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					infoBox(String.format("%.2f", purchaseDao.getTotal()), "Purchase Total");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnPurchases.add(mntmTotal);

		JCheckBoxMenuItem chckbxmntmByLastName = new JCheckBoxMenuItem("By Last Name");
		chckbxmntmByLastName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PurchaseListDialog.setSortByLastName(chckbxmntmByLastName.isSelected());
			}
		});
		mnPurchases.add(chckbxmntmByLastName);

		JCheckBoxMenuItem chckbxmntmByTitle = new JCheckBoxMenuItem("By Title");
		chckbxmntmByTitle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PurchaseListDialog.setSortByTitle(chckbxmntmByTitle.isSelected());
			}
		});
		mnPurchases.add(chckbxmntmByTitle);

		JCheckBoxMenuItem chckbxmntmDescending_1 = new JCheckBoxMenuItem("Descending");
		chckbxmntmDescending_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PurchaseListDialog.setSortByDescending(chckbxmntmDescending_1.isSelected());
			}
		});
		mnPurchases.add(chckbxmntmDescending_1);

		JMenuItem mntmFilterByCustomer = new JMenuItem("Filter by Customer ID");
		// TODO
		mntmFilterByCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				promptCustomerId();
				// customerIDFilterDialog();
			}
		});
		mnPurchases.add(mntmFilterByCustomer);

		JMenuItem mntmList_2 = new JMenuItem("List");
		// TODO
		mntmList_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				purchaseListDialog(false, -1);
			}
		});
		mnPurchases.add(mntmList_2);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke("F1"));
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				infoBox("Assignment2 Books\nBy David Kao 21505466", "About Assignment2 Books");
			}
		});
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void promptCustomerId() {
		String customerID = JOptionPane.showInputDialog(null, "Please Input customer Id", "Customer Id for Purchases", JOptionPane.QUESTION_MESSAGE);
		try {
			if (customerID != null) {
				purchaseListDialog(true, Long.parseLong(customerID));
			}
		} catch (Exception e) {

		}

	}

	private void fileDropDialog() {
		try {
			FileDropDialog dialog = new FileDropDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bookListDialog() {
		try {
			BookListDialog dialog = new BookListDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void customerListDialog() {
		try {
			CustomerListDialog dialog = new CustomerListDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void purchaseListDialog(boolean isFiltered, long customerID) {
		try {
			PurchaseListDialog dialog = new PurchaseListDialog(isFiltered, customerID);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
