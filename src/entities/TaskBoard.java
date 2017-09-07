package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskBoard {
	private List<Task> tasksToBeDone;
	private List<Task> performedTasks;
	private Lock taskLock;
	private Lock submitPerformedTaskLock;
	
	public TaskBoard(){
		this.tasksToBeDone = new ArrayList<>();
		this.performedTasks = new ArrayList<>();
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
	
	
}
