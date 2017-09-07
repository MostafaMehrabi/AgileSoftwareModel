package entities;

import java.util.Set;

public class Task {
	private int storyPoints;
	private Set<SkillAreas> requiredSkillAreas;
	
	public Task(int storyPoints, Set<SkillAreas> requiredSkillAreas){
		this.storyPoints = storyPoints;
		this.requiredSkillAreas = requiredSkillAreas;
	}
	
	public int getStoryPoints(){
		return this.storyPoints;
	}
	
	public Set<SkillAreas> getRequiredSkillAreas(){
		return this.requiredSkillAreas;
	}
}
