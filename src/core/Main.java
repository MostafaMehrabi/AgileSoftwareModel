package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {
	
	private static String baseDirectoryPath = "." + File.separator + "Files";
	private static String mainFileName = "Main";
	private static String personnelFileName = "Personnel";
	private static String teamFileName = "Team";
	private static String taskBoardFileName = "TaskBoard";
	private static String toDoTasksForSpringFileName = "ToDoTasks";
	private static String finishedTasksForSprintFileName = "FinishedTasksForSprint";
	private static String backLogTasksFileName = "BackLogTasks";
	private static String allFinishedTasksFileName = "AllFinishedTasks";

	public static void main(String[] args){
		loadMainInfo();
	}
	
	private static void loadMainInfo(){
		String mainFileName = baseDirectoryPath + File.separator + Main.mainFileName;
		File mainFile = new File(mainFileName);
		if(mainFile.exists()){
			try{
				BufferedReader reader = new BufferedReader(new FileReader(mainFile));
				baseDirectoryPath = reader.readLine();
				personnelFileName = reader.readLine();
				teamFileName = reader.readLine();
				taskBoardFileName = reader.readLine();
				toDoTasksForSpringFileName = reader.readLine();
				finishedTasksForSprintFileName = reader.readLine();
				backLogTasksFileName = reader.readLine();
				allFinishedTasksFileName = reader.readLine();
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
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
		return Main.toDoTasksForSpringFileName;
	}
	
	public static void setToDoTasksForSprintFileName(String name){
		Main.toDoTasksForSpringFileName = name;
	}
	
	public static String getFinishedTasksForSprintFileName(){
		return Main.finishedTasksForSprintFileName;
	}
	
	public static void setFinishedTasksForSprintFileName(String name){
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
}
