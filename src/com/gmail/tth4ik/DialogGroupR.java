package com.gmail.tth4ik;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JScrollBar;

public class DialogGroupR extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DialogGroupR dialog = new DialogGroupR();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DialogGroupR() {
		setTitle("Group Report");
		setBounds(100, 100, 686, 438);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		{
			comboBox = new JComboBox();
			sl_contentPanel.putConstraint(SpringLayout.NORTH, comboBox, 10, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, comboBox, 10, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, comboBox, 249, SpringLayout.WEST, contentPanel);
			contentPanel.add(comboBox);
		}
		{
			JTextPane textPane = new JTextPane();
			sl_contentPanel.putConstraint(SpringLayout.NORTH, textPane, 18, SpringLayout.SOUTH, comboBox);
			sl_contentPanel.putConstraint(SpringLayout.WEST, textPane, 7, SpringLayout.WEST, comboBox);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, textPane, -10, SpringLayout.SOUTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, textPane, -10, SpringLayout.EAST, contentPanel);
			textPane.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			textPane.setEditable(false);
			contentPanel.add(textPane);
		}
		{
			JPanel savetxtButton = new JPanel();
			savetxtButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(savetxtButton, BorderLayout.SOUTH);
			{
				JButton genButton = new JButton("Generate report");
				genButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				genButton.setActionCommand("OK");
				savetxtButton.add(genButton);
				getRootPane().setDefaultButton(genButton);
			}
			{
				JButton cancelButton = new JButton("Save as TXT file\r\n");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				cancelButton.setActionCommand("Cancel");
				savetxtButton.add(cancelButton);
			}
		}
	}

}
