package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import enums.SkillArea;

public class TaskBoard {
	private List<Task> toDoTasks;
	private List<Task> tasksInProgress;
	private List<Task> performedTasks;
	private Lock taskLock;
	private Lock submitPerformedTaskLock;
	private int lastTaskID;
	
	public TaskBoard(){
		this.toDoTasks = new ArrayList<>();
		this.performedTasks = new ArrayList<>();
		this.tasksInProgress = new ArrayList<>();
		this.taskLock = new ReentrantLock();
		this.submitPerformedTaskLock = new ReentrantLock();
		this.lastTaskID = 0;
	}
	
	public void addNewTask(String name, int storyPoints, Set<SkillArea> requiredSkillAreas) throws IllegalArgumentException{
		if(storyPoints < 1 || storyPoints > 10){
			throw new IllegalArgumentException("The value provided as story points for the"
					+ " new task, can only be between 0 and 10, inclusive!");
		}
		int taskID = ++lastTaskID;
		Task newTask = new Task(taskID, name, storyPoints, requiredSkillAreas);
		addTask(newTask);
	}
	
	private void addTask(Task task){
		try{
			taskLock.lock();
			toDoTasks.add(task);
			taskLock.unlock();
		}catch(Exception e){
			System.err.println("An error occurred while trying to add a new task to the task board");
			e.printStackTrace();
		}
	}
	
	public Task pollTask(TeamMember developer){
		taskLock.lock();
		Task chosenTask = null;
		double sai = 0d;
		for(Task task : toDoTasks){
			//calculate self assignment index for each task. 
			double tempSAI = calculateSAI(task, developer);
			if (tempSAI > sai){
				sai = tempSAI;
				chosenTask = task;
			}
		}
		toDoTasks.remove(chosenTask);
		//tasksInProgress.add(chosenTask);
		//before a member starts performing a task, it checks if it can do the task before the 
		//end of sprint reaches, if not, it will return the task to the task board, and removes
		//it from the tasks in progress, and looks for another task.
		//in fact, the worker keeps looking for a task, until it finds one that it can execute before
		//the deadline, and then returns all the rejected tasks back to the "toDoTasks", and adds 
		//the chosen task to the "tasksInProgress" list, then updates the gui!
		taskLock.unlock();
		return chosenTask;
	}
	
	private double calculateSAI(Task task, TeamMember developer){
		double motivationLevel = developer.getMotivation(task);
		double tct = developer.getTaskCompletionTime(task);
		return motivationLevel/tct;
	}	
	
	
	public void submitPerformedTaskToBoard(Task task){
		submitPerformedTaskLock.lock();
		tasksInProgress.remove(task);
		performedTasks.add(task);
		submitPerformedTaskLock.unlock();
	}
	
	public void removeTask(int taskIndex){
		//removes a task, only if the team is not working!
		taskLock.lock();
		Team team = Team.getTeam();
		if(!team.getTeamWorking()){
			toDoTasks.remove(taskIndex);
		}		
		taskLock.unlock();
	}
	
	public void setToDoTasks(List<Task> tasks){
		//only supposed to be called when loading system 
		taskLock.lock();
		this.toDoTasks = tasks;
		taskLock.unlock();
	}
	

	public void setInProgressTasks(List<Task> tasks){
		taskLock.lock();
		this.tasksInProgress = tasks;
		taskLock.unlock();
	}
	
	public void setPerformedTasks(List<Task> tasks){
		//only supposed to be called when loading system 
		submitPerformedTaskLock.lock();
		this.performedTasks = tasks;
		submitPerformedTaskLock.unlock();
	}
	
	public void addToTasksInProgress() {
		
	}
	
	public void returnRejectedTasks(List<Task> tasks) {
		//only called if the worker has rejected tasks (this is checked by each 
		//worker independently). 
	}
	
	public void setTasksInProgress(List<Task> tasks) {
		//only supposed to be called when loading system 
		taskLock.lock();
		this.tasksInProgress = tasks;
		taskLock.unlock();
	}
	
	public List<Task> getToDoTasks(){
		return this.toDoTasks;
	}
	
	public List<Task> getPerformedTasks(){
		return this.performedTasks;
	}
	
	public List<Task> getTasksInProgress(){
		//only supposed to be called when recording the system to files
		//this should theoretically return empty tasks for now, as
		//system is only recorded and loaded before and after sprints
		return this.tasksInProgress;
	}
	
	public int getLastTaskID(){
		return lastTaskID;
	}
	
	public void setLastTaskID(int lastTaskID){
		this.lastTaskID = lastTaskID;
	}
}
