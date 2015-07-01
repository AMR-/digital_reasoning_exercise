package com.aaronmroth.digitalreasoning;

import java.io.IOException;

import com.aaronmroth.digitalreasoning.model.Corpus;
import com.aaronmroth.digitalreasoning.tokenize.Tokenizer;

import static com.aaronmroth.digitalreasoning.IOUtils.*;

public class Main {

	//TODO move to properties
	public static final String corpus_location = "NLP_test/nlp_data.txt";
	public static final String output_name = "output.txt";
	
	public static void main(String... args) throws IOException {
		String raw_corpus = readTextFile(corpus_location);

		Corpus corpus = Tokenizer.tokenize(raw_corpus);
		
		writeToTextFile(output_name, corpus.toXML());
	}

}
