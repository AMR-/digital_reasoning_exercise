package com.aaronmroth.digitalreasoning.tokenize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aaronmroth.digitalreasoning.model.Corpus;
import com.aaronmroth.digitalreasoning.model.Sentence;
import com.aaronmroth.digitalreasoning.model.Token;

public class Tokenizer {
	
	public static final Corpus tokenize(String raw_corpus) {
		List<String> sentences = getSentences(raw_corpus);
		Corpus corpus = new Corpus();
		System.out.println("The following named entities were found (multiple occurrences listed multiple times): ");
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
		Sentence sentence = new Sentence(raw_sentence);
		TreeMap<Integer, Integer> properNounLocations = getProperNounLocations(raw_sentence);
		int cursor = 0;
		for (Map.Entry<Integer, Integer> pnLocation = properNounLocations.firstEntry();
				pnLocation != null;
				properNounLocations.remove(pnLocation.getKey()), pnLocation = properNounLocations.firstEntry()) {
			// do from cursor to index
			int pnIndex = pnLocation.getKey();
			if (cursor < pnIndex) {
				addWordsToSentence(sentence, raw_sentence.substring(cursor, pnIndex));
			}
			// get proper noun
			int pnLength = pnLocation.getValue();
			sentence.addToken(
					(new Token(raw_sentence.substring(pnIndex, pnIndex + pnLength))
						.asProperNoun()));
			cursor = pnIndex + pnLength;
		}
		// add any words after last proper noun
		addWordsToSentence(sentence, raw_sentence.substring(cursor));
		return sentence;
	}
	
	/* <index, length> */
	private static TreeMap<Integer, Integer> getProperNounLocations(String raw_sentence) {
		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		Map<String, Integer> properNounLengths = NamedEntityManager.getProperNounInfo();
		int sentenceLength = raw_sentence.length();
		for (String properNoun : NamedEntityManager.getProperNounList()) {
			int properNounLength = properNounLengths.get(properNoun);
			for (int index = raw_sentence.indexOf(properNoun);
					index >= 0; 
					index = raw_sentence.indexOf(properNoun, index + properNounLength)) {
				if ((index == 0 || isNotALetter(raw_sentence.charAt(index-1))) &&
						(index + properNounLength >= sentenceLength 
								|| isNotALetter(raw_sentence.charAt(index + properNounLength)))) {
					System.out.println(properNoun);
					map.put(index, properNounLengths.get(properNoun));
				}
			}
		}
		return map;
	}
	
	private static boolean isNotALetter(char c) {
		return !Character.isLetter(c);
	}
	
	private static void addWordsToSentence(Sentence sentence, String text) {
		List<String> words = getWords(text);
		for (String word : words) {
			if (!word.trim().equals("")) {
				sentence.addToken(new Token(word));
			}
		}
	}
	
	private static List<String> getWords(String raw_sentence) {
		List<String> words = Arrays.asList(raw_sentence.split("\\W"));
		return words;
	}
	
}
