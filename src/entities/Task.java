package entities;

import java.util.Set;

import enums.SkillArea;

public class Task {
	private String taskName;
	private int taskId;
	private int storyPoints;
	private Set<SkillArea> requiredSkillAreas;
	private int performerID;
	private String taskDescription;
	
	public Task(int taskId, String taskName, int storyPoints, Set<SkillArea> requiredSkillAreas){
		this.taskName = taskName;
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
	
	public void setRequiredSkill(Set<SkillArea> requiredSkillAreas){
		this.requiredSkillAreas = requiredSkillAreas;
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
	
	public String getTaskName(){
		return this.taskName;
	}
	
	public void setTaskName(String name){
		this.taskName = name;
	}
}
