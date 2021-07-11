import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The LexiconNode class implement the Iterable interface and provides iteration capabilities over its
 * children. LexiconNode class, which represents a single node in the lexicon trie (which is a character
 * and an arrayList of LexiconNodes). The class provides methods for interacting with the child nodes (e.g.,
 * adding, getting, and removing children).
 * 
 * @author Edwin Sanchez Huizar
 *
 */
public class LexiconNode implements Iterable<LexiconNode>{

	private ArrayList<LexiconNode> children;

	private char character;

	private boolean isWord;

	/**
	 * The LexiconNode constructor initializes and assigns the default values to the corresponding instance variables.
	 */
	public LexiconNode() {
		character = ' ';
		children = new ArrayList<LexiconNode>();
		isWord = false;
	}

	/**
	 * The LexiconNode constructor takes a character and assigns it to the corresponding instance variables.
	 */
	public LexiconNode(char letter) {
		character = letter;
		children = new ArrayList<LexiconNode>();
		isWord = false;
	}

	/**
	 * The addChild method takes in a LexiconNode and adds it as a child of the current Node in
	 * alphabetical order. 
	 * 
	 * @param letter - LexiconNode representing a character and its children
	 */
	public void addChild(LexiconNode letter) {

		for(int i = 0; i< children.size(); i++) {
			//adds in alphabetic order
			if(children.get(i).character > letter.character) {
				children.add(i, letter);
				return;
			}
		}
		children.add(children.size(),letter);
	}

	/**
	 * The removeChild method takes in a LexiconNode and removes the child from the arrayList of the current Node.
	 * 
	 * @param letter - LexiconNode representing a character and its children
	 */
	public void removeChild(LexiconNode letter) {
		children.remove(letter);
	}

	/**
	 * The makeWord method updates a node as a word when called for.
	 */
	public void makeWord() {
		isWord = true;
	}

	/**
	 * The ejectWord method removes the isWord status from a node.
	 */
	public void ejectWord() {
		isWord = false;
	}


	/**
	 * The isWord method returns the word status of a node.
	 * 
	 * @return True/False if word is a word or not a word, respectively.
	 */
	public boolean isWord() {
		return isWord;
	}

	/**
	 * The isEmpty method returns the status of the array size.
	 * 
	 * @return True/False if arrayList is empty or not, respectively.
	 */
	public boolean isEmpty() {
		return children.isEmpty();
	}

	/**
	 * The getChar method returns the character corresponding to the node.
	 * 
	 * @return a character corresponding to the node.
	 */
	public char getChar() {
		return character;
	}

	/**
	 * The getChild method takes in a character and returns the corresponding child Lexicon node.
	 * 
	 * @param letter - input character.
	 * @return the corresponding child Lexicon node
	 */
	public LexiconNode getChild(char letter) {
		LexiconNode child = null;

		for (LexiconNode node: children) {
			if (node.getChar() == letter) {
				child = node;
			}
		}
		return child;
	}

	/**
	 * The LexiconNode class implements the Iterable interface and provides iteration 
	 * capabilities over its children, which are stored in an arrayList data structure.
	 */
	public Iterator<LexiconNode> iterator() {
		return children.iterator();
	}

	/**
	 * The getWords method recursively traverses the tree starting at some current node,
	 * looking for words and adding them to the List<String> words argument.
	 * 
	 * @param prefix - a String containing all the letters from the top of the tree down to the current node.
	 * @param words - a list of strings that will be updates through the recursive calls.
	 */
	public void getWords(String prefix, List<String> words) {
		//when adding the word, ignore the blank space in the Lexicon root node.
		if(character != ' ') {
			prefix += character;
		}
		//If the current node is a word, add the prefix argument to the List<String> of words.
		if(isWord) {
			words.add(prefix);
		}
		//For each child of the current mode, recursively call getWords on the child node 
		//with a prefix consisting of the current prefix plus the child’s letter.
		for(LexiconNode child: children) {
			child.getWords(prefix, words);
		}
	}
}
