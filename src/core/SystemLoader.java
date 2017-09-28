package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.Task;

import entities.TaskBoard;
import entities.Team;
import entities.TeamMember;
import enums.MemberRole;
import enums.SkillArea;
import enums.TaskAllocationStrategy;
import view.ProgressBar;

public class SystemLoader {
	private static ProgressBar progressBar;
	
	public static Team loadSystem(ProgressBar pb){	
		progressBar = pb;
		
		File baseDirectory = new File(Main.getBaseDirectoryPath());
		if(!baseDirectory.exists()){
			if(!baseDirectory.mkdirs()){
				throw new RuntimeException("System was not able to create one or more directory/directories in path: " + Main.getBaseDirectoryPath());
			}
		}
		
		Team team = loadTeam();
		progressBar.setValue(24);
		
		List<TeamMember> teamPersonnel = loadPersonnel();
		if(!teamPersonnel.isEmpty())
			team.setPersonnel(teamPersonnel);
		progressBar.setValue(43);
		
		TaskBoard taskBoard = loadTaskBoard();
		if(taskBoard != null)
			team.setTaskBoard(taskBoard);
		progressBar.setValue(62);
		
		List<Task> backLog = loadBackLog();
		if(!backLog.isEmpty())
			team.setProjectBackLog(backLog);
		progressBar.setValue(81);
		
		List<Task> allTasksDoneSoFar = loadAllTasksDoneSoFar();
		if(!allTasksDoneSoFar.isEmpty())
			team.setAllTasksDoneSoFar(allTasksDoneSoFar);
		progressBar.setValue(100);
		
		return team;
	}
	
