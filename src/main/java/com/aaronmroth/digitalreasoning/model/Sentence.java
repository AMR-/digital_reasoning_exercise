package com.aaronmroth.digitalreasoning.model;

import java.util.ArrayList;
import java.util.List;

import com.aaronmroth.digitalreasoning.xml.XMLizable;

import static com.aaronmroth.digitalreasoning.xml.XMLUtils.*;

public class Sentence implements XMLizable {
	
	public Sentence(String raw) {
		this.raw = raw;
	}
	
	public String raw;
	
	public List<Token> tokens = new ArrayList<Token>();
	
	public void addToken(Token word) {
		tokens.add(word);
	}

	@Override
	public String toXML() {
		return createTag("sentence", 
					createTag("raw_text", raw) +
					createTag("tokens", tokens)
				);
	}

}
