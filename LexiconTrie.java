import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * A lexicon is defined as the vocabulary of a person, language, or branch of knowledge. More generally, 
 * it can be understood like a regular dictionary (i.e., a list of words), except that a lexicon contains 
 * only the words themselves (and not their definitions as well). The LexiconTrie class implements 
 * an interface for a Lexicon object. The trie data structure (which functions similar to a linkedList, 
 * in terms of nodes) is a letter-tree that efficiently stores strings. Each node in a trie represents a 
 * single letter. The root of the trie is ‘blank space’ and does not represent a letter. It also contains 
 * methods that can add(from file), remove, search, suggest, and match word.
 * 
 * @author Edwin Sanchez Huizar
 *
 */
public class LexiconTrie implements Lexicon{

	private LexiconNode root;

	private int size;

	/**
	 * The LexiconTrie constructor initializes a LexiconNode and stores it to the corresponding
	 * instance variable. The root Lexicon node is represented as a "blank space".
	 */
	public LexiconTrie() {
		root = new LexiconNode(' ');
		size = 0;
	}

	/**
	 * Adds the specified word to the lexicon. Should run in time proportional to
	 * the length of the word being added. Returns whether the lexicon was
	 * modified by adding the word.
	 * 
	 * @param word - The lowercase word to add to the lexicon.
	 * @return True if the word was added and false if the word was already part of the lexicon.
	 * 
	 */
	@Override
	public boolean addWord(String word) {

		String words = word.toLowerCase();
		if (containsWord(words)) {
			return false;
		}
		else {
			LexiconNode currNode = root;
			for (int i = 0; i < words.length(); i++) {
				char letter = words.charAt(i);
				LexiconNode newNode = new LexiconNode(letter);
				if (currNode.getChild(letter) == null) {
					currNode.addChild(newNode);
					currNode = newNode;				
				}
				else {
					currNode = currNode.getChild(letter);
				}
			}
			if (!currNode.isWord()) {
				currNode.makeWord(); 
				size++;
			}
		}
		return true;
	}

	/**
	 * Reads the words contained in the specified file and adds them to the
	 * lexicon. The format of the given file is expected to be one word per line
	 * of the file. All words should be converted to lowercase before adding.
	 * Returns the number of new words added, or -1 if the file could not be read.
	 * 
	 * @param filename - The name of the file to read.
	 * @return The number of new words added, or -1 if the file could not be read.
	 */
	@Override
	public int addWordsFromFile(String filename) {

		int words = 0;
		Scanner scan;
		File parser = new File("words/" + filename.toLowerCase() + ".txt");
		try {
			scan = new Scanner(parser);
		}
		catch (Exception e) {
			return -1;
		}
		while (scan.hasNext()) {
			addWord(scan.next());
			words++; 
		}
		scan.close();
		return words;
	}

	/**
	 * Attempts to remove the specified word from the lexicon. If the word appears
	 * in the lexicon, it is removed and true is returned. If the word does not
	 * appear in the lexicon, the lexicon is unchanged and false is returned.
	 * Should run in time proportional to the length of the word being removed. It
	 * is implementation-dependent whether unneeded prefixes as a result of
	 * removing the word are also removed from the lexicon.
	 * 
	 * @param word - The lowercase word to remove from the lexicon.
	 * @return Whether the word was removed.
	 */
	@Override
	public boolean removeWord(String word) {

		if (!containsWord(word)){
			return false;
		}
		LexiconNode curr = root;
		size--;
		//calls the auxiliary method
		return removeAux(curr, word, 0);
	}

