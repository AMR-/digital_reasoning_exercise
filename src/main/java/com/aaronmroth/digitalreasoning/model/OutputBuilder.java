package com.aaronmroth.digitalreasoning.model;

import static com.aaronmroth.digitalreasoning.utils.IOUtils.*;

import java.io.IOException;

import com.aaronmroth.digitalreasoning.utils.PropertiesManager;

public class OutputBuilder {

	public static final String OUTPUT_NAME = 
			PropertiesManager.instance().get("output_name");
	
	private static OutputBuilder instance;
	
	public static OutputBuilder instance() {
        if (instance == null) {
            synchronized(OutputBuilder.class) {
                if (instance == null) {
                    instance = new OutputBuilder();
                }
            }
        }
        return instance;
    }

	private StringBuffer output;
	
	public OutputBuilder() {
		output = new StringBuffer();
		output.append("<corpuses>");
	}
	
	synchronized public void add(String string) {
		output.append(string);
	}
	
	public void writeToFile() {
		output.append("</corpuses>");
		tryWriteToTextFile(OUTPUT_NAME, output.toString());
	}
	
	private void tryWriteToTextFile(String output_name, String contents) {
		try {
			writeToTextFile(output_name, contents);
		} catch (IOException e) {
			throw new RuntimeException("Unable to write to file.", e);
		}
	}

}
