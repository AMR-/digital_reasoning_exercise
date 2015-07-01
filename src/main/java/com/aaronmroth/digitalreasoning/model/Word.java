package com.aaronmroth.digitalreasoning.model;

import com.aaronmroth.digitalreasoning.xml.XMLizable;

import static com.aaronmroth.digitalreasoning.xml.XMLUtils.*;

public class Word implements XMLizable {
	
	public Word(String text) {
		this.text = text;
	}
	
	public String text;
	
	//public boolean isProperNoun;

	@Override
	public String toXML() {
		return createTag("word", 
					createTag("text", text) /* +
					createTag("isProperNoun", isProperNoun) */
				);
	}
	
}
