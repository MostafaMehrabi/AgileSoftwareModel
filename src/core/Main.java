package core;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import entities.Task;
import entities.TeamMember;
import view.ErrorMessageBox;
import view.MainWindow;
import view.ProgressBar;

public class Main {
	
	private static String baseDirectoryPath = "." + File.separator + "Files";
	private static String statisticsDirectoryPath = "." + File.separator + "Statistics";
	private static String permutationFileName = "Permutation";
	private static String scenarioDirectoryName = "scenarios";
	private static String firstScenarioFolderName = "Scenario1-AllMembersLowExpertiseInAllSkills";
	private static String secondScenarioFolderName = "Scenario2-TwoSkillsThreeHighThreeLow-OneSkillAllMedium";
	private static String thirdScenarioFolderName = "Scenario3-TwoSkillsThreeMediumThreeLow-OneSkillAllHigh";
	private static String fourthScenarioFolderName = "Scenario4-TwoSkillsThreeMediumThreeHigh-OneSkillAllLow";
	private static String fifthScenarioFolderName = "Scenario5-AllMembersNearlyZeroExpertiseInAllSkills";
	private static String mainFileName = "Main";
	private static String personnelFileName = "Personnel";
	private static String teamFileName = "Team";
	private static String taskBoardFileName = "TaskBoard";
	private static String toDoTasksForSprintFileName = "ToDoTasks";
	private static String finishedTasksForSprintFileName = "PerformedTasks";
	private static String backLogTasksFileName = "BackLogTasks";
	private static String allFinishedTasksFileName = "AllPerformedTasks";
	private static String inProgressTasksForSprintFileName = "TasksInProgress";
	private static String taskSyntax = "TASK: ";
	private static String descriptionSyntax = "DESCRIPTION: ";
	private static MainWindow mainWindow = null;
	private static ProgressBar progressBar = null;
	
	
	public static void main(String[] args){
		
		try {
			EventQueue.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					try {
						progressBar = new ProgressBar("Please wait while system is loading ...");
						progressBar.setVisible(true);
					}catch(Exception e) {
						e.printStackTrace();
					}				
				}
			});
		} catch (InvocationTargetException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		loadMainInfo();
		progressBar.setValue(5);
		SystemLoader.loadSystem(progressBar);
		progressBar.close();
		EventQueue.invokeLater(new Runnable() {			
			@Override
			public void run() {
				try{
					mainWindow = new MainWindow();
					mainWindow.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});		
	}
	
	private static void loadMainInfo(){
		String mainFileName = baseDirectoryPath + File.separator + Main.mainFileName;
		File mainFile = new File(mainFileName);
		if(mainFile.exists()){
			try{
				BufferedReader reader = new BufferedReader(new FileReader(mainFile));
				personnelFileName = reader.readLine();
				teamFileName = reader.readLine();
				taskBoardFileName = reader.readLine();
				toDoTasksForSprintFileName = reader.readLine();
				inProgressTasksForSprintFileName = reader.readLine();
				finishedTasksForSprintFileName = reader.readLine();
				backLogTasksFileName = reader.readLine();
				allFinishedTasksFileName = reader.readLine();
				taskSyntax = reader.readLine();
				descriptionSyntax = reader.readLine();
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void updateBackLogTabel(Task task) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainWindow.updateBackLogTabel(task);
			}
		});
	}
	
	public static void updatePersonnelTabel(TeamMember member){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainWindow.updatePersonnelTabel(member);
			}
		});
	}	
	
	public static String getScenarioFolderName(int scenarioNumber) {
		switch (scenarioNumber) {
			case 1:
				return getFirstScenarioFolderName();
			case 2:
				return getSecondScenarioFolderName();
			case 3: 
				return getThridScenarioFolderName();
			case 4:
				return getFourthScenarioFolderName();
			default:
				return getFifthScenarioFolderName();
		}
	}
	
	public static String getFirstScenarioFolderName() {
		return Main.firstScenarioFolderName;
	}
	
	public static String getSecondScenarioFolderName() {
		return Main.secondScenarioFolderName;
	}
	
	public static String getThridScenarioFolderName() {
		return Main.thirdScenarioFolderName;
	}
	
	public static String getFourthScenarioFolderName() {
		return Main.fourthScenarioFolderName;
	}
	
	public static String getFifthScenarioFolderName() {
		return Main.fifthScenarioFolderName;
	}
	
	public static void setScenarioDirectoryName(String name) {
		Main.scenarioDirectoryName = name;
	}
	
	public static String getScenarioDirectoryName() {
		return Main.scenarioDirectoryName;
	}

	public static String getPermutationFileName() {
		return Main.permutationFileName;
	}
	
	public static void setPermutationFileName(String name) {
		Main.permutationFileName = name;
	}
	
	public static String getMainFileName(){
		return Main.mainFileName;
	}
	
	public static String getBaseDirectoryPath(){
		return Main.baseDirectoryPath;
	}
	
	public static void setBaseDirectoryPath(String path){
		Main.baseDirectoryPath = path;
	}
	
	public static void setStatisticsDirectoryPath(String path) {
		Main.statisticsDirectoryPath = path;
	}
	
	public static String getStatisticsDirectoryPath() {
		return Main.statisticsDirectoryPath;
	}

	public static String getTaskBoardFielName(){
		return Main.taskBoardFileName;
	}
	
	public static void setTaskBoardFileName(String name){
		Main.taskBoardFileName = name;
	}
		
	public static String getPersonnelFileName(){
		return Main.personnelFileName;
	}
	
	public static void setPersonnelFileName(String name){
		Main.personnelFileName = name;
	}
	
	public static String getTeamFileName(){
		return Main.teamFileName;
	}
	
	public static void setTeamFileName(String name){
		Main.teamFileName = name;
	}
	
	public static String getToDoTasksForSprintFileName(){
		return Main.toDoTasksForSprintFileName;
	}
	
	public static void setToDoTasksForSprintFileName(String name){
		Main.toDoTasksForSprintFileName = name;
	}
	
	public static String getInProgressTasksFileName(){
		return Main.inProgressTasksForSprintFileName;
	}
	
	public static void setInProgressTasksFileName(String name){
		Main.inProgressTasksForSprintFileName = name;
	}
	
	public static String getPerformedTasksForSprintFileName(){
		return Main.finishedTasksForSprintFileName;
	}
	
	public static void setPerformedTasksForSprintFileName(String name){
		Main.finishedTasksForSprintFileName = name;
	}
	
	public static String getBackLogTasksFileName(){
		return Main.backLogTasksFileName;
	}
	
	public static void setBackLogTasksFileName(String name){
		Main.backLogTasksFileName = name;
	}
	
	public static String getAllFinishedTasksFileName(){
		return Main.allFinishedTasksFileName;
	}
	
	public static void setAllFinishedTasksFileName(String name){
		Main.allFinishedTasksFileName = name;
	}
	
	public static String getTaskSyntax(){
		return Main.taskSyntax;
	}
	
	public static void setTaskSyntax(String syntax){
		Main.taskSyntax = syntax;
	}
	
	public static String  getDescriptionSyntax(){
		return Main.descriptionSyntax;
	}
	
	public static void setDescriptionSyntax(String syntax){
		Main.descriptionSyntax = syntax;
	}
	
	
	public static  void issueErrorMessage(String message) {
		EventQueue.invokeLater(new Runnable() {			
			@Override
			public void run() {
				try {
					ErrorMessageBox errorBox = new ErrorMessageBox(message);
					errorBox.setVisible(true);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static boolean issueQuesionDialogue(String message, String dialogTitle){
		if(dialogTitle.isEmpty())
			dialogTitle = "Please Confirm";
		
		int answer = JOptionPane.showConfirmDialog(null, message, dialogTitle, JOptionPane.YES_NO_OPTION);
		
		if(answer == 0)
			return true;
		else 
			return false;
	}	
	
	public static void setTaskBoardProgress(int progress){
		mainWindow.setTaskBoardProgress(progress);	
	}
	
	public static int getTaskBoardProgress(){
		return mainWindow.getTasBoardProgress();
	}
	
	public static void setTaskBoardSprintNo(int sprintNo){
		EventQueue.invokeLater(new Runnable() {			
			@Override
			public void run() {
				mainWindow.setTaskBoardSprintNo(sprintNo);
			}
		});
	}
	
	public static void setLastSprintVelocity(double velocity){
		EventQueue.invokeLater(new Runnable() {			
			@Override
			public void run() {
				mainWindow.setLastSprintVelocity(velocity);
			}
		});
	}
	
	public static void enableStartButton(boolean enable) {
		mainWindow.enableStartButton(enable);
	}
	
	public synchronized static void repopulateTasksInProgressTable(){
		mainWindow.repopulateTaskInProgressTable();
	}
	
	public synchronized static void repopulateCompletedTasksTable(){
		mainWindow.repopulateCompletedTasksTable();
	}
	
	public synchronized static void repopulateToDoTaskTable(){
		mainWindow.repopulateToDoTasksTable();
	}
	
	public synchronized static void repopulatePersonnelTabel(){
		mainWindow.repopulatePersonnelTable();
	}
	
	public synchronized static void repopulateProjectBackLog() {
		mainWindow.repopulateBackLogTable();
	}
}
