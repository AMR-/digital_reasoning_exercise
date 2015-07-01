package com.aaronmroth.digitalreasoning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class IOUtils {
	
	private static final String resourcesFolder = "src/main/resources/";
	
	public static String readTextFile(String fileName) throws IOException {
	    String content = new String(
	    		Files.readAllBytes(
	    				Paths.get(resourcesFolder + fileName)));
	    return content;
	}
	
	public static void writeToTextFile(String fileName, String content) throws IOException {
		deleteFile(fileName);
	    Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
	}
	
	public static void deleteFile(String fileName) throws IOException {
		Files.delete(Paths.get(fileName));
	}

}
