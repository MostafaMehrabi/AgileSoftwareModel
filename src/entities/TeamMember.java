package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import core.Main;
import enums.MemberRole;
import enums.SkillArea;
import enums.TaskAllocationStrategy;

public class TeamMember {
	private int id;
	private String firstName;
	private String lastName;
	private int completedStoryPoints;
	private Map<SkillArea, Double> expertiseInSkillAreas;
	private MemberRole role;
	
	public TeamMember(int id, String firstName, String lastName, MemberRole role){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.completedStoryPoints = 0;
		expertiseInSkillAreas = new HashMap<>();
		expertiseInSkillAreas.put(SkillArea.BackEnd, 0d);
		expertiseInSkillAreas.put(SkillArea.FrontEnd, 0d);
		expertiseInSkillAreas.put(SkillArea.Design, 0d);
	}
	
	/*before a member starts performing a task, it checks if it can do the task before the 
	 * end of sprint reaches, if not, it will return the task to the task board, and removes
	 * it from the tasks in progress, and looks for another task.
     * in fact, the worker keeps looking for a task, until it finds one that it can execute before
	 * the deadline. If no task is found as such, then the first chosen task is broken into two smaller
	 * tasks (smaller story points), the one that can be done within the time limit. 
	 * Once a task is chosen, the member returns all the rejected tasks back to the "toDoTasks", and adds 
	 * the chosen task to the "tasksInProgress" list, then updates the gui!*/
	public void startWorking(CountDownLatch latch){
		Team team = Team.getTeam();
		TaskBoard taskBoard = team.getTaskBoard();
		Task chosenTask = null;
		while(team.getTeamWorking()){
			chosenTask = taskBoard.pollTask(this);
			if(chosenTask != null){
				if(!memberCanPerformTask(chosenTask)){
					
					boolean foundTask = false; 
					List<Task> rejectedTasks = new ArrayList<>();
					rejectedTasks.add(chosenTask);
					
					while(!taskBoard.isToDoTasksListEmpty()){//whilst there are tasks in the toDoTasks list to poll
						//keep in mind, that this worker has yet not released the lock!
						Task tempTask = taskBoard.pollTask(this);
						if(memberCanPerformTask(tempTask)){
							returnRejectedTasks(rejectedTasks, taskBoard);
							chosenTask = tempTask;
							foundTask = true;
							break;
						}else{
							rejectedTasks.add(tempTask);
						}
					}
					if(!foundTask){
						//if worker went through all tasks, and could not find a task,
						//gets the first chosen task and breaks it into smaller tasks.
						Task spareTask = breakTask(chosenTask);
						rejectedTasks.remove(chosenTask);//replace chosen task with the spare task.
						rejectedTasks.add(0, spareTask);
						returnRejectedTasks(rejectedTasks, taskBoard);
					}
				}
				
				chosenTask.setPerformerID(getID());
				taskBoard.addToTasksInProgress(chosenTask);
				taskBoard.releaseTaskLock();
				execute(team.convertTctToSystemTime(calculateTaskCompletionTime(chosenTask)));
				adjustExpertiseLevels(chosenTask);
				//logInfo();
				taskBoard.submitPerformedTaskToBoard(chosenTask);
			}
		}
		latch.countDown();
	}
	
	private void execute(long time){
		try{
			System.out.println("member " + Thread.currentThread().getId() + " working for " + time + " hours");
			Thread.sleep(time);
		}catch(Exception e){
			Main.issueErrorMessage("Worker encountered probelm\n" + e.getMessage());
		}
	}
	
	private boolean memberCanPerformTask(Task task){
		Team team = Team.getTeam();
		double tct = calculateTaskCompletionTime(task);
		double timeLeftToDeadline = team.getTimeLeftToDeadline();
		if(tct < timeLeftToDeadline)
			return true;
		else
			return false;
	}
	
	private void adjustExpertiseLevels(Task chosenTask){
		Team team = Team.getTeam();
		int storyPoints = chosenTask.getStoryPoints();
		double highestExpertiseLevel = team.getHighExpertiseHigherBoundary();
		Set<SkillArea> requiredSkills = chosenTask.getRequiredSkillAreas();
		
		for(SkillArea skill : requiredSkills){			
			
			double expertiseInSkillArea = expertiseInSkillAreas.get(skill);
			double progressInThisSkillArea = storyPoints * Team.getTeam().getProgressPerStoryPoint();
			expertiseInSkillArea += progressInThisSkillArea;
			
			if(expertiseInSkillArea > highestExpertiseLevel)
				expertiseInSkillArea = highestExpertiseLevel;
			
			expertiseInSkillAreas.put(skill, expertiseInSkillArea);
		}
	}
	
	private void returnRejectedTasks(List<Task> rejectedTasks, TaskBoard taskBoard){
		for(Task rejectedTask : rejectedTasks){
			taskBoard.rejectTask(rejectedTask);
		}
	}
	
