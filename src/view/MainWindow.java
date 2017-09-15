package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.List;
import java.awt.Panel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import enums.MemberRole;

import javax.swing.ListSelectionModel;

public class MainWindow {

	private JFrame frame;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private final ButtonGroup radioButtonGroup = new ButtonGroup();
	private MemberRole role = MemberRole.Developer;
	private JTextField txtFrontEnd;
	private JTextField textField_1;
	private JTextField txtBackEnd;
	private JTextField textField_3;
	private JTextField txtDesign;
	private JTextField textField_5;
	private JTextField textField;
	private JCheckBox  frontEndCheckBox, backEndCheckBox, designCheckBox, testingCheckBox;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 604, 445);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 811, 485);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Add New Developer", null, panel, null);
		panel.setLayout(null);
		
		JLabel firstNameLabel = new JLabel("First Name");
		firstNameLabel.setBounds(10, 11, 69, 27);
		firstNameLabel.setEnabled(false);
		firstNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		panel.add(firstNameLabel);
		
		firstNameTextField = new JTextField();
		firstNameTextField.setBounds(125, 15, 425, 20);
		panel.add(firstNameTextField);
		firstNameTextField.setColumns(10);
		
		lastNameTextField = new JTextField();
		lastNameTextField.setBounds(125, 53, 425, 20);
		lastNameTextField.setColumns(10);
		panel.add(lastNameTextField);
		
		JLabel lastNameLabel = new JLabel("Last Name");
		lastNameLabel.setBounds(10, 49, 69, 27);
		lastNameLabel.setEnabled(false);
		lastNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		panel.add(lastNameLabel);
		
		JLabel roleLabel = new JLabel("Role");
		roleLabel.setBounds(10, 96, 69, 27);
		roleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		roleLabel.setEnabled(false);
		panel.add(roleLabel);
		
		JRadioButton developerRadioButton = new JRadioButton("Developer");
		developerRadioButton.setBounds(200, 99, 109, 23);
		radioButtonGroup.add(developerRadioButton);
		developerRadioButton.setSelected(true);
		panel.add(developerRadioButton);
		developerRadioButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				role = MemberRole.Developer;
			}
		});
		
		JRadioButton testerRadioButton = new JRadioButton("Tester");
		testerRadioButton.setBounds(346, 99, 109, 23);
		radioButtonGroup.add(testerRadioButton);
		panel.add(testerRadioButton);
		
		JLabel expertiseLabel = new JLabel("Expertise in Skill Areas");
		expertiseLabel.setBounds(10, 134, 149, 27);
		expertiseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		expertiseLabel.setEnabled(false);
		panel.add(expertiseLabel);
		
		JButton addNewDeveloperButton = new JButton("Add New Developer");
		addNewDeveloperButton.setBounds(202, 313, 175, 33);
		panel.add(addNewDeveloperButton);
		
		txtFrontEnd = new JTextField();
		txtFrontEnd.setEditable(false);
		txtFrontEnd.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		txtFrontEnd.setText("Front End");
		txtFrontEnd.setBounds(202, 172, 136, 33);
		panel.add(txtFrontEnd);
		txtFrontEnd.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("0 lowest, 30 highest");
		textField_1.setColumns(10);
		textField_1.setBounds(340, 172, 136, 33);
		panel.add(textField_1);
		
		txtBackEnd = new JTextField();
		txtBackEnd.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		txtBackEnd.setEditable(false);
		txtBackEnd.setText("Back End");
		txtBackEnd.setColumns(10);
		txtBackEnd.setBounds(202, 208, 136, 33);
		panel.add(txtBackEnd);
		
		textField_3 = new JTextField();
		textField_3.setToolTipText("0 lowest, 30 highest");
		textField_3.setColumns(10);
		textField_3.setBounds(340, 208, 136, 33);
		panel.add(textField_3);
		
		txtDesign = new JTextField();
		txtDesign.setEditable(false);
		txtDesign.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		txtDesign.setText("Design");
		txtDesign.setColumns(10);
		txtDesign.setBounds(202, 245, 136, 33);
		panel.add(txtDesign);
		
		textField_5 = new JTextField();
		textField_5.setToolTipText("0 lowest, 30 highest");
		textField_5.setColumns(10);
		textField_5.setBounds(340, 245, 136, 33);
		panel.add(textField_5);
		testerRadioButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				role = MemberRole.Tester;
			}
		});
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Add New Task", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setBounds(403, 5, 0, 0);
		panel_1.add(label);
		
		JLabel taskNameLabel = new JLabel("Task Name");
		taskNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		taskNameLabel.setEnabled(false);
		taskNameLabel.setBounds(10, 11, 95, 29);
		panel_1.add(taskNameLabel);
		
		textField = new JTextField();
		textField.setBounds(135, 14, 409, 25);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("");
		label_1.setBounds(20, 58, 46, 14);
		panel_1.add(label_1);
		
		JLabel storyPointsLabel = new JLabel("Story Points");
		storyPointsLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		storyPointsLabel.setEnabled(false);
		storyPointsLabel.setBounds(10, 60, 95, 29);
		panel_1.add(storyPointsLabel);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
		spinner.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		spinner.setBounds(20, 100, 46, 34);
		spinner.setValue(1);
		panel_1.add(spinner);
		
		JLabel skillAreasLabel = new JLabel("Required Skill Areas");
		skillAreasLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		skillAreasLabel.setEnabled(false);
		skillAreasLabel.setBounds(300, 60, 129, 29);
		panel_1.add(skillAreasLabel);
		
		frontEndCheckBox = new JCheckBox("Front End");
		frontEndCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		frontEndCheckBox.setBounds(135, 99, 83, 34);
		panel_1.add(frontEndCheckBox);
		
		
		backEndCheckBox = new JCheckBox("Back End");
		backEndCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		backEndCheckBox.setBounds(363, 99, 79, 34);
		panel_1.add(backEndCheckBox);
		
		designCheckBox = new JCheckBox("Design");
		designCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		designCheckBox.setBounds(261, 99, 65, 34);
		panel_1.add(designCheckBox);
		
		testingCheckBox = new JCheckBox("Testing");
		testingCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		testingCheckBox.setBounds(471, 99, 73, 34);
		panel_1.add(testingCheckBox);
		
		
		frontEndCheckBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(testingCheckBox.isSelected()){
					testingCheckBox.setSelected(false);
				}
			}
		});
		
		backEndCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(testingCheckBox.isSelected()){
					testingCheckBox.setSelected(false);
				}
			}
		});
		
		designCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(testingCheckBox.isSelected()){
					testingCheckBox.setSelected(false);
				}
			}
		});		
		
		testingCheckBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(testingCheckBox.isSelected()){
					frontEndCheckBox.setSelected(false);
					backEndCheckBox.setSelected(false);
					designCheckBox.setSelected(false);
				}
			}
		});
		
		JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		textPane.setBounds(20, 196, 524, 99);
		panel_1.add(textPane);
		
		JLabel taskDescriptionLabel = new JLabel("Task Description");
		taskDescriptionLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		taskDescriptionLabel.setEnabled(false);
		taskDescriptionLabel.setBounds(10, 156, 120, 29);
		panel_1.add(taskDescriptionLabel);
		
		JButton addNewTaskButton = new JButton("Add New Task");
		addNewTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		addNewTaskButton.setBounds(219, 311, 151, 37);
		panel_1.add(addNewTaskButton);
		
		Panel panel_2 = new Panel();
		tabbedPane.addTab("Display Developers", null, panel_2, null);
		panel_2.setLayout(null);
		String[] tableColumns = {"ID", "First Name", "Last Name", "Exp. in Front End", "Exp. in Back End"};
		String[] contentRow1 = {"", "", "", ""};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(tableColumns);
		tableModel.setRowCount(2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 35, 566, 315);
		panel_2.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(tableModel);
		
		Panel panel_3 = new Panel();
		tabbedPane.addTab("Display Task Board", null, panel_3, null);
		panel_3.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Team Admin", null, panel_4, null);
		panel_4.setLayout(null);
	}
}
