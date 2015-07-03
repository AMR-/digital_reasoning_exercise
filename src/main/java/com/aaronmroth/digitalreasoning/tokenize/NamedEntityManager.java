package com.aaronmroth.digitalreasoning.tokenize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aaronmroth.digitalreasoning.utils.PropertiesManager;

import static com.aaronmroth.digitalreasoning.utils.IOUtils.*;

public class NamedEntityManager {
	
	private static final String NAMED_ENTITY_LOCATION = 
			PropertiesManager.instance().get("named_entity_location");
	
	private static NamedEntityManager instance;
	
	private List<String> properNouns = null;
	
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
		populateProperNounList();
	}
	
	public List<String> getProperNounList() {
		return properNouns;
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
