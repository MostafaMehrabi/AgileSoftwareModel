package entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import core.Main;

public class TaskBoard {
	private List<Task> toDoTasks;
	private List<Task> tasksInProgress;
	private List<Task> performedTasks;
	private Lock taskLock;
	private int currentSprint;
	
	public TaskBoard(){
		this.toDoTasks = new ArrayList<>();
		this.performedTasks = new ArrayList<>();
		this.tasksInProgress = new ArrayList<>();
		this.taskLock = new ReentrantLock();
		this.currentSprint = 1;
	}	
	
	public boolean isToDoTasksListEmpty(){
		return toDoTasks.isEmpty();
	}
	
	public Task pollTask(TeamMember developer){
		taskLock.lock();
		
		Team team = Team.getTeam();
		
		if(isToDoTasksListEmpty()){
			team.sprintFinished();
			return null;
		}
		
		Task chosenTask = toDoTasks.get(0);
		double chosenSAI = calculateSAI(chosenTask, developer);
		
		for(Task task : toDoTasks){
			int priority = task.getPriority();
			
			if(priority > chosenTask.getPriority()){
				//if the task has a higher priority, definitely pick this.
				chosenTask = task;
			}
			
			else if(priority == chosenTask.getPriority()){
				//if the tasks are of the same priority then calculate self assignment index (sai)
				//only replace if (sai) is bigger than current task. 
				double tempSAI = calculateSAI(task, developer);
				if (tempSAI > chosenSAI){
					chosenSAI = tempSAI;
					chosenTask = task;
				}
			}
		}

		removeObjectFromList(toDoTasks, chosenTask);
		
		return chosenTask;
	}
	
	public void releaseTaskLock(){
		taskLock.unlock();
	}
	
	private double calculateSAI(Task task, TeamMember developer){
		double motivationLevel = developer.calculateMotivation(task);
		double tct = developer.calculateTaskCompletionTime(task);
		return motivationLevel/tct;
	}	
	
	private synchronized void removeObjectFromList(List<Task> list, Task task){
		Iterator<Task> iterator = list.iterator();
		while(iterator.hasNext()){
			Task tempTask = iterator.next();
			if(tempTask.equals(task)){
				iterator.remove();
				return;
			}				
		}
	}
	
	public void submitPerformedTaskToBoard(Task task){
		taskLock.lock();
		removeObjectFromList(tasksInProgress, task);
		performedTasks.add(task);
		Main.repopulateTasksInProgressTable();
		Main.repopulateCompletedTasksTable();
		taskLock.unlock();
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
		taskLock.lock();
		this.performedTasks = tasks;
		taskLock.unlock();
	}
	
	public void addToTasksInProgress(Task task) {
		tasksInProgress.add(task);
		Main.repopulateToDoTaskTable();
		Main.repopulateTasksInProgressTable();
	}
	
	public void rejectTask(Task task) {
		toDoTasks.add(task);
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
	
	public int getCurrentSprint(){
		return this.currentSprint;
	}
	
	public void setCurrentSprint(int sprintNo){
		this.currentSprint = sprintNo;
	}
	
	public void goToNextSprint(){
		this.currentSprint++;
	}	
}
