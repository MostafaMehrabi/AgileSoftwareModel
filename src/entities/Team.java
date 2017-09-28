package entities;

//Very important: mechanism for adding a backlog history must be added, so that we can hold onto the list of project backlogs,
//because we must be able to repeat them for different task allocation strategies.
//priority attribute must be added to tasks, as well as task allocation methods that consider priority (a place holder for this)!

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import core.Main;
import core.SystemRecorder;
import enums.MemberRole;
import enums.SkillArea;
import enums.TaskAllocationStrategy;


public class Team {
	private TaskAllocationStrategy taskAllocationStrategy;
	private int lastMemebrID;
	private List<TeamMember> teamPersonnel;
	private List<Task> backLog;
	private List<Task> allTasksDoneSoFar;
	private double storyPointCoefficient;
	private double progressPerStoryPoint;
	private int lowExpertiseLowerBoundary, lowExpertiseHigherBoundary;
	private int mediumExpertiseLowerBoundary, mediumExpertiseHigherBoundary;
	private int highExpertiseLowerBoundary, highExpertiseHigherBoundary;
	private int lowExpertiseCoefficient, mediumExpertiseCoefficient, highExpertiseCoefficient;
	private int tctToSystemTimeCoefficient;
	private boolean stopAfterEachSprint, teamWorking;
	private static Team team = null;
	private TaskBoard taskBoard;
	private int lastTaskID;

	
	private Team(){
		this.taskAllocationStrategy = TaskAllocationStrategy.ExpertiseBased;
		teamPersonnel = new ArrayList<>();
		backLog = new ArrayList<>();
		allTasksDoneSoFar = new ArrayList<>();
		taskBoard = new TaskBoard();
		//setting default values for team properties, these values can be customized later.
		storyPointCoefficient = 8;
		progressPerStoryPoint = 0.3d;
		lowExpertiseLowerBoundary = 0; lowExpertiseHigherBoundary = 5;
		mediumExpertiseLowerBoundary = 6; mediumExpertiseHigherBoundary = 20;
		highExpertiseLowerBoundary = 21; highExpertiseHigherBoundary = 30;
		lowExpertiseCoefficient = 1; mediumExpertiseCoefficient = 3; highExpertiseCoefficient = 5;
		tctToSystemTimeCoefficient = 25;
		stopAfterEachSprint = false; teamWorking = false;
		lastMemebrID = 0;
		lastTaskID = 0;
	}
	
	public static Team getTeam(){
		if(team == null){
			team = new Team();
		}
		return team;
	}
	
	public int getLastTaskID(){
		return lastTaskID;
	}
	
	public void setLastTaskID(int lastTaskID){
		this.lastTaskID = lastTaskID;
	}
	
	public List<Task> getProjectBackLog(){
		return this.backLog;
	}
	
	public void setProjectBackLog(List<Task> taskList){
		this.backLog = taskList;
	}
	
	public void addToProjectBackLog(List<Task> taskList){
		backLog.addAll(taskList);
	}
	
	public void addToProjectBackLog(Task task){
		backLog.add(task);
	}
	
	public List<Task> getAllTasksDoneSoFar(){
		return this.allTasksDoneSoFar;
	}
	
	public void setAllTasksDoneSoFar(List<Task> taskList){
		this.allTasksDoneSoFar = taskList;
	}
	
	public void addToAllTasksDoneSoFar(List<Task> taskList){
		this.allTasksDoneSoFar.addAll(taskList);
	}
	
	public void addToAllTasksDoneSoFar(Task task){
		this.allTasksDoneSoFar.add(task);
	}
	
	public int getLastMemberID(){
		return this.lastMemebrID;
	}
	
	public void setLastMemberID(int id){
		this.lastMemebrID = id;
	}
	
	public void setPersonnel(List<TeamMember> personnel){
		this.teamPersonnel = personnel;
	}
	
	public List<TeamMember> getPersonnel(){
		return this.teamPersonnel;
	}
	
	public long convertTctToSystemTime(double tct){
		return (long) (tct * tctToSystemTimeCoefficient);
	}
	
	public TaskAllocationStrategy getTaskAllocationStrategy(){
		return this.taskAllocationStrategy;
	}
	
