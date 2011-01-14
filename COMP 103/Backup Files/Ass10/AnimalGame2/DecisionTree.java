/**
	* Decision Tree class for the Animal Game
	* Based on the program in Budd, p335-338, 
	* It uses DecisionTree rather than BinaryNode, so that the additional
	* 	functionality for this version of the game can be added easily.
	*/

import java.io.*;
import java.util.Enumeration;
import javax.swing.*;
import jds.Set;
import jds.collection.Vector;
import java.awt.BorderLayout;
import java.awt.event.*;

/**
	* Based on the BinaryNode class
	*/
class DecisionTree {

	public Object value;	// value being held by node (the animal or the question 

	private DecisionTree leftChild = null;
	private DecisionTree rightChild = null;
	private DecisionTree parent = null;						 // Extension only


	public DecisionTree(String value) {
		this.value = value;
	}

	public DecisionTree(String value, DecisionTree parent) {
		this.value = value;
		this.parent = parent;
	}


	/**Construct a decision tree from an enumerator
		* Assumes the file is in the required format, and does minimal checking.
		*/
	public DecisionTree(Enumeration e) {
		this(e, null);							 // call the next constructor with a null parent
	}

	public DecisionTree(Enumeration e, DecisionTree parent) {
		this.parent = parent;

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
			leftChild = new DecisionTree(e, this);
			rightChild = new DecisionTree(e, this);
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
		* 	children to be new leaves containing the given Strings 
		*/
	public void setNode(String question, String left, String right) {
		value = question;
		leftChild = new DecisionTree(left, this);
		rightChild = new DecisionTree(right, this);
	}

	/** Print the tree with indentation to the textArea */
	public void printTree(JTextArea textArea) {
		printTree(0, textArea);
	}

	/** Recursive helper method for printing the tree.	Passes down
		*	the level of the tree so that it can be indented correctly
		*/
	private void printTree(int level, JTextArea textArea) {
		if (leftChild != null)
			leftChild.printTree(level + 1, textArea);

		for (int i = 0; i < level; i++)
			textArea.append("		");

		textArea.append(value + "\n");
		if (rightChild != null)
			rightChild.printTree(level + 1, textArea);
	}

	public void printQuestions(JTextArea textArea) {
		textArea.append("All questions (ordered by level): \n");
		//make a new queue
		Vector queue = new Vector();
		//Add the current object to the vector
		queue.addLast(this);
		while (!queue.isEmpty()) {
			//Get the new DecisionTree from the vector
			DecisionTree next = (DecisionTree)queue.elementAt(0);
			//and remove it so we don't get it again
			queue.removeElementAt(0);
			if (!next.isLeaf()) {
				textArea.append("" + next.getValue() + "\n");
				//Add the left and right children to the vector
				queue.addLast(next.getLeft());
				queue.addLast(next.getRight());
			}
		}
	}


	/** Return the number of animals in the tree, i.e. the size of the fringe. */
	public int countAnimals() {
		if (leftChild == null && rightChild == null)
			return 1;
		else {
			int c = 0;
			if (leftChild != null)
				c += leftChild.countAnimals();
			if (rightChild != null)
				c += rightChild.countAnimals();
			return c;
		}
	}


	/** Return	length of the longest path in the tree (i.e. its height). */
	public int longestPath() {
		if (leftChild == null && rightChild == null) {
			return 0;
		}
		else {
			int h = 0;
			if (leftChild != null)
				h = Math.max(h, leftChild.longestPath());
			if (rightChild != null)
				h = Math.max(h, rightChild.longestPath());
			return h + 1;
		}
	}

	/** Print any duplicated questions on the textarea */
	public void printDuplicates(JTextArea textArea) {
		Set allQuestions = new ArraySet();
		printDuplicates(allQuestions, textArea);
	}

	private void printDuplicates(Set allQuestions, JTextArea textArea) {
		if (leftChild == null)
			return;

		//check the question at this node.
		if (allQuestions.containsElement(value))
			textArea.append("Duplicate question: " + value + "\n");
		else
			allQuestions.addElement(value);

		//check the subtrees
		leftChild.printDuplicates(allQuestions, textArea);
		rightChild.printDuplicates(allQuestions, textArea);
	}



	/** Traverse the tree and print it to a file in XML format */
	public void printToFile(PrintWriter file) {
		file.println("<node>");
		if (leftChild == null && rightChild == null) {
			// no child nodes means this must be an animal node
			file.println("<animal>" + value.toString() + "</animal>");
		}
		else {
			// if there are child nodes, this must be a question node
			file.println("<question>" + value.toString() + "</question>");
			if (leftChild != null)
				leftChild.printToFile(file);
			if (rightChild != null)
				rightChild.printToFile(file);
		}
		file.println("</node>");
	}


	/** Print the trail of questions leading to a given animal on the textarea
		* One approach is to first find the leaf node containing the animal (if 
		* present)then show the trail to that node.	
		*/
	public void printTrail(String animal, JTextArea textArea) {
		DecisionTree leaf = find(animal);
		if (leaf != null)
			leaf.showTrail(textArea);
		else
			textArea.append(animal + " is not in the tree\n");
	}

	/* Find returns the leaf node in the tree containing the specified animal,
		 or null if there is no such animal in the tree.*/
	private DecisionTree find(String animal) {
		if (leftChild == null) {											// we are at a leaf
			if (value.equals(animal))
				return this;
			else
				return null;
		}
		DecisionTree left = leftChild.find(animal);
		if (left != null)
			return left;
		else
			return rightChild.find(animal);
	}

	/** Show the trail of questions from the root to this node,
		* 	using the parent pointers 
		*/
	public void showTrail(JTextArea textArea) {

		if (parent == null)													 // we are at the top
			return;

		// print the questions to get to the parent
		parent.showTrail(textArea);

		//print the questions to get from the parent to here
		textArea.append((String)parent.value);
		if (parent.leftChild == this)
			textArea.append(" (Yes)\n");
		else if (parent.rightChild == this)
			textArea.append(" (No)\n");
		else
			textArea.append(" Error\n");

	}

	/** Display the tree on the canvas. */

	public void displayTree(DrawingCanvas canvas) {
		canvas.clear(false);
		displayTree(canvas, 10, 50);
		canvas.display();
	}

	// given the left side and height to draw this subtree,
	// it returns the width of this subtree
	private int displayTree(DrawingCanvas canvas, int left, int top) {
		int leftWidth = 0;
		int rightWidth = 0;

		if (leftChild != null) {
			leftWidth = leftChild.displayTree(canvas, left, top + 35);
			rightWidth = rightChild.displayTree(canvas, left + leftWidth + 10, top +
																					

																					35);
		}

		int valueWidth = ((String)value).length() * 7;
		int width = Math.max(leftWidth + 10 + rightWidth, valueWidth);

		int center = left + width / 2;

		canvas.drawString(value.toString(), center - valueWidth / 2, top, false);
		if (leftChild != null) {
			canvas.drawLine(center, top, left + leftWidth / 2, top + 25, false);
			canvas.drawLine(center, top, left + leftWidth + 10 + rightWidth / 2, 
											top + 25, false);
		}

		return width;
	}






}

