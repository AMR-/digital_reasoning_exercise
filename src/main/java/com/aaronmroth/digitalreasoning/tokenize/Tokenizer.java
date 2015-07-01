package com.aaronmroth.digitalreasoning.tokenize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aaronmroth.digitalreasoning.model.Corpus;
import com.aaronmroth.digitalreasoning.model.Sentence;
import com.aaronmroth.digitalreasoning.model.Word;

public class Tokenizer {
	
	public static final Corpus tokenize(String raw_corpus) {
		List<String> sentences = getSentences(raw_corpus);
		Corpus corpus = new Corpus();
		for (String raw_sentence : sentences) {
			corpus.addSentence(toStructuredSentence(raw_sentence));
		}
		return corpus;
	}
	
	private static List<String> getSentences(String raw_corpus) {
		List<String> sentences = new ArrayList<String>();
		Pattern sentence_breaker = Pattern.compile("(.+?(\\.|\\?|!)['\"]*)(\\W+[A-Z0-9]|\\W*$)");
		Matcher matcher = sentence_breaker.matcher(raw_corpus);
		String startOfSentence = "";
		while (matcher.find()) {
			sentences.add(startOfSentence + matcher.group(1));
			startOfSentence = matcher.group(3).trim();
		}
		return sentences;
	}
	
	private static Sentence toStructuredSentence(String raw_sentence) {
		List<String> words = getWords(raw_sentence);
		Sentence sentence = new Sentence(raw_sentence);
		for (String word : words) {
			if (!word.trim().equals("")) {
				sentence.addWord(new Word(word));
			}
		}
		return sentence;
	}
	
	private static List<String> getWords(String raw_sentence) {
		List<String> words = Arrays.asList(raw_sentence.split("\\W"));
		return words;
	}
	
}
