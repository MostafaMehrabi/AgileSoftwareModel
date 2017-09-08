package entities;

import java.util.Set;

public class Task {
	private int storyPoints;
	private Set<SkillArea> requiredSkillAreas;
	private long performerID;
	
	public Task(int storyPoints, Set<SkillArea> requiredSkillAreas){
		this.storyPoints = storyPoints;
		this.requiredSkillAreas = requiredSkillAreas;
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
	
	public long getPerformerID(){
		return this.performerID;
	}
	
	public double getLearningPotential(){
		Team team = Team.getTeam();
		double lp = requiredSkillAreas.size() * team.getStoryPointCoefficient();
		return lp;
	}
}
