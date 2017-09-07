package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Team {
	private TaskAllocationStrategy taskAllocationStrategy;
	private List<Developer> teamDevelopers;
	private TaskBoard taskBoard;
	private double storyPointCoefficient;
	private int lowExpertiseLowerBoundary, lowExpertiseHigherBoundary;
	private int mediumExpertiseLowerBoundary, mediumExpertiseHigherBoundary;
	private int highExpertiseLowerBoundary, highExpertiseHigherBoundary;
	private static Team team = null;	
	
	private Team(){
		this.taskAllocationStrategy = TaskAllocationStrategy.ExpertiseBased;
		teamDevelopers = new ArrayList<>();
		taskBoard = new TaskBoard();
		//default value for story point coefficient is 0.3
		storyPointCoefficient = 0.3d;
		lowExpertiseLowerBoundary = 0; lowExpertiseHigherBoundary = 5;
		mediumExpertiseLowerBoundary = 6; mediumExpertiseHigherBoundary = 20;
		highExpertiseLowerBoundary = 21; highExpertiseHigherBoundary = 30;
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
	
	public void addNewTask(Set<SkillAreas> requiredSkillAreas, int storyPoints) throws IllegalArgumentException{
		if(storyPoints < 1 || storyPoints > 10){
			throw new IllegalArgumentException("The value provided as story points for the"
					+ " new task, can only be between 0 and 10, inclusive!");
		}
		Task newTask = new Task(storyPoints, requiredSkillAreas);
		taskBoard.addTask(newTask);
	}
	
	public TaskAllocationStrategy getTaskAllocationStrategy(){
		return this.taskAllocationStrategy;
	}
	
	public void setStoryPointCoefficient(double value){
		this.storyPointCoefficient = value;
	}
	
	public double getStoryPointCoefficient(){
		return this.storyPointCoefficient;
	}
	
	public int getLowExpertiseLowerBoundary(){
		return lowExpertiseLowerBoundary;
	}
	
	public int getLowExpertiseHigherBoundary(){
		return lowExpertiseHigherBoundary;
	}
	
	//public 
	
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
