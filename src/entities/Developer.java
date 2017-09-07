package entities;

import java.util.HashMap;
import java.util.Map;

public class Developer {
	private long id;
	private String firstName;
	private String lastName;
	private int completedStoryPoints;
	private Map<SkillAreas, Integer> expertiseMap;
	
	public Developer(long id, String firstName, String lastName){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.completedStoryPoints = 0;
		expertiseMap = new HashMap<>();
		expertiseMap.put(SkillAreas.BackEnd, 0);
		expertiseMap.put(SkillAreas.FrontEnd, 0);
		expertiseMap.put(SkillAreas.Design, 0);
	}
	
	public void startWorking(){
		
	}
}
