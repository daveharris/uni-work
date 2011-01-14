import jds.Indexed;
import jds.SortAlgorithm;
import jds.sort.*;
import jds.collection.Vector;
import java.util.Comparator;
import java.util.Enumeration;

import java.io.*;
import javax.swing.JOptionPane;

/* Code for Assignment 4, July 2003
 * Name: David Harris
 * Usercode: harrisdavi3
 * ID: 300069566
 */

public class MeasureSorting{

	public static void main(String[] args){
		Vector lines = new Vector();
		Vector copy = new Vector();
		int counter;
		Enumeration e;

		int lineNumber = Integer.parseInt(JOptionPane.showInputDialog("Number of items to sort:"));
		if(lineNumber > 114710) {
			JOptionPane.showMessageDialog(null, "The number you entered was " + "\n" + " above the limit of 114, 170 !!");
			return;
		}

		try {
			BufferedReader sequencesFile = new BufferedReader(new FileReader("sequences"));
			String dataLine = sequencesFile.readLine();
			for (counter =0; counter < lineNumber; counter++) {
				lines.addLast(dataLine);
				dataLine = sequencesFile.readLine();
			}

			//SelectionSort
			for (e = lines.elements() ; e.hasMoreElements() ;)
				copy.addLast(e.nextElement());
			System.out.println("Time taken for SelectionSort:");
			SortAlgorithm sSorter = new InsertionSort(new StringComparator());
			sorter(sSorter, copy);
			copy = new Vector();

			//InsertionSort
			for (e = lines.elements() ; e.hasMoreElements() ;)
				copy.addLast(e.nextElement());
			System.out.println("Time taken for InsertionSort:");
			SortAlgorithm iSorter = new InsertionSort(new StringComparator());
			sorter(iSorter, copy);
			copy = new Vector();

			//BubbleSort
			for (e = lines.elements() ; e.hasMoreElements() ;)
				copy.addLast(e.nextElement());
			System.out.println("Time taken for BubbleSort:");
			SortAlgorithm bSorter = new BubbleSort(new StringComparator());
			sorter(bSorter, copy);
			copy = new Vector();


			//ShellSort
			for (e = lines.elements() ; e.hasMoreElements() ;)
				copy.addLast(e.nextElement());
			System.out.println("Time taken for ShellSort:");
			SortAlgorithm shSorter = new ShellSort(new StringComparator());
			sorter(shSorter, copy);
			copy = new Vector();

			//QuickSort
			for (e = lines.elements() ; e.hasMoreElements() ;)
				copy.addLast(e.nextElement());
			System.out.println("Time taken for QuickSort:");
			SortAlgorithm qSorter = new Partition(new StringComparator());
			sorter(qSorter, copy);
			copy = new Vector();

			//MergeSort
			for (e = lines.elements() ; e.hasMoreElements() ;)
				copy.addLast(e.nextElement());
			System.out.println("Time taken for MergeSort:");
			SortAlgorithm mSorter = new MergeSort(new StringComparator());
			sorter(mSorter, copy);
			copy = new Vector();

			sequencesFile.close();
		}

		catch(IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void sorter (SortAlgorithm SAlogo, Vector v) {
		long start = System.currentTimeMillis();
		SAlogo.sort(v);
		System.out.println("Time: "+(System.currentTimeMillis()- start));
	}
}

/*long start = System.currentTimeMillis();
	System.out.println("Time: "+(System.currentTimeMillis()- start));*/
/*Comparator insertionSort = new InsertionSort();
		InsertionSort iSort = new InsertionSort(insertionSort);
		iSort.Sort(lines);*/