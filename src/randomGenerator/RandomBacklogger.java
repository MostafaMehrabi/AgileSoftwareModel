package randomGenerator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Main;

public class RandomBacklogger {
	private static int numberOfRandomePermutations = 10;
	private static int numberOfBacklogTasks = 400;
	private static List<Integer> indexList = new ArrayList<>();
	private static List<Integer> permutationList = new ArrayList<>();
	public static void main(String[] args) {
		generateRandomPermutations();
	}
	
	private static void initializeList() {
		indexList.clear();
		for (int i = 0; i < numberOfBacklogTasks; i++) {
			indexList.add(i);
		}
	}
	
	private static void generateRandomPermutations() {
		Random rand = new Random();
		for(int generationIteration = 1; generationIteration <= numberOfRandomePermutations; generationIteration++) {
			initializeList();
			permute(generationIteration, rand);
			recordPermutationFile(generationIteration);
		}
	}
	
	private static void permute(int generationIteration, Random rand) {
		String numberPostFix = "";
		switch (generationIteration) {
			case 1:
				numberPostFix = "st";
				break;
			case 2:
				numberPostFix = "nd";
				break;
			case 3:
				numberPostFix = "rd";
				break;
			default:
				numberPostFix = "th";
				break;
		}
		
		System.out.println("Processing the " + generationIteration + numberPostFix + " permutation");
		permutationList.clear();
		while(!indexList.isEmpty()) {
			int randomIndex = rand.nextInt(indexList.size());
			int randomNumber = indexList.remove(randomIndex);
			permutationList.add(randomNumber);
		}
	}
	
	private static void recordPermutationFile(int generationIteration) {
		String fileName = Main.getBaseDirectoryPath() + File.separator + Main.getPermutationFileName() + generationIteration;
		try {
			PrintWriter writer = new PrintWriter(fileName);
			for(int taskIndex : permutationList) {
				writer.println(taskIndex);
			}
			writer.flush();
			writer.close();
		}catch(IOException e) {
			System.err.println("Problem encountered while trying to create file " + fileName + " for random permutations.");
			e.printStackTrace();
		}
	}
}
