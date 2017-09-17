package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import entities.Task;

import entities.TaskBoard;
import entities.Team;
import entities.TeamMember;
import enums.MemberRole;
import enums.SkillArea;
import enums.TaskAllocationStrategy;

public class SystemLoader {
	

	
	public static Team loadSystem(){
		File baseDirectory = new File(Main.getBaseDirectoryPath());
		if(!baseDirectory.exists()){
			if(!baseDirectory.mkdirs()){
				throw new RuntimeException("System was not able to create one or more directory/directories in path: " + Main.getBaseDirectoryPath());
			}
		}
		Team team = loadTeam();
		
		List<TeamMember> teamPersonnel = loadPersonnel();
		if(!teamPersonnel.isEmpty())
			team.setPersonnel(teamPersonnel);
		
		TaskBoard taskBoard = loadTaskBoard();
		if(taskBoard != null)
			team.setTaskBoard(taskBoard);
		
		List<Task> backLog = loadBackLog();
		if(!backLog.isEmpty())
			team.setProjectBackLog(backLog);
		
		List<Task> allTasksDoneSoFar = loadAllTasksDoneSoFar();
		if(!allTasksDoneSoFar.isEmpty())
			team.setAllTasksDoneSoFar(allTasksDoneSoFar);
		
		return team;
	}
	
	public static Team loadSystem(String baseDirPath){
		Main.setBaseDirectoryPath(baseDirPath);
		return loadSystem();
	}
	
