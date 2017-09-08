package entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Developer {
	private long id;
	private String firstName;
	private String lastName;
	private int completedStoryPoints;
	private Map<SkillArea, Double> expertiseInSkillAreas;
	private DeveloperRole role;
	
	public Developer(long id, String firstName, String lastName, DeveloperRole role){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.completedStoryPoints = 0;
		expertiseInSkillAreas = new HashMap<>();
		expertiseInSkillAreas.put(SkillArea.BackEnd, 0d);
		expertiseInSkillAreas.put(SkillArea.FrontEnd, 0d);
		expertiseInSkillAreas.put(SkillArea.Design, 0d);
	}
	
	public void startWorking(){
		
	}
	
	public Map<SkillArea, Double> getExpertiseInSkillAreas(){
		return this.expertiseInSkillAreas;
	}
	
	/**
	 * In this model, motivation is directly proportional to learning potential if
	 * the task allocation strategy is learning-based, as is inversely proportional
	 * to learning potential if the task allocation strategy is expertise-based.
	 * @param task
	 * @return
	 */
	public double getMotivation(Task task){
		Team team = Team.getTeam();
		int storyPoints = task.getStoryPoints();
		double potentialLearning = 0d;
		double progressPerStoryPoint = team.getProgressPerStoryPoint();
		double highestExpertiseLevel = team.getHighExpertiseHigherBoundary();
		Set<SkillArea> requiredSkillAreas = task.getRequiredSkillAreas();
		for(SkillArea skillArea : requiredSkillAreas){
			double expertiseInSkillArea = expertiseInSkillAreas.get(skillArea);
			if(expertiseInSkillArea < highestExpertiseLevel){
				double potentialProgressInThisSkill = storyPoints * progressPerStoryPoint;
				if ((expertiseInSkillArea + potentialProgressInThisSkill) > highestExpertiseLevel){
					potentialProgressInThisSkill = highestExpertiseLevel - expertiseInSkillArea;
				}
				potentialLearning += potentialProgressInThisSkill; 
			}
		}
		if(team.getTaskAllocationStrategy().equals(TaskAllocationStrategy.ExpertiseBased)){
			potentialLearning = 1d / potentialLearning;
		}
		return potentialLearning;
	}
	
	public double getTaskCompletionTime(Task task) throws IllegalArgumentException{
		int overallExpertise = 0;
		Set<SkillArea> requiredSkillAreas = task.getRequiredSkillAreas();
		int storyPoints = task.getStoryPoints();
		try{			
			for(SkillArea skillArea : requiredSkillAreas){
				overallExpertise += getExpertiseCoefficient(expertiseInSkillAreas.get(skillArea));
		}
		}catch(IllegalArgumentException e){
			throw e;
		}
		
		double averageExpertise =  ((double) overallExpertise / (double) requiredSkillAreas.size());
		return  ((double) (Team.getTeam().getStoryPointCoefficient() * storyPoints) / averageExpertise);
	}
	
	public int getExpertiseCoefficient(double expertiseLevel) throws IllegalArgumentException{
		Team team = Team.getTeam();
		if(expertiseLevel >= team.getLowExpertiseLowerBoundary() && expertiseLevel <= team.getLowExpertiseHigherBoundary())
			return team.getLowExpertiseCoefficient();
		if(expertiseLevel >= team.getMediumExpertiseLowerBoundary() && expertiseLevel <= team.getMediumExpertiseHigherBoundary())
			return team.getMediumExpertiseCoefficient();
		if(expertiseLevel >= team.getHighExpertiseLowerBoundary() && expertiseLevel <= team.getHighExpertiseHigherBoundary())
			return team.getHighExpertiseCofficient();
		else
			throw new IllegalArgumentException("Expertise level in a skill area must be between 0 and 30 inclusive! Received expertise level: " + expertiseLevel);
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public long getID(){
		return this.id;
	}
	
	public DeveloperRole getDeveloperRole(){
		return this.role;
	}
	
	public int getCompletedStoryPoints(){
		return this.completedStoryPoints;
	}
	
	public void setCompletedStoryPoints(int storyPoints){
		this.completedStoryPoints = storyPoints;
	}
}
