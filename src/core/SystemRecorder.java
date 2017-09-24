package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.List;
import java.util.Set;

import entities.Task;
import entities.TaskBoard;
import entities.Team;
import entities.TeamMember;
import enums.SkillArea;

public class SystemRecorder {
	public static void recordSystemStatus(){
		File baseDirectory = new File(Main.getBaseDirectoryPath());
		if(!baseDirectory.exists()){
			if(!baseDirectory.mkdirs()){
				throw new RuntimeException("System was not able to create one or more directory/directories in path: " + Main.getBaseDirectoryPath());
			}
		}	
		recordMain();
		recordTeam();
		recordPersonnel();
		recordTaskBoard();
		recordBackLog();
		recordAllPerformedTasks();
	}
	
	private static void recordMain(){
		String mainFileName = Main.getBaseDirectoryPath() + File.separator + Main.getMainFileName();
		try {
			PrintWriter writer = new PrintWriter(mainFileName);
			writer.println(Main.getBaseDirectoryPath());
			writer.println(Main.getPersonnelFileName());
			writer.println(Main.getTeamFileName());
			writer.println(Main.getTaskBoardFielName());
			writer.println(Main.getToDoTasksForSprintFileName());
			writer.println(Main.getInProgressTasksFileName());
			writer.println(Main.getPerformedTasksForSprintFileName());
			writer.println(Main.getBackLogTasksFileName());
			writer.println(Main.getAllFinishedTasksFileName());
			writer.println(Main.getTaskSyntax());
			writer.println(Main.getDescriptionSyntax());
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void recordTeam(){
		String teamFileName = Main.getBaseDirectoryPath() + File.separator + Main.getTeamFileName();
		try{
			Team team = Team.getTeam();
			PrintWriter writer = new PrintWriter(teamFileName);
			
			String taskAllocationStrategy = team.getTaskAllocationStrategy().toString();
			writer.println(taskAllocationStrategy);
			
			String storyPointsCoefficient = Double.toString(team.getStoryPointCoefficient());
			writer.println(storyPointsCoefficient);
			
			String progressPerStoryPoint = Double.toString(team.getProgressPerStoryPoint());
			writer.println(progressPerStoryPoint);
			
			String lowerExpertiseBoundaries = team.getLowExpertiseLowerBoundary() + " " + team.getLowExpertiseHigherBoundary();
			writer.println(lowerExpertiseBoundaries);
			
			String mediumExpertiseBoundaries = team.getMediumExpertiseLowerBoundary() + " " + team.getMediumExpertiseHigherBoundary();
			writer.println(mediumExpertiseBoundaries);
			
			String highExpertiseBoundaries = team.getHighExpertiseLowerBoundary() + " " + team.getHighExpertiseHigherBoundary();
			writer.println(highExpertiseBoundaries);
			
			String expertiseCoefs = team.getLowExpertiseCoefficient() + " " + team.getMediumExpertiseCoefficient() + " " + team.getHighExpertiseCoefficient();
			writer.println(expertiseCoefs);
			
			String tctToSystemTimeCoef = Integer.toString(team.getTctToSystemTimeCoefficient());
			writer.println(tctToSystemTimeCoef);
			
			String stopAfterEachSprint = Boolean.toString(team.getStopAfterEachSprint());
			writer.println(stopAfterEachSprint);
			
			String teamWorking = Boolean.toString(team.getTeamWorking());
			writer.println(teamWorking);
			
			String lastMemberID = Integer.toString(team.getLastMemberID());
			writer.println(lastMemberID);
			
			String lastTaskID = Integer.toString(team.getLastTaskID());
			writer.println(lastTaskID);
			
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void recordPersonnel(){
		//each person is recorded as:
		//id firstName lastName role completedStoryPoints expertiseInBE expertiseInFE expertiseInDesign
		String personnelFileName = Main.getBaseDirectoryPath() + File.separator + Main.getPersonnelFileName();
		try{
			PrintWriter writer = new PrintWriter(personnelFileName);
			List<TeamMember> personnel = Team.getTeam().getPersonnel();
			for(TeamMember member : personnel){
				String memberString = "";
				
				String id = Integer.toString(member.getID());
				memberString += id + " ";
				
				String firstName = member.getFirstName();
				memberString  += firstName + " ";
				
				String lastName = member.getLastName();
				memberString += lastName + " ";
				
				String role = member.getMemberRole().toString();
				memberString += role + " ";
				
				String completedStoryPoint = Integer.toString(member.getCompletedStoryPoints());
				memberString += completedStoryPoint + " ";
				
				String expertiseInBackEnd = Double.toString(member.getExpertiseAtSkillArea(SkillArea.BackEnd));
				memberString += expertiseInBackEnd + " ";
				
				String expertiseInFrontEnd = Double.toString(member.getExpertiseAtSkillArea(SkillArea.FrontEnd));
				memberString += expertiseInFrontEnd + " ";
				
				String expertiseInDesign = Double.toString(member.getExpertiseAtSkillArea(SkillArea.Design));
				memberString += expertiseInDesign;
				
				writer.println(memberString);
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void recordTaskBoard(){
		String taskBoardFileName = Main.getBaseDirectoryPath() + File.separator + Main.getTaskBoardFielName();
		TaskBoard taskBoard = Team.getTeam().getTaskBoard();
		try{
			PrintWriter writer = new PrintWriter(taskBoardFileName);
			//place holder for future records for taskBoard, currently taskBoard does not have anything to record. 
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		recordToDoTasks(taskBoard.getToDoTasks());
		recordInProgressTasks(taskBoard.getTasksInProgress());
		recordPeformedTasks(taskBoard.getPerformedTasks());
	}
	
	private static void recordToDoTasks(List<Task> tasks){
		String toDoTasksFileName = Main.getBaseDirectoryPath() + File.separator + Main.getToDoTasksForSprintFileName();
		writeTasksToFile(tasks, toDoTasksFileName);
	}
	
	private static void recordInProgressTasks(List<Task> tasks){
		String inProgressTasksFileName = Main.getBaseDirectoryPath() + File.separator + Main.getInProgressTasksFileName();
		writeTasksToFile(tasks, inProgressTasksFileName);
	}
	
	private static void recordPeformedTasks(List<Task> tasks){
		String performedTasksFileName = Main.getBaseDirectoryPath() + File.separator + Main.getPerformedTasksForSprintFileName();
		writeTasksToFile(tasks, performedTasksFileName);
	}
	
	private static void  recordBackLog(){
		String backLogFileName = Main.getBaseDirectoryPath() + File.separator + Main.getBackLogTasksFileName();
		writeTasksToFile(Team.getTeam().getProjectBackLog(), backLogFileName);
	}
	
	private static void recordAllPerformedTasks(){
		String allTasksFileName = Main.getBaseDirectoryPath() + File.separator + Main.getAllFinishedTasksFileName();
		writeTasksToFile(Team.getTeam().getAllTasksDoneSoFar(), allTasksFileName);
	}
	
	private static void writeTasksToFile(List<Task> tasks, String fileName){
		//every task is saved as:
		//TASK: taskName id storyPoint performerID [skillAreas...]
		//DESCRIPTION: [optional description...]
		try{
			PrintWriter writer = new PrintWriter(fileName);
			for(Task task : tasks){
				String taskString = Main.getTaskSyntax();
				
				String taskName = task.getTaskName();
				taskString += taskName + " ";
				
				String taskID = Integer.toString(task.getTaskID());
				taskString += taskID + " "; 
				
				String storyPoints = Integer.toString(task.getStoryPoints());
				taskString += storyPoints + " ";
				
				String performerID = Integer.toString(task.getPerformerID());
				taskString += performerID + " ";
				
				String requiredSkills = "";
				Set<SkillArea> skillAreas = task.getRequiredSkillAreas();
				int skillAreasSize = skillAreas.size();
				int counter = 1;
				for(SkillArea skill : skillAreas){
					requiredSkills += skill.toString();
					if(counter != skillAreasSize)
						requiredSkills += " ";
				}
				taskString += requiredSkills;
				writer.println(taskString);
				
				String taskDescription = task.getTaskDescription();
				if(!taskDescription.isEmpty()){
					String taskDescripion = Main.getDescriptionSyntax() + taskDescription;
					writer.println(taskDescripion);
				}				
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
