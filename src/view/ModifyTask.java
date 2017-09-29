package view;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import enums.SkillArea;

import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import core.Main;
import entities.Team;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class ModifyTask {

	private JFrame frmAddmodifyTask;
	private JTextField taskNameTextField;
	private JTextField taskDescriptionTextField;
	List<SkillArea> requiredSkillAreas;

	/**
	 * Create the application.
	 */
	public ModifyTask() {
		requiredSkillAreas = new ArrayList<>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddmodifyTask = new JFrame();
		frmAddmodifyTask.setTitle("Add/Modify Task");
		frmAddmodifyTask.setBounds(100, 100, 668, 362);
		frmAddmodifyTask.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmAddmodifyTask.getContentPane().setLayout(null);
		
		taskNameTextField = new JTextField();
		taskNameTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		taskNameTextField.setBounds(214, 11, 400, 37);
		frmAddmodifyTask.getContentPane().add(taskNameTextField);
		taskNameTextField.setColumns(10);
		
		taskDescriptionTextField = new JTextField();
		taskDescriptionTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		taskDescriptionTextField.setColumns(10);
		taskDescriptionTextField.setBounds(214, 67, 400, 37);
		frmAddmodifyTask.getContentPane().add(taskDescriptionTextField);
		
		JLabel taskNameLabel = new JLabel("Task Name");
		taskNameLabel.setEnabled(false);
		taskNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		taskNameLabel.setBounds(10, 9, 88, 37);
		frmAddmodifyTask.getContentPane().add(taskNameLabel);
		
		JLabel lblTaskDescription = new JLabel("Task Description");
		lblTaskDescription.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblTaskDescription.setEnabled(false);
		lblTaskDescription.setBounds(10, 67, 137, 37);
		frmAddmodifyTask.getContentPane().add(lblTaskDescription);
		
		JLabel storyPiontLabel = new JLabel("Story Pionts");
		storyPiontLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		storyPiontLabel.setEnabled(false);
		storyPiontLabel.setBounds(10, 126, 137, 37);
		frmAddmodifyTask.getContentPane().add(storyPiontLabel);
		
		JSpinner storyPointSpinner = new JSpinner();
		storyPointSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
		storyPointSpinner.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		storyPointSpinner.setBounds(37, 174, 61, 34);
		frmAddmodifyTask.getContentPane().add(storyPointSpinner);
		
		JLabel lblRequiredSkillAreas = new JLabel("Required Skill Areas");
		lblRequiredSkillAreas.setHorizontalAlignment(SwingConstants.CENTER);
		lblRequiredSkillAreas.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblRequiredSkillAreas.setEnabled(false);
		lblRequiredSkillAreas.setBounds(366, 136, 137, 37);
		frmAddmodifyTask.getContentPane().add(lblRequiredSkillAreas);
		
		JCheckBox backEndCheckBox = new JCheckBox("Back End");
		backEndCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		backEndCheckBox.setBounds(214, 195, 97, 23);
		frmAddmodifyTask.getContentPane().add(backEndCheckBox);
		
		JCheckBox frontEndCheckBox = new JCheckBox("Front End");
		frontEndCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		frontEndCheckBox.setBounds(328, 195, 97, 23);
		frmAddmodifyTask.getContentPane().add(frontEndCheckBox);
		
		JCheckBox designCheckBox = new JCheckBox("Design");
		designCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		designCheckBox.setBounds(450, 195, 97, 23);
		frmAddmodifyTask.getContentPane().add(designCheckBox);
		
		JCheckBox testingCheckBox = new JCheckBox("Testing");
		testingCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		testingCheckBox.setBounds(549, 195, 97, 23);
		frmAddmodifyTask.getContentPane().add(testingCheckBox);
		
		backEndCheckBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(backEndCheckBox.isSelected())
					testingCheckBox.setSelected(false);
			}
		});
		
		frontEndCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(frontEndCheckBox.isSelected())
					testingCheckBox.setSelected(false);
			}
		});
		
		designCheckBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(designCheckBox.isSelected())
					testingCheckBox.setSelected(false);
			}
		});
		
		testingCheckBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(testingCheckBox.isSelected()){
					backEndCheckBox.setSelected(false);
					frontEndCheckBox.setSelected(false);
					designCheckBox.setSelected(false);
				}
			}
		});
		
		JLabel priorityLabel = new JLabel("Priority");
		priorityLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		priorityLabel.setEnabled(false);
		priorityLabel.setBounds(10, 219, 137, 37);
		frmAddmodifyTask.getContentPane().add(priorityLabel);
		
		JSpinner prioritySpinner = new JSpinner();
		prioritySpinner.setModel(new SpinnerNumberModel(1, 1, 5, 1));
		prioritySpinner.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		prioritySpinner.setBounds(37, 267, 61, 34);
		frmAddmodifyTask.getContentPane().add(prioritySpinner);
		
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//potential robustness and error checking code can be added here
				String taskName = taskNameTextField.getText();
				String taskDescription = taskDescriptionTextField.getText();
				Integer storyPoints = (Integer) storyPointSpinner.getValue();
				Integer priority = (Integer) prioritySpinner.getValue();
				Set<SkillArea> skillAreas = new HashSet<>();
				if(frontEndCheckBox.isSelected())
					skillAreas.add(SkillArea.FrontEnd);
				if(backEndCheckBox.isSelected())
					skillAreas.add(SkillArea.BackEnd);
				if(designCheckBox.isSelected())
					skillAreas.add(SkillArea.Design);
				if(testingCheckBox.isSelected())
					skillAreas.add(SkillArea.Testing);
				frmAddmodifyTask.setVisible(false);
				frmAddmodifyTask.dispose();
				try{
					Team.getTeam().addNewTaskToBackLog(taskName, taskDescription, storyPoints, skillAreas, priority);
				}catch(Exception exception){
					Main.issueErrorMessage(exception.getMessage());
				}
			}
		});
		doneButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		doneButton.setBounds(358, 255, 256, 46);
		frmAddmodifyTask.getContentPane().add(doneButton);
	}
	
	public void setVisible(boolean visibility){
		frmAddmodifyTask.setVisible(visibility);
	}
}
