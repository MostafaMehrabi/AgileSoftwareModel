package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskBoard {
	private List<Task> tasksToBeDone;
	private List<Task> tasksBeingPerformed;
	private List<Task> performedTasks;
	private Lock taskLock;
	private Lock submitPerformedTaskLock;
	
	public TaskBoard(){
		this.tasksToBeDone = new ArrayList<>();
		this.performedTasks = new ArrayList<>();
		this.tasksBeingPerformed = new ArrayList<>();
		this.taskLock = new ReentrantLock();
		this.submitPerformedTaskLock = new ReentrantLock();
	}
	
	public void addTask(Task task){
		try{
			taskLock.lock();
			tasksToBeDone.add(task);
			taskLock.unlock();
		}catch(Exception e){
			System.err.println("An error occurred while trying to add a new task to the task board");
			e.printStackTrace();
		}
	}
	
	public Task pollTask(Developer developer){
		taskLock.lock();
		Task chosenTask = null;
		double sai = 0d;
		for(Task task : tasksToBeDone){
			//calculate self assignment index for each task. 
			double tempSAI = calculateSAI(task, developer);
			if (tempSAI > sai){
				sai = tempSAI;
				chosenTask = task;
			}
		}
		tasksToBeDone.remove(chosenTask);
		tasksBeingPerformed.add(chosenTask);
		taskLock.unlock();
		return chosenTask;
	}
	
	private double calculateSAI(Task task, Developer developer){
		double motivationLevel = developer.getMotivation(task);
		double tct = developer.getTaskCompletionTime(task);
		return motivationLevel/tct;
	}	
	
	
	public void submitPerformedTaskToBoard(Task task){
		submitPerformedTaskLock.lock();
		tasksBeingPerformed.remove(task);
		performedTasks.add(task);
		submitPerformedTaskLock.unlock();
	}
	
	public void removeTask(int taskIndex){
		//removes a task, only if the team is not working!
		taskLock.lock();
		Team team = Team.getTeam();
		if(!team.getTeamWorking()){
			tasksToBeDone.remove(taskIndex);
		}		
		taskLock.unlock();
	}
	
	public void setTasksToBeDone(List<Task> tasks){
		taskLock.lock();
		this.tasksToBeDone = tasks;
		taskLock.unlock();
	}
	
	public void setFinishedTasks(List<Task> tasks){
		submitPerformedTaskLock.lock();
		this.performedTasks = tasks;
		submitPerformedTaskLock.unlock();
	}
	
	public List<Task> getTasksToBeDone(){
		return this.tasksToBeDone;
	}
	
	public List<Task> getPerformedTasks(){
		return this.performedTasks;
	}
}
