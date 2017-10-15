package view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Panel;
import javax.swing.table.DefaultTableModel;

import core.Main;
import core.SystemLoader;
import core.SystemRecorder;
import entities.Task;
import entities.Team;
import entities.TeamMember;
import enums.SkillArea;
import enums.TaskAllocationStrategy;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.Canvas;

public class MainWindow {

	private JFrame frame;
	
	private JTable personnelTable;
	private JTable toDoTasksTable;
	private JTable tasksInProgressTable;
	private JTable finishedTasksTable;
	private JTable backLogTable;
	
	private DefaultTableModel toDoTaskTableModel = new DefaultTableModel();
	private DefaultTableModel tasksInProgressTableModel = new DefaultTableModel();
	private DefaultTableModel finishedTasksTableModel = new DefaultTableModel();
	private DefaultTableModel backLogTableModel;
	private DefaultTableModel personnelTableModel;

	private JTextField numOfRandomTasksTextField;
	private JTextField storyPointCoefficientTextField;
	private JTextField progressPerStoryPointTextField;
	private JTextField lowExpertiseLowerBoundaryTextField;
	private JTextField lowExpertiseCoefficientTextField;
	private JTextField lowExpertiseUpperBoundaryTextField;
	private JTextField medExpertiseLowerBoundaryTextField;
	private JTextField medExpertiseUpperBoundaryTextField;
	private JTextField medExpertiseCoefficientTextField;
	private JTextField highExpertiseLowerBoundaryTextField;
	private JTextField highExpertiseUpperBoundaryTextField;
	private JTextField highExpertiseCoefficientTextField;
	private JTextField TCTtoSystemCoefTextField;
	
	private JLabel lastSprintVelocityValueLabel;
	private JLabel stopAfterEachSprintLabel;
	private JLabel sprintNumberLabel;