	public void setStoryPointCoefficient(double value){
		this.storyPointCoefficient = value;
	}
	
	public void setTaskAllocationStrategy(TaskAllocationStrategy strategy){
		this.taskAllocationStrategy = strategy;
	}
	
	public double getStoryPointCoefficient(){
		return this.storyPointCoefficient;
	}
	
	public void setLowExpertiseCoefficient(int coef){
		this.lowExpertiseCoefficient = coef;
	}
	
	public void setMediumExpertiseCoefficient(int coef){
		this.mediumExpertiseCoefficient = coef;
	}
	
	public void setHighExpertiseCoefficient(int coef){
		this.highExpertiseCoefficient = coef;
	}
	
	public int getLowExpertiseCoefficient(){
		return this.lowExpertiseCoefficient;
	}
	
	public int getMediumExpertiseCoefficient(){
		return this.mediumExpertiseCoefficient;
	}
	
	public int getHighExpertiseCoefficient(){
		return this.highExpertiseCoefficient;
	}
	
	public void setProgressPerStoryPoint(double value){
		this.progressPerStoryPoint = value;
	}
	
	public double getProgressPerStoryPoint(){
		return this.progressPerStoryPoint;
	}
	
	public int getLowExpertiseLowerBoundary(){
		return lowExpertiseLowerBoundary;
	}
	
	public int getLowExpertiseHigherBoundary(){
		return lowExpertiseHigherBoundary;
	}
	
	public int getMediumExpertiseLowerBoundary(){
		return mediumExpertiseLowerBoundary;
	}
	
	public int getMediumExpertiseHigherBoundary(){
		return mediumExpertiseHigherBoundary;
	}
	
	public int getHighExpertiseLowerBoundary(){
		return highExpertiseLowerBoundary;
	}
	
	public int getHighExpertiseHigherBoundary(){
		return highExpertiseHigherBoundary;
	}
	
	public int getTctToSystemTimeCoefficient(){
		return this.tctToSystemTimeCoefficient;
	}
	
	public void setTctToSystemTimeCoefficient(int coef){
		this.tctToSystemTimeCoefficient = coef;
	}
	
	public void setStopAfterEachSprint(boolean stop){
		this.stopAfterEachSprint = stop;
	}
	
	public boolean getStopAfterEachSprint(){
		return this.stopAfterEachSprint;
	}
	
	public void setTeamWorking(boolean working){
		this.teamWorking = working;
	}
	
	public boolean getTeamWorking(){
		return this.teamWorking;
	}
	
	public void setTaskBoard(TaskBoard taskBoard){
		this.taskBoard = taskBoard;
	}
	
	public TaskBoard getTaskBoard(){
		return this.taskBoard;
	}
	
	public void setLowExpertiseBoundaries(int low, int high){
		if(low > high)
			throw new IllegalArgumentException("The lowe boundary is larger than the high boundary!");
		if(low < 0)
			throw new IllegalArgumentException("Unacceptable lower boundary fow low expertise");
		if(high > mediumExpertiseLowerBoundary)
			throw new IllegalArgumentException("Unacceptable higher boundary fow low expertise");
		
		lowExpertiseLowerBoundary = low; lowExpertiseHigherBoundary = high;
	}
	
	public void setMediumExpertiesBoundaries(int low, int high){
		if(low > high)
			throw new IllegalArgumentException("The lowe boundary is larger than the high boundary!");
		if(low < lowExpertiseHigherBoundary)
			throw new IllegalArgumentException("Unacceptable lower boundary fow medium expertise");
		if(high > highExpertiseLowerBoundary)
			throw new IllegalArgumentException("Unacceptable higher boundary for medium expertise");
		
		mediumExpertiseLowerBoundary = low; mediumExpertiseHigherBoundary = high;
	}
	
	public void setHighExpertiseBoundaries(int low, int high){
		if(low > high)
			throw new IllegalArgumentException("The lowe boundary is larger than the high boundary!");
		if(low < mediumExpertiseHigherBoundary)
			throw new IllegalArgumentException("Unacceptable lower boundary fow high expertise");
		highExpertiseLowerBoundary = low; highExpertiseHigherBoundary = high;
	}
	