	public static Team loadSystem(String baseDirPath, ProgressBar pb){
		Main.setBaseDirectoryPath(baseDirPath);
		return loadSystem(pb);
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
							team.setHoursToSystemTimeCoefficient(Integer.parseInt(line));
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
						case 11://the twelfth line tells the last task ID 
							team.setLastTaskID(Integer.parseInt(line));
							break;
						case 12://the thirteenth line tells the number of hours in each sprint
							team.setHoursPerSplit(Integer.parseInt(line));
							break;
						case 13://the fourteenth line tells the number of story pionts that are initially moved for the first sprint, if randomly moved by the system
							team.setInitialStoryPoints(Integer.parseInt(line));
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
	
	private static TaskBoard loadTaskBoard(){
		TaskBoard taskBoard = null;
		String TaskBoardFileName = Main.getBaseDirectoryPath() + File.separator + Main.getTaskBoardFielName();
		File taskBoardFile = new File(TaskBoardFileName);
		if(taskBoardFile.exists()){
			try{
				taskBoard = new TaskBoard();
				BufferedReader reader = new BufferedReader(new FileReader(taskBoardFile));
				String line = reader.readLine();
				taskBoard.setSprintNo(Integer.parseInt(line));
				
				List<Task> toDoTasks = loadToDoTasks();
				if(!toDoTasks.isEmpty())
					taskBoard.setToDoTasks(toDoTasks);
				
				List<Task> inProgressTasks = loadTasksInProgress();
				if(!inProgressTasks.isEmpty())
					taskBoard.setInProgressTasks(inProgressTasks);
				
				List<Task> performedTasks = loadPerformedTasks();

				if(!performedTasks.isEmpty())
					taskBoard.setPerformedTasks(performedTasks);
				
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return taskBoard;
	}
	

	private static List<Task> loadToDoTasks(){
		String toDoTasksFileName = Main.getBaseDirectoryPath() + File.separator + Main.getToDoTasksForSprintFileName();
		return loadTasksFromFile(toDoTasksFileName);
	}
	
	private static List<Task> loadTasksInProgress(){
		String inProgressTasksFileName = Main.getBaseDirectoryPath() + File.separator + Main.getInProgressTasksFileName();
		return loadTasksFromFile(inProgressTasksFileName);
	}
	
	private static List<Task> loadPerformedTasks(){
		String performedTasksFileName = Main.getBaseDirectoryPath() + File.separator + Main.getPerformedTasksForSprintFileName();
		return loadTasksFromFile(performedTasksFileName);
	}
	
	private static List<Task> loadBackLog(){
		String backLogFileName = Main.getBaseDirectoryPath() + File.separator + Main.getBackLogTasksFileName();
		return loadTasksFromFile(backLogFileName);
	}
	
	private static List<Task> loadAllTasksDoneSoFar(){
		String allFinishedTasksFileName = Main.getBaseDirectoryPath() + File.separator + Main.getAllFinishedTasksFileName();
		return loadTasksFromFile(allFinishedTasksFileName);
	}
	
	private static List<Task> loadTasksFromFile(String fileName){
		//Tasks are saved in each file using the following format
		//TASKS: taskName id storyPoints performerID [skillAreas...]
		//DESCRIPTION: [optional description about the task]
		List<Task> tasks = new ArrayList<>();
		File taskFile = new File(fileName);
		if(taskFile.exists()){
			try{
				BufferedReader reader = new BufferedReader(new FileReader(taskFile));
				String line = reader.readLine();
				while(line != null){
					if(line.startsWith(Main.getTaskSyntax())){
						String[] elements = line.split(" ");
						//first element is TASK:
						//second element is taskName
						String taskName = elements[1];
						//third element is taskID
						int taskID = Integer.parseInt(elements[2]);
						//fourth element is storyPionts
						int storyPoints = Integer.parseInt(elements[3]);
						//fifth element is performerID 
						int performerID = Integer.parseInt(elements[4]);
						Set<SkillArea> requiredSkillAreas = new HashSet<>();
						if(elements.length > 5){
							for(int index = 5; index < elements.length; index++){
								requiredSkillAreas.add(resolveSkillArea(elements[index]));
							}
						}
						
						Task task = new Task(taskID, taskName, storyPoints, requiredSkillAreas);
						task.setPerformerID(performerID);

						String taskDescription = "";
						line = reader.readLine();
						if(line != null){
							if(line.startsWith(Main.getDescriptionSyntax())){
								taskDescription = line.substring(Main.getDescriptionSyntax().length());
								line = reader.readLine();
							}
						}						
						task.setTaskDescription(taskDescription);
						tasks.add(task);
					}
					if(line!=null && line.isEmpty()){
						line = reader.readLine();
					}
				}
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return tasks;
	}	
	
	private static TaskAllocationStrategy resolveTaskAllocationStrategy(String line){
		if(line.equals(TaskAllocationStrategy.ExpertiseBased.toString()))
			return TaskAllocationStrategy.ExpertiseBased;
		else if(line.equals(TaskAllocationStrategy.LearningBased.toString()))
			return TaskAllocationStrategy.LearningBased;
		else
			throw new RuntimeException("THE TASK ALLOCATION STRATEGY THAT IS LOADED BY THE SYSTEM IS NOT VALID");
	}
	
	private static MemberRole resolveRole(String role){
		if(role.equals(MemberRole.Developer.toString()))
			return MemberRole.Developer;
		else if(role.equals(MemberRole.Tester.toString()))
			return MemberRole.Tester;
		else
			throw new RuntimeException("THE MEMBER ROLE THAT IS LOADED BY THE SYSTEM IS NOT VALID");				
	}
	
	private static SkillArea resolveSkillArea(String skill){
		String frontEnd = SkillArea.FrontEnd.toString();
		String backEnd = SkillArea.BackEnd.toString();
		String design = SkillArea.Design.toString();
		String testing = SkillArea.Testing.toString();
		if(skill.equals(frontEnd))
			return SkillArea.FrontEnd;
		else if(skill.equals(backEnd))
			return SkillArea.BackEnd;
		else if (skill.equals(design))
			return SkillArea.Design;
		else if (skill.equals(testing))
			return SkillArea.Testing;
		else
			throw new RuntimeException("THE SKILL AREA THAT IS LOADED BY THE SYSTEM IS NOT VALID");
	}
}
