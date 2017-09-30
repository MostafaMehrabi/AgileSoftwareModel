package entities;

import java.awt.EventQueue;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import core.Main;
import enums.MemberRole;
import enums.SkillArea;

public class TaskBoard {
	private ConcurrentLinkedQueue<Task> toDoTasks;
	private ConcurrentLinkedQueue<Task> tasksInProgress;
	private ConcurrentLinkedQueue<Task> performedTasks;
	private volatile Lock taskLock;
	private int currentSprint;
//	private volatile AtomicBoolean workerCanPoll = new AtomicBoolean(true);
	
	public TaskBoard(){
		this.toDoTasks = new ConcurrentLinkedQueue<>();
		this.performedTasks = new ConcurrentLinkedQueue<>();
		this.tasksInProgress = new ConcurrentLinkedQueue<>();
		this.taskLock = new ReentrantLock();
		this.currentSprint = 1;
	}	
	
	public boolean isToDoTasksListEmpty(){
		return toDoTasks.isEmpty();
	}
	
	public Task pollTask(TeamMember developer, boolean alreadyOwnsLock){
		if(!alreadyOwnsLock) {
			taskLock.lock();
		}
		
		if(isToDoTasksListEmpty()){
			return null;
		}
		
		Task chosenTask = null;
		double chosenSAI = 0d;
		boolean foundTask = false;
		
		//the developer picks the first task the he/she can, and compares others with this one!
		for(Task task : toDoTasks) {
			if(memberAllowedToPickTask(task, developer)) {
				foundTask = true;
				chosenTask = task;
				chosenSAI = calculateSAI(chosenTask, developer);
				break;
			}
		}
		
		if(!foundTask) {
			//if there is no task for the member to pick (based on their role), the he/she is finished for this sprint
			return null;
		}
	
		for(Task task : toDoTasks){
			if(memberAllowedToPickTask(task, developer)) {
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
		}

		removeObjectFromList(toDoTasks, chosenTask);
		
		return chosenTask;
	}
	
	public void releaseTaskLock(TeamMember developer){		
		try {
			taskLock.unlock();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean memberAllowedToPickTask(Task task, TeamMember developer) {
		Set<SkillArea> requiredSkills = task.getRequiredSkillAreas();
		if(requiredSkills.contains(SkillArea.Testing)) {
			//if the task is a testing task, and the member is not a tester
			//then return false.
			if(!developer.getMemberRole().equals(MemberRole.Tester))
				return false;
		}else {
			//other wise if the task is a development task, and the member is 
			//not a developer, return false. 
			if(!developer.getMemberRole().equals(MemberRole.Developer))
				return false;
		}
		return true;
	}
	
	private double calculateSAI(Task task, TeamMember developer){		
		double motivationLevel = developer.calculateMotivation(task);
		double tct = developer.calculateTaskCompletionTime(task);
		return motivationLevel/tct;
	}	
	
	private synchronized void removeObjectFromList(ConcurrentLinkedQueue<Task> list, Task task){
		Iterator<Task> iterator = list.iterator();
		while(iterator.hasNext()){
			Task tempTask = iterator.next();
			if(tempTask.equals(task)){
				iterator.remove();
				return;
			}				
		}
	}
	
	public synchronized void submitPerformedTaskToBoard(Task task){
		removeObjectFromList(tasksInProgress, task);
		performedTasks.add(task);
		try {
			EventQueue.invokeAndWait(new Runnable() {				
				@Override
				public void run() {
					Main.repopulateTasksInProgressTable();
					Main.repopulateCompletedTasksTable();				
				}
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setToDoTasks(ConcurrentLinkedQueue<Task> tasks){
		//only supposed to be called when loading system 
		this.toDoTasks = tasks;
	}
	

	public void setInProgressTasks(ConcurrentLinkedQueue<Task> tasks){
		this.tasksInProgress = tasks;
	}
	
	public void setPerformedTasks(ConcurrentLinkedQueue<Task> tasks){
		//only supposed to be called when loading system 
		this.performedTasks = tasks;
	}
	
	public synchronized void addToTasksInProgress(Task task) {
		tasksInProgress.add(task);
		try {
			EventQueue.invokeAndWait(new Runnable() {			
				@Override
				public void run() {
					Main.repopulateToDoTaskTable();
					Main.repopulateTasksInProgressTable();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void rejectTask(Task task) {
		toDoTasks.add(task);
	}
	
	public void setTasksInProgress(ConcurrentLinkedQueue<Task> tasks) {
		//only supposed to be called when loading system 
		this.tasksInProgress = tasks;
	}
	
	public ConcurrentLinkedQueue<Task> getToDoTasks(){
		return this.toDoTasks;
	}
	
	public ConcurrentLinkedQueue<Task> getPerformedTasks(){
		return this.performedTasks;
	}
	
	public ConcurrentLinkedQueue<Task> getTasksInProgress(){
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