	public void createRandomTasks(int numOfRandomTasks) {
		for(int i = 1; i <= numOfRandomTasks; i++) {
			lastTaskID += 1;
			Random rand = new Random();
			int randomNo = rand.nextInt(30) + 1; //returns a random value between 1 and 28
			//for a wider range of variety, and lesser chance of repetition this interval has been considered
			//to be between 1 to 28, for every interval of 4 digits, we have a certain permutation of skills required. 
			//and only if the number is 29 or 30, it will be a testing task 
			Set<SkillArea> requiredSkills = new HashSet<>();
			if(randomNo <= 4) {
				requiredSkills.add(SkillArea.BackEnd);
			}else if(randomNo <= 8) {
				requiredSkills.add(SkillArea.FrontEnd);
			}else if(randomNo <= 12) {
				requiredSkills.add(SkillArea.Design);
			}else if(randomNo <= 16) {
				requiredSkills.add(SkillArea.BackEnd);
				requiredSkills.add(SkillArea.FrontEnd);
			}else if(randomNo <= 20) {
				requiredSkills.add(SkillArea.BackEnd);
				requiredSkills.add(SkillArea.Design);
			}else if(randomNo <= 24) {
				requiredSkills.add(SkillArea.FrontEnd);
				requiredSkills.add(SkillArea.Design);
			}else if(randomNo <= 28) {
				requiredSkills.add(SkillArea.BackEnd);
				requiredSkills.add(SkillArea.FrontEnd);
				requiredSkills.add(SkillArea.Design);
			}else if(randomNo <= 30) {
				requiredSkills.add(SkillArea.Testing);
			}
			
			int taskId = lastTaskID;
			String taskName = "RandomTask_" + i;
			int storyPoints = (rand.nextInt(10) + 1); //a random number between 1 - 10
			Task newTask = new Task(taskId, taskName, storyPoints, requiredSkills);
			addTaskToBackLog(newTask);
		}
	}
	
	public void addNewMember(String firstName, String lastName, MemberRole role, double expInBE, double expInFE, double expInDesign){
		TeamMember newDeveloper = new TeamMember(++lastMemebrID, firstName, lastName, role);
		newDeveloper.setExpertiseAtSkillArea(SkillArea.BackEnd, expInBE);
		newDeveloper.setExpertiseAtSkillArea(SkillArea.FrontEnd, expInFE);
		newDeveloper.setExpertiseAtSkillArea(SkillArea.Design, expInDesign);
		teamPersonnel.add(newDeveloper);
		Main.updatePersonnelTabel(newDeveloper);
	}		
	
	public void addNewTaskToBackLog(String name, String description, int storyPoints, Set<SkillArea> requiredSkillAreas) throws IllegalArgumentException{
		if(storyPoints < 1 || storyPoints > 10){
			throw new IllegalArgumentException("The value provided as story points for the"
					+ " new task, can only be between 0 and 10, inclusive!");
		}
		int taskID = ++lastTaskID;
		Task newTask = new Task(taskID, name, storyPoints, requiredSkillAreas);
		if(!description.isEmpty())
			newTask.setTaskDescription(description);
		addTaskToBackLog(newTask);
	}
	
	private void addTaskToBackLog(Task task){
		backLog.add(task);
		SystemRecorder.recordDefaultBackLog();
		Main.updateBackLogTabel(task);
	}	
	
	public void moveToSprintBackLog(int[] selectedIndecies){
		int arraySize = selectedIndecies.length;
		List<Task> tasks = new ArrayList<>();
		for(int index = (arraySize-1); index >= 0; index--){
			Task task = backLog.remove(selectedIndecies[index]);
			tasks.add(task);
		}
		Collections.reverse(tasks);
		taskBoard.setToDoTasks(tasks);
	}
	
	public List<Task> getToDoTasks(){
		return taskBoard.getToDoTasks();
	}
	
	public List<Task> getTasksInProgress(){
		return taskBoard.getTasksInProgress();
	}
	
	public List<Task> getPerformedTasks(){
		return taskBoard.getPerformedTasks();
	}
}
