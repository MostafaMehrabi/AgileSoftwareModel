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
	private String expertiseBasedFolder = null;
	private String learningBasedFolder = null;
	private int hoursPerSprint = 0;
	private int systemToModelTimeCoef = 0;
	
	private Statistics() {
		expertiseBasedFolder = "ExpertiseBased";
		learningBasedFolder = "LearningBased";
		hoursPerSprint = Team.getTeam().getHoursPerSpring();
		systemToModelTimeCoef = Team.getTeam().getSystemToModelTimeCoefficient();
		String statsFolderName = Main.getStatisticsDirectoryPath();
		String expertiseFolderName = statsFolderName + File.separator + expertiseBasedFolder;
		String learningFolderName = statsFolderName + File.separator + learningBasedFolder;
		
		File expertiseFolder = new File(expertiseFolderName);
		File learningFolder = new File(learningFolderName);
		if(!expertiseFolder.exists()){
			expertiseFolder.mkdirs();
		}
		if(!learningFolder.exists()){
			learningFolder.mkdirs();
		}
			
		int currentSprint = Team.getTeam().getCurrentSprint();
		if(currentSprint == 1) {
			createTeamStatsFile();
			createPersonnelStatsFiles();
		}
	}
	
	public static Statistics getStatRecorder() {
		if(stats == null) {
			stats = new Statistics();
		}
		return stats;
	}
	
	private String getTeamStatsFileName(){
		Team team = Team.getTeam();
		TaskAllocationStrategy strategy = team.getTaskAllocationStrategy();
		if(strategy.equals(TaskAllocationStrategy.ExpertiseBased)){
			return Main.getStatisticsDirectoryPath() + File.separator + expertiseBasedFolder + File.separator + "TeamStats.csv";
		}else if (strategy.equals(TaskAllocationStrategy.LearningBased)){
			return Main.getStatisticsDirectoryPath() + File.separator + learningBasedFolder + File.separator + "TeamStats.csv";
		}			
		return null;
	}
	
	private String getPersonnelFilePrefix(){
		Team team = Team.getTeam();
		TaskAllocationStrategy strategy = team.getTaskAllocationStrategy();
		if(strategy.equals(TaskAllocationStrategy.ExpertiseBased)){
			return Main.getStatisticsDirectoryPath() + File.separator + expertiseBasedFolder + File.separator + "Personnel_";
		}else if (strategy.equals(TaskAllocationStrategy.LearningBased)){
			return Main.getStatisticsDirectoryPath() + File.separator + learningBasedFolder + File.separator + "Personnel_";
		}			
		return null;
	}
	
	private String getPerformedTasksFilePrefix(){
		Team team = Team.getTeam();
		TaskAllocationStrategy strategy = team.getTaskAllocationStrategy();
		if(strategy.equals(TaskAllocationStrategy.ExpertiseBased)){
			return 	Main.getStatisticsDirectoryPath() + File.separator + expertiseBasedFolder + File.separator + "tasksPeformedForSprint_";
		}else if(strategy.equals(TaskAllocationStrategy.LearningBased)){
			return 	Main.getStatisticsDirectoryPath() + File.separator + learningBasedFolder + File.separator + "tasksPeformedForSprint_";
		}
		return null;
	}
	
	private void createTeamStatsFile(){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(getTeamStatsFileName());
			String title = "sprint,velocity,storyPoints,duration,idlePeriod";
			writer.println("sep=,");
			writer.println(title);
			writer.flush();
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
	
	public void logSprintInfo(ConcurrentLinkedQueue<Task> finishedTasks, double sprintVelocity, int sprintNo, long duration) {
		//sprint,velocity,storyPoints,duration,idlePeriod
		int accomplishedStoryPoints = logPerformedTasksForSprint(finishedTasks, sprintNo);
		int idlePeriod = hoursPerSprint - ((int) duration);
		if(idlePeriod < 0)
			idlePeriod = 0;
		PrintWriter teamStatsLogger = loadTeamStatsFile();
		String record = Integer.toString(sprintNo) + "," + Double.toString(sprintVelocity) + "," 
				+ Integer.toString(accomplishedStoryPoints) + "," + Long.toString(duration) + "," + Integer.toString(idlePeriod);
		teamStatsLogger.println(record);	
		teamStatsLogger.flush();
		teamStatsLogger.close();
	}
	
	public void logPersonnelInfo(TeamMember member, int sprintNo, int taskID, int storyPoints, boolean started, long systemTime) {
		//sprint,systemTime,modelTime,taskID,storyPoints,start/end,BackEnd Exp,FrontEnd Exp,Design Exp
		try {
			PrintWriter memberLogger = loadPersonnelFile(member.getID());
			String startEnd = (started) ? "start" : "end";
			long mt = systemTime / systemToModelTimeCoef;
			int modelTime = (int) mt + ((sprintNo-1)*hoursPerSprint);
			String record = sprintNo + "," + systemTime + "," + modelTime + "," + taskID + "," + storyPoints + "," + startEnd + ","	+ member.getExpertiseAtSkillArea(SkillArea.BackEnd) 
					+ "," + member.getExpertiseAtSkillArea(SkillArea.FrontEnd) + "," + member.getExpertiseAtSkillArea(SkillArea.Design);
			memberLogger.println(record);
			memberLogger.flush();
			memberLogger.close();
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHILE LOGGING SPRINT INFORMATION FOR MEMBER WITH ID: " + member.getID());
			e.printStackTrace();
		}
	}
}
