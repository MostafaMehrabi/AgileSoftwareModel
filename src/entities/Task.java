package entities;

import java.util.Set;

public class Task {
	private int taskId;
	private int storyPoints;
	private Set<SkillArea> requiredSkillAreas;
	private long performerID;
	private String taskDescription;
	
	public Task(int taskId, int storyPoints, Set<SkillArea> requiredSkillAreas){
		this.taskId = taskId;
		this.storyPoints = storyPoints;
		this.requiredSkillAreas = requiredSkillAreas;
		this.taskDescription = "";
	}
	
	public int getStoryPoints(){
		return this.storyPoints;
	}
	
	public Set<SkillArea> getRequiredSkillAreas(){
		return this.requiredSkillAreas;
	}
	
	public void setPerformerID(long performerID){
		this.performerID = performerID;
	}
	
	public void setTaskDescription(String description){
		this.taskDescription = description;
	}
	
	public long getPerformerID(){
		return this.performerID;
	}
	
	public double getLearningPotential(){
		Team team = Team.getTeam();
		double lp = requiredSkillAreas.size() * team.getStoryPointCoefficient();
		return lp;
	}
}
