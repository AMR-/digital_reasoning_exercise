package com.aaronmroth.digitalreasoning.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class IOUtils {
	
	private static final String RESOURCES_DIR =
			PropertiesManager.instance().get("resources_dir");
	
	/* Zip File IO */
	
	public static List<String> readMultipleFromZip(String filename) throws IOException {
		List<String> multipleFileContents = new ArrayList<String>();
		ZipFile zipFile = new ZipFile(RESOURCES_DIR + filename);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			InputStream stream = zipFile.getInputStream(entry);
			@SuppressWarnings("resource") // eclipse's warning is wrong, it gets closed
			Scanner scanner = new Scanner(stream).useDelimiter("\\A");
			if (scanner.hasNext()) {
				String contents = scanner.next();
				if (!contents.contains("Mac OS X") && // imperfectly address Mac bug
						!contents.matches(".*[A-Z0-9]{8}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{12}.*")) { 
					multipleFileContents.add(contents);
				}
			}
			scanner.close();
			stream.close();
		}
		zipFile.close();
		return multipleFileContents;
	}
	
	/* Single File IO */
	
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
