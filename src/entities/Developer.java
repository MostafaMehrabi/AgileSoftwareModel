package entities;

import java.util.HashMap;
import java.util.Map;

public class Developer {
	private long id;
	private String firstName;
	private String lastName;
	private int completedStoryPoints;
	private Map<SkillAreas, Integer> expertiseInSkillAreas;
	private DeveloperRole role;
	
	public Developer(long id, String firstName, String lastName, DeveloperRole role){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.completedStoryPoints = 0;
		expertiseInSkillAreas = new HashMap<>();
		expertiseInSkillAreas.put(SkillAreas.BackEnd, 0);
		expertiseInSkillAreas.put(SkillAreas.FrontEnd, 0);
		expertiseInSkillAreas.put(SkillAreas.Design, 0);
	}
	
	public void startWorking(){
		
	}
	
	public Map<SkillAreas, Integer> getExpertiseInSkillAreas(){
		return this.expertiseInSkillAreas;
	}
	
	private double getMotivation(){
		return 0d;
	}
}
