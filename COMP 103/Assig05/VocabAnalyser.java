import jds.Map;
import jds.Indexed;
import jds.SortAlgorithm;
import jds.collection.Vector;
import jds.sort.Partition;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.awt.Color;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.io.*;

/* Code for Assignment 5, Aug 2003
 * Name: David Harris
 * Usercode: harrisdavi3
 * ID: 300069566
 */

public class VocabAnalyser implements ActionListener{

	private DrawingCanvas canvas;
	private JTextArea textArea;
	private int count;
	private Map hashMap;
	private Vector wordVector;
	private String fileList;
	private int wordIncrement;

	public static void main(String args[]){
		new VocabAnalyser();
	}


	public VocabAnalyser(){
		// set up the user interface
		JFrame frame = new JFrame("Analyse Vocabulary");
		frame.setSize(1024, 768);
		//Establish the drawing canvas for the graph, nad add the scrollbar
		canvas = new DrawingCanvas();
		frame.getContentPane().add(new JScrollPane(canvas), BorderLayout.CENTER);
		//Establish the text area, to which i will print the results, and add the scrolbar
		textArea = new JTextArea(25, 30);
		frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.WEST);
		textArea.setTabSize(6);
		//Create the panel which we add the buttons to
		JPanel buttonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		//make the buttons, and add tem to the panel
		addButton(buttonPanel, "Read File");
		addButton(buttonPanel, "Reset Counts");
		addButton(buttonPanel, "Save Counts");
		addButton(buttonPanel, "Quit");

		frame.setVisible(true);
		//Make a new OpenHashMap object and the fileList String
		hashMap = new OpenHashMap();
		fileList = "";

