package entities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Very important: mechanism for adding a backlog history must be added, so that we can hold onto the list of project backlogs,
//because we must be able to repeat them for different task allocation strategies.
//priority attribute must be added to tasks, as well as task allocation methods that consider priority (a place holder for this)!

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.Timer;

import core.Main;
import enums.MemberRole;
import enums.SkillArea;
import enums.TaskAllocationStrategy;


public class Team {
	private TaskAllocationStrategy taskAllocationStrategy;
	private int lastMemebrID;
	private List<TeamMember> teamPersonnel;
	private List<Task> backLog;
	private List<Task> allTasksDoneSoFar;
	private List<String> log;
	private double storyPointCoefficient;
	private double progressPerStoryPoint;
	private int lowExpertiseLowerBoundary, lowExpertiseHigherBoundary;
	private int mediumExpertiseLowerBoundary, mediumExpertiseHigherBoundary;
	private int highExpertiseLowerBoundary, highExpertiseHigherBoundary;
	private int lowExpertiseCoefficient, mediumExpertiseCoefficient, highExpertiseCoefficient;
	private int hoursToSystemTimeCoefficient;
	private boolean stopAfterEachSprint, teamWorking;
	private static Team team = null;
	private TaskBoard taskBoard;
	private int lastTaskID;
	private int hoursPerSprint;
	private int initialStoryPoints;
	private int numberOfSprintsPerProject;
	private double lastSprintVelocity;
	private ExecutorService executors;
	private Timer timer;
	private long sprintStartTime, sprintFinishTime;
	private CountDownLatch latch;

	
	private Team(){
		this.taskAllocationStrategy = TaskAllocationStrategy.ExpertiseBased;
		teamPersonnel = new ArrayList<>();
		backLog = new ArrayList<>();
		allTasksDoneSoFar = new ArrayList<>();
		log = new ArrayList<>();
		taskBoard = new TaskBoard();
		//setting default values for team properties, these values can be customized later.
		storyPointCoefficient = 8;
		progressPerStoryPoint = 0.3d;
		lowExpertiseLowerBoundary = 0; lowExpertiseHigherBoundary = 5;
		mediumExpertiseLowerBoundary = 6; mediumExpertiseHigherBoundary = 20;
		highExpertiseLowerBoundary = 21; highExpertiseHigherBoundary = 30;
		lowExpertiseCoefficient = 1; mediumExpertiseCoefficient = 3; highExpertiseCoefficient = 5;
		hoursToSystemTimeCoefficient = 750; //MAKES A SPRINT TAKE 1 MINUTE IN SYSTEM TIME
		stopAfterEachSprint = false; teamWorking = false;
		hoursPerSprint = 80;
		initialStoryPoints = 70;
		lastMemebrID = 0;
		lastTaskID = 0;
		numberOfSprintsPerProject = 26; //each sprint 2 weeks, 26 sprints is roughly 1 year project!
		lastSprintVelocity = 0;
		executors = null;
		sprintStartTime = 0l;
		sprintFinishTime = 0l;
		latch = null;
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
	
	public int getHoursPerSpring(){
		return this.hoursPerSprint;
	}
	
	public void setHoursPerSprint(int hours){
		this.hoursPerSprint = hours;
	}
	
	public int getInitialStoryPoints(){
		return this.initialStoryPoints;
	}
	
	public void setInitialStoryPoints(int points){
		this.initialStoryPoints = points; 
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
		long tctLong = Double.valueOf(tct).longValue();
		return tctLong * hoursToSystemTimeCoefficient;
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
	
	public int getHoursToSystemTimeCoefficient(){
		return this.hoursToSystemTimeCoefficient;
	}
	
	public void setHoursToSystemTimeCoefficient(int coef){
		this.hoursToSystemTimeCoefficient = coef;
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
	
	public int getNumberOfSprintsPerProject(){
		return this.numberOfSprintsPerProject;
	}
	
	public void setNumberOfSprintsPerProject(int sprints){
		this.numberOfSprintsPerProject = sprints;
	}
	
	public long getSprintStartTime() {
		return this.sprintStartTime;
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
	
	public void addNewTaskToBackLog(String name, String description, int storyPoints, Set<SkillArea> requiredSkillAreas, int priority) throws IllegalArgumentException{
		if(storyPoints < 1 || storyPoints > 10){
			throw new IllegalArgumentException("The value provided as story points for the"
					+ " new task, can only be between 0 and 10, inclusive!");
		}
		int taskID = ++lastTaskID;
		Task newTask = new Task(taskID, name, storyPoints, requiredSkillAreas);
		newTask.setPriority(priority);
		
		if(!description.isEmpty())
			newTask.setTaskDescription(description);
		
		addTaskToBackLog(newTask);
	}
	
	private void addTaskToBackLog(Task task){
		backLog.add(task);
		Main.updateBackLogTabel(task);
	}	
	
	public void moveToSprintBackLog(int[] selectedIndices){
		int arraySize = selectedIndices.length;
		List<Task> tasks = new ArrayList<>();
		for(int index = (arraySize-1); index >= 0; index--){
			Task task = backLog.remove(selectedIndices[index]);
			tasks.add(task);
		}
		Collections.reverse(tasks);
		ConcurrentLinkedQueue<Task> toDoTasks = taskBoard.getToDoTasks();
		toDoTasks.addAll(tasks);
		taskBoard.setToDoTasks(toDoTasks);
	}
	
	public void deleteFromBackLog(int[] selectedIndices){
		for(int index = (selectedIndices.length - 1); index >= 0; index--){
			backLog.remove(selectedIndices[index]);
		}
	}
	
	public double getTimeLeftToDeadline(){
		long currentTime = System.currentTimeMillis();
		long duration = currentTime - sprintStartTime;
		duration /= hoursToSystemTimeCoefficient;
		long timeLeft = hoursPerSprint - duration; 
		return (double)timeLeft;
	}
	
	public ConcurrentLinkedQueue<Task> getToDoTasks(){
		return taskBoard.getToDoTasks();
	}
	
	public ConcurrentLinkedQueue<Task> getTasksInProgress(){
		return taskBoard.getTasksInProgress();
	}
	
	public ConcurrentLinkedQueue<Task> getPerformedTasks(){
		return taskBoard.getPerformedTasks();
	}
	
	public int getCurrentSprint(){
		return taskBoard.getCurrentSprint();
	}
	
	public void setCurrentSprint(int number){
		taskBoard.setCurrentSprint(number);
	}
	
	public void readyForNextSprint(){
		taskBoard.goToNextSprint();
	}
	
	public void waitForSprintToFinish(){
		//consider the case that there might be tasks left in the toDoTask list when sprint is over!
		//stop the timer, and the rest...
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		timer.stop();
		setTeamWorking(false);
		executors.shutdown();
		//even though task pool is empty, timer stops only when all workers have finished.
		sprintFinishTime = System.currentTimeMillis();

		calculateForNextSprint();
		double velocity = 0d;
		//velocity = calculateVelocity();
		
		long supposedTime = sprintStartTime + (hoursPerSprint * hoursToSystemTimeCoefficient);
	
		String logEntryOne = "Sprint" + getCurrentSprint() + " took " + (sprintFinishTime - sprintStartTime) + ", at velocity: " + Double.toString(velocity);
		String logEntryTwo = "Supposed time for team to finishe this sprint was: " + supposedTime + ", but team finished at: " + sprintFinishTime;		
		//logInfo();
		Main.repopulatePersonnelTabel();
		readyForNextSprint();
//		if(!stopAfterEachSprint){
//			startNewSprint();
//		}
	}
	
	public void startNewSprint(){
		Thread motherThread = new Thread(new Runnable() {			
			@Override
			public void run() {
				setTeamWorking(true);
				latch = new CountDownLatch(teamPersonnel.size());
				sprintStartTime = System.currentTimeMillis();
				Main.setTaskBoardSprintNo(getCurrentSprint());
				Main.setLastSprintVelocity(lastSprintVelocity);
				//remember to disable the start button until sprint is over! or maybe even until project is over?
					
				executors = Executors.newFixedThreadPool(teamPersonnel.size());
				for(TeamMember member : teamPersonnel){
					Worker worker = new Worker(member, latch);
					executors.submit(worker);			
				}
				startSprintTimer();	
				waitForSprintToFinish();				
			}
		});
		motherThread.start();
	}
	
	private void calculateForNextSprint(){
		//calculateVelocity();
		//logInfo();
		//based on the current team velocity move as much tasks as calculated
		//to the sprint backlog
	
	}
	
	private void startSprintTimer(){
		Main.setTaskBoardProgress(0);
		int sprintLengthInSystemTime = hoursPerSprint * hoursToSystemTimeCoefficient;
		int lengthOfPercent = sprintLengthInSystemTime / 100;
		timer = new Timer(lengthOfPercent, new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				int progress = Main.getTaskBoardProgress();
				if(progress == 100){
					setTeamWorking(false);		
				}else{
					long duration = System.currentTimeMillis() - sprintStartTime;
					int progressValue = (int) (duration / lengthOfPercent);
					Main.setTaskBoardProgress(progressValue);
				}
			}
		});
		timer.start();
	}	
}