	/**
	 * The removeAux method recursively deletes the non-word nodes that correspond
	 * to the word that is to be deleted. This method assumes that the word is contained
	 * within the Trie, otherwise, the removeWord method would return false.
	 * 
	 * @param curr - the current Lexicon Node
	 * @param word - The lowercase word to remove from the lexicon.
	 * @param idx - the index used to reach base case.
	 * @return
	 */
	public boolean removeAux(LexiconNode curr, String word, int idx) {

		//checks that the index is equal to the word length.
		if (idx == word.length()) {
			// if curr is word, then it makes isWord = false using the ejectWord method.
			if (!curr.isWord()) {
				return false;
			}
			curr.ejectWord();
			return curr.isEmpty();
		}

		char c = word.charAt(idx);
		LexiconNode node = curr.getChild(c);
		//deletes nodes that are not part of another word (takes care of prefix), recursively
		if (removeAux(node, word, idx + 1 ) && !node.isWord()) {
			curr.removeChild(curr.getChild(c));
			return curr.isEmpty();
		}
		return true;
	}

	/**
	 * Returns the number of words contained in the lexicon. Should run in
	 * constant time.
	 * 
	 * @return The number of words in the lexicon.
	 */
	@Override
	public int numWords() {
		return size;
	}

	/**
	 * The search method is a helper method for the containsWord and containsPrefix 
	 * method. The search method traces out the path of a word/prefix letter by letter.
	 * 
	 * @param search - the word or prefix.
	 * @return null if child is not contained, lexiconNode if all corresponding children
	 *  are contained.
	 */
	public LexiconNode search(String search) {

		String str = search.toLowerCase();
		LexiconNode currNode = root;
		for (int i = 0; i < str.length(); i++) {
			if (currNode.getChild(str.charAt(i)) == null) {
				return null;
			}
			else {
				//updates currNode, going done letter by letter
				currNode = currNode.getChild(str.charAt(i));
			}
		}
		return currNode;
	}

	/**
	 * Checks whether the given word exists in the lexicon. Should run in time
	 * proportional to the length of the word being looked up.
	 * 
	 * @param word - The lowercase word to lookup in the lexicon.
	 * @return Whether the given word exists in the lexicon.
	 */
	@Override
	public boolean containsWord(String word) {
		if (search(word) == null)  {
			return false;
		}
		return search(word).isWord();
	}

	/**
	 * Checks whether any words in the lexicon begin with the specified prefix. A
	 * word is defined to be a prefix of itself, and the empty string is defined
	 * to be a prefix of everything. Should run in time proportional to the length
	 * of the prefix.
	 * 
	 * @param prefix - The lowercase prefix to lookup in the lexicon.
	 * @return Whether the given prefix exists in the lexicon.
	 */
	@Override
	public boolean containsPrefix(String prefix) {
		if (search(prefix) == null)  {
			return false;
		}
		return search(prefix).isWord() || true;
	}

	/**
	 * Returns an iterator over all words contained in the lexicon. The iterator
	 * should return words in the lexicon in alphabetical order.
	 */
	@Override
	public Iterator<String> iterator() {
		List<String> words = new ArrayList<String>();
		root.getWords("", words);
		return words.iterator();
	}

	/**
	 * The suggestCorrections method calls on its helper function, suggestCorrectionsAux to 
	 * return a set of words in the lexicon that are suggested corrections for a
	 * given (possibly misspelled) word. It passes in the root node of the Trie, an empty prefix, and
	 * an empty set in which the recursive function fills and returns back. 
	 * 
	 * @param target -  The target word to be corrected.
	 * @param maxDistance - The maximum word distance of suggested corrections.
	 * @return A set of all suggested corrections within maxDistance of the target
	 * word.
	 */
	@Override
	public Set<String> suggestCorrections(String target, int maxDistance) {
		LexiconNode currNode = root;
		String prefix = "";
		Set<String> suggestList = new HashSet<String>();
		return suggestCorrectionsAux(target, maxDistance, prefix, suggestList, currNode);
	}

