package com.aaronmroth.digitalreasoning.utils;

public class StringUtils {
	
	public static String repeatedString(String string, int repeats) {
        return (repeats == 0) ? "" : String.format("%0" + Integer.toString(repeats) + "d", 0).replace("0", string);
    }

}
