package com.aaronmroth.digitalreasoning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class IOUtils {
	
	private static final String RESOURCES_DIR = "src/main/resources/";
	
	public static String readTextFile(String fileName) throws IOException {
	    String content = new String(
	    		Files.readAllBytes(
	    				Paths.get(RESOURCES_DIR + fileName)));
	    return content;
	}
	
	public static List<String> readTextFileByLines(String fileName) throws IOException {
	    List<String> lines = Files.readAllLines(
	    		Paths.get(RESOURCES_DIR + fileName));
	    return lines;
	}
	
	public static void writeToTextFile(String fileName, String content) throws IOException {
		deleteFile(fileName);
	    Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
	}
	
	public static void deleteFile(String fileName) throws IOException {
		try {
			Files.delete(Paths.get(fileName));
		} catch (NoSuchFileException e) {
			// if there is no file, it does not need to be deleted
		}
	}

}
