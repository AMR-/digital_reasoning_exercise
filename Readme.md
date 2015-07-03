Aaron Roth Digital Reasoning Remote Programming Exercise
========================================================

Running the Program
-------------------

The main class is com.aaronmroth.digitalreasoning.Main.java

Assumptions
-----------

* Configuration parameters are set appropriately (may throw Exceptions otherwise)
* The total number of corpuses in a zip to process can be loaded into memory as a List of Strings (i.e. there is enough memory to store all text at one time)
* All sentences end with either a period, question mark, or exclamation point, or one of those characters followed by a single or double quotation mark.  The only exception is end-of-file, which can also end a sentence.
* All sentences begin with a capital letter or number
* A sentence-ending character followed by non-alphanumerics characters followed by a sentence-beginning character denotes a sentence break (note: this assumption has exceptions in the real world, see Limitations)
* There can be punctuation characters and whitespace between sentences
* All words and proper nouns have spaces between them
* If a phrase or word is found that is a string matching the string of a proper noun with _'s_ appended, that whole string should be considered a proper noun
* Every word contains at least one alphanumeric character
* Words are comprised of alphanumeric characters, periods, apostrophes, and dashes.  Any other character should be considered a word boundary
* If a word is the last word in a sentence, it does not end in a period (note: obviously this assumption is incorrect (certain acronyms and abbreviations).  It is true in the majority of cases, but not all, and should be fixed in future version, but even as it stands now should not provide a huge barrier to further processing)
* There may be other implicit assumptions in the regex that cause it to not properly handle characters that are not usually found in the english language such as letters with diacritical marks

Limitations
-----------

* Any time the assumptions are violated, the text will may parsed incorrectly.
* If there would be an abbreviation or acronym that ended with a period in the middle of a sentence followed by a word that started with a capital letter such as "I" or a proper noun, it would mistakenly be treated as a sentence break.  (This situation does not arise in the training set but could arise in further texts.)
* Abbreviations (that end with a period) at the end of a sentence become tokens without a period while abbreviations in the middle of a sentence become tokens that include the period.  Ultimately, I feel that the optimal case would be to include the period when included in the text since it communicates information about the word.  This is a limtiation to resolve.  At the same time, if there is an intent to parse acronyms and abbreviations, such parsing would need to account for the fact that many acronyms and abbreviations can be spelled with or without periods.  Whether the original did include periods or not may be relevant though, and maintaining that information would be desirable.
* single-quotes on a word due to quotation vs single-quotes due to possession are not currently distinguished, both are included
* The current method of finding named entities matches based on an existing list.  This method is costly to update and maintain, and is imprecise as well.  E.g. matches "Europe" but not "European" (it would also be improper to instruct the algorithm to assume that an elongation of a proper noun is also a proper noun)
* If the source corpus or set of source corpuses are comprised of text that is larger than what can be stored in memory at a single time, the program will not be able to process it, and would need to be updated to accommodate such tasks.

Other approaches
----------------

* Instead of breaking sentences and words by patterns on their break, could iterate word by word, phrase by phrase (perhaps limited to anytime a period was encountered), and attempt to determine through more complicated rules whether the sentence was ended or not.  My instinct says this would be slower and would not produce better results. Although it would make it easier to resolve the "acronmyn-with-periods-followed-by-word-starting-with-capital-letter-in-middle-of-sentence" issue.  It could be resolved with regexes still, but at a certain point regexes become less readable so it may also depend on the desired tradeoff between readability and speed
* My current method of identifying proper noun tokens is to identify where they occur in the sentence string, and only then iterate over the string, tokenizing non-proper-noun sections based on word boundaries and proper-noun sections as proper nouns.   A different approach (similar to previous suggestion) would be to iterate word by word over the sentence without looking ahead.  If a word could be a proper noun or could be part of a proper noun, check if it is or not, and then tokenize the word or phrase appropriately.   Ultimately this seemed unnecessarily expensive and cumbersome.  (I could be wrong about it being more expensive, might be worth testing or investigating a bit.)
* Some proper nouns are strings of characters which are supersets of other proper nouns.  Given my approach, it is necessary to avoid double-counting a proper noun when this occurs.  My approach is to start with the longest proper nouns first.  For a sentence string, I keep track of which characters have already been identified as part of a proper noun.  For each proper noun I check if the range where it lies overlaps with a previously found proper noun, and if so I do not record it.   My approach to performing this check is to record previously found proper nouns in the form of a bitmask (one bit per character), and turn the current found proper noun into a bitmask representing it's location, and compare.  An alternatively approach would be to iterate through the map that I am already building, and compare.  I figured that the bitmask approach would be faster, since it does not incur the cost of an additional loop.
* An alternative approach to identifying proper nouns would be to attempt to identify them by a series of rules, as opposed to a hard coded list.  It could be based on things such as capital letters, identification of acronyms, and position in sentence.
* Another approach to identifying proper nouns would be to use machine learning techniques.  You could train a system by telling it what should be tokenized as a proper noun for a given set of corpuses, and seeing if the system was able to determine the means of making this determination to a certain accuracy on new texts
