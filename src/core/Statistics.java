package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;

import entities.Task;
import entities.Team;
import entities.TeamMember;
import enums.SkillArea;
import enums.TaskAllocationStrategy;

public class Statistics {
	
	private static Statistics stats = null;
	private String expertiseBasedFolderName = null;
	private String learningBasedFolderName = null;
	private String extrasFolderName = null;
	private int hoursPerSprint = 0;
	private int systemToModelTimeCoef = 0;
	
	private Statistics() {
		expertiseBasedFolderName = "ExpertiseBased";
		learningBasedFolderName = "LearningBased";
		extrasFolderName = "extras";
		hoursPerSprint = Team.getTeam().getHoursPerSpring();
		systemToModelTimeCoef = Team.getTeam().getSystemToModelTimeCoefficient();
	}
	
	public static Statistics getStatRecorder() {
		if(stats == null) {
			stats = new Statistics();
			stats.createFolders();		

		}
		return stats;
	}
	
	private void createFolders() {
		Team team = Team.getTeam();
		for(int permutationIndex = 1; permutationIndex <= team.getTotalNumberOfPermutations(); permutationIndex++) {
			String statsFolderPath = Main.getStatisticsDirectoryPath();
			String permutationFolderPath = statsFolderPath + File.separator + Main.getPermutationFileName() + permutationIndex;
			File permutationFolder = new File(permutationFolderPath);
			if(!permutationFolder.exists()) {
				permutationFolder.mkdirs();
			}
			
			for(int scenarioIndex = 1; scenarioIndex <= team.getTotalNumberOfScenarios(); scenarioIndex++) {
				String scenarioFolderName = Main.getScenarioDirectoryName(scenarioIndex);
				String scenarioFolderPath = permutationFolderPath + File.separator + scenarioFolderName;
				File scenarioFolder = new File(scenarioFolderPath);
				if(!scenarioFolder.exists()) {
					scenarioFolder.mkdirs();
				}
				//extras is where additional information regarding the task log of a sprint, and each and every task tried by an employee goes.
				String expertiseBasedFolderPath = scenarioFolderPath + File.separator + expertiseBasedFolderName + File.separator + extrasFolderName;
				File expertiseBasedFolder = new File(expertiseBasedFolderPath);
				if(!expertiseBasedFolder.exists())
					expertiseBasedFolder.mkdirs();
				
				String learningBasedFolderPath = scenarioFolderPath + File.separator + learningBasedFolderName + File.separator + extrasFolderName;
				File learningBasedFolder = new File(learningBasedFolderPath);
				if(!learningBasedFolder.exists())
					learningBasedFolder.mkdirs();
				
			}
		}
	}
	
	//returns the statistic folder for a specific permutation and scenario
	private String getStatsFolder() {
		Team team = Team.getTeam();
		String permutationFolderName = Main.getStatisticsDirectoryPath() + File.separator + Main.getPermutationFileName() + team.getPermutationNumber();
		String scenarioFolderName = Main.getScenarioDirectoryName(team.getScenarioNumber());
		String scenarioPath = permutationFolderName + File.separator + scenarioFolderName;
		TaskAllocationStrategy strategy = team.getTaskAllocationStrategy();
		if(strategy.equals(TaskAllocationStrategy.ExpertiseBased)){
			return scenarioPath + File.separator + expertiseBasedFolderName;
		}else if (strategy.equals(TaskAllocationStrategy.LearningBased)){
			return scenarioPath + File.separator + learningBasedFolderName;
		}			
		return null;
	}
	
	private String getTeamStatsFileName(){
		return getStatsFolder() + File.separator + "TeamStats.csv";
	}
	
	private String getPersonnelFilePrefix(){
		return getStatsFolder() + File.separator + "Personnel_";
	}
	
	private String getPerformedTasksFilePrefix(){
		return getStatsFolder() + File.separator + "tasksPeformedForSprint_";
	}
	
	private String getSprintTaskLogPrefix() {
		return getStatsFolder() + File.separator + extrasFolderName + File.separator + "taskLogForSprint_"; 
	}
	
	private String getPersonnelExtraInfoPrefix() {
		return getStatsFolder() + File.separator + extrasFolderName + File.separator + "extraInformationForMember_";
	}
	
