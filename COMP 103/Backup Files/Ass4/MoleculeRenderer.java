import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.*;
import java.util.Enumeration;
import java.util.Comparator;
import jds.Indexed;
import jds.Map;
import jds.collection.Vector;
import jds.SortAlgorithm;
import jds.sort.*;

/* Code for Assignment 4, Aug 2003
 * Name: David Harris
 * Usercode: harrisdavi3
 * ID: 300069566
 */

/* Progam to render a molecule on the screen from three possible perspectives.
	 The description of the molecule (atom types and positions) is read in from a file
	 The molecule is rendered by drawing a colored circle for each atom.
	 To make sure that the nearest atoms appear in front of the furthest atoms, they
	 atoms must be drawn in order from the furthest away to the nearest.
	 Each of the perspectives imposes a different ordering on the atoms. */

// reads a file with a map of
//	atom-type : size and color
// reads a file with the description of a molecule:
//	- atom type, and (x, y, z) position of each atom
//	- x is from left to right
//	 - y is from top to bottom
//	- z is from front to back.
// renders the molecule on the screen from one of three directions
//	- colored circle for each atom
//	- color and size of each circle given by the map
//	- direction is one of
//	- front	(ie, drawn from largest z to smallest z)
//	 - right	(ie, drawn from smallest x to largest x)
//	- bottom (ie, drawn from smallest y to largest y)

public class MoleculeRenderer implements ActionListener{

	// set up interface
	private DrawingCanvas canvas;
	private ArrayMap map = new ArrayMap();
	String action = "";
	private Vector atomVector = new Vector();
	//private Atom atom;

	 public static void main(String args[]){
		new MoleculeRenderer();
	}

	public MoleculeRenderer() {
		readAtomType();
		JFrame frame = new JFrame("Molecule Renderer");
		frame.setSize(800,600);

		//The graphics area
		canvas = new DrawingCanvas();
		frame.getContentPane().add(canvas, BorderLayout.CENTER);

		//The buttons
		JPanel buttonPanel = new JPanel();

		JButton readButton = new JButton("Read");
		JButton frontButton = new JButton("Front");
		JButton rightButton = new JButton("Right");
		JButton bottomButton = new JButton("Bottom");
		JButton quitButton = new JButton("Quit");

		readButton.addActionListener(this);
		frontButton.addActionListener(this);
		rightButton.addActionListener(this);
		bottomButton.addActionListener(this);
		quitButton.addActionListener(this);

		buttonPanel.add(readButton);
		buttonPanel.add(frontButton);
		buttonPanel.add(rightButton);
		buttonPanel.add(bottomButton);
		buttonPanel.add(quitButton);

		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		frame.setVisible(true);
	}

	// respond to buttons
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Read")) {
			String inputFileName = JOptionPane.showInputDialog("Molecule file name:");
 			readMolecule(inputFileName);
			action = "Front";
		}
		else if (e.getActionCommand().equals("Front"))
			action = "Front";
		else if (e.getActionCommand().equals("Right"))
			action = "Right";
		else if (e.getActionCommand().equals("Bottom"))
			action = "Bottom";
		else if (e.getActionCommand().equals("Quit"))
			System.exit(0);
		render();
	}

	// Read the atomtype-size-color data from the atomtype-info file
	public void readAtomType() {
		map = new ArrayMap();
		try {
			BufferedReader atomFile = new BufferedReader(new FileReader("atomtype-info"));
			String dataLine = atomFile.readLine();
			while(dataLine != null) {
				AtomTypeInfo atomType = new AtomTypeInfo(dataLine);
				String key = atomType.getType();
				map.set(key, atomType);
				dataLine = atomFile.readLine();
			}
			atomFile.close();
		}
		catch(IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	// Read the atomtype-position data from a molecule file
	public void readMolecule(String fileName) {
		atomVector = new Vector();
		try {
			BufferedReader molFile = new BufferedReader(new FileReader(fileName));
			String dataLine = molFile.readLine();
			while(dataLine != null) {
				Atom newAtom = new Atom(dataLine);
				atomVector.addLast(newAtom);
				dataLine = molFile.readLine();
			}
			molFile.close();
		}
		catch(IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	// Render the molecule in the specified direction
	public void render() {
		if(action.equals("Front")){
			Comparator front = new FrontSort();
			SortAlgorithm frontSorter = new MergeSort(front);
			frontSorter.sort(atomVector);
		}
		if(action.equals("Right")) {
			Comparator right = new RightSort();
			SortAlgorithm rightSorter = new MergeSort(right);
			rightSorter.sort(atomVector);
		}
		if(action.equals("Bottom")) {
			Comparator bottom = new BottomSort();
			SortAlgorithm bottomSorter = new MergeSort(bottom);
			bottomSorter.sort(atomVector);
		}

		canvas.clear();
		for (Enumeration e = atomVector.elements(); e.hasMoreElements(); ) {
			Atom atom = (Atom) e.nextElement();
			String type = atom.getType();
			AtomTypeInfo info = (AtomTypeInfo) map.get(type);
			atom.render(canvas, info.getSize(), info.getColor(), action);
			}
		canvas.display();
		action = "";
	}

	// Private comparator classes

	private class FrontSort implements Comparator {
		public int compare(Object o1, Object o2) {
			Atom atom1 = (Atom) o1;
			Atom atom2 = (Atom) o2;
			return atom1.behind(atom2);
		}
	}

	private class RightSort implements Comparator {
		public int compare(Object o1, Object o2) {
			Atom atom1 = (Atom) o1;
			Atom atom2 = (Atom) o2;
			return atom1.leftOf(atom2);
		}
	}

	private class BottomSort implements Comparator {
		public int compare(Object o1, Object o2) {
			Atom atom1 = (Atom) o1;
			Atom atom2 = (Atom) o2;
			return atom1.above(atom2);
		}
	}


}
