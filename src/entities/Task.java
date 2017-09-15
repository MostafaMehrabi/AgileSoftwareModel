package entities;

import java.util.Set;

import enums.SkillArea;

public class Task {
	private int taskId;
	private int storyPoints;
	private Set<SkillArea> requiredSkillAreas;
	private int performerID;
	private String taskDescription;
	
	public Task(int taskId, int storyPoints, Set<SkillArea> requiredSkillAreas){
		this.taskId = taskId;
		this.storyPoints = storyPoints;
		this.requiredSkillAreas = requiredSkillAreas;
		this.taskDescription = "";
		this.performerID = -1;//when a task is not done by anyone yet!
	}
	
	public int getStoryPoints(){
		return this.storyPoints;
	}
	
	public Set<SkillArea> getRequiredSkillAreas(){
		return this.requiredSkillAreas;
	}
	
	public void setPerformerID(int performerID){
		this.performerID = performerID;
	}
	
	public void setTaskDescription(String description){
		this.taskDescription = description;
	}
	
	public String getTaskDescription(){
		return this.taskDescription;
	}
	
	public int getPerformerID(){
		return this.performerID;
	}
	
	public int getTaskID(){
		return this.taskId;
	}
	
	public double getLearningPotential(){
		Team team = Team.getTeam();
		double lp = requiredSkillAreas.size() * team.getStoryPointCoefficient();
		return lp;
	}
}
