package com.aaronmroth.digitalreasoning.tokenize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaronmroth.digitalreasoning.utils.PropertiesManager;

import static com.aaronmroth.digitalreasoning.utils.IOUtils.*;

public class NamedEntityManager {
	
	private static final String NAMED_ENTITY_LOCATION = 
			PropertiesManager.instance().get("named_entity_location");
	
	private static NamedEntityManager instance;
	
	private List<String> properNouns = null;
	private Map<String, Integer> properNounInfo = null;
	
	public static NamedEntityManager instance() {
        if (instance == null) {
            synchronized(NamedEntityManager.class) {
                if (instance == null) {
                    instance = new NamedEntityManager();
                }
            }
        }
        return instance;
    }
	
	public NamedEntityManager() {
		populateProperNounInfo();
	}
	
	public List<String> getProperNounList() {
		return properNouns;
	}

	public Map<String, Integer> getProperNounInfo() {
		return properNounInfo;
	}
	
	private void populateProperNounInfo() {
		populateProperNounList();
		properNounInfo = new HashMap<String, Integer>();
		for (String properNoun : properNouns) {
			properNounInfo.put(properNoun, properNoun.length());
		}
	}

	private void populateProperNounList() {
		try {
			List<String> lines = readTextFileByLines(NAMED_ENTITY_LOCATION);
			properNouns = new ArrayList<String>();
			for (String line : lines) {
				if (!line.trim().equals("")) {
					properNouns.add(line);
				}
			}
			Collections.sort(properNouns, new StringLengthComparator());
		} catch (IOException e) {
			throw new RuntimeException("Could not load Named Entities", e);
		}
	}

}
