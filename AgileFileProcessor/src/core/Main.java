package core;

import java.io.File;
import java.util.Map;

import components.Expertise;
import fileProcessor.FileProcessor;
import fileProcessor.FileWriter;

public class Main {
	private static String foldersPath = "." +File.separator+ "Files";
	private static String rawDataFolderPath = foldersPath+File.separator+"RawData";
	private static String resultsDataFolderPath = foldersPath + File.separator + "Results"; 
	private static String scenariosPrefix = "Scenario";
	private static String personnelPrefix = "Personnel_";
	private static int numOfSprints = 25;
	private static int sprintDuration = 80;
	private static int samplingRate = 20;
	private static int numberOfScenarios = 5;
	private static int numberOfPersonnel = 7;
	public static void main(String[] args) {
		createResultsFolders();
		startProcess();
		System.out.println("Done");
	}
	
	private static void createResultsFolders() {
		for(int i = 1; i <= 2; i++) {
			String approach = "";
			if(i == 1)
				approach = "ExpertiseBased";
			else
				approach = "LearningBased";
			for(int folderIndex = 1; folderIndex <= numberOfScenarios; folderIndex++) {
				String folderName = resultsDataFolderPath + File.separator + scenariosPrefix + folderIndex + File.separator + approach;
				File folder = new File(folderName);
				if(!folder.exists())
					folder.mkdirs();
			}
		}
	}
	
	private static void startProcess() {
		for(int folderIndex = 1; folderIndex <= numberOfScenarios; folderIndex++) {
			for(int i = 1; i <= 2; i++) {
				String approach = "";
				if (i == 1)
					approach = "ExpertiseBased";
				else 
					approach = "LearningBased";
				
				String folderName = scenariosPrefix + folderIndex;
				String sourceFolderPath = rawDataFolderPath + File.separator + folderName + File.separator + approach;
				String resultFolderPath = resultsDataFolderPath + File.separator + folderName + File.separator + approach;
				for(int personnelIndex = 1; personnelIndex <= numberOfPersonnel; personnelIndex++) {
					if(personnelIndex != 5) {
						String fileName = personnelPrefix + personnelIndex + ".csv";
						String sourceFile = sourceFolderPath + File.separator + fileName;
						String resultFile = resultFolderPath + File.separator + fileName;
						FileProcessor processor = new FileProcessor(sourceFile);
						Map<Integer, Expertise> result = processor.process();
						FileWriter writer = new FileWriter(resultFile, result);
						writer.writeToFile();
					}
				}
			}
		}		
	}
	
	public static int getSamplingRate() {
		return samplingRate;
	}
	public static int getMaxHour() {
		return numOfSprints*sprintDuration;
	}
}
