package com.aaronmroth.digitalreasoning.tokenize;

import java.math.BigInteger;
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

import static com.aaronmroth.digitalreasoning.utils.StringUtils.*;

public class Tokenizer {
	
	private static ThreadLocal<BigInteger> locations = new ThreadLocal<BigInteger>();
	
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
		Pattern sentence_breaker = Pattern.compile("(.+?(\\.|\\?|!|$)['\"]*)(\\W+[A-Z0-9]|\\W*$)", Pattern.DOTALL);
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
			processFromCursorToProperNoun(sentence, cursor, pnLocation);
			cursor = processProperNoun(sentence, pnLocation);
		}
		// add any words after last proper noun
		addWordsToSentence(sentence, raw_sentence.substring(cursor));
		return sentence;
	}

	/* The map returned: <index of PN in sentence, length of PN> */ 
	private static TreeMap<Integer, Integer> getProperNounLocations(String raw_sentence) {
		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		locations.set(new BigInteger("0")); //tracks found PN's, 1 bit per char
		Map<String, Integer> properNounLengths = NamedEntityManager.instance().getProperNounInfo();
		for (String properNoun : NamedEntityManager.instance().getProperNounList()) {
			int properNounLength = properNounLengths.get(properNoun); //TODO remove?
			addAllOccurrences(map, properNoun, properNounLength, raw_sentence);
		}
		locations.set(null);
		return map;
	}
	
	private static void addAllOccurrences(TreeMap<Integer, Integer> map, String properNoun, int properNounLength, String raw_sentence) {
		for (int index = raw_sentence.indexOf(properNoun);
				index >= 0; 
				index = raw_sentence.indexOf(properNoun, index + properNounLength)) {
			boolean isProperNoun = addProperNounIfValid(index, properNounLength, raw_sentence, map);
			if (isProperNoun) {
				System.out.println(properNoun);
			}
		}
	}
	
	/* returns true if valid and added, false otherwise */
	private static boolean addProperNounIfValid(int index, int properNounLength, String raw_sentence, TreeMap<Integer, Integer> map) {
		int sentenceLength = raw_sentence.length();
		if (!isStandalonePhrase(index, properNounLength, raw_sentence, sentenceLength)) {
			return false;
		}
		BigInteger pnMask = bitmaskForSection(index, properNounLength);
		if (currentOverlapsPreviousPN(pnMask)) {
			return false;
		}
		properNounLength = setProperNounLength(index, properNounLength, raw_sentence, sentenceLength);
		locations.set(locations.get().or(pnMask));
		map.put(index, properNounLength);
		return true;
	}
	
	private static boolean isStandalonePhrase(int index, int properNounLength, String raw_sentence, int sentenceLength) {
		// standalone phrase, i.e. Sun vs Suntan
		return (index == 0 || isNotALetter(raw_sentence.charAt(index-1))) &&
				(index + properNounLength >= sentenceLength 
				|| isNotALetter(raw_sentence.charAt(index + properNounLength)));
	}
	
	private static boolean currentOverlapsPreviousPN(BigInteger current) {
		return (locations.get().and(current).bitCount() > 0);
	}
	
	private static int setProperNounLength(int index, int properNounLength, String raw_sentence, int sentenceLength) {
		// account for possessive 's
		if (index + properNounLength + 2 < sentenceLength && 
				raw_sentence.substring(index + properNounLength, 
				index + properNounLength + 2).equals("'s")) {
			properNounLength += 2;
		}
		return properNounLength;
	}
	
	private static boolean isNotALetter(char c) {
		return !Character.isLetter(c);
	}
	
	private static BigInteger bitmaskForSection(int start, int length) {
		return new BigInteger(repeatedString("1", length)
				+ repeatedString("0", start), 2);
	}
	
	private static void processFromCursorToProperNoun(Sentence sentence, int cursor, Map.Entry<Integer, Integer> pnLocation) {
		int pnIndex = pnLocation.getKey();
		if (cursor < pnIndex) {
			addWordsToSentence(sentence, sentence.raw.substring(cursor, pnIndex));
		}
	}
	
	private static int processProperNoun(Sentence sentence,  Map.Entry<Integer, Integer> pnLocation) {
		int pnIndex = pnLocation.getKey();
		int pnLength = pnLocation.getValue();
		sentence.addToken(
				(new Token(sentence.raw.substring(pnIndex, pnIndex + pnLength))
					.asProperNoun()));
		return pnIndex + pnLength; // return new cursor location
	}
	
	private static void addWordsToSentence(Sentence sentence, String text) {
		List<String> words = getWords(text);
		for (String word : words) {
			if (word.matches(".*\\w.*")) {
				sentence.addToken(new Token(word));
			}
		}
	}
	
	private static List<String> getWords(String raw_sentence) {
		List<String> words = Arrays.asList(raw_sentence.split("[^-.'\\w]"));
		int last = words.size() - 1;
		if (last >= 0) {
			words.set(last, words.get(last).replaceAll("\\.$", ""));
		}
		return words;
	}
	
}
