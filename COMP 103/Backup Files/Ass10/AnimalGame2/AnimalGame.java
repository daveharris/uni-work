/**
	* Guess the Animal Game.
	* Based on program in Budd, p335-338, but substantially modified.
	*/

import java.io.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.StringTokenizer;
import jds.collection.Vector;

public class AnimalGame implements ActionListener {

	/** the output area */
	private DrawingCanvas canvas;
	private JTextArea textArea;

	/** the DecisionTree object which holds the database */
	private DecisionTree root;

	/** main method creats a new AnimalGame Object user interface */
	public static void main(String args[]) {
		new AnimalGame();
	}


	public AnimalGame() {
		JFrame frame = new JFrame("Guess the animal game");

		frame.setSize(800, 600);
		canvas = new DrawingCanvas();
		frame.getContentPane().add(new JScrollPane(canvas), BorderLayout.CENTER);

		//The text area
		textArea = new JTextArea(60, 40);
		frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.WEST);

		//The buttons
		JPanel buttonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
		addButton(buttonPanel, "Play");
		addButton(buttonPanel, "New");
		addButton(buttonPanel, "Load");
		addButton(buttonPanel, "Save");
		addButton(buttonPanel, "Print");
		addButton(buttonPanel, "Trail");
		addButton(buttonPanel, "Quit");

		//For error checking
		addButton(buttonPanel, "Error Check");

		frame.setVisible(true);


		root = new DecisionTree("Cat");

		textArea.append("Lets play guess the animal\n");
	}

	/** easy button adder */
	private void addButton(JPanel panel, String name) {
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
	}

	/* Respond to button presses */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Play"))
			play();
		else if (e.getActionCommand().equals("New"))
			newTree();
		else if (e.getActionCommand().equals("Load"))
			loadTree();
		else if (e.getActionCommand().equals("Save"))
			saveTree();
		else if (e.getActionCommand().equals("Print"))
			printTree();
		else if (e.getActionCommand().equals("Trail"))
			printTrail();
		else if (e.getActionCommand().equals("Quit"))
			System.exit(0);
		//Error Checking
		else if (e.getActionCommand().equals("Error Check")) {
			printErrorCheck();
		}
	}

	/** Make a new tree, asking for the first animal. */
	private void newTree() {
		root = new DecisionTree(JOptionPane.showInputDialog("First animal:"));
		textArea.setText("");
	}


	/** Play the game. */
	public void play() {
		DecisionTree current = root;
		JOptionPane.showMessageDialog(null, "Think of an animal");
		while (current != null) {
			// if node is not a leaf then it is a question
			if (!current.isLeaf()) {
				String qn = (String)current.getValue();
				if (yesNo(qn))
					current = current.getLeft();
				else
					current = current.getRight();
			}
			else {
				// no children, it is an answer
				String guess = ("I think I know. Is it a " + current.value);
				if (yesNo(guess))
					new JOptionPane("I Won");
				else																			// time to learn something
					learnNewAnimal(current);
				current = null;
			}
		}

	}


	/** Display a question, and get a yes/no answer. */
	private boolean yesNo(String message) {
		int ans = JOptionPane.showConfirmDialog
				(null, message + "\n" + "Please answer Yes or No", "Yes/No",
				 JOptionPane.YES_NO_OPTION);
		return ans == JOptionPane.YES_OPTION;
	}

	/** Get information about a new animal and add it to the tree. */
	private void learnNewAnimal(DecisionTree current) {

		String currentAnimal = (String)current.value;
		String newAnimal = JOptionPane.showInputDialog("What is your animal ?");
		String question = JOptionPane.showInputDialog
				("What is a yes/no question that I can use to tell a " +
				 currentAnimal + " from a " + newAnimal + "?");

		// Make new leaf nodes, with current as parent

		if (yesNo("For a " + newAnimal + ", what is the answer?"))
			current.setNode(question, newAnimal, currentAnimal);
		else
			current.setNode(question, currentAnimal, newAnimal);
	}



	/** print the tree and statistics */
	public void printTree() {
		textArea.append("---------------\nTree is:" + "\n");
		root.printTree(textArea);

		textArea.append("Number of animals: " + root.countAnimals() + "\n");
		textArea.append("Longest path: " + root.longestPath() + "\n");
		root.printDuplicates(textArea);
		textArea.append("---------------\n");
		root.displayTree(canvas);

		root.printQuestions(textArea);
	}


	/** Load decision tree from file */
	public void loadTree() {
		textArea.setText("");
		//String filename = "animals.xml";
		String filename = JOptionPane.showInputDialog("Input the name of the input file");
		try {
			BufferedReader f = new BufferedReader(new FileReader(filename));
			Vector allTokens = new Vector();
			while (true) {
				String line = f.readLine();
				if (line == null)
					break;																	//end of file
				Enumeration e = new StringTokenizer(line, "\n\r<>");
				while (e.hasMoreElements()) {
					String token = ((String)e.nextElement()).trim();
					//System.out.println("> "+token);
					if (token.length() > 1)					 // skip over single character tokens
						allTokens.addLast(token);
				}
			}
			f.close();

			//for (Enumeration en=allTokens.elements(); en.hasMoreElements();)
			// System.out.println(en.nextElement());

			Enumeration e = allTokens.elements();
			if (allTokens.elementAt(0).equals("animalgame"))
				e.nextElement();
			root = new DecisionTree(e);
		}
		catch (FileNotFoundException fnfe) {
			System.err.println("Unable to find file: " + filename);
		}
		catch (IOException ioe) {
			System.err.println("Problem reading token stream from file: " + ioe);
		}
		textArea.append("Loaded tree from " + filename + "\n");
		printTree();
	}


	/** Write decision tree to file */
	private void saveTree() {
		String filename =
				JOptionPane.showInputDialog("Input the name of the output file");
		textArea.append("---------------\nSaving to " + filename + "....");
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			pw.println("<animalgame>");

			root.printToFile(pw);

			pw.println("</animalgame>");
			pw.flush();
			pw.close();
			textArea.append("done\n");

		}
		catch (IOException ioe) {
			System.err.println("Problem writing tree to file: " + ioe);
		}
	}


	private void printTrail() {
		String animal =
				JOptionPane.showInputDialog("Which animal is the trail for");
		textArea.append("---------------\nTrail to " + animal + ":\n");
		root.printTrail(animal, textArea);
	}

	private void printErrorCheck() {
		textArea.append(String.valueOf(root.countAnimals()) + "\n");

	}

}
