package com.gmail.tth4ik;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JDesktopPane;
import javax.swing.SpringLayout;
import javax.swing.JInternalFrame;
import javax.swing.JTree;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.TitledBorder;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JTextField;
import java.awt.Label;
import javax.swing.JLayeredPane;

public class InterfaceTest {
	private JFrame frmWarehouseManagementSystem;
	private static ARMClient client;
	private static MySQL msql;
	private JTextField textField;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		msql = new MySQL();
		client = new ARMClient();
		msql.initialization("ARMDB");
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
	 */
	public InterfaceTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWarehouseManagementSystem = new JFrame();
		frmWarehouseManagementSystem.setTitle("Warehouse Management System");
		frmWarehouseManagementSystem.setBounds(100, 100, 715, 459);
		frmWarehouseManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmWarehouseManagementSystem.getContentPane().setLayout(springLayout);

		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, frmWarehouseManagementSystem.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, frmWarehouseManagementSystem.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, frmWarehouseManagementSystem.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, 225, SpringLayout.WEST, frmWarehouseManagementSystem.getContentPane());
		frmWarehouseManagementSystem.getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.NORTH, frmWarehouseManagementSystem.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel_1, 3, SpringLayout.EAST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, panel_1, -85, SpringLayout.SOUTH, frmWarehouseManagementSystem.getContentPane());
		frmWarehouseManagementSystem.getContentPane().add(panel_1);

		JPanel panel_2 = new JPanel();
		springLayout.putConstraint(SpringLayout.EAST, panel_1, -6, SpringLayout.WEST, panel_2);
		springLayout.putConstraint(SpringLayout.NORTH, panel_2, 0, SpringLayout.NORTH, frmWarehouseManagementSystem.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel_2, 576, SpringLayout.WEST, frmWarehouseManagementSystem.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel_2, 0, SpringLayout.SOUTH, frmWarehouseManagementSystem.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel_2, 0, SpringLayout.EAST, frmWarehouseManagementSystem.getContentPane());
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Groups of Items");
		JTree tree = new JTree(top);
		tree.setToolTipText("This is information about current group");
		tree.setBackground(Color.WHITE);
		tree.setEditable(true);
		sl_panel.putConstraint(SpringLayout.NORTH, tree, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, tree, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, tree, 215, SpringLayout.WEST, panel);
		tree.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		panel.add(tree);
		
		JButton button = new JButton("Add group");
		sl_panel.putConstraint(SpringLayout.SOUTH, tree, -23, SpringLayout.NORTH, button);
		sl_panel.putConstraint(SpringLayout.NORTH, button, 352, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, button, -32, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, button, 43, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, button, 181, SpringLayout.WEST, panel);
		button.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(button);
		frmWarehouseManagementSystem.getContentPane().add(panel_2);
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_2.setLayout(sl_panel_2);

		JButton btnAddProduct = new JButton("Add product");
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnAddProduct, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnAddProduct, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnAddProduct, -341, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnAddProduct, -10, SpringLayout.EAST, panel_2);
		panel_2.add(btnAddProduct);

		JButton btnRefresh = new JButton("Refresh");
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnRefresh, 17, SpringLayout.SOUTH, btnAddProduct);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnRefresh, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnRefresh, -263, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnRefresh, 0, SpringLayout.EAST, btnAddProduct);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		panel_2.add(btnRefresh);
		
		JButton btnConnect = new JButton("Connect");
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnConnect, -59, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnConnect, 0, SpringLayout.WEST, btnAddProduct);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnConnect, -31, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnConnect, 0, SpringLayout.EAST, btnAddProduct);
		panel_2.add(btnConnect);
		
		JButton btnGroupReport = new JButton("Group report");
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnGroupReport, 21, SpringLayout.SOUTH, btnRefresh);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnGroupReport, 0, SpringLayout.EAST, btnAddProduct);
		panel_2.add(btnGroupReport);
		
		JButton btnFullReport = new JButton("Full report");
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnFullReport, 18, SpringLayout.SOUTH, btnGroupReport);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnFullReport, 0, SpringLayout.WEST, btnGroupReport);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnFullReport, 46, SpringLayout.SOUTH, btnGroupReport);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnFullReport, 0, SpringLayout.EAST, btnAddProduct);
		panel_2.add(btnFullReport);
		
		JPanel panel_3 = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel_3, 6, SpringLayout.SOUTH, panel_1);
		
		JButton btnDeleteProduct = new JButton("Delete Product");
		sl_panel_1.putConstraint(SpringLayout.WEST, btnDeleteProduct, 37, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnDeleteProduct, -40, SpringLayout.SOUTH, panel_1);
		panel_1.add(btnDeleteProduct);
		
		JButton btnEditProduct = new JButton("Edit Product");
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnEditProduct, 0, SpringLayout.NORTH, btnDeleteProduct);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnEditProduct, -149, SpringLayout.EAST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnEditProduct, -40, SpringLayout.EAST, panel_1);
		panel_1.add(btnEditProduct);
		
		JButton btnSelladd = new JButton("Sell/Add Products");
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnSelladd, 6, SpringLayout.SOUTH, btnDeleteProduct);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnSelladd, 106, SpringLayout.WEST, panel_1);
		panel_1.add(btnSelladd);
		
		Label label = new Label("Name: ");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label, 22, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, label, 318, SpringLayout.WEST, panel_1);
		panel_1.add(label);
		
		Label label_1 = new Label("Manufacturer");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label_1, 6, SpringLayout.SOUTH, label);
		sl_panel_1.putConstraint(SpringLayout.WEST, label_1, 0, SpringLayout.WEST, label);
		sl_panel_1.putConstraint(SpringLayout.EAST, label_1, 0, SpringLayout.EAST, label);
		panel_1.add(label_1);
		
		Label label_2 = new Label("Quantity:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label_2, 6, SpringLayout.SOUTH, label_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, label_2, 0, SpringLayout.WEST, label);
		sl_panel_1.putConstraint(SpringLayout.EAST, label_2, 0, SpringLayout.EAST, label);
		panel_1.add(label_2);
		
		Label label_3 = new Label("Price:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label_3, 6, SpringLayout.SOUTH, label_2);
		sl_panel_1.putConstraint(SpringLayout.WEST, label_3, 0, SpringLayout.WEST, label);
		sl_panel_1.putConstraint(SpringLayout.EAST, label_3, 0, SpringLayout.EAST, label);
		panel_1.add(label_3);
		
		Label label_4 = new Label("Description:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, label_4, 6, SpringLayout.SOUTH, label_3);
		sl_panel_1.putConstraint(SpringLayout.WEST, label_4, 0, SpringLayout.WEST, label);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, label_4, -88, SpringLayout.NORTH, btnDeleteProduct);
		sl_panel_1.putConstraint(SpringLayout.EAST, label_4, 0, SpringLayout.EAST, label);
		panel_1.add(label_4);
		springLayout.putConstraint(SpringLayout.WEST, panel_3, 6, SpringLayout.EAST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, panel_3, -10, SpringLayout.SOUTH, frmWarehouseManagementSystem.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel_3, -6, SpringLayout.WEST, panel_2);
		frmWarehouseManagementSystem.getContentPane().add(panel_3);
		SpringLayout sl_panel_3 = new SpringLayout();
		panel_3.setLayout(sl_panel_3);
		
		textField = new JTextField();
		sl_panel_3.putConstraint(SpringLayout.NORTH, textField, 20, SpringLayout.NORTH, panel_3);
		sl_panel_3.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, panel_3);
		sl_panel_3.putConstraint(SpringLayout.EAST, textField, 219, SpringLayout.WEST, panel_3);
		panel_3.add(textField);
		textField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		sl_panel_3.putConstraint(SpringLayout.NORTH, btnSearch, 0, SpringLayout.NORTH, textField);
		sl_panel_3.putConstraint(SpringLayout.WEST, btnSearch, 3, SpringLayout.EAST, textField);
		sl_panel_3.putConstraint(SpringLayout.EAST, btnSearch, -10, SpringLayout.EAST, panel_3);
		panel_3.add(btnSearch);
		
		JLayeredPane layeredPane = new JLayeredPane();
		frmWarehouseManagementSystem.getContentPane().add(layeredPane);
	}

	private void createNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode product = null;
		String[] array = msql.getGroupNames().split(";");
		for (String groupname : array) {
			category = new DefaultMutableTreeNode(groupname);
			top.add(category);
			String[] array1 = msql.getGroupProductData(msql.getGroupID(groupname)).split("\n");
			for (String groupinfo : array1) {
				product = new DefaultMutableTreeNode(groupinfo.substring(0, groupinfo.indexOf(";", 0)));
				category.add(product);
			}

		}

	}
	
	
	
	
}