	private static Team loadTeam(){
		Team team = Team.getTeam();
		String teamFileName = Main.getBaseDirectoryPath() + File.separator + Main.getTeamFileName();
		File teamFile = new File(teamFileName);
		if(teamFile.exists()){
			try{
				BufferedReader fileReader = new BufferedReader(new FileReader(teamFile));
				String line = fileReader.readLine();
				int lineCounter = 0;
				while(line != null){
					switch(lineCounter){
						case 0: //the first line tells the task allocation strategy
							team.setTaskAllocationStrategy(resolveTaskAllocationStrategy(line));
							break;
						case 1://the second line tells the coefficient for each story point (for calculating tct)
							team.setStoryPointCoefficient(Double.parseDouble(line));
							break;
						case 2://the third line tells the progress point allocated to each story point
							team.setProgressPerStoryPoint(Double.parseDouble(line));
							break;
						case 3://the fourth line tells the lower and higher boundaries of low expertise
							String[] lowBoundaries = line.split(" ");
							team.setLowExpertiseBoundaries(Integer.parseInt(lowBoundaries[0]), Integer.parseInt(lowBoundaries[1]));
							break;
						case 4://the fifth line tells the lower and higher boundaries of medium expertise
							String[] medBoundaries = line.split(" ");
							team.setMediumExpertiesBoundaries(Integer.parseInt(medBoundaries[0]), Integer.parseInt(medBoundaries[1]));
							break;
						case 5://the sixth line tells the lower and higher boundaries of high expertise
							String[] highBoundaries = line.split(" ");
							team.setHighExpertiseBoundaries(Integer.parseInt(highBoundaries[0]), Integer.parseInt(highBoundaries[1]));
							break;
						case 6://the seventh line tells the coefficients for low, medium and high expertise
							String[] expertiseCoefficients = line.split(" ");
							team.setLowExpertiseCoefficient(Integer.parseInt(expertiseCoefficients[0]));
							team.setMediumExpertiseCoefficient(Integer.parseInt(expertiseCoefficients[1]));
							team.setHighExpertiseCoefficient(Integer.parseInt(expertiseCoefficients[2]));
							break;
						case 7://the eighth line tells the tct to system time coefficient
							team.setTctToSystemTimeCoefficient(Integer.parseInt(line));
							break;
						case 8://the ninth line tells whether the system should stop after each sprint
							team.setStopAfterEachSprint(Boolean.parseBoolean(line));
							break;
						case 9://the tenth line tells whether team is working, theoretically should be false. Because system is always 
							   //saved and loaded when the team is not working. 
							team.setTeamWorking(Boolean.parseBoolean(line));
							break;
						case 10://the eleventh line tells the latest id that is used for a team member
							team.setLastMemberID(Integer.parseInt(line));
							break;
					}
					line = fileReader.readLine();
					lineCounter++;
				}
				fileReader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return team;
	}
	
	private static TaskAllocationStrategy resolveTaskAllocationStrategy(String line){
		if(line.equals(TaskAllocationStrategy.ExpertiseBased.toString()))
			return TaskAllocationStrategy.ExpertiseBased;
		else if(line.equals(TaskAllocationStrategy.LearningBased.toString()))
			return TaskAllocationStrategy.LearningBased;
		else
			throw new RuntimeException("THE TASK ALLOCATION STRATEGY THAT IS LOADED BY THE SYSTEM IS NOT VALID");
	}
	
	private static List<TeamMember> loadPersonnel(){
		List<TeamMember> personnel = new ArrayList<>();
		String personnelFileName = Main.getBaseDirectoryPath() + File.separator + Main.getPersonnelFileName();
		File personnelFile = new File(personnelFileName);
		if(personnelFile.exists()){
			try{
				BufferedReader fileReader = new BufferedReader(new FileReader(personnelFile));
				String line = fileReader.readLine();
				while(line != null){
					//each member is saved in the personnel file with the following format
					//id firstName lastName role completedStoryPoints expertiseInBE expertiseInFE expertiseInDesign
					String[] elements = line.split(" ");
					int id = Integer.parseInt(elements[0]);
					String firstName = elements[1]; 
					String lastName = elements[2];
					MemberRole role = resolveRole(elements[3]);
					int completedStoryPoints = Integer.parseInt(elements[4]);
					double expertiseInBackEnd = Double.parseDouble(elements[5]);
					double expertiseInFrontEnd = Double.parseDouble(elements[6]);
					double expertiseInDesign = Double.parseDouble(elements[7]);
					TeamMember member = new TeamMember(id, firstName, lastName, role);
					member.setCompletedStoryPoints(completedStoryPoints);
					member.setExpertiseAtSkillArea(SkillArea.BackEnd, expertiseInBackEnd);
					member.setExpertiseAtSkillArea(SkillArea.FrontEnd, expertiseInFrontEnd);
					member.setExpertiseAtSkillArea(SkillArea.Design, expertiseInDesign);
					personnel.add(member);
					line = fileReader.readLine();
				}
				fileReader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return personnel;
	}
	
	private static MemberRole resolveRole(String role){
		if(role.equals(MemberRole.Developer.toString()))
			return MemberRole.Developer;
		else if(role.equals(MemberRole.Tester.toString()))
			return MemberRole.Tester;
		else
			throw new RuntimeException("THE MEMBER ROLE THAT IS LOADED BY THE SYSTEM IS NOT VALID");				
	}
	
	private static TaskBoard loadTaskBoard(){
		TaskBoard taskBoard = null;
		String TaskBoardFileName = Main.getBaseDirectoryPath() + File.separator + Main.getTaskBoardFielName();
		File taskBoardFile = new File(TaskBoardFileName);
		if(taskBoardFile.exists()){
			try{
				taskBoard = new TaskBoard();
				BufferedReader reader = new BufferedReader(new FileReader(taskBoardFile));
				int lastTaskID = Integer.parseInt(reader.readLine());
				taskBoard.setLastTaskID(lastTaskID);
				List<Task> toDoTasks = loadToDoTask();
				List<Task> tasksInProgress = loadTasksInProgress();
				List<Task> performedTasks = loadPerformedTasks();
				
				if(!toDoTasks.isEmpty())
					taskBoard.setToDoTasks(toDoTasks);
				
				if(!tasksInProgress.isEmpty())
					taskBoard.setTasksInProgress(tasksInProgress);
				
				if(!performedTasks.isEmpty())
					taskBoard.setPerformedTasks(performedTasks);
				
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return taskBoard;
	}
	
	private static List<Task> loadToDoTask(){
		//tasks should be conventionally recorded in files following the format:
		//TASK: id name storyPoints performerID [required skills...]
		//DESCRIPTION: if any description provided, will be held in this optional record. 
		return null;
	}
	
	private static List<Task> loadTasksInProgress(){
		//theoretically should always be empty, because recording the system
		//should only take place after or before a sprint starts. 
		return null;
	}
	
	private static List<Task> loadPerformedTasks(){
		return null;
	}
	
	private static List<Task> loadBackLog(){
		return null;
	}
	
	private static List<Task> loadAllTasksDoneSoFar(){
		return null;
	}
}