	private void createTeamStatsFile(){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(getTeamStatsFileName());
			String title = "sprint,velocity,storyPoints,duration,idlePeriod";
			writer.println("sep=,");
			writer.println(title);
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHEN CREATING LOG FILE FOR TEAM");
			e.printStackTrace();
		}
		writer.flush();
		writer.close();
	}
	
	private PrintWriter loadTeamStatsFile() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(getTeamStatsFileName(), true)));
			return writer;
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHEN OPENING THE LOG FILE FOR TEAM");
			e.printStackTrace();
		}
		return null;
	}
		
	private void createPersonnelStatsFiles() {
		List<TeamMember> personnel = Team.getTeam().getPersonnel();
		//create csv file for logging standard information of personnel for each scenario...
		try {
			for(TeamMember member : personnel) {
				String fileName = getPersonnelFilePrefix() + member.getID() + ".csv";
				PrintWriter  writer = new PrintWriter(fileName);
				String title = "sprint,systemTime,modelTime,taskID,storyPoints,start/end,BackEnd Exp,FrontEnd Exp,Design Exp";
				writer.println("sep=,");
				writer.println(title);
				writer.flush();
				writer.close();
			}	
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHILE TRYING TO CREATE CSV FILES FOR PERSONNEL");
			e.printStackTrace();
		}		
		// create csv file for logging extra information of personnel for each scenario...
		try {
			for(TeamMember member : personnel) {
				String fileName = getPersonnelExtraInfoPrefix() + member.getID() + ".csv";
				PrintWriter writer = new PrintWriter(fileName);
				String title = "sprint,systemTime,modelTime,task,storyPoints,picked/accepted/rejected,description";
				writer.println("sep=,");
				writer.println(title);
				writer.flush();
				writer.close();
			}
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHILE TRYING TO CREATE EXTRA CSV FILES FOR PERSONNEL");
			e.printStackTrace();
		}
	}
	
	private PrintWriter loadPersonnelFile(int id) {
		try {
			String fileName = getPersonnelFilePrefix() + id + ".csv";
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
			return writer;
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHILE TRYING TO LOAD THE FILE FOR TEAM MEMBER WITH ID: " + id);
			e.printStackTrace();
		}
		return null;
	}
	
	private String listAllSkills(Task task) {
		String skillString = ""; 
		Set<SkillArea> skills = task.getRequiredSkillAreas();
		int counter = 0;
		for(SkillArea skill : skills) {
			counter++;
			skillString += skill.toString();
			if(counter != skills.size())
				skillString += " "; 
		}
		return skillString;
	}
	
	private int logPerformedTasksForSprint(ConcurrentLinkedQueue<Task> finishedTasks, int sprintNo) {
		String fileName = getPerformedTasksFilePrefix() + sprintNo + ".csv";
		int totalStoryPoints = 0;
		try {
			PrintWriter taskLogger = new PrintWriter(fileName);
			String title = "TaskName,TaskID,Priority,StoryPoints,PerformerID,RequiredSkills";
			taskLogger.println("sep=,");
			taskLogger.println(title);
			for(Task task : finishedTasks) {
				totalStoryPoints += task.getStoryPoints();
				String record = task.getTaskName() + "," + task.getTaskID() + "," + task.getPriority() + "," + task.getStoryPoints() 
						+ "," + task.getPerformerID() + "," + listAllSkills(task);
				taskLogger.println(record);
			}
			taskLogger.flush();
			taskLogger.close();
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHEN CREATING A LOG FILE FOR RECORDING THE TASKS PERFORMED IN SPRINT NO. " + sprintNo);
			e.printStackTrace();
		}
		return totalStoryPoints;
	}
	
	public void createBaseFiles() {
		createTeamStatsFile();
		createPersonnelStatsFiles();
	}
	
	public void logSprintInfo(ConcurrentLinkedQueue<Task> finishedTasks, double sprintVelocity, int sprintNo, long duration) {
		//sprint,velocity,storyPoints,duration,idlePeriod
		int accomplishedStoryPoints = logPerformedTasksForSprint(finishedTasks, sprintNo);
		int modelDuration = (int) (duration / systemToModelTimeCoef);
		int idlePeriod = hoursPerSprint - (modelDuration);
		if(idlePeriod < 0)
			idlePeriod = 0;
		PrintWriter teamStatsLogger = loadTeamStatsFile();
		String record = Integer.toString(sprintNo) + "," + Double.toString(sprintVelocity) + "," 
				+ Integer.toString(accomplishedStoryPoints) + "," + Integer.toString(modelDuration) + "," + Integer.toString(idlePeriod);
		teamStatsLogger.println(record);	
		teamStatsLogger.flush();
		teamStatsLogger.close();
	}
	
	public void logPersonnelInfo(TeamMember member, List<String> log) {
		//sprint,systemTime,modelTime,taskID,storyPoints,start/end,BackEnd Exp,FrontEnd Exp,Design Exp
		try {
			PrintWriter memberLogger = loadPersonnelFile(member.getID());
			for(String record : log) {
				memberLogger.println(record);
				memberLogger.flush();
			}
			memberLogger.close();
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHILE LOGGING SPRINT INFORMATION FOR MEMBER WITH ID: " + member.getID());
			e.printStackTrace();
		}
	}
	
	//this method logs every task that has been taken at the beginning of each sprint
	public void logSprintTaskLog(ConcurrentLinkedQueue<Task> finishedTasks, int sprintNo) {
		String fileName = getSprintTaskLogPrefix() + sprintNo + ".csv";
		try {
			PrintWriter taskLogger = new PrintWriter(fileName);
			String title = "TaskName,TaskID,Priority,StoryPoints,RequiredSkills";
			taskLogger.println("sep=,");
			taskLogger.println(title);
			for(Task task : finishedTasks) {
				String record = task.getTaskName() + "," + task.getTaskID() + "," + task.getPriority() + "," + task.getStoryPoints() 
						+ "," + listAllSkills(task);
				taskLogger.println(record);
			}
			taskLogger.flush();
			taskLogger.close();
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHEN CREATING A LOG FILE FOR RECORDING THE TASKS PERFORMED IN SPRINT NO. " + sprintNo);
			e.printStackTrace();
		}
	}
	
	public void logExtraPersonnelInfo(TeamMember member, List<String> infoLog) {
		String personnelFileName = getPersonnelExtraInfoPrefix() + member.getID() + ".csv";
		PrintWriter logWriter = null;
		try {
			logWriter = new PrintWriter(new BufferedWriter(new FileWriter(personnelFileName, true)));
			for(String record : infoLog) {
				logWriter.println(record);
			}
			logWriter.flush();
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHEN LOGGING EXTRA INFORMATION FOR MEMBER " + member.getFirstName() + " ID: " + member.getID());
			e.printStackTrace();
		}finally {
			logWriter.close();
		}
	}
}
