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

public class Statistics {
	
	private static Statistics stats = null;
	private String teamStatsFileName = null;
	private String personnelFilePreFix = null;
	
	private Statistics() {
		String statsFolderName = Main.getStatisticsDirectoryPath();
		File statsFolder = new File(statsFolderName);
		if(!statsFolder.exists()) {
			statsFolder.mkdirs();
		}
		
		teamStatsFileName = Main.getStatisticsDirectoryPath() + File.separator + "TeamStats.csv";
		personnelFilePreFix = Main.getStatisticsDirectoryPath() + File.separator + "Personnel_";
		
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
	
	private void createTeamStatsFile(){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(teamStatsFileName);
			String title = "sprint,velocity,storyPoints,duration";
			writer.println(title);
			writer.flush();
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHEN CREATING LOG FILE FOR TEAM");
			e.printStackTrace();
		}
		writer.flush();
		writer.close();
	}
	
	private void createPersonnelStatsFiles() {
		List<TeamMember> personnel = Team.getTeam().getPersonnel();
		try {
			for(TeamMember member : personnel) {
				String fileName = personnelFilePreFix + member.getID() + ".csv";
				PrintWriter  writer = new PrintWriter(fileName);
				String title = "sprint,time,taskID,storyPoints,start/end,BackEnd Exp,FrontEnd Exp,Design Exp";
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
			String fileName = personnelFilePreFix + id + ".csv";
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
			return writer;
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHILE TRYING TO LOAD THE FILE FOR TEAM MEMBER WITH ID: " + id);
			e.printStackTrace();
		}
		return null;
	}
	
	private PrintWriter loadTeamStatsFile() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(teamStatsFileName, true)));
			return writer;
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHEN OPENING THE LOG FILE FOR TEAM");
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
		String fileName = Main.getStatisticsDirectoryPath() + File.separator + "tasksPeformedForSprint_" + sprintNo + ".csv";
		int totalStoryPoints = 0;
		try {
			PrintWriter taskLogger = new PrintWriter(fileName);
			String title = "TaskName,TaskID,Priority,StoryPoints,PerformerID,RequiredSkills";
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
		//sprint,velocity,storyPoints,duration
		int accomplishedStoryPoints = logPerformedTasksForSprint(finishedTasks, sprintNo);
		PrintWriter teamStatsLogger = loadTeamStatsFile();
		String record = Integer.toString(sprintNo) + "," + Double.toString(sprintVelocity) + "," + Integer.toString(accomplishedStoryPoints) + "," + Long.toString(duration);
		teamStatsLogger.println(record);	
		teamStatsLogger.flush();
		teamStatsLogger.close();
	}
	
	public void logPersonnelInfo(TeamMember member, int sprintNo, Task performedTask, boolean started, long time) {
		//sprint,time,taskID,storyPoints,start/end,BackEnd Exp,FrontEnd Exp,Design Exp
		try {
			PrintWriter memberLogger = loadPersonnelFile(member.getID());
			String startEnd = (started) ? "start" : "end";
			String record = sprintNo + "," + time + "," + performedTask.getTaskID() + "," + performedTask.getStoryPoints() + "," + startEnd + ","
					+ member.getExpertiseAtSkillArea(SkillArea.BackEnd) + "," + member.getExpertiseAtSkillArea(SkillArea.FrontEnd) + "," + member.getExpertiseAtSkillArea(SkillArea.Design);
			memberLogger.println(record);
			memberLogger.flush();
			memberLogger.close();
		}catch(Exception e) {
			Main.issueErrorMessage("ERROR INCURED WHILE LOGGING SPRINT INFORMATION FOR MEMBER WITH ID: " + member.getID());
			e.printStackTrace();
		}
	}
}
