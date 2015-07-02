package com.aaronmroth.digitalreasoning.model;

import com.aaronmroth.digitalreasoning.xml.XMLizable;

import static com.aaronmroth.digitalreasoning.xml.XMLUtils.*;

public class Token implements XMLizable {
	
	public Token(String text) {
		this.text = text;
	}
	
	public String text;
	
	public boolean isProperNoun;
	
	public Token asProperNoun() {
		this.isProperNoun = true;
		return this;
	}

	@Override
	public String toXML() {
		return createTag("token", 
					createTag("text", text) +
					createTag("isProperNoun", isProperNoun) 
				);
	}
	
}
