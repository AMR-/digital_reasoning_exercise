package com.aaronmroth.digitalreasoning.tokenize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aaronmroth.digitalreasoning.IOUtils.*;

public class NamedEntityManager {
	
	//TODO add to properties
	private static final String NAMED_ENTITY_LOCATION = "NLP_test/NER.txt";
	
	private static List<String> properNouns = null;
	private static Map<String, Integer> properNounInfo = null;
	
	public static List<String> getProperNounList() {
		if (properNouns == null || properNounInfo == null) {
			populateProperNounInfo();
		}
		return properNouns;
	}

	public static Map<String, Integer> getProperNounInfo() {
		if (properNouns == null || properNounInfo == null) {
			populateProperNounInfo();
		}
		return properNounInfo;
	}
	
	private static void populateProperNounInfo() {
		populateProperNounList();
		properNounInfo = new HashMap<String, Integer>();
		for (String properNoun : properNouns) {
			properNounInfo.put(properNoun, properNoun.length());
		}
	}

	private static void populateProperNounList() {
		try {
			List<String> lines = readTextFileByLines(NAMED_ENTITY_LOCATION);
			properNouns = new ArrayList<String>();
			for (String line : lines) {
				if (!line.trim().equals("")) {
					properNouns.add(line);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not load Named Entities", e);
		}
	}

}
