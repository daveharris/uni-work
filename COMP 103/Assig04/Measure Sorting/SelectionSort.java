import java.util.Enumeration;
import jds.SortAlgorithm;
import java.util.Comparator;
import jds.Indexed;

/**
 * SelectionSort	for COMP 103	Assignment 4
 * Name: David Harris
 * Usercode; harrisdavi3
 * ID: 300069566
 */

public class SelectionSort implements SortAlgorithm {

// constructor (taking a comparator as a parameter)
	public SelectionSort (Comparator t) {
		test = t;
	}

	private Comparator test;

// sort method (taking an Indexed collection as a parameter)
	public void sort (Indexed v) {

		for(int i = v.size()-1; i>0; i--) {
			int largest = i;
			for(int j=0; j<i; j++) {
				if(test.compare(v.elementAt(j), v.elementAt(i)) > 0)
					largest = j;
			}
			Object temp = v.elementAt(largest);
			v.setElementAt(v.elementAt(i), largest);
			v.setElementAt(temp, i);
		}
	}
}

