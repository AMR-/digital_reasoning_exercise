package com.aaronmroth.digitalreasoning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aaronmroth.digitalreasoning.model.OutputBuilder;
import com.aaronmroth.digitalreasoning.tokenize.TokenRunner;
import com.aaronmroth.digitalreasoning.utils.PropertiesManager;

import static com.aaronmroth.digitalreasoning.utils.IOUtils.*;

public class Main {

	public static final boolean USE_ZIP =
			Boolean.parseBoolean(PropertiesManager.instance().get("use_zip")); 
	public static final String CORPUS_LOCATION =
			PropertiesManager.instance().get("corpus_location");
	public static final String ZIP_LOCATION = 
			PropertiesManager.instance().get("zip_location");
	
	public static void main(String... args) throws IOException {
		List<String> raw_corpuses = getRawCorpuses();
		tokenize(raw_corpuses);
		OutputBuilder.instance().writeToFile();
	}
	
	private static List<String> getRawCorpuses() throws IOException {
		List<String> raw_corpuses;
		if (USE_ZIP) {
			raw_corpuses = readMultipleFromZip(ZIP_LOCATION);
		} else {
			raw_corpuses = new ArrayList<String>();
			raw_corpuses.add(readTextFile(CORPUS_LOCATION));
		}
		return raw_corpuses;
	}
	
	private static void tokenize(List<String> raw_corpuses) {
		System.out.println("The following named entities were found (multiple occurrences listed multiple times): ");
		ExecutorService executor = Executors.newFixedThreadPool(4);
		for (String raw_corpus : raw_corpuses) {
			Runnable tokenRunner = new TokenRunner(raw_corpus);
			executor.execute(tokenRunner);
		}
		executor.shutdown();
		while (!executor.isTerminated()) { }
	}

}
