package com.gmail.tth4ik;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.SpringLayout;
import javax.swing.JInternalFrame;
import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.UnknownHostException;
import java.rmi.server.Operation;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dialog.ModalityType;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.awt.Label;
import java.awt.SecondaryLoop;

import javax.swing.JLayeredPane;
import javax.swing.JToggleButton;
import java.awt.Window.Type;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class InterfaceTest {
	private JFrame frmWarehouseManagementSystem;
	private static ARMClient client;
	private int countclickconnect = 0;
	DefaultMutableTreeNode top;
	DefaultMutableTreeNode category = null;
	DefaultMutableTreeNode product = null;
	JTree tree;
	JTextPane textPane;
	JTextPane textPane_1;
	JTextPane textPane_2;
	JTextPane textPane_3;
	JTextPane textPane_4;
	JScrollPane jspt;
	DefaultTreeModel model;
	String selectedGroupAtTree;
	private boolean isFreeTree = true;

	/**
	 * Користувацький інтерфейс нашої системи управління складом. Включає у себе
	 * основне вікно, яке має 3 основні зони - дерево груп товарів; екран
	 * відображення інформації про окремий товар; панель з основними кнопками
	 * управління. При використанні більшості з кнопок відкриваються діалогові
	 * вікна, аби виконувати усі потрібні операції. Кнопки у бульшості випадків
	 * відправляють повідомлення на сервер та отримують від нього відповіді. У
	 * цьому класі прописано, як має реагувати на відповіді сервера клієнт і
	 * користувацький інтерфейс у кожному окремому випадку.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfaceTest window = new InterfaceTest();
					window.frmWarehouseManagementSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public InterfaceTest() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		frmWarehouseManagementSystem = new JFrame();
		frmWarehouseManagementSystem.setResizable(true);
		frmWarehouseManagementSystem.setTitle("Warehouse Management System");
		frmWarehouseManagementSystem.setBounds(100, 100, 715, 459);
		frmWarehouseManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		final JPanel panel_1 = new JPanel();

		JPanel panel_2 = new JPanel();
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		top = new DefaultMutableTreeNode("Groups of Items");
		tree = new JTree(top);
		model = (DefaultTreeModel) tree.getModel();

		jspt = new JScrollPane(tree);
		sl_panel.putConstraint(SpringLayout.NORTH, jspt, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, jspt, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, jspt, -109, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, jspt, 215, SpringLayout.WEST, panel);
		jspt.setBorder(null);
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (isFreeTree == false)
					return;
				if (client == null)
					return;
				try {
					client.sendCommandToServer("search");
					String node = e.getNewLeadSelectionPath().getLastPathComponent().toString();
					String response = client.sendMessageToServerAndGetResponse(node);
					if (response.equalsIgnoreCase("")) {
						selectedGroupAtTree = node.toString();
						return;
					}
					selectedGroupAtTree = null;
					String[] array = response.split("§");
					textPane.setText(array[1]);
					textPane_1.setText(array[3]);
					textPane_2.setText(array[4]);
					textPane_3.setText(array[5]);
					textPane_4.setText(array[2]);
					panel_1.repaint();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		tree.setBackground(Color.WHITE);
		tree.setEditable(true);
		tree.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.add(jspt);

		JButton button = new JButton("Add group");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client == null)
					return;
				initializeDialogAddGroup();
			}

			private void initializeDialogAddGroup() {
				final JDialog dialAddGr = new JDialog();
				JPanel contentPanel = new JPanel();
				dialAddGr.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialAddGr.setTitle("Add group");
				dialAddGr.setBounds(100, 100, 450, 269);
				dialAddGr.getContentPane().setLayout(new BorderLayout());
				contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				dialAddGr.getContentPane().add(contentPanel, BorderLayout.CENTER);
				SpringLayout sl_contentPanel = new SpringLayout();
				contentPanel.setLayout(sl_contentPanel);

				JLabel lblGroupName = new JLabel("Group name:");
				sl_contentPanel.putConstraint(SpringLayout.NORTH, lblGroupName, 23, SpringLayout.NORTH, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.WEST, lblGroupName, 20, SpringLayout.WEST, contentPanel);
				lblGroupName.setFont(new Font("Tahoma", Font.BOLD, 13));
				contentPanel.add(lblGroupName);

				JLabel lblInfo = new JLabel("Info:");
				sl_contentPanel.putConstraint(SpringLayout.NORTH, lblInfo, 18, SpringLayout.SOUTH, lblGroupName);
				sl_contentPanel.putConstraint(SpringLayout.WEST, lblInfo, 0, SpringLayout.WEST, lblGroupName);
				sl_contentPanel.putConstraint(SpringLayout.EAST, lblInfo, -363, SpringLayout.EAST, contentPanel);
				lblInfo.setFont(new Font("Tahoma", Font.BOLD, 13));
				contentPanel.add(lblInfo);

				final JTextArea textFieldInfo = new JTextArea();
				JScrollPane jsp = new JScrollPane(textFieldInfo);
				jsp.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				sl_contentPanel.putConstraint(SpringLayout.NORTH, jsp, 28, SpringLayout.SOUTH, lblGroupName);
				sl_contentPanel.putConstraint(SpringLayout.WEST, jsp, 16, SpringLayout.EAST, lblInfo);
				sl_contentPanel.putConstraint(SpringLayout.SOUTH, jsp, -24, SpringLayout.SOUTH, contentPanel);
				textFieldInfo.setLineWrap(true);
				contentPanel.add(jsp);

				final JTextField textFieldName = new JTextField();
				textFieldName.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				sl_contentPanel.putConstraint(SpringLayout.EAST, jsp, 0, SpringLayout.EAST, textFieldName);
				sl_contentPanel.putConstraint(SpringLayout.WEST, textFieldName, 6, SpringLayout.EAST, lblGroupName);
				sl_contentPanel.putConstraint(SpringLayout.SOUTH, textFieldName, 0, SpringLayout.SOUTH, lblGroupName);
				sl_contentPanel.putConstraint(SpringLayout.EAST, textFieldName, -66, SpringLayout.EAST, contentPanel);
				contentPanel.add(textFieldName);
				{
					JPanel buttonPane = new JPanel();
					buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
					dialAddGr.getContentPane().add(buttonPane, BorderLayout.SOUTH);
					{
						JButton okButton = new JButton("OK");
						okButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (textFieldName.getText().isEmpty())
									return;
								try {
									client.sendCommandToServer("addgroup");
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								if (textFieldInfo.getText().isEmpty())
									textFieldInfo.setText(" ");
								String s = textFieldName.getText() + "§" + textFieldInfo.getText();
								try {
									String response = client.sendMessageToServerAndGetResponse(s);
									JFrame parent = new JFrame();
									if (response.equalsIgnoreCase("Can't create group with empty name")) {
										dialAddGr.setVisible(false);
										textFieldInfo.setText("");
										textFieldName.setText("");
										JOptionPane.showMessageDialog(parent, response);
										dialAddGr.setVisible(true);
										return;
									}
									if (response.equalsIgnoreCase("Name is used")) {
										dialAddGr.setVisible(false);
										textFieldInfo.setText("");
										textFieldName.setText("");
										JOptionPane.showMessageDialog(parent, response);
										client.sendCommandToServer("end1");
										dialAddGr.setVisible(true);
										return;
									}
									dialAddGr.dispose();
									createNodes(top);
									tree.repaint();
									JOptionPane.showMessageDialog(parent, response);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						});
						okButton.setActionCommand("OK");
						buttonPane.add(okButton);
						dialAddGr.getRootPane().setDefaultButton(okButton);
					}
					{
						JButton cancelButton = new JButton("Cancel");
						cancelButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								dialAddGr.dispose();
							}
						});
						cancelButton.setActionCommand("Cancel");
						buttonPane.add(cancelButton);
					}
				}
				dialAddGr.setVisible(true);
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, button, 16, SpringLayout.SOUTH, jspt);
		sl_panel.putConstraint(SpringLayout.WEST, button, 41, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, button, -46, SpringLayout.EAST, panel);
		button.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(button);
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_2.setLayout(sl_panel_2);

		JButton btnAddProduct = new JButton("Add product");
		btnAddProduct.setMnemonic('a');
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnAddProduct, 71, SpringLayout.NORTH, panel_2);
		btnAddProduct.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAddProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (client == null)
					return;
				try {
					initalizeJDialogAddingP();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnAddProduct, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnAddProduct, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnAddProduct, -10, SpringLayout.EAST, panel_2);
		panel_2.add(btnAddProduct);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setMnemonic('r');
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnRefresh, 17, SpringLayout.SOUTH, btnAddProduct);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnRefresh, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnRefresh, 78, SpringLayout.SOUTH, btnAddProduct);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnRefresh, -10, SpringLayout.EAST, panel_2);
		btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					createNodes(top);
					textPane.setText("");
					textPane_1.setText("");
					textPane_2.setText("");
					textPane_3.setText("");
					textPane_4.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel_2.add(btnRefresh);

		JButton btnGroupReport = new JButton("Group report");
		btnGroupReport.setMnemonic('g');
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnGroupReport, 170, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnGroupReport, -10, SpringLayout.EAST, panel_2);
		btnGroupReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (client == null)
						return;
					initalizeJDialogReportG();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnGroupReport.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_2.add(btnGroupReport);

		JButton btnFullReport = new JButton("Full report");
		btnFullReport.setMnemonic('f');
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnFullReport, 18, SpringLayout.SOUTH, btnGroupReport);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnFullReport, 8, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnFullReport, 46, SpringLayout.SOUTH, btnGroupReport);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnFullReport, -10, SpringLayout.EAST, panel_2);
		btnFullReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client == null)
					return;
				initializeDialogReportF();
			}
		});
		btnFullReport.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_2.add(btnFullReport);

		JPanel panel_3 = new JPanel();

		JButton btnDeleteProduct = new JButton("Delete Product");
		btnDeleteProduct.setMnemonic('d');
		btnDeleteProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (client == null)
					return;
				if (textPane.getText().isEmpty())
					return;
				String toDelete = textPane.getText();
				JFrame frm = new JFrame();
				int n = JOptionPane.showConfirmDialog(frm, "Would you delete this product from BD??", "Delete product",
						JOptionPane.YES_NO_OPTION);
				if (n == 1)
					return;
				if (n == 0) {
					try {
						JFrame jrm = new JFrame();
						client.sendCommandToServer("deleteproduct");
						String response = client.sendMessageToServerAndGetResponse(toDelete);
						JOptionPane.showMessageDialog(jrm, response);
						createNodes(top);
						model.reload();
						tree.repaint();
						textPane.setText("");
						textPane_1.setText("");
						textPane_2.setText("");
						textPane_3.setText("");
						textPane_4.setText("");
						panel_1.repaint();
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		});
		sl_panel_1.putConstraint(SpringLayout.WEST, btnDeleteProduct, 0, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnDeleteProduct, -40, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnDeleteProduct, 108, SpringLayout.WEST, panel_1);
		btnDeleteProduct.setFont(new Font("SansSerif", Font.PLAIN, 11));
		panel_1.add(btnDeleteProduct);

		JButton btnEditProduct = new JButton("Edit Product");
		btnEditProduct.setMnemonic('e');
		sl_panel_1.putConstraint(SpringLayout.WEST, btnEditProduct, -102, SpringLayout.EAST, panel_1);
		btnEditProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (client == null)
						return;
					if (textPane.getText().isEmpty())
						return;
					initializeEditProductDialog();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void initializeEditProductDialog() throws IOException {
				final JDialog dialEdit = new JDialog();
				final JPanel contentPanel = new JPanel();
				final JTextField textField;
				final JTextField textField_1;
				final JTextArea textField_2;
				dialEdit.setTitle("Edit product");
				dialEdit.setBounds(100, 100, 450, 300);
				dialEdit.getContentPane().setLayout(new BorderLayout());
				dialEdit.getContentPane().add(contentPanel, BorderLayout.CENTER);
				SpringLayout sl_contentPanel = new SpringLayout();
				contentPanel.setLayout(sl_contentPanel);

				JTextArea txtrEditingProductProduct = new JTextArea();
				sl_contentPanel.putConstraint(SpringLayout.SOUTH, txtrEditingProductProduct, 32, SpringLayout.NORTH,
						contentPanel);
				txtrEditingProductProduct.setLineWrap(true);
				txtrEditingProductProduct.setEditable(false);
				sl_contentPanel.putConstraint(SpringLayout.NORTH, txtrEditingProductProduct, 10, SpringLayout.NORTH,
						contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.WEST, txtrEditingProductProduct, 10, SpringLayout.WEST,
						contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.EAST, txtrEditingProductProduct, -10, SpringLayout.EAST,
						contentPanel);
				txtrEditingProductProduct.setFont(new Font("Dialog", Font.BOLD, 12));
				txtrEditingProductProduct.setBackground(UIManager.getColor("Button.background"));
				txtrEditingProductProduct.setText("Editing product: " + textPane.getText());
				contentPanel.add(txtrEditingProductProduct);

				textField = new JTextField();
				sl_contentPanel.putConstraint(SpringLayout.NORTH, textField, 11, SpringLayout.SOUTH,
						txtrEditingProductProduct);
				sl_contentPanel.putConstraint(SpringLayout.WEST, textField, -280, SpringLayout.EAST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.EAST, textField, -56, SpringLayout.EAST, contentPanel);
				contentPanel.add(textField);

				textField.setColumns(10);

				textField_1 = new JTextField();
				sl_contentPanel.putConstraint(SpringLayout.WEST, textField_1, 0, SpringLayout.WEST, textField);
				sl_contentPanel.putConstraint(SpringLayout.EAST, textField_1, -56, SpringLayout.EAST, contentPanel);
				textField_1.setColumns(10);
				contentPanel.add(textField_1);

				textField_2 = new JTextArea();
				textField_2.setLineWrap(true);
				JScrollPane jsp = new JScrollPane(textField_2);
				sl_contentPanel.putConstraint(SpringLayout.NORTH, jsp, 17, SpringLayout.SOUTH, textField_1);
				sl_contentPanel.putConstraint(SpringLayout.WEST, jsp, 152, SpringLayout.WEST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.EAST, jsp, 0, SpringLayout.EAST, textField);
				textField_2.setColumns(10);
				contentPanel.add(jsp);

				final JSpinner spinner = new JSpinner();
				spinner.setToolTipText("If you don't want to change price leave \"-1\" value");
				spinner.setModel(new SpinnerNumberModel(new Integer(-1), new Integer(-1), null, new Integer(10)));
				sl_contentPanel.putConstraint(SpringLayout.WEST, spinner, 278, SpringLayout.WEST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.EAST, spinner, -10, SpringLayout.EAST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.SOUTH, jsp, -19, SpringLayout.NORTH, spinner);
				sl_contentPanel.putConstraint(SpringLayout.SOUTH, spinner, -10, SpringLayout.SOUTH, contentPanel);
				contentPanel.add(spinner);

				JLabel lblNewName = new JLabel("New name:");
				sl_contentPanel.putConstraint(SpringLayout.NORTH, lblNewName, 11, SpringLayout.SOUTH,
						txtrEditingProductProduct);
				sl_contentPanel.putConstraint(SpringLayout.WEST, lblNewName, 32, SpringLayout.WEST, contentPanel);
				contentPanel.add(lblNewName);

				JLabel lblNewManufacturer = new JLabel("New manufacturer:");
				sl_contentPanel.putConstraint(SpringLayout.NORTH, lblNewManufacturer, 21, SpringLayout.SOUTH,
						lblNewName);
				sl_contentPanel.putConstraint(SpringLayout.NORTH, textField_1, -3, SpringLayout.NORTH,
						lblNewManufacturer);
				sl_contentPanel.putConstraint(SpringLayout.WEST, lblNewManufacturer, 0, SpringLayout.WEST, lblNewName);
				contentPanel.add(lblNewManufacturer);

				JLabel lblNewLabel = new JLabel("New info:");
				sl_contentPanel.putConstraint(SpringLayout.NORTH, lblNewLabel, 21, SpringLayout.SOUTH,
						lblNewManufacturer);
				sl_contentPanel.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, lblNewName);
				contentPanel.add(lblNewLabel);

				JLabel lblNewPrice = new JLabel("New price:");
				sl_contentPanel.putConstraint(SpringLayout.WEST, lblNewPrice, 201, SpringLayout.WEST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblNewPrice, 0, SpringLayout.SOUTH, spinner);
				sl_contentPanel.putConstraint(SpringLayout.EAST, lblNewPrice, -11, SpringLayout.WEST, spinner);
				contentPanel.add(lblNewPrice);
				{
					JPanel buttonPane = new JPanel();
					buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
					dialEdit.getContentPane().add(buttonPane, BorderLayout.SOUTH);
					{
						JButton okButton = new JButton("OK");
						okButton.setActionCommand("OK");
						buttonPane.add(okButton);
						dialEdit.getRootPane().setDefaultButton(okButton);
						okButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								JFrame parent = new JFrame();
								if ((textField.getText().isEmpty()) && (textField_2.getText().isEmpty())
										&& (textField_1.getText().isEmpty())
										&& (spinner.getValue().toString().equals("-1"))) {
									JOptionPane.showMessageDialog(parent, "You didn't add new information. Closing...");
									dialEdit.dispose();
									return;
								}
								try {
									client.sendCommandToServer("editproduct");
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								if (textField.getText().equals(" "))
									textField.setText("  ");
								if (textField_1.getText().isEmpty())
									textField_1.setText(" ");
								if (textField_2.getText().isEmpty())
									textField_2.setText(" ");
								if (textField.getText().isEmpty())
									textField.setText(" ");
								String s = "";
								s += textPane.getText() + "§" + textField.getText() + "§" + textField_2.getText() + "§"
										+ textField_1.getText() + "§" + spinner.getValue();
								try {
									String response = client.sendMessageToServerAndGetResponse(s);
									if (response.equalsIgnoreCase("Name is used")) {
										dialEdit.setVisible(false);
										textField.setText("");
										textField_1.setText("");
										textField_2.setText("");
										JOptionPane.showMessageDialog(parent, response);
										client.sendCommandToServer("end1");
										dialEdit.setVisible(true);
										return;
									}
									if (response.equalsIgnoreCase("Can't use white space to rename")) {
										dialEdit.setVisible(false);
										textField.setText("");
										textField_1.setText("");
										textField_2.setText("");
										JOptionPane.showMessageDialog(parent, response);
										client.sendCommandToServer("end1");
										dialEdit.setVisible(true);
										return;
									}
									dialEdit.dispose();
									createNodes(top);
									model.reload();
									tree.repaint();
									JOptionPane.showMessageDialog(parent, response);
									client.sendCommandToServer("search");
									String res;
									if (textField.getText().equalsIgnoreCase(" ")) {
										res = client.sendMessageToServerAndGetResponse(textPane.getText());
									} else {
										res = client.sendMessageToServerAndGetResponse(textField.getText());
									}
									String[] array1 = res.split("§");
									textPane.setText(array1[1]);
									textPane_1.setText(array1[3]);
									textPane_2.setText(array1[4]);
									textPane_3.setText(array1[5]);
									textPane_4.setText(array1[2]);
									panel_1.repaint();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						});
					}
					{
						JButton cancelButton = new JButton("Cancel");
						cancelButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								dialEdit.dispose();
							}
						});
						cancelButton.setActionCommand("Cancel");
						buttonPane.add(cancelButton);
					}
				}
				dialEdit.setVisible(true);
			}

		});

		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnEditProduct, -40, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnEditProduct, 0, SpringLayout.EAST, panel_1);
		btnEditProduct.setFont(new Font("SansSerif", Font.PLAIN, 11));
		panel_1.add(btnEditProduct);

		JButton btnSelladd = new JButton("Sell/Add Products");
		btnSelladd.setMnemonic('s');
		btnSelladd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client == null)
					return;
				if (textPane.getText().equals(""))
					return;
				initializeChangeQuantity(textPane.getText());
			}

			private void initializeChangeQuantity(final String s) {
				final JDialog dialogSetQuantity = new JDialog();
				JPanel contentPanel = new JPanel();
				final JSpinner spinner = new JSpinner();
				dialogSetQuantity.setTitle("Change product quantity");
				dialogSetQuantity.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialogSetQuantity.setBounds(100, 100, 450, 212);
				dialogSetQuantity.getContentPane().setLayout(new BorderLayout());
				contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				dialogSetQuantity.getContentPane().add(contentPanel, BorderLayout.CENTER);

				JButton btnSell = new JButton("Sell");
				btnSell.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if (spinner.getValue().toString().equals("0"))
								return;
							client.sendCommandToServer("changequantity");
							String response = client
									.sendMessageToServerAndGetResponse(s + "§" + "-" + spinner.getValue().toString());
							JFrame jrm = new JFrame();
							if (response.equalsIgnoreCase("Can't change quantity,too much to delete")) {
								JOptionPane.showMessageDialog(jrm, response);
								return;
							}
							JOptionPane.showMessageDialog(jrm, response);
							int updatedQuantity = Integer.parseInt(textPane_2.getText())
									- Integer.parseInt(spinner.getValue().toString());
							textPane_2.setText("" + updatedQuantity);
							dialogSetQuantity.dispose();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				});
				JButton btnAdd = new JButton("Add");
				btnAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							JFrame jrm = new JFrame();
							if (spinner.getValue().toString().equals("0"))
								return;
							client.sendCommandToServer("changequantity");
							String response = client
									.sendMessageToServerAndGetResponse(s + "§" + "+" + spinner.getValue().toString());
							JOptionPane.showMessageDialog(jrm, response);
							int updatedQuantity = Integer.parseInt(textPane_2.getText())
									+ Integer.parseInt(spinner.getValue().toString());
							textPane_2.setText("" + updatedQuantity);
							dialogSetQuantity.dispose();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				});

				spinner.setToolTipText("Enter quantity value you want to sell/add");
				spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
				SpringLayout sl_contentPanel = new SpringLayout();
				sl_contentPanel.putConstraint(SpringLayout.NORTH, spinner, 33, SpringLayout.NORTH, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.WEST, spinner, 160, SpringLayout.WEST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.EAST, spinner, 253, SpringLayout.WEST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.NORTH, btnAdd, 87, SpringLayout.NORTH, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.WEST, btnAdd, 259, SpringLayout.WEST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.EAST, btnAdd, 373, SpringLayout.WEST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.NORTH, btnSell, 87, SpringLayout.NORTH, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.WEST, btnSell, 50, SpringLayout.WEST, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.EAST, btnSell, 162, SpringLayout.WEST, contentPanel);
				contentPanel.setLayout(sl_contentPanel);
				contentPanel.add(btnSell);
				contentPanel.add(btnAdd);
				contentPanel.add(spinner);
				{
					JPanel buttonPane = new JPanel();
					buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
					dialogSetQuantity.getContentPane().add(buttonPane, BorderLayout.SOUTH);
					{
						JButton cancelButton = new JButton("Cancel");
						cancelButton.setActionCommand("Cancel");
						buttonPane.add(cancelButton);
						cancelButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								dialogSetQuantity.dispose();

							}
						});
					}
				}
				dialogSetQuantity.setVisible(true);
			}
		});
		sl_panel_1.putConstraint(SpringLayout.WEST, btnSelladd, 6, SpringLayout.EAST, btnDeleteProduct);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnSelladd, -40, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnSelladd, -6, SpringLayout.WEST, btnEditProduct);
		btnSelladd.setFont(new Font("SansSerif", Font.PLAIN, 11));
		panel_1.add(btnSelladd);

		Label label = new Label("Name: ");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label, 22, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, label, 112, SpringLayout.WEST, panel_1);
		label.setFont(new Font("Dialog", Font.BOLD, 12));
		panel_1.add(label);

		Label label_1 = new Label("Manufacturer:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label_1, 6, SpringLayout.SOUTH, label);
		sl_panel_1.putConstraint(SpringLayout.WEST, label_1, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, label_1, 118, SpringLayout.WEST, panel_1);
		label_1.setFont(new Font("Dialog", Font.BOLD, 12));
		panel_1.add(label_1);

		Label label_2 = new Label("Quantity:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label_2, 6, SpringLayout.SOUTH, label_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, label_2, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, label_2, 118, SpringLayout.WEST, panel_1);
		label_2.setFont(new Font("Dialog", Font.BOLD, 12));
		panel_1.add(label_2);

		Label label_3 = new Label("Price:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label_3, 6, SpringLayout.SOUTH, label_2);
		sl_panel_1.putConstraint(SpringLayout.WEST, label_3, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, label_3, 118, SpringLayout.WEST, panel_1);
		label_3.setFont(new Font("Dialog", Font.BOLD, 12));
		panel_1.add(label_3);

		Label label_4 = new Label("Description:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label_4, 6, SpringLayout.SOUTH, label_3);
		sl_panel_1.putConstraint(SpringLayout.WEST, label_4, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, label_4, -88, SpringLayout.NORTH, btnDeleteProduct);
		sl_panel_1.putConstraint(SpringLayout.EAST, label_4, 118, SpringLayout.WEST, panel_1);
		label_4.setFont(new Font("Dialog", Font.BOLD, 12));
		panel_1.add(label_4);

		textPane = new JTextPane();
		textPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panel_1.putConstraint(SpringLayout.WEST, textPane, -205, SpringLayout.EAST, panel_1);
		textPane.setEditable(false);
		sl_panel_1.putConstraint(SpringLayout.EAST, textPane, -10, SpringLayout.EAST, panel_1);
		panel_1.add(textPane);

		textPane_1 = new JTextPane();
		textPane_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panel_1.putConstraint(SpringLayout.NORTH, textPane_1, 51, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, textPane, -6, SpringLayout.NORTH, textPane_1);
		textPane_1.setEditable(false);
		sl_panel_1.putConstraint(SpringLayout.WEST, textPane_1, -205, SpringLayout.EAST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textPane_1, -10, SpringLayout.EAST, panel_1);
		panel_1.add(textPane_1);

		textPane_2 = new JTextPane(); //
		textPane_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panel_1.putConstraint(SpringLayout.NORTH, textPane_2, 7, SpringLayout.SOUTH, textPane_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, textPane_2, 19, SpringLayout.EAST, label_2);
		sl_panel_1.putConstraint(SpringLayout.EAST, textPane_2, -10, SpringLayout.EAST, panel_1);
		textPane_2.setEditable(false);
		panel_1.add(textPane_2);

		textPane_3 = new JTextPane();
		textPane_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panel_1.putConstraint(SpringLayout.NORTH, textPane_3, 8, SpringLayout.SOUTH, textPane_2);
		sl_panel_1.putConstraint(SpringLayout.WEST, textPane_3, 19, SpringLayout.EAST, label_3);
		sl_panel_1.putConstraint(SpringLayout.EAST, textPane_3, -10, SpringLayout.EAST, panel_1);
		textPane_3.setEditable(false);
		panel_1.add(textPane_3);

		textPane_4 = new JTextPane();
		textPane_4.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textPane_4.setEditable(false);
		JScrollPane jsp2 = new JScrollPane(textPane_4);
		sl_panel_1.putConstraint(SpringLayout.NORTH, jsp2, 10, SpringLayout.SOUTH, textPane_3);
		sl_panel_1.putConstraint(SpringLayout.WEST, jsp2, 19, SpringLayout.EAST, label_4);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, jsp2, -47, SpringLayout.NORTH, btnEditProduct);
		sl_panel_1.putConstraint(SpringLayout.EAST, jsp2, -10, SpringLayout.EAST, panel_1);
		jsp2.setBorder(null);
		panel_1.add(jsp2);

		JButton btnNewButton = new JButton("Delete group");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client == null)
					return;
				if (selectedGroupAtTree == null)
					return;
				JFrame frm = new JFrame();
				int n = JOptionPane.showConfirmDialog(frm,
						"Would you delete this group and all it's products from BD??", "Delete group",
						JOptionPane.YES_NO_OPTION);
				if (n == 1)
					return;
				if (n == 0) {
					try {
						JFrame jrm = new JFrame();
						client.sendCommandToServer("deletegroup");
						String response = client.sendMessageToServerAndGetResponse(selectedGroupAtTree);
						JOptionPane.showMessageDialog(jrm, response);
						createNodes(top);
						model.reload();
						tree.repaint();
						textPane.setText("");
						textPane_1.setText("");
						textPane_2.setText("");
						textPane_3.setText("");
						textPane_4.setText("");
						panel_1.repaint();
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewButton, -43, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, btnNewButton, 41, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNewButton, -24, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewButton, -46, SpringLayout.EAST, panel);
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel.add(btnNewButton);

		JButton btnGroupsInformation = new JButton("Group Info");
		btnGroupsInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client == null)
					return;
				if (selectedGroupAtTree == null)
					return;
				try {
					initializeGroupInfoDialog();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			private void initializeGroupInfoDialog() throws IOException {
				final JDialog grInfoDialog = new JDialog();
				final JPanel contentPanel = new JPanel();
				grInfoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				grInfoDialog.setTitle("Group Information");
				grInfoDialog.setBounds(100, 100, 450, 300);
				grInfoDialog.getContentPane().setLayout(new BorderLayout());
				contentPanel.setToolTipText(
						"Click on \"Edit\" button and than on \"Save changes\" to change information about group");
				contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				grInfoDialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
				SpringLayout sl_contentPanel = new SpringLayout();
				contentPanel.setLayout(sl_contentPanel);
				{
					final JLabel lblName = new JLabel("Name:");
					sl_contentPanel.putConstraint(SpringLayout.NORTH, lblName, 10, SpringLayout.NORTH, contentPanel);
					sl_contentPanel.putConstraint(SpringLayout.WEST, lblName, 27, SpringLayout.WEST, contentPanel);
					sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblName, 45, SpringLayout.NORTH, contentPanel);
					lblName.setFont(new Font("Dialog", Font.BOLD, 14));
					contentPanel.add(lblName);

					final JLabel lblInfo = new JLabel("Info:");
					sl_contentPanel.putConstraint(SpringLayout.NORTH, lblInfo, 17, SpringLayout.SOUTH, lblName);
					sl_contentPanel.putConstraint(SpringLayout.WEST, lblInfo, 27, SpringLayout.WEST, contentPanel);
					lblInfo.setFont(new Font("Dialog", Font.BOLD, 14));
					contentPanel.add(lblInfo);

					final JTextField textField = new JTextField();
					textField.setText(selectedGroupAtTree);
					sl_contentPanel.putConstraint(SpringLayout.WEST, textField, 92, SpringLayout.WEST, contentPanel);
					sl_contentPanel.putConstraint(SpringLayout.EAST, textField, -91, SpringLayout.EAST, contentPanel);
					sl_contentPanel.putConstraint(SpringLayout.EAST, lblName, -11, SpringLayout.WEST, textField);
					sl_contentPanel.putConstraint(SpringLayout.NORTH, textField, 8, SpringLayout.NORTH, lblName);
					textField.setBackground(Color.WHITE);
					textField.setEditable(false);
					textField.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
					contentPanel.add(textField);
					textField.setColumns(10);

					final JTextPane textInformation = new JTextPane();
					client.sendCommandToServer("getgroupinfo");
					String response = client.sendMessageToServerAndGetResponse(selectedGroupAtTree);
					final String[] temparray = response.split("§");
					System.out.println(temparray[0]);
					textInformation.setText(temparray[1]);
					JScrollPane jsp = new JScrollPane(textInformation);
					sl_contentPanel.putConstraint(SpringLayout.NORTH, jsp, 24, SpringLayout.SOUTH, textField);
					sl_contentPanel.putConstraint(SpringLayout.EAST, lblInfo, -16, SpringLayout.WEST, jsp);
					sl_contentPanel.putConstraint(SpringLayout.WEST, jsp, 92, SpringLayout.WEST, contentPanel);
					sl_contentPanel.putConstraint(SpringLayout.EAST, jsp, 0, SpringLayout.EAST, textField);
					textInformation.setEditable(false);
					textInformation.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
					contentPanel.add(jsp);

					JButton btnEdit = new JButton("Edit");
					btnEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							textField.setEditable(true);
							textInformation.setEditable(true);
							textField.setText("");
							textInformation.setText("");
						}
					});
					sl_contentPanel.putConstraint(SpringLayout.NORTH, btnEdit, 76, SpringLayout.SOUTH, lblInfo);
					sl_contentPanel.putConstraint(SpringLayout.WEST, btnEdit, 10, SpringLayout.WEST, contentPanel);
					sl_contentPanel.putConstraint(SpringLayout.SOUTH, btnEdit, -61, SpringLayout.SOUTH, contentPanel);
					btnEdit.setToolTipText("Click on this and than start to edit information");
					contentPanel.add(btnEdit);

					JButton btnNewButton = new JButton("Save changes");

					{
						sl_contentPanel.putConstraint(SpringLayout.SOUTH, jsp, -40, SpringLayout.NORTH, btnNewButton);
						sl_contentPanel.putConstraint(SpringLayout.NORTH, btnNewButton, 6, SpringLayout.SOUTH, btnEdit);
						sl_contentPanel.putConstraint(SpringLayout.WEST, btnNewButton, 10, SpringLayout.WEST,
								contentPanel);
						contentPanel.add(btnNewButton);
						btnNewButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								JFrame framechange = new JFrame();
								if ((textInformation.getText().equalsIgnoreCase(temparray[1]))
										&& (textField.getText().equalsIgnoreCase(temparray[0]))) {
									grInfoDialog.setVisible(false);
									JOptionPane.showMessageDialog(framechange, "Nothing changed, try again");
									grInfoDialog.setVisible(true);
									return;
								}
								if (textField.getText().equalsIgnoreCase("")
										&& textInformation.getText().equalsIgnoreCase("")) {
									grInfoDialog.setVisible(false);
									JOptionPane.showMessageDialog(framechange,
											"You didnt input new infomation, try again");
									grInfoDialog.setVisible(true);
									return;
								}
								try {
									client.sendCommandToServer("editgroup");
									if (textInformation.getText().isEmpty())
										textInformation.setText(" ");
									if (textField.getText().isEmpty())
										textField.setText(" ");
									String response = client.sendMessageToServerAndGetResponse(
											temparray[0] + "§" + textField.getText() + "§" + textInformation.getText());
									if (response.equals("Can't change name to whitespace")) {
										grInfoDialog.setVisible(false);
										JOptionPane.showMessageDialog(framechange, response);
										grInfoDialog.setVisible(true);
										return;
									}
									if (response.equals("Name is used")) {
										grInfoDialog.setVisible(false);
										JOptionPane.showMessageDialog(framechange, response);
										grInfoDialog.setVisible(true);
										return;
									}
									grInfoDialog.dispose();
									JOptionPane.showMessageDialog(framechange, response);
									createNodes(top);
									return;
								} catch (IOException e1) {

									e1.printStackTrace();
								}

							}
						});
					}
					{
						JButton btnCancel = new JButton("Cancel");
						sl_contentPanel.putConstraint(SpringLayout.SOUTH, btnCancel, -30, SpringLayout.SOUTH,
								contentPanel);
						sl_contentPanel.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.EAST,
								contentPanel);
						contentPanel.add(btnCancel);
						btnCancel.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								textField.setEditable(true);
								textInformation.setEditable(true);
								grInfoDialog.dispose();

							}
						});
					}

					grInfoDialog.setVisible(true);
				}

			}
		});
		btnGroupsInformation.setMnemonic('i');
		sl_panel.putConstraint(SpringLayout.SOUTH, button, -6, SpringLayout.NORTH, btnGroupsInformation);
		sl_panel.putConstraint(SpringLayout.NORTH, btnGroupsInformation, -68, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, btnGroupsInformation, 41, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnGroupsInformation, -46, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnGroupsInformation, -49, SpringLayout.SOUTH, panel);
		btnGroupsInformation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(btnGroupsInformation);

		final JButton btnConnect = new JButton("Connect");
		btnConnect.setMnemonic('c');
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnConnect, -59, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnConnect, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnConnect, -10, SpringLayout.EAST, panel_2);
		btnConnect.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (countclickconnect == 0) {
					btnConnect.setText("Disconnect");
					btnConnect.repaint();
					try {
						client = new ARMClient();
						countclickconnect = 1;
						createNodes(top);
						return;
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (countclickconnect == 1) {
					btnConnect.setText("Connect");
					btnConnect.repaint();
					try {
						client.sendCommandToServer("end");
						countclickconnect = 0;
						client = null;
						top.removeAllChildren();
						model.reload(top);
						textPane.setText("");
						textPane_1.setText("");
						textPane_2.setText("");
						textPane_3.setText("");
						textPane_4.setText("");
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnConnect, -31, SpringLayout.SOUTH, panel_2);
		panel_2.add(btnConnect);
		SpringLayout sl_panel_3 = new SpringLayout();
		panel_3.setLayout(sl_panel_3);

		final JTextField textField = new JTextField();
		textField.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panel_3.putConstraint(SpringLayout.NORTH, textField, 20, SpringLayout.NORTH, panel_3);
		sl_panel_3.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, panel_3);
		sl_panel_3.putConstraint(SpringLayout.SOUTH, textField, -23, SpringLayout.SOUTH, panel_3);
		sl_panel_3.putConstraint(SpringLayout.EAST, textField, 219, SpringLayout.WEST, panel_3);
		panel_3.add(textField);
		textField.setColumns(10);

		JButton btnSearch = new JButton("Search");
		sl_panel_3.putConstraint(SpringLayout.WEST, btnSearch, -117, SpringLayout.EAST, panel_3);
		sl_panel_3.putConstraint(SpringLayout.EAST, btnSearch, -10, SpringLayout.EAST, panel_3);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (client == null)
						return;
					if (textField.getText().isEmpty())
						return;
					client.sendCommandToServer("search");
					String productName = textField.getText();
					String response = client.sendMessageToServerAndGetResponse(productName);
					if (response.equalsIgnoreCase("")) {
						return;
					}
					String[] array = response.split("§");
					textPane.setText(array[1]);
					textPane_1.setText(array[3]);
					textPane_2.setText(array[4]);
					textPane_3.setText(array[5]);
					textPane_4.setText(array[2]);
					panel_1.repaint();

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		sl_panel_3.putConstraint(SpringLayout.NORTH, btnSearch, 20, SpringLayout.NORTH, panel_3);
		btnSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_3.add(btnSearch);
		GroupLayout groupLayout = new GroupLayout(frmWarehouseManagementSystem.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE).addGap(3)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 342, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(panel_3,
										GroupLayout.PREFERRED_SIZE, 339, GroupLayout.PREFERRED_SIZE)))
						.addGap(6).addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)));
		groupLayout
				.setVerticalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 412, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup().addGap(10)
										.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 317,
												GroupLayout.PREFERRED_SIZE)
										.addGap(6).addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 69,
												GroupLayout.PREFERRED_SIZE))
								.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 412, GroupLayout.PREFERRED_SIZE));
		frmWarehouseManagementSystem.getContentPane().setLayout(groupLayout);
	}

	/**
	 * Метод, який генерує гілки дерева груп-товарів. Використовується для
	 * завантаження при початку роботи, для оновлення під час роботи тощо
	 */
	private void createNodes(DefaultMutableTreeNode top) throws IOException {
		isFreeTree = false;
		if (client == null)
			return;
		top.removeAllChildren();
		tree = new JTree(top);
		client.sendCommandToServer("groupnames");
		String response = client.sendMessageToServerAndGetResponse("go");
		String[] array = response.split("§");
		for (String groupName : array) {
			if (groupName == null) {
				continue;
			}
			category = new DefaultMutableTreeNode(groupName);
			model.insertNodeInto(category, top, top.getChildCount());
			client.sendCommandToServer("groupnames1");
			String s1 = client.sendCommandToServer(groupName);
			String[] array1 = s1.split("§");
			for (String productname : array1) {
				if (productname == null)
					continue;
				product = new DefaultMutableTreeNode(productname);
				model.insertNodeInto(product, category, category.getChildCount());
			}
			model.reload();
			tree.repaint();
			jspt.revalidate();
			isFreeTree = true;

		}
	}

	/**
	 * Метод ініціалізовує діалогове вікно, у якому можна згенерувати повний
	 * звіт по БД
	 */
	public void initializeDialogReportF() {
		JDialog dialGroupF = new JDialog();
		JPanel contentPanel = new JPanel();
		final JTextPane textPane;
		dialGroupF.setTitle("Full Report");
		dialGroupF.setBounds(100, 100, 686, 438);
		dialGroupF.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialGroupF.getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		{
			textPane = new JTextPane();
			JScrollPane sp = new JScrollPane(textPane);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, sp, 10, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, sp, 10, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, sp, -15, SpringLayout.SOUTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, sp, -17, SpringLayout.EAST, contentPanel);
			textPane.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			textPane.setEditable(false);
			contentPanel.add(sp);
			contentPanel.repaint();
		}
		dialGroupF.setVisible(true);
		{
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			dialGroupF.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			{
				JButton genButton = new JButton("Generate report");
				genButton.addActionListener(new ActionListener() {
					String response;

					public void actionPerformed(ActionEvent e) {
						try {
							String response = client.sendCommandToServer("fullreport");
							textPane.setText(response);
							textPane.repaint();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

				genButton.setActionCommand("Generate");
				buttonPanel.add(genButton);
				dialGroupF.getRootPane().setDefaultButton(genButton);

			}
			{
				JButton saveButton = new JButton("Save as TXT file\r\n");
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String report = textPane.getText();
						try {
							writeFullReport(report);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
				saveButton.setActionCommand("Save");
				buttonPanel.add(saveButton);
			}
		}
	}

	/**
	 * Метод ініціалізовує діалогове вікно, у якому можна згенерувати звіт по
	 * певній групі
	 */
	public void initalizeJDialogReportG() throws IOException {
		JDialog dialGroupR = new JDialog();
		JPanel contentPanel = new JPanel();
		final JTextPane textPane;
		final JComboBox<String> comboBox1;
		dialGroupR.setTitle("Group Report");
		dialGroupR.setBounds(100, 100, 686, 438);
		dialGroupR.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialGroupR.getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		{
			comboBox1 = new JComboBox();
			sl_contentPanel.putConstraint(SpringLayout.NORTH, comboBox1, 10, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, comboBox1, 10, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, comboBox1, 249, SpringLayout.WEST, contentPanel);
			client.sendCommandToServer("groupnames");
			String response = client.sendCommandToServer("go");
			String[] array;
			if (response != null) {
				array = response.split("§");
				for (String name : array) {
					if (name == null)
						continue;
					comboBox1.addItem(name);
					comboBox1.repaint();
				}

			}
			contentPanel.add(comboBox1);
		}
		{
			textPane = new JTextPane();
			JScrollPane sp = new JScrollPane(textPane);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, sp, 18, SpringLayout.SOUTH, comboBox1);
			sl_contentPanel.putConstraint(SpringLayout.WEST, sp, 7, SpringLayout.WEST, comboBox1);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, sp, -10, SpringLayout.SOUTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, sp, -10, SpringLayout.EAST, contentPanel);
			textPane.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			sp.setVisible(true);
			textPane.setEditable(false);
			contentPanel.add(sp);

			contentPanel.repaint();
		}
		{
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			dialGroupR.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			{
				JButton genButton = new JButton("Generate report");
				genButton.addActionListener(new ActionListener() {
					String response;

					public void actionPerformed(ActionEvent e) {
						try {
							System.out.println(client.sendCommandToServer("groupreport"));
							String response = client
									.sendMessageToServerAndGetResponse(comboBox1.getSelectedItem().toString());
							textPane.setText(response);
							textPane.repaint();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

				genButton.setActionCommand("OK");
				buttonPanel.add(genButton);
				dialGroupR.getRootPane().setDefaultButton(genButton);

			}
			{
				JButton saveButton = new JButton("Save as TXT file\r\n");
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String group = comboBox1.getSelectedItem().toString();
						String report = textPane.getText();
						try {
							writeGroupReport(report, group);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
				saveButton.setActionCommand("Cancel");
				buttonPanel.add(saveButton);
			}
		}
		dialGroupR.setVisible(true);
	}

	/**
	 * Отримує на вхід повний звіт по БД, генерує назву файлу, створює
	 * унікальний файл і записує у нього звіт і зберігає локально
	 */
	private void writeFullReport(String report) throws IOException {
		Date date = new Date();
		@SuppressWarnings("deprecation")
		File reportfile = new File("FULL_REPORT_" + date.getDate() + "-" + date.getMonth() + "-" + date.getHours() + "-"
				+ date.getSeconds() + ".txt");
		reportfile.createNewFile();
		FileWriter fwr = new FileWriter(reportfile);
		BufferedWriter bwr = new BufferedWriter(fwr);
		bwr.write(report);
		bwr.close();
		fwr.close();
		date = null;
	}

	/**
	 * Отримує на вхід груповий звіт, назву групи, генерує назву файлу, створює
	 * унікальний файл і записує у нього звіт і зберігає локально
	 */
	private void writeGroupReport(String report, String groupName) throws IOException {
		Date date = new Date();
		@SuppressWarnings("deprecation")
		File reportfile = new File("GROUP_REPORT_" + groupName + "_" + date.getDate() + "-" + date.getMonth() + "-"
				+ date.getHours() + "-" + date.getSeconds() + ".txt");
		reportfile.createNewFile();
		FileWriter fwr = new FileWriter(reportfile);
		BufferedWriter bwr = new BufferedWriter(fwr);
		bwr.write(report);
		bwr.close();
		fwr.close();
		date = null;
	}

	/**
	 * Метод ініціалізовує діалогове вікно, у якому можна провести операцію
	 * додавання нового продукта до БД
	 */
	public void initalizeJDialogAddingP() throws IOException {
		final JDialog dialog = new JDialog();
		JPanel contentPanel = new JPanel();
		final JTextArea textField_2;
		final JTextField textField;
		final JTextField textField_1;
		dialog.setModalityType(ModalityType.TOOLKIT_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setTitle("Add Product");
		dialog.setBounds(100, 100, 450, 426);
		dialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

		final JComboBox comboBox = new JComboBox();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, comboBox, 0, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, comboBox, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, comboBox, -189, SpringLayout.EAST, contentPanel);
		client.sendCommandToServer("groupnames");
		String response = client.sendMessageToServerAndGetResponse("go");
		String[] array = response.split("§");
		for (String group : array)
			comboBox.addItem(group);
		contentPanel.add(comboBox);

		textField_2 = new JTextArea();
		textField_2.setLineWrap(true);
		textField_2.setColumns(10);
		JScrollPane jsp1 = new JScrollPane(textField_2);
		sl_contentPanel.putConstraint(SpringLayout.WEST, jsp1, 20, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, jsp1, -13, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, jsp1, -69, SpringLayout.EAST, contentPanel);
		contentPanel.add(jsp1);
		textField_2.setColumns(10);

		JTextArea label_2 = new JTextArea("Info:");
		label_2.setBackground(UIManager.getColor("Button.background"));
		label_2.setEditable(false);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, jsp1, 6, SpringLayout.SOUTH, label_2);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, label_2, -81, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, label_2, 24, SpringLayout.WEST, contentPanel);
		label_2.setFont(new Font("Dialog", Font.BOLD, 12));
		contentPanel.add(label_2);

		JTextArea label = new JTextArea("Product Name:");
		label.setBackground(UIManager.getColor("Button.background"));
		label.setEditable(false);
		sl_contentPanel.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, comboBox);
		label.setFont(new Font("Dialog", Font.BOLD, 12));
		contentPanel.add(label);

		JTextArea label_1 = new JTextArea("Manufacturer:");
		label_1.setBackground(UIManager.getColor("Button.background"));
		label_1.setEditable(false);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, label, -6, SpringLayout.NORTH, label_1);
		sl_contentPanel.putConstraint(SpringLayout.WEST, label_1, 0, SpringLayout.WEST, comboBox);
		label_1.setFont(new Font("Dialog", Font.BOLD, 12));
		contentPanel.add(label_1);

		JTextArea lblQuantity = new JTextArea("Quantity:");
		lblQuantity.setBackground(UIManager.getColor("Button.background"));
		lblQuantity.setEditable(false);
		sl_contentPanel.putConstraint(SpringLayout.EAST, lblQuantity, -206, SpringLayout.EAST, contentPanel);
		lblQuantity.setFont(new Font("SansSerif", Font.BOLD, 12));
		contentPanel.add(lblQuantity);

		final JSpinner spinner = new JSpinner();
		spinner.setToolTipText("Use spinner or input number");
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
		sl_contentPanel.putConstraint(SpringLayout.NORTH, spinner, 6, SpringLayout.SOUTH, lblQuantity);
		sl_contentPanel.putConstraint(SpringLayout.WEST, spinner, 157, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, spinner, -189, SpringLayout.EAST, contentPanel);
		contentPanel.add(spinner);

		JTextArea lblPrice = new JTextArea("Price:");
		lblPrice.setBackground(UIManager.getColor("Button.background"));
		lblPrice.setEditable(false);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblPrice, 6, SpringLayout.SOUTH, spinner);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblPrice, 10, SpringLayout.WEST, spinner);
		lblPrice.setFont(new Font("SansSerif", Font.BOLD, 12));
		contentPanel.add(lblPrice);

		textField = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, comboBox);
		contentPanel.add(textField);
		textField.setColumns(10);

		final JSpinner spinner_1 = new JSpinner();
		spinner_1.setToolTipText("Use spinner or input number");
		spinner_1.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
		sl_contentPanel.putConstraint(SpringLayout.NORTH, spinner_1, 6, SpringLayout.SOUTH, lblPrice);
		sl_contentPanel.putConstraint(SpringLayout.WEST, spinner_1, 0, SpringLayout.WEST, spinner);
		sl_contentPanel.putConstraint(SpringLayout.EAST, spinner_1, 0, SpringLayout.EAST, comboBox);
		contentPanel.add(spinner_1);

		textField_1 = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblQuantity, 22, SpringLayout.SOUTH, textField_1);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textField_1, 72, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, textField, -2, SpringLayout.NORTH, textField_1);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, label_1, 0, SpringLayout.SOUTH, textField_1);
		sl_contentPanel.putConstraint(SpringLayout.EAST, textField_1, 0, SpringLayout.EAST, comboBox);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton okButton = new JButton("OK");
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			dialog.getRootPane().setDefaultButton(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (comboBox.getSelectedItem().toString().equals("")) {
						JFrame parent = new JFrame();
						dialog.setVisible(false);
						JOptionPane.showMessageDialog(parent, "No groups created, cant add product without group!");
						dialog.setVisible(true);
						return;
					}
					try {
						client.sendCommandToServer("addproduct");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					String s = "";
					String groupName = comboBox.getSelectedItem().toString();
					if (textField_1.getText().isEmpty())
						textField_1.setText(" ");
					if (textField_2.getText().isEmpty()) {
						textField_2.setText(" ");
					}
					s = groupName + "§" + textField.getText() + "§" + textField_2.getText() + "§"
							+ textField_1.getText() + "§" + spinner.getValue() + "§" + spinner_1.getValue();
					try {
						String response = client.sendMessageToServerAndGetResponse(s);
						JFrame parent = new JFrame();
						if (response.equalsIgnoreCase("Can't create product with empty name")) {
							dialog.setVisible(false);
							JOptionPane.showMessageDialog(parent, response);
							textField_1.setText("");
							textField_2.setText("");
							client.sendCommandToServer("end1");
							dialog.setVisible(true);
							return;
						}
						if (response.equalsIgnoreCase("Name is used")) {
							dialog.setVisible(false);
							textField_1.setText("");
							textField_2.setText("");
							JOptionPane.showMessageDialog(parent, response);
							client.sendCommandToServer("end1");
							dialog.setVisible(true);
							return;
						}
						dialog.dispose();
						createNodes(top);
						tree.repaint();
						JOptionPane.showMessageDialog(parent, response);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						client.sendCommandToServer("end1");
					} catch (IOException e) {
						e.printStackTrace();
					}
					dialog.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
		dialog.setVisible(true);
	}
}