		// initialise the counts
		wordIncrement = 0;
	}

	private void addButton(JPanel panel, String name){
		//A method which is called on the add the button to the panel
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
	}

	//Respond to buttons
	public void actionPerformed(ActionEvent e){
		//Responds the button pushes, by lisning for user responses
		if (e.getActionCommand().equals("Read File")) {
			readFile();
			plotCounts();
		}
		else if (e.getActionCommand().equals("Reset Counts"))
			resetCounts();
		else if (e.getActionCommand().equals("Save Counts"))
			saveCounts();
		else if (e.getActionCommand().equals("Quit"))
			System.exit(0);
	}

	//Resets the map of wordCount objects, clears the screen and rests the counts
	public void resetCounts() {
		hashMap = new OpenHashMap();
		canvas.clear();
		textArea.setText("");
		wordIncrement = 0;
		fileList = "";
	}

	public void readFile() {
		try {
			String fileName = FileDialog.open();
			//Add the filename to a string to print out later
			fileList += (fileName + "\n");
			BufferedReader f = new BufferedReader(new FileReader(fileName));
			//Read the first line and make it all lower case
			String dataLine = f.readLine();
			while (dataLine != null) {
				//The expection string, so it knows what to make it a new word
				String chs = " \t\n\r.,;!:?+-*/%=<>\"\'\\(){}[]";
				//Go through the words, until there aren't any left
				for (Enumeration data = new StringTokenizer(dataLine, chs); data.hasMoreElements(); ) {
					//Increment the number of words so we can print it out later
					wordIncrement++;
					//Get the word, and make it a string, so it can be used as the key in the map
					String word = ((String) data.nextElement()).toLowerCase();
					//If the key is not in the map, just increment the count
					if (hashMap.containsKey(word))
						((WordCount)hashMap.get(word)).increment();
					//Else make a new WordCount object
					else {
						WordCount wCount = new WordCount(word);
						hashMap.set(word, wCount);
					}
				}
				//Read the next line
				dataLine = f.readLine();
			}
			//Call on the two methods that use the data i've jsu stored
			sortCounts();
			displayTable();

			f.close();
		}
		catch(IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}
	}

	public void displayTable() {
		//Clear the screen and make a number to keep track of the list
		textArea.setText("");
		int rank =	0;
		String output = "";
		//Print out the current files, the total number of words and the header
		textArea.append("Current files are: \n" + fileList + "\n\n");
		textArea.append("WordCount is: " + wordIncrement + "\n");
		textArea.append("\nRank\tCount\tWord\n");
		textArea.append("==========================\n");
		//Go thriugh the vector, printing the word and count, which is already sorted
		for (Enumeration e = wordVector.elements(); e.hasMoreElements(); ) {
			WordCount wC = (WordCount) e.nextElement();
			rank++;
			output += (rank + "\t" + wC.getCount() + "\t" + wC.getWord() + "\n");
		}
		//Print out the informatio n to the text area
		textArea.append(output);
	}

	public void plotCounts() {
		//Clear the screen, and initalise the x-y co-ords
		canvas.clear();
	 	double x1 = 0.0;
	 	double y1 = 0.0;
	 	double x2 = 0.0;
	 	double y2 = 0.0;
	 	//Brings the graph 500 pixels down from the top
	 	double height = 500.0;
		double first = ((WordCount) wordVector.elementAt(0)).getCount();
		//Work out the scale, in realtion tot he highest(first) point
		double multiplier = height / first;
		//Draw the x-y axes
	 	canvas.drawLine(40, (int)height, 1000, (int)height);
		canvas.drawLine(40, 0, 40, (int)height);
		//Print out the scale up the side, if the height is less than 1000
		if (first < 1000) {
			canvas.drawString("50", 2 ,(int)(height-50*multiplier));
			canvas.drawString("100", 2 ,(int)(height-100*multiplier));
		}
		//Loop around, printing out the numbers
		for (double i=0; i<first; i=i+1000.0)
			canvas.drawString("" + (int)i, 2, (int)(height-i*multiplier));

		for(Enumeration e = wordVector.elements(); e.hasMoreElements(); ) {
			x2++; //Move the x value along by one each time
	 		y2 = ((WordCount)e.nextElement()).getCount();
	 		y2 = y2*multiplier;
	 		//If its the first time around, start next the axes
	 		if(x1==0 && y1==0) {
	 			x1=x2;
	 			y1=y2;
	 		}
	 		//Draws the line by segements
	 		canvas.drawLine((int)x1+40, (int)height-(int)y1, (int)x2+40, (int)height-(int)y2);
	 		//Stores the values for mnext time around
	 		y1 = y2;
	 		x1 = x2;
	 	}
	 	canvas.display();
	}

	public void saveCounts() {
		try{
			//Work out where to save, and use the getText method
			String fileName = FileDialog.save();
			PrintWriter printer = new PrintWriter(new FileWriter(fileName));
			String dataLine = textArea.getText();
			//Print the data to the file
			printer.println(dataLine);
			printer.close();
		}
		catch (IOException ex) {
			System.out.println("IO Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}


	 public void sortCounts() {
	 	//Initialise the vector and add all the items from the map
	 	wordVector = new Vector();
	 	for (Enumeration e = hashMap.elements(); e.hasMoreElements();)
	 		wordVector.addLast(e.nextElement());
		//Sort the vector using QuickSort, and put back in the vector
		SortAlgorithm sorter = new Partition(new WordCountComparator());
		sorter.sort(wordVector);
	 }

	private class WordCount {
	 	private String word;
		private int count;
			//The constructor, makes the word and gives it a count of 1
			public WordCount (String w) {
				word = w;
				count = 1;
			}

			public int getCount() {
				return count;
			}

			public String getWord() {
				return word;
			}

			public void increment() {
				count++;
			}
	}


	class WordCountComparator implements Comparator{
		//The empty constructor
		public WordCountComparator(){		}
		//The method that works out which item comes before which when sorting
		public int compare(Object o1, Object o2) {
			WordCount wC1 = (WordCount) o1;
			WordCount wC2 = (WordCount) o2;
			return (wC2.getCount() - wC1.getCount());
		}
	}

}
