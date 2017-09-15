package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Panel;
import javax.swing.table.DefaultTableModel;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import javax.swing.ScrollPaneConstants;
import java.awt.SystemColor;

import enums.MemberRole;


public class MainWindow {

	private JFrame frame;
	private final ButtonGroup radioButtonGroup = new ButtonGroup();
	private MemberRole role = MemberRole.Developer;
	private JTable table;
	private JTable toDoTasksTable;
	private JTable tasksInProgressTable;
	private JTable finishedTasksTable;
	private DefaultTableModel toDoTaskTableModel = new DefaultTableModel();
	private DefaultTableModel tasksInProgressTableModel = new DefaultTableModel();
	private DefaultTableModel finishedTasksTableModel = new DefaultTableModel();



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
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setResizable(false);
		frame.setBounds(100, 100, 902, 502);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setAutoscrolls(true);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		tabbedPane.setBounds(0, 0, 896, 474);
		frame.getContentPane().add(tabbedPane);
		String[] toDoTasksColumn = {"ID", "Name", "Skills"};
		toDoTaskTableModel.setColumnIdentifiers(toDoTasksColumn);
		toDoTaskTableModel.setRowCount(2);
		String[] tasksInProgressColumn = {"ID", "Name", "PerformerID"};
		tasksInProgressTableModel.setColumnIdentifiers(tasksInProgressColumn);
		tasksInProgressTableModel.setRowCount(2);
		String[] finishedTasksColumn = {"ID", "Name", "PerformerID"};
		finishedTasksTableModel.setColumnIdentifiers(finishedTasksColumn);
		finishedTasksTableModel.setRowCount(2);
		String[] tableColumns = {"ID", "First Name", "Last Name", "Exp. in Front End", "Exp. in Back End"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(tableColumns);
		tableModel.setRowCount(2);
		
		JPanel teamAdminPanel = new JPanel();
		teamAdminPanel.setBackground(SystemColor.menu);
		tabbedPane.addTab("Team Admin", null, teamAdminPanel, null);
		teamAdminPanel.setLayout(null);
		
		JPanel displayProjectBackLogPanel = new JPanel();
		displayProjectBackLogPanel.setBackground(SystemColor.menu);
		tabbedPane.addTab("Project BackLog", null, displayProjectBackLogPanel, null);
		
		
		
		Panel displayTaskBoardPanel = new Panel();
		displayTaskBoardPanel.setBackground(SystemColor.menu);
		tabbedPane.addTab("Task Board", null, displayTaskBoardPanel, null);
		displayTaskBoardPanel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(130, 83, 235, 272);
		displayTaskBoardPanel.add(scrollPane_1);
		
		toDoTasksTable = new JTable();
		toDoTasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(toDoTasksTable);
		toDoTasksTable.setModel(toDoTaskTableModel);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(385, 83, 241, 272);
		displayTaskBoardPanel.add(scrollPane_2);
		
		tasksInProgressTable = new JTable();
		tasksInProgressTable.setRowSelectionAllowed(false);
		scrollPane_2.setViewportView(tasksInProgressTable);
		tasksInProgressTable.setModel(tasksInProgressTableModel);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
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
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.GREEN);
		progressBar.setBounds(212, 400, 587, 31);
		displayTaskBoardPanel.add(progressBar);
		
		JLabel lblTimeLeftTo = new JLabel("Time Left to the End of Sprint");
		lblTimeLeftTo.setEnabled(false);
		lblTimeLeftTo.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblTimeLeftTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeLeftTo.setBounds(385, 363, 241, 31);
		displayTaskBoardPanel.add(lblTimeLeftTo);
		
		JLabel taskBoardLabel = new JLabel("Task Board of Sprint No. ");
		taskBoardLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		taskBoardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		taskBoardLabel.setBounds(353, 11, 175, 30);
		displayTaskBoardPanel.add(taskBoardLabel);
		
		JLabel sprintNumberLabel = new JLabel("1");
		sprintNumberLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
		sprintNumberLabel.setBounds(521, 11, 52, 30);
		displayTaskBoardPanel.add(sprintNumberLabel);
		
		JButton startSprintButton = new JButton("Start the Sprint");
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
		
		Panel displayTeamMembersPanel = new Panel();
		displayTeamMembersPanel.setBackground(SystemColor.menu);
		tabbedPane.addTab("Team Members", null, displayTeamMembersPanel, null);
		displayTeamMembersPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 35, 566, 315);
		displayTeamMembersPanel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(tableModel);
				
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
}
