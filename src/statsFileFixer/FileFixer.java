package statsFileFixer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import core.Main;
import entities.Team;

public class FileFixer {
	private static int numOfPermutations;
	private static int numOfScenarios;
	private static String delimiter;
	private static String personnelFileTitle;
	private static String teamStatsTitle;
	private static String extraInfoTitle;

	public static void main(String[] args){
		numOfPermutations = Team.getTeam().getTotalNumberOfPermutations();
		numOfScenarios = Team.getTeam().getTotalNumberOfScenarios();
		delimiter = "sep=,";
		personnelFileTitle = "sprint,systemTime,modelTime,taskID,storyPoints,start/end,BackEnd Exp,FrontEnd Exp,Design Exp";
		teamStatsTitle = "sprint,velocity,storyPoints,duration,idlePeriod";
		extraInfoTitle = "sprint,systemTime,modelTime,task,storyPoints,picked/accepted/rejected,description";
		fixFiles();
	}
	
	private static String getPersonnelFileName(int permutation, int scenario, boolean expertise, int personnelID) {
		String strategy = (expertise) ? "ExpertiseBased" : "LearningBased";
		String personnelFileName = Main.getStatisticsDirectoryPath() + File.separator + Main.getPermutationFileName() + permutation
				+ File.separator + Main.getScenarioDirectoryName(scenario) + File.separator + strategy + File.separator + Main.getPersonnelFileName() + "_" + personnelID + ".csv";
		return personnelFileName;
	}
	
	private static String getTeamStatsFileName(int permutation, int scenario, boolean expertise) {
		String strategy = (expertise) ? "ExpertiseBased" : "LearningBased";
		String teamStatsFileName = Main.getStatisticsDirectoryPath() + File.separator + Main.getPermutationFileName() + permutation
				+ File.separator + Main.getScenarioDirectoryName(scenario) + File.separator + strategy + File.separator + "TeamStats.csv";
		return teamStatsFileName;
	}
	
	private static String getPersonnelExtraInfoFileName(int permutation, int scenario, boolean expertise, int personnelID) {
		String strategy = (expertise) ? "ExpertiseBased" : "LearningBased";
		String personnelExtraInfoFileName = Main.getStatisticsDirectoryPath() + File.separator + Main.getPermutationFileName() + permutation
				+ File.separator + Main.getScenarioDirectoryName(scenario) + File.separator + strategy + File.separator + "extras" + File.separator 
				+ "extraInformationForMember_" + personnelID + ".csv";
		return personnelExtraInfoFileName;
	}
	
	private static void fixFiles() {
		for(int permuteIndex = 1; permuteIndex <= numOfPermutations; permuteIndex++) {
			for(int scenarioIndex = 1; scenarioIndex <= numOfScenarios; scenarioIndex++) {
				for(int expertiseIndex = 1; expertiseIndex <=2; expertiseIndex++) {
					boolean expertise = (expertiseIndex == 1) ? true : false;
					fixPersonnelFiles(permuteIndex, scenarioIndex, expertise);
					fixTeamStatsFiles(permuteIndex, scenarioIndex, expertise);
				}
			}
		}
		System.out.println("done!");
	}
	
	private static void fixPersonnelFiles(int permutation, int scenario, boolean expertise) {
		for (int personnelID = 1; personnelID <= 7; personnelID++) {
			if(personnelID != 5) {
				fixMainPersonnelInfo(permutation, scenario, expertise, personnelID);
				fixExtraPersonnelInfo(permutation, scenario, expertise, personnelID);
			}
		}
	}
	
	private static void fixMainPersonnelInfo(int permutation, int scenario, boolean expertise, int personnelID) {
		String fileName = getPersonnelFileName(permutation, scenario, expertise, personnelID);
		File personnelFile = new File(fileName);
		if(!personnelFile.exists())
			return;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(personnelFile));
			List<String> content = new ArrayList<>();
			String line = reader.readLine();
			while (line != null) {
				content.add(line);
				line = reader.readLine();
			}
			line = content.get(0);
			if(!line.equals(delimiter)) {
				content.add(0, delimiter);
			}
			line = content.get(1);
			if(!line.equals(personnelFileTitle)) {
				content.add(1, personnelFileTitle);
			}
			reader.close();
			PrintWriter writer = new PrintWriter(fileName);
			for(String record : content) {
				writer.println(record);
			}
			writer.flush();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void fixExtraPersonnelInfo(int permutation, int scenario, boolean expertise, int personnelID) {
		String fileName = getPersonnelExtraInfoFileName(permutation, scenario, expertise, personnelID);
		File personnelFile = new File(fileName);
		if(!personnelFile.exists())
			return;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(personnelFile));
			List<String> content = new ArrayList<>();
			String line = reader.readLine();
			while (line != null) {
				content.add(line);
				line = reader.readLine();
			}
			line = content.get(0);
			if(!line.equals(delimiter)) {
				content.add(0, delimiter);
			}
			line = content.get(1);
			if(!line.equals(extraInfoTitle)) {
				content.add(1, extraInfoTitle);
			}
			reader.close();
			PrintWriter writer = new PrintWriter(fileName);
			for(String record : content) {
				writer.println(record);
			}
			writer.flush();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void fixTeamStatsFiles(int permutation, int scenario, boolean expertise) {
		String fileName = getTeamStatsFileName(permutation, scenario, expertise);
		File teamStatsFile = new File(fileName);
		if(!teamStatsFile.exists())
			return;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(teamStatsFile));
			List<String> content = new ArrayList<>();
			String line = reader.readLine();
			while (line != null) {
				content.add(line);
				line = reader.readLine();
			}
			line = content.get(0);
			if(!line.equals(delimiter)) {
				content.add(0, delimiter);
			}
			line = content.get(1);
			if(!line.equals(teamStatsTitle)) {
				content.add(1, teamStatsTitle);
			}
			reader.close();
			PrintWriter writer = new PrintWriter(fileName);
			for(String record : content) {
				writer.println(record);
			}
			writer.flush();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
