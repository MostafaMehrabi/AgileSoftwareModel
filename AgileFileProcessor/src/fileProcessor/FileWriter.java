package fileProcessor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import components.Expertise;

public class FileWriter {
	private String fileName; 
	private Map<Integer, Expertise> result;
	public FileWriter(String fileName, Map<Integer, Expertise> result) {
		this.fileName = fileName;
		this.result = result;
	}
	
	private PrintWriter getPrintWriter() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName);
			writer.println("sep=,");
			writer.println("ModelTime, BackEnd, FrontEnd, Design");
		}catch(IOException e) {
			System.err.println("Error occurred while trying to open a print writer for file: " + fileName);
			e.printStackTrace();
		}
		return writer;
	}
	
	public void writeToFile() {
		PrintWriter writer = getPrintWriter();
		try {
			for(Entry<Integer, Expertise> entry : result.entrySet()) {
				int modelTime = entry.getKey();
				Expertise expertise = entry.getValue();
				writer.println(Integer.toString(modelTime) + "," + expertise.getBackEnd() + "," + expertise.getFrontEnd() + "," + expertise.getDesign());
			}
			writer.close();
		}catch(Exception e) {
			System.err.println("Error occurred while trying to write to file: " + fileName);
			e.printStackTrace();
		}
	}
}