	private Task breakTask(Task chosenTask){
		double timeLeft = Team.getTeam().getTimeLeftToDeadline();
		double averageExpertise = calculateAverageExpertiseCoefficient(chosenTask.getRequiredSkillAreas());
		double supposedStoryPoint = (timeLeft * averageExpertise) / Team.getTeam().getStoryPointCoefficient();
		System.out.println("time left: " + timeLeft + ", tct: " + calculateTaskCompletionTime(chosenTask) + " for thread " + Thread.currentThread().getId());
		int idealStoryPoints = Double.valueOf(supposedStoryPoint).intValue();
		int initialStoryPoints = chosenTask.getStoryPoints();
		int spareTaskStoryPoints = initialStoryPoints - idealStoryPoints;
		System.out.println("points: " + initialStoryPoints + ", " + idealStoryPoints + ", " + spareTaskStoryPoints + " for member " + Thread.currentThread().getId());
		Task spareTask = new Task(chosenTask.getTaskID(), chosenTask.getTaskName(), spareTaskStoryPoints, chosenTask.getRequiredSkillAreas());
		spareTask.setPriority(chosenTask.getPriority());
		spareTask.setTaskDescription(chosenTask.getTaskDescription());
		chosenTask.setStoryPoints(idealStoryPoints);
		return spareTask;
	}
	
	private double calculateAverageExpertiseCoefficient(Set<SkillArea> requiredSkillAreas){
		int overallExpertise = 0;
		try{			
			for(SkillArea skillArea : requiredSkillAreas){
				overallExpertise += getExpertiseCoefficient(expertiseInSkillAreas.get(skillArea));
			}
		}catch(IllegalArgumentException e){
			Main.issueErrorMessage(e.getMessage());
		}
		
		double averageExpertise =  ((double) overallExpertise / (double) requiredSkillAreas.size());
		return averageExpertise;
	}
	
	/**
	 * In this model, motivation is directly proportional to learning potential if
	 * the task allocation strategy is learning-based, as is inversely proportional
	 * to learning potential if the task allocation strategy is expertise-based.
	 * @param task
	 * @return
	 */
	public double calculateMotivation(Task task){
		Team team = Team.getTeam();
		int storyPoints = task.getStoryPoints();
		double potentialLearning = 0d;
		double progressPerStoryPoint = team.getProgressPerStoryPoint();
		double highestExpertiseLevel = team.getHighExpertiseHigherBoundary();
		Set<SkillArea> requiredSkillAreas = task.getRequiredSkillAreas();
		
		for(SkillArea skillArea : requiredSkillAreas){
			double expertiseInThisSkillArea = expertiseInSkillAreas.get(skillArea);
			if(expertiseInThisSkillArea < highestExpertiseLevel){
				double potentialProgressInThisSkillArea = storyPoints * progressPerStoryPoint;
				if ((expertiseInThisSkillArea + potentialProgressInThisSkillArea) > highestExpertiseLevel){
					potentialProgressInThisSkillArea = highestExpertiseLevel - expertiseInThisSkillArea;
				}
				potentialLearning += potentialProgressInThisSkillArea; 
			}
		}
		
		if(team.getTaskAllocationStrategy().equals(TaskAllocationStrategy.ExpertiseBased)){
			potentialLearning = 1d / potentialLearning;
		}
		
		return potentialLearning;
	}
	
	public double calculateTaskCompletionTime(Task task){
		Set<SkillArea> requiredSkillAreas = task.getRequiredSkillAreas();
		int storyPoints = task.getStoryPoints();
		
		double averageExpertise = calculateAverageExpertiseCoefficient(requiredSkillAreas);
		
		return  (Team.getTeam().getStoryPointCoefficient() * storyPoints / averageExpertise);
	}
	
	public int getExpertiseCoefficient(double expertiseLevel) throws IllegalArgumentException{
		Team team = Team.getTeam();
		if(expertiseLevel >= team.getLowExpertiseLowerBoundary() && expertiseLevel <= team.getLowExpertiseHigherBoundary())
			return team.getLowExpertiseCoefficient();
		if(expertiseLevel >= team.getMediumExpertiseLowerBoundary() && expertiseLevel <= team.getMediumExpertiseHigherBoundary())
			return team.getMediumExpertiseCoefficient();
		if(expertiseLevel >= team.getHighExpertiseLowerBoundary() && expertiseLevel <= team.getHighExpertiseHigherBoundary())
			return team.getHighExpertiseCoefficient();
		else
			throw new IllegalArgumentException("Expertise level in a skill area must be between 0 and 30 inclusive! Received expertise level: " + expertiseLevel);
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public void setFirstName(String name){
		this.firstName = name;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public void setLastName(String name){
		this.lastName = name;
	}
	
	public int getID(){
		return this.id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public MemberRole getMemberRole(){
		return this.role;
	}
	
	public void setMemberRole(MemberRole role){
		this.role = role;
	}
	
	public double getExpertiseAtSkillArea(SkillArea skillArea){
		if(skillArea.equals(SkillArea.Testing))
			return 0d;
		else
			return expertiseInSkillAreas.get(skillArea);
	}
	
	public void setExpertiseAtSkillArea(SkillArea skillArea, double expertise){
		if(skillArea.equals(SkillArea.Testing))
			throw new IllegalArgumentException("EXPERTISE LEVEL FOR TESTING SKILL IS NOT SUPPORTED AT THE MOMENT");
		else{
			expertiseInSkillAreas.put(skillArea, expertise);
		}
	}
	
	public int getCompletedStoryPoints(){
		return this.completedStoryPoints;
	}
	
	public void setCompletedStoryPoints(int storyPoints){
		this.completedStoryPoints = storyPoints;
	}
}
