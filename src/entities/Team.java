package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import core.DeveloperRole;
import core.TaskAllocationStrategy;

public class Team {
	private TaskAllocationStrategy taskAllocationStrategy;
	private List<Developer> teamDevelopers;
	private TaskBoard taskBoard;
	private int storyPointCoefficient;
	private double progressPerStoryPoint;
	private double lowExpertiseLowerBoundary, lowExpertiseHigherBoundary;
	private double mediumExpertiseLowerBoundary, mediumExpertiseHigherBoundary;
	private double highExpertiseLowerBoundary, highExpertiseHigherBoundary;
	private int lowExpertiseCoefficient, mediumExpertiseCoefficient, highExpertiseCoefficient;
	private int tctToSystemTimeCoefficient;
	private boolean stopAfterEachSprint, teamWorking;
	private static Team team = null;	
	private int lastTaskID;
	
	
	private Team(){
		this.taskAllocationStrategy = TaskAllocationStrategy.ExpertiseBased;
		teamDevelopers = new ArrayList<>();
		taskBoard = new TaskBoard();
		//setting default values for team properties, these values can be customized later.
		storyPointCoefficient = 8;
		progressPerStoryPoint = 0.3d;
		lowExpertiseLowerBoundary = 0; lowExpertiseHigherBoundary = 5;
		mediumExpertiseLowerBoundary = 6; mediumExpertiseHigherBoundary = 20;
		highExpertiseLowerBoundary = 21; highExpertiseHigherBoundary = 30;
		lowExpertiseCoefficient = 1; mediumExpertiseCoefficient = 3; highExpertiseCoefficient = 5;
		tctToSystemTimeCoefficient = 10;
		stopAfterEachSprint = false; teamWorking = false;
		lastTaskID = 0;
	}
	
	public static Team getTeam(){
		if(team == null){
			team = new Team();
		}
		return team;
	}
	
	public void addNewDeveloper(long id, String firstName, String lastName, DeveloperRole role){
		Developer newDeveloper = new Developer(id, firstName, lastName, role);
		teamDevelopers.add(newDeveloper);
	}
	
	public void addNewTask(Set<SkillArea> requiredSkillAreas, int storyPoints) throws IllegalArgumentException{
		if(storyPoints < 1 || storyPoints > 10){
			throw new IllegalArgumentException("The value provided as story points for the"
					+ " new task, can only be between 0 and 10, inclusive!");
		}
		int taskID = ++lastTaskID;
		Task newTask = new Task(taskID, storyPoints, requiredSkillAreas);
		taskBoard.addTask(newTask);
	}
	
	public long convertTctToSystemTime(double tct){
		return (long) (tct * tctToSystemTimeCoefficient);
	}
	
	public TaskAllocationStrategy getTaskAllocationStrategy(){
		return this.taskAllocationStrategy;
	}
	
	public void setStoryPointCoefficient(int value){
		this.storyPointCoefficient = value;
	}
	
	public int getStoryPointCoefficient(){
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
	
	public int getHighExpertiseCofficient(){
		return this.highExpertiseCoefficient;
	}
	
	public void setProgressPerStoryPoint(double value){
		this.progressPerStoryPoint = value;
	}
	
	public double getProgressPerStoryPoint(){
		return this.progressPerStoryPoint;
	}
	
	public double getLowExpertiseLowerBoundary(){
		return lowExpertiseLowerBoundary;
	}
	
	public double getLowExpertiseHigherBoundary(){
		return lowExpertiseHigherBoundary;
	}
	
	public double getMediumExpertiseLowerBoundary(){
		return mediumExpertiseLowerBoundary;
	}
	
	public double getMediumExpertiseHigherBoundary(){
		return mediumExpertiseHigherBoundary;
	}
	
	public double getHighExpertiseLowerBoundary(){
		return highExpertiseLowerBoundary;
	}
	
	public double getHighExpertiseHigherBoundary(){
		return highExpertiseHigherBoundary;
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
	
	public void setLowExpertiseBoundaries(int low, int high){
		if(low > high)
			throw new IllegalArgumentException("The lowe boundary is larger than the high boundary!");
		if(low < 0)
			throw new IllegalArgumentException("The lower boundary is less than 0");
		if(low > mediumExpertiseLowerBoundary)
			throw new IllegalArgumentException("Unacceptable lower boundary");
		if(high > mediumExpertiseLowerBoundary)
			throw new IllegalArgumentException("Unacceptable higher boundary");
		
		lowExpertiseLowerBoundary = low; lowExpertiseHigherBoundary = high;
	}
}
