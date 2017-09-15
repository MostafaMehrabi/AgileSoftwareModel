package entities;

import java.util.ArrayList;
import java.util.List;

import enums.MemberRole;
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
	
	
	private Team(){
		this.taskAllocationStrategy = TaskAllocationStrategy.ExpertiseBased;
		teamPersonnel = new ArrayList<>();
		backLog = new ArrayList<>();
		allTasksDoneSoFar = new ArrayList<>();
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
	}
	
	public static Team getTeam(){
		if(team == null){
			team = new Team();
		}
		return team;
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
	
	public int getLasMemberID(){
		return this.lastMemebrID;
	}
	
	public void setLastMemberID(int id){
		this.lastMemebrID = id;
	}
	
	public void addNewMember(String firstName, String lastName, MemberRole role){
		TeamMember newDeveloper = new TeamMember(++lastMemebrID, firstName, lastName, role);
		teamPersonnel.add(newDeveloper);
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
	
	public int getHighExpertiseCofficient(){
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
}
