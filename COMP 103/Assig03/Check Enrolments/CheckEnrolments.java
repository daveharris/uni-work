import jds.Set;
import jds.collection.Vector;
import jds.collection.SortedVector;
import java.util.Comparator;
import java.util.Enumeration;
import java.io.*;
import javax.swing.JOptionPane;

/* Code for Assignment 3, July 2003
 * Name: David Harris
 * Usercode: harrisdavi3
 * ID: 300069566
 */

public class CheckEnrolments{
	public static void main(String[] args){

		String coursesFname = (args.length==0) ? JOptionPane.showInputDialog("Name of course file") : args[0];
		System.out.println("Timing SortedArraySet: ");
		checkCourses(new SortedArraySet(), coursesFname);
		System.out.println("Timing ArraySet: ");
		checkCourses(new ArraySet(), coursesFname);
	}

	/** Check student applications.
	 *	It first reads the names of all the courses being offered from the
	 *	coursesFile, and adds them to the (empty) set that it was passed.
	 *	It then reads the enrolments from the enrolFile, checking each
	 *	course against the set of all the courses are offered.
	 *	For any course that is not offered, it prints out the student ID
	 *	 and the course to System.out
	 *	The enrolFile lists the courses that each student is enrolling for,
	 *	 with one line containing a student ID, followed by lines containing
	 *	 each courses for that student, eg:
	 *	 400095268
	 *	 QUAN 102
	 *	 SPAN 216
	 *	 SPAN 311
	 *	 400084767
	 *	 FINM 865
	 *	 ORST 482
	 *	 400069038
	 *	 ITAL 311
	 *		...
	 */
	private static void checkCourses(Set currentCourses, String coursesFname){
		try {
			BufferedReader coursesFile = new BufferedReader(new FileReader(coursesFname));
			// Read the courses from the file into the Set.
			//Read the first line, and store it in a string
			String courseLine = coursesFile.readLine();
			//Continue reading file until the line is null
			while (courseLine != null) {
				//Add the course to the Set, and then read the next line
				currentCourses.addElement(courseLine);
				courseLine = coursesFile.readLine();
			}
			//Close the coursesFile file
			coursesFile.close();
			

			BufferedReader enrolFile = new BufferedReader(new FileReader("enrolments"));
			//Start the system clock, so we can time the program
			long start = System.currentTimeMillis();
			//Read enrolments and check all courses
			//Read the first line, and store it in a string
			String nameLine = enrolFile.readLine();
			//Continue reading file until the line is null
			while (nameLine != null) {
				//If the line is an ID number, store it in a string
				if(nameLine.startsWith("4000"))
					coursesFname = nameLine;
				else {
					//Else, if the course is not int the Set, print out a message
					if (!currentCourses.containsElement(nameLine))
						System.out.println("Student " + coursesFname + " :  " + nameLine + " is not offered");
				}
				//Reads the next line of the file
				nameLine = enrolFile.readLine();
			}
			//Print out the time it took for the program to run
			System.out.println("Time: "+(System.currentTimeMillis()- start));
			//Close the enrol file
			enrolFile.close();
		}
		catch(IOException ex) {
			//Catch all expections
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