	/**
	 * The suggestCorrectionsAux is the auxiliary method for the suggestCorrections method. 
	 * Returns a set of words in the lexicon that are suggested corrections for a
	 * given (possibly misspelled) word. Suggestions will include all words in the
	 * lexicon that are at most maxDistance distance from the target word, where
	 * the distance between two words is defined as the number of character
	 * positions in which the words differ. Should run in time proportional to the
	 * length of the target word.
	 * 
	 * @param target - The target word to be corrected.
	 * @param maxDistance - The maximum word distance of suggested corrections.
	 * @param prefix - a String containing all the letters form the top of the tree down to 
	 * the current Node.
	 * @param suggestList - empty set, that is to be filled with suggestions. 
	 * @param currNode - root node that is traversed through the recursion
	 * @return A set of all suggested corrections within maxDistance of the target
	 * word.
	 */
	public Set<String> suggestCorrectionsAux(String target, int maxDistance, String prefix, Set<String> suggestList, LexiconNode currNode) {
		if(target.length() == prefix.length() && currNode.isWord()) {
			suggestList.add(prefix);
			return suggestList;
		}
		else if(target.length() == prefix.length() && !currNode.isWord()) {
			return suggestList;
		}
		else {
			for(LexiconNode child: currNode) {
				char l = child.getChar();
				if(l == target.charAt(prefix.length())) {
					suggestCorrectionsAux(target, maxDistance, prefix + l, suggestList, child);
				}
				else if(l != target.charAt(prefix.length()) && maxDistance == 0) {
					suggestCorrectionsAux(target, maxDistance, prefix, suggestList, child);
				}
				else if(l != target.charAt(prefix.length()) && maxDistance > 0){
					suggestCorrectionsAux(target, maxDistance - 1 , prefix + l, suggestList, child);
				}
			}
		}
		return suggestList;
	}

	/**
	 * The matchRegex method calls on its help function, matchRegexAux, to return a set of all 
	 * words in the lexicon that match the given regular expression pattern. The regular expression 
	 * pattern may contain only letters and wild-card characters '*', '?', and '_'.
	 * 
	 * @param pattern - The regular expression pattern to match.
	 * @return A set of all words in the lexicon matching the pattern.
	 */
	@Override
	public Set<String> matchRegex(String pattern) {
		LexiconNode currNode = root;
		Set<String> matchList = new HashSet<String>();
		String prefix = "";
		return matchRegexAux(pattern, matchList, prefix, currNode);
	}

	/**
	 * The matchRegexAux is the auxiliary method for the matchRegex method. It returns a set of all words in 
	 * the lexicon that match the given regular expression pattern. The regular expression pattern may contain only letters
	 * and wild-card characters '*', '?', and '_'.
	 * 
	 * @param pattern - The regular expression pattern to match.
	 * @param matchList - empty set, that is to be filled with suggestions.
	 * @param prefix - a String containing all the letters form the top of the tree down to 
	 * the current Node.
	 * @param currNode - root node that is traversed through the recursion
	 * @return A set of all words in the lexicon matching the pattern.
	 */
	public Set<String> matchRegexAux(String pattern, Set<String> matchList, String prefix, LexiconNode currNode){
		if(pattern.length()== 0 && currNode.isWord()) {
			matchList.add(prefix);
			return matchList;
		}
		else if(pattern.length()== 0 && !currNode.isWord()) {
			return matchList;
		}
		else {
			char c = pattern.charAt(0);
			if(c == '?' || c == '*'){
				matchRegexAux(pattern.substring(1), matchList, prefix, currNode);
			}
			for(LexiconNode child: currNode) {
				char l = child.getChar();
				String newPrefix = prefix + l;
				if(c == '?' || c== '_') {
					matchRegexAux(pattern.substring(1), matchList, newPrefix, child);
				}
				else if(c == '*') {
					matchRegexAux(pattern, matchList, newPrefix, child);
				}
				else if(c == l) {
					matchRegexAux(pattern.substring(1), matchList, newPrefix, child);
				}
			}
		}
		return matchList;
	}
}
