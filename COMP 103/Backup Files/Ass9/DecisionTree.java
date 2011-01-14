/**
	* Decision Tree class for the Animal Game
	* Based on the program in Budd, p335-338,
	* It uses DecisionTree rather than BinaryNode, so that the additional
	*	functionality for this version of the game can be added easily.
	*/

import java.io.*;
import java.util.Enumeration;
import javax.swing.*;
import jds.Set;
import java.awt.BorderLayout;
import java.awt.event.*;

/**
	* Based on the BinaryNode class
	*/
class DecisionTree {

	public Object value;	// value being held by node (the animal or the question

	private DecisionTree leftChild = null;
	private DecisionTree rightChild = null;

	public DecisionTree(String value) {
		this.value = value;
	}

	/**Construct a decision tree from an enumerator
		* Assumes the file is in the required format, and does minimal checking.
		*/
	public DecisionTree(Enumeration e) {

		String tag = (String)e.nextElement();
		if (!tag.equals("node"))
			throw new RuntimeException("Missing <node> tag: " + tag);

		tag = ((String)e.nextElement()).toLowerCase();

		if (tag.equals("animal")) {
			value = (String)e.nextElement();						// Read and save animal name
			tag = (String)e.nextElement();							// read the close animal tag
		}
		else if (tag.equals("question")) {
			value = (String)e.nextElement();						// Read the question
			tag = (String)e.nextElement();							// read the close question tag

			// Read the left and right subtrees
			leftChild = new DecisionTree(e);
			rightChild = new DecisionTree(e);
		}
		else
			throw new RuntimeException("Missing <animal> or <question> tag in node: " + tag);
		tag = (String)e.nextElement();
		if (!tag.equals("/node"))
			throw new RuntimeException("Missing </node> tag: " + tag);

	}


	public boolean isLeaf() {
		return (leftChild == null && rightChild == null);
	}

	public Object getValue() {
		return value;
	}

	public DecisionTree getLeft() {
		return leftChild;
	}

	public DecisionTree getRight() {
		return rightChild;
	}


	/** Changes the value of this node, and sets its left and right
		*	children to be new leaves containing the given Strings
		*/
	public void setNode(String question, String left, String right) {
		value = question;
		leftChild = new DecisionTree(left);
		rightChild = new DecisionTree(right);
	}

	/** Print the tree with indentation to the textArea */
	public void printTree(JTextArea textArea) {
		printTree(0, textArea);
	}

	private void printTree(int depth, JTextArea textArea) {
		if(getLeft() != null)
			getLeft().printTree(depth+1, textArea);

		for (int i = 0; i < depth; i++)
			textArea.append("    ");
		textArea.append("" + getValue() + "\n");

		if(getRight() != null)
			getRight().printTree(depth+1, textArea);
	}



	/** Return the number of animals in the tree, i.e. the size of the fringe. */
	public int countAnimals() {
		// YOUR CODE HERE
		if(getRight() == null && getLeft()== null)
			return 1;
		else if(getRight() == null && getLeft() != null)
			return 1+ getRight().countAnimals();
		else if(getLeft() == null && getRight() != null)
			return 1 + getLeft().countAnimals();
		else
			return getLeft().countAnimals() + getRight().countAnimals();
	}

	/** Return	length of the longest path in the tree (i.e. its height). */
	public int longestPath() {
		if(isLeaf())
			return 0;

		int right = getRight().longestPath();
		int left = getLeft().longestPath();

		if(right > left)
			return right + 1;
		else if(right < left)
			return left + 1;
		else
			return left + 1;
	}


	public void printDuplicates(JTextArea textArea) {
		ArraySet set  = new ArraySet();
		printDuplicates(set, textArea);
	}

	/** Print any duplicated questions on the textarea */
	public void printDuplicates(ArraySet set, JTextArea textArea) {
		if(isLeaf())
			return;
		else {
			if(set.containsElement(value))
				textArea.append("Duplicates are:" +"\t"+ value + "\n");
			set.addElement(value);
			getLeft().printDuplicates(set, textArea);
			getRight().printDuplicates(set, textArea);
		}
	}

	/** Traverse the tree and print it to a file in XML format */
	public void printToFile(PrintWriter file) {
		file.println("<node>");
		if(isLeaf()) {
			file.println("<animal>"+ value + "</animal>");
		}
		else{
			file.println("<question>"+ value + "</question>");
			getLeft().printToFile(file);
			getRight().printToFile(file);
		}
			file.println("</node>");
	}


	/** Print the trail of questions leading to a given animal on the textarea
		* One approach is to first find the leaf node containing the animal (if
		* present)then show the trail to that node.
		*/
	public void printTrail(String animal, JTextArea textArea) {

		// YOUR CODE HERE	(Extension only)

	}

	/** Display the tree on the canvas. */

	public void displayTree(DrawingCanvas canvas) {

		// YOUR CODE HERE	(challenge)

	}
}

