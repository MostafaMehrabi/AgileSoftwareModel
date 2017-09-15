package core;

import java.io.File;

public class SystemLoader {
	public static void loadSystem(){
		String baseDirPath = "." + File.separator + "Files";
		loadSystem(baseDirPath);
	}
	
	public static void loadSystem(String baseDirPath){
		File baseDirectory = new File(baseDirPath);
		if(!baseDirectory.exists()){
			if(!baseDirectory.mkdirs()){
				throw new RuntimeException("System was not able to create one or more directory/directories in path: " + baseDirPath);
			}
		}
		
	}
}
