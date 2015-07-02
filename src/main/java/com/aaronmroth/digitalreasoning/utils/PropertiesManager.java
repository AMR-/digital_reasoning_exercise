package com.aaronmroth.digitalreasoning.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
	
	private static PropertiesManager instance;
	
	public static PropertiesManager instance() {
        if (instance == null) {
            synchronized(PropertiesManager.class) {
                if (instance == null) {
                    instance = new PropertiesManager();
                }
            }
        }
        return instance;
    }
	
	private Properties properties;
	
	public PropertiesManager() {
		properties = new Properties();
		loadProperties();
	}
	
	private void loadProperties() {
		try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String get(String name) {
		return properties.getProperty(name);
	}

}
