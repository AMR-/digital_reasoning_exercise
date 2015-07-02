package com.aaronmroth.digitalreasoning.tokenize;

import com.aaronmroth.digitalreasoning.model.Corpus;
import com.aaronmroth.digitalreasoning.model.OutputBuilder;

public class TokenRunner implements Runnable {
	
	private String raw_corpus;
	
	private Corpus structured_corpus = null;

	public TokenRunner(String raw_corpus) {
		this.raw_corpus = raw_corpus;
	}

	@Override
	public void run() {
		structured_corpus = Tokenizer.tokenize(raw_corpus);
		OutputBuilder.instance().add(structured_corpus.toXML());
	}
	
}