	private JComboBox<String> allocationStrategyComboBox;
	private JCheckBox stopAfterEachSprintCheckBox;
	private DefaultComboBoxModel<String> allocationStrategyComboBoxModel;
	private JProgressBar taskBoardProgressBar;
	private JTextField hoursPerSpringTextField;
	private JTextField sprintsPerProjectTextField;
	private JButton startSprintButton;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		loadGUI();
	}	

	private void loadGUI(){
		repopulateTeamTab();
		repopulateBackLogTable();
		repopulateToDoTasksTable();
		repopulateTaskInProgressTable();
		repopulateCompletedTasksTable();
		repopulatePersonnelTable();
	}	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setResizable(false);
		frame.setBounds(100, 100, 902, 502);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setAutoscrolls(true);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		tabbedPane.setBounds(0, 0, 896, 474);
		frame.getContentPane().add(tabbedPane);
		
		String[] toDoTasksColumn = {"ID", "Story Points", "Priority", "Skills"};
		toDoTaskTableModel.setColumnIdentifiers(toDoTasksColumn);
		
		String[] tasksInProgressColumn = {"ID", "Story Point", "PerformerID"};
		tasksInProgressTableModel.setColumnIdentifiers(tasksInProgressColumn);
		
		String[] finishedTasksColumn = {"ID", "Story Point", "PerformerID"};
		finishedTasksTableModel.setColumnIdentifiers(finishedTasksColumn);
		
		String[] personnelTableColumn = {"ID", "First Name", "Last Name", "Role", "Exp. in Front End", "Exp. in Back End", "Exp. in Design"};
		personnelTableModel = new DefaultTableModel();
		personnelTableModel.setColumnIdentifiers(personnelTableColumn);

		JPanel displayProjectBackLogPanel = new JPanel();		
		displayProjectBackLogPanel.setBackground(SystemColor.menu);
		tabbedPane.addTab("Project BackLog", null, displayProjectBackLogPanel, null);
		displayProjectBackLogPanel.setLayout(null);
		
		JScrollPane backLogPane = new JScrollPane();
		backLogPane.setBounds(22, 24, 631, 393);
		displayProjectBackLogPanel.add(backLogPane);
		
		backLogTableModel = new DefaultTableModel();
		String[] backLogTabelColumn = {"ID", "Story Points", "Priority",  "Name", "Required Skills"};
		backLogTableModel.setColumnIdentifiers(backLogTabelColumn);
		backLogTable = new JTable();
		backLogTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE){
					int[] selecetedIndices = backLogTable.getSelectedRows();
					deleteSelectedRows(selecetedIndices);
				}
			}
		});
		backLogTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		backLogPane.setViewportView(backLogTable);
		backLogTable.setModel(backLogTableModel);
		
		displayProjectBackLogPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				backLogTable.clearSelection();
			}
		});
		
		JButton generateRandomTasksButton = new JButton("Generate Random Tasks");
		generateRandomTasksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String number = numOfRandomTasksTextField.getText();
				int numberOfTasks;
				try {
					numberOfTasks = Integer.parseInt(number);
					Team.getTeam().createRandomTasks(numberOfTasks);
				}catch(NumberFormatException exception) {
					Main.issueErrorMessage("Content " + number + " is not a valid integer number! Please try again!");
					numOfRandomTasksTextField.setText("");
				}
			}
		});
		generateRandomTasksButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		generateRandomTasksButton.setBounds(677, 125, 187, 45);
		displayProjectBackLogPanel.add(generateRandomTasksButton);
		
		numOfRandomTasksTextField = new JTextField();
		numOfRandomTasksTextField.setToolTipText("A number greater than 0");
		numOfRandomTasksTextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		numOfRandomTasksTextField.setBounds(677, 84, 187, 29);
		displayProjectBackLogPanel.add(numOfRandomTasksTextField);
		numOfRandomTasksTextField.setColumns(10);
		
		JLabel numOfRandomTasksLabel = new JLabel("Number of Random Tasks");
		numOfRandomTasksLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numOfRandomTasksLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		numOfRandomTasksLabel.setEnabled(false);
		numOfRandomTasksLabel.setBounds(677, 44, 187, 29);
		displayProjectBackLogPanel.add(numOfRandomTasksLabel);
		
		JButton newSingleTaskButton = new JButton("Create New Task");
		newSingleTaskButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ModifyTask newTaskWindow = new ModifyTask();
				newTaskWindow.setVisible(true);
			}
		});
		newSingleTaskButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		newSingleTaskButton.setBounds(677, 214, 187, 45);
		displayProjectBackLogPanel.add(newSingleTaskButton);
		
		JLabel lblOr = new JLabel("OR");
		lblOr.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblOr.setEnabled(false);
		lblOr.setBounds(761, 181, 31, 22);
		displayProjectBackLogPanel.add(lblOr);
		
		JButton addTasksForSprintButton = new JButton("Move Tasks to First Sprint");
		addTasksForSprintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedIndices = backLogTable.getSelectedRows();
				
				if(selectedIndices.length == 0){
					boolean answer = Main.issueQuesionDialogue("You have not selected any tasks, do you want the system to "
							+ "\n automatically select and move to sprint backlog?", "");
					if (answer){
						automaticTaskMoveToFirstSprint();
						repopulateBackLogTable();
						repopulateToDoTasksTable();
						tabbedPane.setSelectedIndex(1);
					}
				}				
				else{
					Team.getTeam().moveToSprintBackLog(selectedIndices);	
					repopulateBackLogTable();
					repopulateToDoTasksTable();
					tabbedPane.setSelectedIndex(1);
				}
			
			}
		});
		addTasksForSprintButton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		addTasksForSprintButton.setBounds(677, 344, 187, 45);
		displayProjectBackLogPanel.add(addTasksForSprintButton);
		
		JLabel lblNewLabel = new JLabel("Add Tasks to Sprint BackLog");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setEnabled(false);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel.setBounds(677, 304, 187, 29);
		displayProjectBackLogPanel.add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(677, 291, 187, 12);
		displayProjectBackLogPanel.add(separator);
		
		
		
		Panel displayTaskBoardPanel = new Panel();
		displayTaskBoardPanel.setBackground(SystemColor.menu);
		tabbedPane.addTab("Task Board", null, displayTaskBoardPanel, null);
		displayTaskBoardPanel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(130, 83, 235, 272);
		displayTaskBoardPanel.add(scrollPane_1);
		
		toDoTasksTable = new JTable();
		toDoTasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(toDoTasksTable);
		toDoTasksTable.setModel(toDoTaskTableModel);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(385, 83, 241, 272);
		displayTaskBoardPanel.add(scrollPane_2);
		
		tasksInProgressTable = new JTable();
		tasksInProgressTable.setRowSelectionAllowed(false);
		scrollPane_2.setViewportView(tasksInProgressTable);
		tasksInProgressTable.setModel(tasksInProgressTableModel);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(646, 83, 235, 272);
		displayTaskBoardPanel.add(scrollPane_3);
		
		finishedTasksTable = new JTable();
		finishedTasksTable.setRowSelectionAllowed(false);
		scrollPane_3.setViewportView(finishedTasksTable);
		finishedTasksTable.setModel(finishedTasksTableModel);
		
		JLabel toDoTaskLabel = new JLabel("ToDo Tasks");
		toDoTaskLabel.setEnabled(false);
		toDoTaskLabel.setHorizontalAlignment(SwingConstants.CENTER);
		toDoTaskLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		toDoTaskLabel.setBounds(130, 52, 235, 31);
		displayTaskBoardPanel.add(toDoTaskLabel);
		
		JLabel tasksBeingPerformedLabel = new JLabel("Tasks in Progress");
		tasksBeingPerformedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tasksBeingPerformedLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		tasksBeingPerformedLabel.setEnabled(false);
		tasksBeingPerformedLabel.setBounds(385, 52, 241, 31);
		displayTaskBoardPanel.add(tasksBeingPerformedLabel);
		
		JLabel finishedTasksLabel = new JLabel("Finished Tasks");
		finishedTasksLabel.setHorizontalAlignment(SwingConstants.CENTER);
		finishedTasksLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		finishedTasksLabel.setEnabled(false);
		finishedTasksLabel.setBounds(646, 52, 235, 31);
		displayTaskBoardPanel.add(finishedTasksLabel);
		
		taskBoardProgressBar = new JProgressBar();
		taskBoardProgressBar.setStringPainted(true);
		taskBoardProgressBar.setForeground(Color.GREEN);
		taskBoardProgressBar.setBounds(168, 400, 631, 31);
		displayTaskBoardPanel.add(taskBoardProgressBar);
		
		JLabel lblTimeLeftTo = new JLabel("Time Left to the End of Sprint");
		lblTimeLeftTo.setEnabled(false);
		lblTimeLeftTo.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblTimeLeftTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeLeftTo.setBounds(385, 363, 241, 31);
		displayTaskBoardPanel.add(lblTimeLeftTo);
		
		JLabel taskBoardLabel = new JLabel("Task Board of Sprint No. ");
		taskBoardLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		taskBoardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		taskBoardLabel.setBounds(181, 11, 175, 30);
		displayTaskBoardPanel.add(taskBoardLabel);
		
		sprintNumberLabel = new JLabel();
		sprintNumberLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
		sprintNumberLabel.setBounds(349, 11, 52, 30);
		displayTaskBoardPanel.add(sprintNumberLabel);
		
		startSprintButton = new JButton("Start the Sprint");
		startSprintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyTeamSettings();
				Team.getTeam().startProcess();
			}
		});
		startSprintButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		startSprintButton.setBounds(10, 143, 110, 31);
		displayTaskBoardPanel.add(startSprintButton);
		
		JButton removeTaskButton = new JButton("Remove Task");
		removeTaskButton.setEnabled(false);
		removeTaskButton.setBounds(10, 185, 110, 31);
		displayTaskBoardPanel.add(removeTaskButton);
		
		JButton modifyTaskButton = new JButton("Modify Task");
		modifyTaskButton.setEnabled(false);
		modifyTaskButton.setBounds(10, 227, 110, 31);
		displayTaskBoardPanel.add(modifyTaskButton);
		
		JLabel lastSprintVelocityLabel = new JLabel("Last Sprint Velocity");
		lastSprintVelocityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lastSprintVelocityLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lastSprintVelocityLabel.setBounds(538, 11, 175, 30);
		displayTaskBoardPanel.add(lastSprintVelocityLabel);
		
		lastSprintVelocityValueLabel = new JLabel();
		lastSprintVelocityValueLabel.setText("0");
		lastSprintVelocityValueLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lastSprintVelocityValueLabel.setBounds(706, 11, 110, 30);
		displayTaskBoardPanel.add(lastSprintVelocityValueLabel);
		
		JPanel teamAdminPanel = new JPanel();
		teamAdminPanel.setBackground(SystemColor.menu);
		tabbedPane.addTab("Team Admin", null, teamAdminPanel, null);
		teamAdminPanel.setLayout(null);
		
		JLabel taskAllocationStrategyLabel = new JLabel("Task Allocation Strategy");
		taskAllocationStrategyLabel.setEnabled(false);
		taskAllocationStrategyLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		taskAllocationStrategyLabel.setBounds(10, 11, 155, 25);
		teamAdminPanel.add(taskAllocationStrategyLabel);
		
		allocationStrategyComboBox = new JComboBox<String>();
		allocationStrategyComboBoxModel = new DefaultComboBoxModel<String>();
		allocationStrategyComboBoxModel.addElement(TaskAllocationStrategy.ExpertiseBased.toString());
		allocationStrategyComboBoxModel.addElement(TaskAllocationStrategy.LearningBased.toString());
		allocationStrategyComboBox.setModel(allocationStrategyComboBoxModel);
		allocationStrategyComboBox.setSelectedIndex(0);
		allocationStrategyComboBox.setBounds(10, 47, 155, 25);
		teamAdminPanel.add(allocationStrategyComboBox);
		
		JLabel storyPointCoefficientLabel = new JLabel("Story Point Coefficient");
		storyPointCoefficientLabel.setEnabled(false);
		storyPointCoefficientLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		storyPointCoefficientLabel.setBounds(10, 100, 155, 25);
		teamAdminPanel.add(storyPointCoefficientLabel);
		
		storyPointCoefficientTextField = new JTextField();
		storyPointCoefficientTextField.setColumns(10);
		storyPointCoefficientTextField.setBounds(10, 136, 155, 25);
		teamAdminPanel.add(storyPointCoefficientTextField);
		
		JLabel progressPerStoryPointLabel = new JLabel("Progress Per Story Point");
		progressPerStoryPointLabel.setEnabled(false);
		progressPerStoryPointLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		progressPerStoryPointLabel.setBounds(10, 203, 155, 25);
		teamAdminPanel.add(progressPerStoryPointLabel);
		
		progressPerStoryPointTextField = new JTextField();
		progressPerStoryPointTextField.setColumns(10);
		progressPerStoryPointTextField.setBounds(10, 239, 155, 25);
		teamAdminPanel.add(progressPerStoryPointTextField);
		
		JLabel lowExpertiseLabel = new JLabel("Low Expertise ");
		lowExpertiseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lowExpertiseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lowExpertiseLabel.setEnabled(false);
		lowExpertiseLabel.setBounds(509, 11, 155, 25);
		teamAdminPanel.add(lowExpertiseLabel);
		
		lowExpertiseLowerBoundaryTextField = new JTextField();
		lowExpertiseLowerBoundaryTextField.setColumns(10);
		lowExpertiseLowerBoundaryTextField.setBounds(369, 46, 78, 25);
		teamAdminPanel.add(lowExpertiseLowerBoundaryTextField);
		
		lowExpertiseUpperBoundaryTextField = new JTextField();
		lowExpertiseUpperBoundaryTextField.setColumns(10);
		lowExpertiseUpperBoundaryTextField.setBounds(586, 46, 78, 25);
		teamAdminPanel.add(lowExpertiseUpperBoundaryTextField);
	
		
		lowExpertiseCoefficientTextField = new JTextField();
		lowExpertiseCoefficientTextField.setColumns(10);
		lowExpertiseCoefficientTextField.setBounds(771, 46, 78, 25);
		teamAdminPanel.add(lowExpertiseCoefficientTextField);		
			
		JLabel lowerBoundaryLabel = new JLabel("Lower Boundary");
		lowerBoundaryLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lowerBoundaryLabel.setEnabled(false);
		lowerBoundaryLabel.setBounds(248, 44, 111, 25);
		teamAdminPanel.add(lowerBoundaryLabel);
		
		JLabel lowExpertiseUpperBoundaryLabel = new JLabel("Upper Boundary");
		lowExpertiseUpperBoundaryLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lowExpertiseUpperBoundaryLabel.setEnabled(false);
		lowExpertiseUpperBoundaryLabel.setBounds(465, 44, 111, 25);
		teamAdminPanel.add(lowExpertiseUpperBoundaryLabel);
		
		JLabel lowExpertiseCoefficientLabel = new JLabel("Coefficient");
		lowExpertiseCoefficientLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lowExpertiseCoefficientLabel.setEnabled(false);
		lowExpertiseCoefficientLabel.setBounds(690, 44, 71, 25);
		teamAdminPanel.add(lowExpertiseCoefficientLabel);
		
		JLabel medExpertiseLabel = new JLabel("Medium Expertise ");
		medExpertiseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		medExpertiseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		medExpertiseLabel.setEnabled(false);
		medExpertiseLabel.setBounds(509, 100, 155, 25);
		teamAdminPanel.add(medExpertiseLabel);
		
		JLabel medExpertiseLowerBoundaryLabel = new JLabel("Lower Boundary");
		medExpertiseLowerBoundaryLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		medExpertiseLowerBoundaryLabel.setEnabled(false);
		medExpertiseLowerBoundaryLabel.setBounds(248, 136, 111, 25);
		teamAdminPanel.add(medExpertiseLowerBoundaryLabel);
		
		medExpertiseLowerBoundaryTextField = new JTextField();
		medExpertiseLowerBoundaryTextField.setColumns(10);
		medExpertiseLowerBoundaryTextField.setBounds(369, 138, 78, 25);
		teamAdminPanel.add(medExpertiseLowerBoundaryTextField);
			
		JLabel medExpertiseUpperBoundaryLabel = new JLabel("Upper Boundary");
		medExpertiseUpperBoundaryLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		medExpertiseUpperBoundaryLabel.setEnabled(false);
		medExpertiseUpperBoundaryLabel.setBounds(465, 136, 111, 25);
		teamAdminPanel.add(medExpertiseUpperBoundaryLabel);
		
		medExpertiseUpperBoundaryTextField = new JTextField();
		medExpertiseUpperBoundaryTextField.setColumns(10);
		medExpertiseUpperBoundaryTextField.setBounds(586, 138, 78, 25);
		teamAdminPanel.add(medExpertiseUpperBoundaryTextField);
		
		JLabel medExpertiseCoefficientLabel = new JLabel("Coefficient");
		medExpertiseCoefficientLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		medExpertiseCoefficientLabel.setEnabled(false);
		medExpertiseCoefficientLabel.setBounds(690, 136, 71, 25);
		teamAdminPanel.add(medExpertiseCoefficientLabel);
		
		medExpertiseCoefficientTextField = new JTextField();
		medExpertiseCoefficientTextField.setColumns(10);
		medExpertiseCoefficientTextField.setBounds(771, 138, 78, 25);
		teamAdminPanel.add(medExpertiseCoefficientTextField);		
		
		JLabel highExpertiseLabel = new JLabel("High Expertise ");
		highExpertiseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		highExpertiseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		highExpertiseLabel.setEnabled(false);
		highExpertiseLabel.setBounds(509, 203, 155, 25);
		teamAdminPanel.add(highExpertiseLabel);
		
		JLabel highExpertiseLowerBoundaryLabel = new JLabel("Lower Boundary");
		highExpertiseLowerBoundaryLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		highExpertiseLowerBoundaryLabel.setEnabled(false);
		highExpertiseLowerBoundaryLabel.setBounds(248, 239, 111, 25);
		teamAdminPanel.add(highExpertiseLowerBoundaryLabel);
		
		highExpertiseLowerBoundaryTextField = new JTextField();
		highExpertiseLowerBoundaryTextField.setColumns(10);
		highExpertiseLowerBoundaryTextField.setBounds(369, 241, 78, 25);
		teamAdminPanel.add(highExpertiseLowerBoundaryTextField);
		
		JLabel highExpertiseUpperBoundaryLabel = new JLabel("Upper Boundary");
		highExpertiseUpperBoundaryLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		highExpertiseUpperBoundaryLabel.setEnabled(false);
		highExpertiseUpperBoundaryLabel.setBounds(465, 239, 111, 25);
		teamAdminPanel.add(highExpertiseUpperBoundaryLabel);
		
		highExpertiseUpperBoundaryTextField = new JTextField();
		highExpertiseUpperBoundaryTextField.setColumns(10);
		highExpertiseUpperBoundaryTextField.setBounds(586, 241, 78, 25);
		teamAdminPanel.add(highExpertiseUpperBoundaryTextField);
		
		JLabel highExpertiseCoefficient = new JLabel("Coefficient");
		highExpertiseCoefficient.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		highExpertiseCoefficient.setEnabled(false);
		highExpertiseCoefficient.setBounds(690, 239, 71, 25);
		teamAdminPanel.add(highExpertiseCoefficient);
		
		highExpertiseCoefficientTextField = new JTextField();
		highExpertiseCoefficientTextField.setColumns(10);
		highExpertiseCoefficientTextField.setBounds(771, 241, 78, 25);
		teamAdminPanel.add(highExpertiseCoefficientTextField);
		
		JLabel hoursToSystemTimeCoefLabel = new JLabel("Model to System Time Coefficient");
		hoursToSystemTimeCoefLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		hoursToSystemTimeCoefLabel.setEnabled(false);
		hoursToSystemTimeCoefLabel.setBounds(10, 296, 208, 25);
		teamAdminPanel.add(hoursToSystemTimeCoefLabel);
		
		TCTtoSystemCoefTextField = new JTextField();
		TCTtoSystemCoefTextField.setColumns(10);
		TCTtoSystemCoefTextField.setBounds(10, 332, 155, 25);
		teamAdminPanel.add(TCTtoSystemCoefTextField);
		
		stopAfterEachSprintLabel = new JLabel("Stop After Each Sprint");
		stopAfterEachSprintLabel.setToolTipText("Enabling this option causes the system to stop after each sprint, and wait for direct user command for starting the next sprint. ");
		stopAfterEachSprintLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		stopAfterEachSprintLabel.setEnabled(false);
		stopAfterEachSprintLabel.setBounds(670, 332, 144, 25);
		teamAdminPanel.add(stopAfterEachSprintLabel);
		
		stopAfterEachSprintCheckBox = new JCheckBox("");
		stopAfterEachSprintCheckBox.setBounds(820, 334, 29, 25);
		teamAdminPanel.add(stopAfterEachSprintCheckBox);
		
		
		JButton saveSystemButton = new JButton("Save System");
		saveSystemButton.setBounds(694, 374, 155, 48);
		teamAdminPanel.add(saveSystemButton);
		
		JButton saveAsSystemDefaultsButton = new JButton("Save As System Defaults");
		saveAsSystemDefaultsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsDefaultSettings();
			}
		});
		
		saveAsSystemDefaultsButton.setBounds(482, 374, 182, 48);
		teamAdminPanel.add(saveAsSystemDefaultsButton);
		
		JButton loadSystemDefaultsButton = new JButton("Load System Defaults");
		loadSystemDefaultsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadSystemDefaults();
			}
		});
		loadSystemDefaultsButton.setBounds(258, 374, 182, 48);
		teamAdminPanel.add(loadSystemDefaultsButton);
		
		JLabel hoursPerSpringLabel = new JLabel("Hours per Sprint");
		hoursPerSpringLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		hoursPerSpringLabel.setEnabled(false);
		hoursPerSpringLabel.setBounds(248, 296, 155, 25);
		teamAdminPanel.add(hoursPerSpringLabel);
		
		hoursPerSpringTextField = new JTextField();
		hoursPerSpringTextField.setColumns(10);
		hoursPerSpringTextField.setBounds(248, 332, 155, 25);
		teamAdminPanel.add(hoursPerSpringTextField);
		
		JLabel sprintsPerProjectLabel = new JLabel("Sprints per Project");
		sprintsPerProjectLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		sprintsPerProjectLabel.setEnabled(false);
		sprintsPerProjectLabel.setBounds(465, 296, 155, 25);
		teamAdminPanel.add(sprintsPerProjectLabel);
		
		sprintsPerProjectTextField = new JTextField();
		sprintsPerProjectTextField.setColumns(10);
		sprintsPerProjectTextField.setBounds(465, 332, 155, 25);
		teamAdminPanel.add(sprintsPerProjectTextField);
		saveSystemButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveSystem();
			}
		});
		
		Panel displayTeamMembersPanel = new Panel();
		displayTeamMembersPanel.setBackground(SystemColor.menu);
		tabbedPane.addTab("Team Members", null, displayTeamMembersPanel, null);
		displayTeamMembersPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 35, 640, 354);
		displayTeamMembersPanel.add(scrollPane);
		
		personnelTable = new JTable();
		scrollPane.setViewportView(personnelTable);
		personnelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		personnelTable.setModel(personnelTableModel);
		
		JButton btnNewButton = new JButton("Add New Member");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ModifyMember newMember = new ModifyMember();
				newMember.setVisible(true);
			}
		});
		btnNewButton.setBounds(703, 107, 157, 40);
		displayTeamMembersPanel.add(btnNewButton);
		
		JButton modifyMemberButton = new JButton("Modify Member");
		modifyMemberButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		modifyMemberButton.setBounds(703, 188, 157, 40);
		displayTeamMembersPanel.add(modifyMemberButton);
		
		JButton removeMemberButton = new JButton("Remove Member");
		removeMemberButton.setBounds(703, 273, 157, 40);
		displayTeamMembersPanel.add(removeMemberButton);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Statistics Report", null, panel, null);
		panel.setLayout(null);
		
		Canvas graphsCanvas = new Canvas();
		graphsCanvas.setBackground(Color.WHITE);
		graphsCanvas.setBounds(10, 10, 692, 426);
		panel.add(graphsCanvas);
		
		JButton viewReportButton = new JButton("View Report");
		viewReportButton.setBounds(722, 99, 159, 51);
		panel.add(viewReportButton);
				
		toDoTasksTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}			
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}			
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = toDoTasksTable.getSelectedRow();
				if(i != -1){
					removeTaskButton.setEnabled(true);
					modifyTaskButton.setEnabled(true);
				}else{
					removeTaskButton.setEnabled(false);
					modifyTaskButton.setEnabled(false);
				}
			}
		});			
	}
	
	public void setVisible(boolean visibility){
		frame.setVisible(visibility);
	}
	
	private void applyTeamSettings(){
		Team team = Team.getTeam();
		String strategy = (String) allocationStrategyComboBoxModel.getSelectedItem();
		if(strategy.equals(TaskAllocationStrategy.ExpertiseBased.toString()))
			team.setTaskAllocationStrategy(TaskAllocationStrategy.ExpertiseBased);
		else if(strategy.equals(TaskAllocationStrategy.LearningBased.toString()))
			team.setTaskAllocationStrategy(TaskAllocationStrategy.LearningBased);
		try {
			String spc = storyPointCoefficientTextField.getText();
			double storyPointCoef = Double.parseDouble(spc);
			team.setStoryPointCoefficient(storyPointCoef);
		}catch(NumberFormatException exception) {
			storyPointCoefficientTextField.setText(Double.toString(team.getStoryPointCoefficient()));				
			Main.issueErrorMessage("The value provided for story point coefficients is not a valid double value, try again!");
		}
		
		try {
			String pps = progressPerStoryPointTextField.getText();
			double progressPerStoryPoint = Double.parseDouble(pps);
			team.setProgressPerStoryPoint(progressPerStoryPoint);					
		}catch(NumberFormatException exception) {
			progressPerStoryPointTextField.setText(Double.toString(team.getProgressPerStoryPoint()));
			Main.issueErrorMessage("The value provided for progress per story point is not a valid double value, try again!");
		}
		
		int step = 0;
		try {
			String lelb = lowExpertiseLowerBoundaryTextField.getText();
			int lowExpertiseLowerBoundary = Integer.parseInt(lelb);
								
			step = 1;
			String lehb = lowExpertiseUpperBoundaryTextField.getText();
			int lowExpertiseUpperBoundary = Integer.parseInt(lehb);
			team.setLowExpertiseBoundaries(lowExpertiseLowerBoundary, lowExpertiseUpperBoundary);
			
			step = 2;
			String lec = lowExpertiseCoefficientTextField.getText();
			int lowExpertiseCoefficient = Integer.parseInt(lec);
			team.setLowExpertiseCoefficient(lowExpertiseCoefficient);
		}catch(NumberFormatException exception) {
			if(step == 0) {
				lowExpertiseLowerBoundaryTextField.setText(Integer.toString(team.getLowExpertiseLowerBoundary()));
				Main.issueErrorMessage("The value provided for low expertise lower boundary is not a valid integer, try again!");
			}else if (step == 1) {
				lowExpertiseUpperBoundaryTextField.setText(Integer.toString(team.getLowExpertiseHigherBoundary()));
				Main.issueErrorMessage("The value provided for low expertise uppder boundary is not a valid integer, try again!");
			}else if (step == 2) {
				lowExpertiseCoefficientTextField.setText(Integer.toString(team.getLowExpertiseCoefficient()));
				Main.issueErrorMessage("The value provided for low expertise coefficient is not a valid integer, try again!");
			}
		}
		
		step = 0;
		try {
			String melb = medExpertiseLowerBoundaryTextField.getText();
			int medExpertiseLowerBoundary = Integer.parseInt(melb);
			
			step = 1;
			String mehb = medExpertiseUpperBoundaryTextField.getText();
			int medExpertiseUpperBoundary = Integer.parseInt(mehb);
			team.setMediumExpertiesBoundaries(medExpertiseLowerBoundary, medExpertiseUpperBoundary);
			
			step = 2;
			String mec = medExpertiseCoefficientTextField.getText();
			int medExpertiseCoefficient = Integer.parseInt(mec);
			team.setMediumExpertiseCoefficient(medExpertiseCoefficient);
		}catch(NumberFormatException exception) {
			if(step == 0) {
				medExpertiseLowerBoundaryTextField.setText(Integer.toString(team.getMediumExpertiseLowerBoundary()));
				Main.issueErrorMessage("The value provided for medium expertise lower boundary is not a valid integer, try again!");
			}else if (step == 1) {
				medExpertiseUpperBoundaryTextField.setText(Integer.toString(team.getMediumExpertiseHigherBoundary()));
				Main.issueErrorMessage("The value provided for medium expertise uppder boundary is not a valid integer, try again!");
			}else if (step == 2) {
				medExpertiseCoefficientTextField.setText(Integer.toString(team.getMediumExpertiseCoefficient()));
				Main.issueErrorMessage("The value provided for medium expertise coefficient is not a valid integer, try again!");
			}
		}
		
		step = 0;
		try {
			String helb = highExpertiseLowerBoundaryTextField.getText();
			int highExpertiseLowerBoundary = Integer.parseInt(helb);
			
			step = 1;
			String hehb = highExpertiseUpperBoundaryTextField.getText();
			int highExpertiseUpperBoundary = Integer.parseInt(hehb);
			team.setHighExpertiseBoundaries(highExpertiseLowerBoundary, highExpertiseUpperBoundary);
			
			step = 2;
			String hec = highExpertiseCoefficientTextField.getText();
			int highExpertiseCoefficient = Integer.parseInt(hec);
			team.setHighExpertiseCoefficient(highExpertiseCoefficient);
		}catch(NumberFormatException exception) {
			if(step == 0) {
				highExpertiseLowerBoundaryTextField.setText(Integer.toString(team.getHighExpertiseLowerBoundary()));
				Main.issueErrorMessage("The value provided for high expertise lower boundary is not a valid integer, try again!");
			}else if (step == 1) {
				highExpertiseUpperBoundaryTextField.setText(Integer.toString(team.getHighExpertiseHigherBoundary()));
				Main.issueErrorMessage("The value provided for high expertise uppder boundary is not a valid integer, try again!");
			}else if (step == 2) {
				highExpertiseCoefficientTextField.setText(Integer.toString(team.getHighExpertiseCoefficient()));
				Main.issueErrorMessage("The value provided for high expertise coefficient is not a valid integer, try again!");
			}
		}
		
		try {
			String tst = TCTtoSystemCoefTextField.getText();
			int tctToSystemTime = Integer.parseInt(tst);
			team.setHoursToSystemTimeCoefficient(tctToSystemTime);
		}catch(NumberFormatException exception) {
			TCTtoSystemCoefTextField.setText(Integer.toString(team.getHoursToSystemTimeCoefficient()));
			Main.issueErrorMessage("The value provided for TCT to system time coefficient is not a valid integer, try again!");
		}
		
		try{
			String hoursPerSprint = hoursPerSpringTextField.getText();
			int hoursPerSpringInt = Integer.parseInt(hoursPerSprint);
			team.setHoursPerSprint(hoursPerSpringInt);
		}catch(NumberFormatException exception){
			hoursPerSpringTextField.setText(Integer.toString(team.getHoursPerSpring()));
			Main.issueErrorMessage("The value provided for hours-per-sprint is not a valid integer, try again!");
		}		
		
		try{
			String sprintsPerProject = sprintsPerProjectTextField.getText();
			int sprintsPerProjectInt = Integer.parseInt(sprintsPerProject);
			team.setNumberOfSprintsPerProject(sprintsPerProjectInt);
		}catch(NumberFormatException exception){
			sprintsPerProjectTextField.setText(Integer.toString(team.getNumberOfSprintsPerProject()));
			Main.issueErrorMessage("The value provided for sprints-per-project is not a valid integer, try again!");
		}
	}
	
	private void saveSystem(){
		applyTeamSettings();
		SystemRecorder.recordSystemStatus();
	}
	
	private void deleteSelectedRows(int[] selectedIndices){
		Team team = Team.getTeam();
		team.deleteFromBackLog(selectedIndices);
		repopulateBackLogTable();
	}
	
	private void saveAsDefaultSettings(){
		applyTeamSettings();
		SystemRecorder.recordAsSystemDefault();
	}
	
	private void loadSystemDefaults(){
		SystemLoader.loadDefaultSystemSettings();
		repopulateTeamTab();
		repopulateBackLogTable();
		repopulateToDoTasksTable();
		repopulateTaskInProgressTable();
		repopulateCompletedTasksTable();
		repopulatePersonnelTable();
	}
	
	public void updateBackLogTabel(Task task) {
		String taskId = Integer.toString(task.getTaskID());
		String storyPoints = Integer.toString(task.getStoryPoints());
		String priority = Integer.toString(task.getPriority());
		String taskName = task.getTaskName();
		String skillsString = "";
		int counter = 1;
		Set<SkillArea> skills = task.getRequiredSkillAreas();
		for(SkillArea skill : skills) {
			skillsString += skill.toString();
			if(counter != skills.size())
				skillsString += ", ";
			counter++;
		}
		
		String[] rowData = {taskId, storyPoints, priority, taskName, skillsString};
		backLogTableModel.addRow(rowData);
	}	
	
	public void updatePersonnelTabel(TeamMember member){
		String id = Integer.toString(member.getID());
		String firstName = member.getFirstName();
		String lastName = member.getLastName();
		String role = member.getMemberRole().toString();
		String expInFE = Double.toString(member.getExpertiseAtSkillArea(SkillArea.FrontEnd));
		String expInBE = Double.toString(member.getExpertiseAtSkillArea(SkillArea.BackEnd));
		String expInDesign = Double.toString(member.getExpertiseAtSkillArea(SkillArea.Design));
		
		String[] rowData = {id, firstName, lastName, role, expInFE, expInBE, expInDesign};
		personnelTableModel.addRow(rowData);
	}
	
	public void repopulateBackLogTable(){
		int numOfRows = backLogTableModel.getRowCount();
		for(int index = (numOfRows-1); index >= 0; index--)
			backLogTableModel.removeRow(index);
		
		List<Task> tasks = Team.getTeam().getProjectBackLog();
		for(Task task : tasks){
			String taskId = Integer.toString(task.getTaskID());
			String storyPoints = Integer.toString(task.getStoryPoints());
			String priority = Integer.toString(task.getPriority());
			String taskName = task.getTaskName();
			String skillsString = "";
			int counter = 1;
			Set<SkillArea> skills = task.getRequiredSkillAreas();
			for(SkillArea skill : skills) {
				skillsString += skill.toString();
				if(counter != skills.size())
					skillsString += ", ";
				counter++;
			}
			
			String[] rowData = {taskId, storyPoints, priority, taskName, skillsString};
			backLogTableModel.addRow(rowData);
		}
	}	
	
	public void repopulateToDoTasksTable(){
		int numOfRows = toDoTaskTableModel.getRowCount();
		for(int index = (numOfRows-1); index >= 0; index--)
			toDoTaskTableModel.removeRow(index);
		
		ConcurrentLinkedQueue<Task> tasks = Team.getTeam().getToDoTasks();
		for(Task task : tasks){
			String taskId = Integer.toString(task.getTaskID());
			String storyPoints = Integer.toString(task.getStoryPoints());
			String priority = Integer.toString(task.getPriority());
			String skillsString = "";
			int counter = 1;
			Set<SkillArea> skills = task.getRequiredSkillAreas();
			for(SkillArea skill : skills) {
				skillsString += skill.toString();
				if(counter != skills.size())
					skillsString += ", ";
				counter++;
			}
			
			String[] rowData = {taskId, storyPoints, priority, skillsString};
			toDoTaskTableModel.addRow(rowData);
		}
	}
	
	public void repopulateTaskInProgressTable(){
		int numOfRows = tasksInProgressTableModel.getRowCount();
		for(int index = (numOfRows-1); index >= 0; index--)
			tasksInProgressTableModel.removeRow(index);
		
		ConcurrentLinkedQueue<Task> tasks = Team.getTeam().getTasksInProgress();
		for(Task task : tasks){
			String taskId = Integer.toString(task.getTaskID());
			String storyPoints = Integer.toString(task.getStoryPoints());
			String performerID = Integer.toString(task.getPerformerID());
			
			String[] rowData = {taskId, storyPoints, performerID};
			tasksInProgressTableModel.addRow(rowData);
		}
	}
	
	public void repopulateCompletedTasksTable(){
		int numOfRows = finishedTasksTableModel.getRowCount();
		for(int index = (numOfRows-1); index >= 0; index--)
			finishedTasksTableModel.removeRow(index);
		
		ConcurrentLinkedQueue<Task> tasks = Team.getTeam().getPerformedTasks();
		for(Task task : tasks){
			String taskId = Integer.toString(task.getTaskID());
			String storyPoints = Integer.toString(task.getStoryPoints());
			String performerID = Integer.toString(task.getPerformerID());
			
			String[] rowData = {taskId, storyPoints, performerID};
			finishedTasksTableModel.addRow(rowData);
		}
	}
		
	public void repopulatePersonnelTable(){
		int numOfRows = personnelTableModel.getRowCount();
		for(int index = (numOfRows-1); index >= 0; index--)
			personnelTableModel.removeRow(index);
		
		List<TeamMember> members = Team.getTeam().getPersonnel();
		for(TeamMember member : members){
			String ID = Integer.toString(member.getID());
			String firstName = member.getFirstName();
			String lastname = member.getLastName();
			String role = member.getMemberRole().toString();
			String expInFE = Double.toString(member.getExpertiseAtSkillArea(SkillArea.FrontEnd));
			String expInBE = Double.toString(member.getExpertiseAtSkillArea(SkillArea.BackEnd));
			String expInDesign = Double.toString(member.getExpertiseAtSkillArea(SkillArea.Design));
			
			String[] rowData = {ID, firstName, lastname, role, expInFE, expInBE, expInDesign};
			personnelTableModel.addRow(rowData);
		}
	}
	
	public void repopulateTeamTab(){
		Team team = Team.getTeam();
		allocationStrategyComboBoxModel.setSelectedItem(team.getTaskAllocationStrategy().toString());
		storyPointCoefficientTextField.setText(Double.toString(team.getStoryPointCoefficient()));
		progressPerStoryPointTextField.setText(Double.toString(team.getProgressPerStoryPoint()));
		lowExpertiseLowerBoundaryTextField.setText(Integer.toString(team.getLowExpertiseLowerBoundary()));
		lowExpertiseUpperBoundaryTextField.setText(Integer.toString(team.getLowExpertiseHigherBoundary()));
		lowExpertiseCoefficientTextField.setText(Integer.toString(team.getLowExpertiseCoefficient()));
		medExpertiseLowerBoundaryTextField.setText(Integer.toString(team.getMediumExpertiseLowerBoundary()));
		medExpertiseUpperBoundaryTextField.setText(Integer.toString(team.getMediumExpertiseHigherBoundary()));		
		medExpertiseCoefficientTextField.setText(Integer.toString(team.getMediumExpertiseCoefficient()));	
		highExpertiseLowerBoundaryTextField.setText(Integer.toString(team.getHighExpertiseLowerBoundary()));	
		highExpertiseUpperBoundaryTextField.setText(Integer.toString(team.getHighExpertiseHigherBoundary()));	
		highExpertiseCoefficientTextField.setText(Integer.toString(team.getHighExpertiseCoefficient()));	
		TCTtoSystemCoefTextField.setText(Integer.toString(team.getHoursToSystemTimeCoefficient()));
		sprintNumberLabel.setText(Integer.toString(team.getCurrentSprint()));
		sprintsPerProjectTextField.setText(Integer.toString(team.getNumberOfSprintsPerProject()));
		hoursPerSpringTextField.setText(Integer.toString(team.getHoursPerSpring()));
		boolean stopAfterSprint = team.getStopAfterEachSprint();
		if(stopAfterSprint)
			stopAfterEachSprintCheckBox.setSelected(true);
		else
			stopAfterEachSprintCheckBox.setSelected(false);
	}	
	
	public int getTasBoardProgress(){
		return taskBoardProgressBar.getValue();
	}
	
	public void setTaskBoardProgress(int progress){
		taskBoardProgressBar.setValue(progress);
		taskBoardProgressBar.repaint();
	}
	
	public  void setTaskBoardSprintNo(int sprintNo){
		sprintNumberLabel.setText(Integer.toString(sprintNo));
	}
	
	public void setLastSprintVelocity(double velocity){
		DecimalFormat df = new DecimalFormat("#.##");		
		lastSprintVelocityValueLabel.setText(df.format(velocity));
	}
	
	public void enableStartButton(boolean enable) {
		this.startSprintButton.setEnabled(enable);
	}
	
	private void automaticTaskMoveToFirstSprint(){
		Team team = Team.getTeam();
		List<Integer> taskIndices = new ArrayList<>();
		int storyPointsToBeMoved = 0;
		int taskIndex = 0;
		do{
			taskIndices.add(taskIndex);
			storyPointsToBeMoved += Integer.parseInt((String)backLogTableModel.getValueAt(taskIndex, 1));
			taskIndex++;
		}while(storyPointsToBeMoved < team.getInitialStoryPoints());
		int[] array = new int[taskIndices.size()];
		for(int index = 0; index < taskIndices.size(); index++){
			array[index] = taskIndices.get(index);
		}
		team.moveToSprintBackLog(array);
	}
}
