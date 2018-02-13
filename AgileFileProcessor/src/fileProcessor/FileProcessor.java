package fileProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import components.Expertise;
import core.Main;

public class FileProcessor {
	private String fileName;
	private Map<Integer, Expertise> result;
	private int samplingRate, maxHour;
	private Expertise baseExpertise;
	public FileProcessor(String fileName) {
		this.fileName = fileName;
		this.result = new TreeMap<>();
		this.samplingRate = Main.getSamplingRate();
		this.maxHour = Main.getMaxHour();
		this.baseExpertise = null;
	}
	
	private BufferedReader getBufferedReader() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
		}catch(IOException e) {
			System.err.println("Error occurred while trying to read file " + fileName);
			e.printStackTrace();
		}
		return reader;
	}
	
	private void processFile(){
		int sampleHour = 0;
		Expertise expertise = null;
		BufferedReader reader = getBufferedReader();
		boolean baseExpertiseRecorder = false;
		String line = null;
		try {
			//the first two lines are not interesting for us!
			line = reader.readLine();
			if(line.equals("sep=,"))
				line = reader.readLine();
			while((line = reader.readLine()) != null) {
				String[] elements = line.split(",|;");
				int modelTime = Integer.parseInt(elements[2]);
				expertise = new Expertise(elements[6], elements[7], elements[8]);
				if(!baseExpertiseRecorder) {
					baseExpertise = new Expertise(elements[6], elements[7], elements[8]);
					baseExpertiseRecorder = true;
				}
				int div = modelTime / samplingRate;
				sampleHour = div * samplingRate;
				result.put(sampleHour, expertise);
			}	
			while(sampleHour < maxHour) {
				sampleHour += samplingRate;
				result.put(sampleHour, expertise);
			}
			reader.close();
		} catch (Exception e) {
			System.err.println("Error occurred while trying to close file reader for file " + fileName);
			System.err.println("record being read: " + line);
			e.printStackTrace();
		}
	}
	
	private void fillTheGaps() {
		Expertise lastExpertise = baseExpertise;
		for (int sampleHour = 0; sampleHour <= maxHour; sampleHour += samplingRate) {
			if(result.containsKey(sampleHour)) {
				lastExpertise = result.get(sampleHour);
			}else {
				result.put(sampleHour,  lastExpertise);
			}
		}		
	}
	
	public Map<Integer, Expertise> process(){
		processFile();
		fillTheGaps();
		return result;
	}
}
