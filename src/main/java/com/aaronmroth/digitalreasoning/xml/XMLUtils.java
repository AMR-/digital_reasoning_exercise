package com.aaronmroth.digitalreasoning.xml;

import java.util.List;

public class XMLUtils {
	
	public static final String createTag(String name, boolean value) {
		return "<" + name + ">" + Boolean.toString(value)
				+ "</" + name + ">";
	}

	public static final String createTag(String name, String value) {
		return "<" + name + ">" + value + "</" + name + ">";
	}
	
	public static final String createTag(String name, List<? extends XMLizable> list) {
		StringBuffer sb = new StringBuffer();
		for (XMLizable element : list) {
			sb.append(element.toXML());
		}
		return "<" + name + ">" + sb.toString() + "</" + name + ">";
	}

}
