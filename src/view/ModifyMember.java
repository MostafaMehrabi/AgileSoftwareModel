package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import enums.MemberRole;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class ModifyMember {

	private JFrame newMemberFrame;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private final ButtonGroup roleButtonGroup = new ButtonGroup();
	MemberRole role = MemberRole.Developer;
	private JTextField txtBackEnd;
	private JTextField backEndTextField;
	private JTextField txtFrontEnd;
	private JTextField frontEndTextField;
	private JTextField txtDesign;
	private JTextField designTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyMember window = new ModifyMember();
					window.newMemberFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ModifyMember() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		newMemberFrame = new JFrame();
		newMemberFrame.setResizable(false);
		newMemberFrame.setTitle("Add/Modify Team Member");
		newMemberFrame.setBounds(100, 100, 575, 444);
		newMemberFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newMemberFrame.getContentPane().setLayout(null);
		
		firstNameTextField = new JTextField();
		firstNameTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		firstNameTextField.setBounds(171, 31, 363, 34);
		newMemberFrame.getContentPane().add(firstNameTextField);
		firstNameTextField.setColumns(10);
		
		lastNameTextField = new JTextField();
		lastNameTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lastNameTextField.setColumns(10);
		lastNameTextField.setBounds(171, 87, 363, 34);
		newMemberFrame.getContentPane().add(lastNameTextField);
		
		JLabel firstNameLabel = new JLabel("First Name");
		firstNameLabel.setEnabled(false);
		firstNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		firstNameLabel.setBounds(20, 31, 91, 34);
		newMemberFrame.getContentPane().add(firstNameLabel);
		
		JLabel lastNameLabel = new JLabel("Last Name");
		lastNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lastNameLabel.setEnabled(false);
		lastNameLabel.setBounds(20, 87, 91, 34);
		newMemberFrame.getContentPane().add(lastNameLabel);
		
		JLabel roleLabel = new JLabel("Role");
		roleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		roleLabel.setEnabled(false);
		roleLabel.setBounds(20, 156, 91, 34);
		newMemberFrame.getContentPane().add(roleLabel);
		
		JRadioButton developerRadioButton = new JRadioButton("Developer");
		developerRadioButton.setSelected(true);
		roleButtonGroup.add(developerRadioButton);
		developerRadioButton.setBounds(211, 164, 109, 23);
		newMemberFrame.getContentPane().add(developerRadioButton);
		developerRadioButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				role = MemberRole.Developer;
				backEndTextField.setEnabled(true);
				frontEndTextField.setEnabled(true);
				designTextField.setEnabled(true);
			}
		});
		
		JRadioButton testerRadioButton = new JRadioButton("Tetster");
		roleButtonGroup.add(testerRadioButton);
		testerRadioButton.setBounds(384, 164, 109, 23);
		newMemberFrame.getContentPane().add(testerRadioButton);
		
		JLabel expertiseLabel = new JLabel("Expertise in Skill Areas");
		expertiseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		expertiseLabel.setEnabled(false);
		expertiseLabel.setBounds(20, 210, 151, 34);
		newMemberFrame.getContentPane().add(expertiseLabel);
		
		txtBackEnd = new JTextField();
		txtBackEnd.setEditable(false);
		txtBackEnd.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		txtBackEnd.setText("Back End");
		txtBackEnd.setBounds(211, 211, 138, 34);
		newMemberFrame.getContentPane().add(txtBackEnd);
		txtBackEnd.setColumns(10);
		
		backEndTextField = new JTextField();
		backEndTextField.setHorizontalAlignment(SwingConstants.CENTER);
		backEndTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		backEndTextField.setText("0");
		backEndTextField.setColumns(10);
		backEndTextField.setBounds(359, 211, 138, 34);
		newMemberFrame.getContentPane().add(backEndTextField);
		
		txtFrontEnd = new JTextField();
		txtFrontEnd.setEditable(false);
		txtFrontEnd.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		txtFrontEnd.setText("Front End");
		txtFrontEnd.setColumns(10);
		txtFrontEnd.setBounds(211, 256, 138, 34);
		newMemberFrame.getContentPane().add(txtFrontEnd);
		
		frontEndTextField = new JTextField();
		frontEndTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		frontEndTextField.setHorizontalAlignment(SwingConstants.CENTER);
		frontEndTextField.setText("0");
		frontEndTextField.setColumns(10);
		frontEndTextField.setBounds(359, 256, 138, 34);
		newMemberFrame.getContentPane().add(frontEndTextField);
		
		txtDesign = new JTextField();
		txtDesign.setEditable(false);
		txtDesign.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		txtDesign.setText("Design");
		txtDesign.setColumns(10);
		txtDesign.setBounds(211, 296, 138, 34);
		newMemberFrame.getContentPane().add(txtDesign);
		
		designTextField = new JTextField();
		designTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		designTextField.setHorizontalAlignment(SwingConstants.CENTER);
		designTextField.setText("0");
		designTextField.setColumns(10);
		designTextField.setBounds(359, 296, 138, 34);
		newMemberFrame.getContentPane().add(designTextField);
		
		JButton btnNewButton = new JButton("Done");
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnNewButton.setBounds(171, 355, 223, 38);
		newMemberFrame.getContentPane().add(btnNewButton);
		testerRadioButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				role = MemberRole.Tester;
				backEndTextField.setEnabled(false);
				frontEndTextField.setEnabled(false);
				designTextField.setEnabled(false);
			}
		});
	}
}
