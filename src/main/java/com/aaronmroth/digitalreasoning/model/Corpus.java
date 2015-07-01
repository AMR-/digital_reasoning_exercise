package com.aaronmroth.digitalreasoning.model;

import java.util.ArrayList;
import java.util.List;

import com.aaronmroth.digitalreasoning.xml.XMLizable;

import static com.aaronmroth.digitalreasoning.xml.XMLUtils.*;

public class Corpus implements XMLizable {
	
	public Corpus() {}
	
	public List<Sentence> sentences = new ArrayList<Sentence>();
	
	public void addSentence(Sentence sentence) {
		sentences.add(sentence);
	}

	@Override
	public String toXML() {
		return createTag("corpus", sentences);
	}

}
