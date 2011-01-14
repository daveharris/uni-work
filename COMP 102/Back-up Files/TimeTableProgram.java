

/** Program:      Main Question for Comp102 -  Lab 8
 *  Author: David Harris
 *  Date: 14/05/03
 *  Description:  This program will allow the user to create and manipulate timetables.
 *                 The program will first display an empty timetable and give the user
 *		  the option to:
 *		  - create a new timetable,
 *		  - load a timetable from a file,
 *		  - add an activity to the current timetable,
 *		  - delete an activity from the current timetable,
 *		  - find all the activities that are associated with a particular course,
 *		  - find a user specified block of free hours,
 *		  - save the current time table to a file,
 *		  - quit the application.
 */

import javax.swing.*;
import java.io.*;

      
public class TimeTableProgram {
	public static void main(String[] args){
		TimeTable tTable = new TimeTable();
		String[] menu={"New", "Load", "Add", "Delete", "Search", "Free Time", "Save", "Exit" };
	
		while(true){
			int choice = JOptionPane.showOptionDialog(null, "select a function", "Menu",
			JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, menu, menu[3]);

			if (choice==7) System.exit(0);	
				switch (choice){
				case 0: tTable.newTimeTable();
					break;
				case 1: tTable.loadTimeTable();
					break;
				case 2: tTable.addActivity();
					break;
				case 3: tTable.deleteActivity();
					break;
				case 4: tTable.searchCourse();
					break;
				case 5: tTable.freeTime();
					break;
				case 6: tTable.saveTimeTable();
					break;
          }
       }
	}
}



/** Class:       TimeTable
 *
 *  Description: The TimeTable class represents a timetable.  The timetable is
 *  stored in a 2D array indexed by day and hour of an activity.
 *	 The class contants methods to save and load a timetable from a
 *  file, manipulate a timetable and display a timetable.
 */

class TimeTable {
	/* CONSTANTS */
	private int DAYS = 5;   /* the total number of days in the timetable */
	private int HOURS = 12; /* the total number of hours in a day */

	
	/* Fields */
	private Activity[][] timeTable;    /* the time table, indexed by day and hour */

	public TimeTable() {
		newTimeTable();
		printTimeTable();
	}

	public void newTimeTable() {  /* make a new time table */
	/* Your Code Here */
		timeTable = new Activity[DAYS][HOURS];
	}
 
	public void addActivity() {
	/* add an activity to the timetable. Ask the user for day and time, then for the activity, then add to the timetable*/
	/* Your Code Here */
		
		int day = readDay("Enter the day");
		int hour = readHour("Enter the time");
		Activity temp = new Activity();
	
		if (timeTable[day][hour] == null) {
			temp.prompt();
			timeTable[day][hour] = temp;
			printTimeTable();
		}
			else {
				int confirm = JOptionPane.showConfirmDialog(null, "There is an activity there, continue?", "Confim", JOptionPane.YES_NO_OPTION);
				if (confirm == 0) {
					temp.prompt();
					timeTable[day][hour] = temp;
					printTimeTable();
				}
				else {
					return;
				}
			}
	}

	/* delete an activity from the timetable */
	/* Ask the user for day and time*/
	public void deleteActivity() {
	/* Your Code Here */
		int day = readDay("Enter the day");
		int hour = readHour("Enter the time");
		
		if (timeTable[day][hour] == null) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Confim", JOptionPane.YES_NO_OPTION);
			if (confirm == 0) {
				timeTable[day][hour] = null;
			}
				else {
					return;
				}
		}
			else{
				timeTable[day][hour] = null;
			}
	}

	/*     Save files are of the form...
	*       Any number of repetitions of
	*       - day, hour, activity
	*/
 /* save the timetable to a file */
	public void saveTimeTable() { 
	/* open the file */
	/* Your Code Here */
		try {
			String outFileName = JOptionPane.showInputDialog("Output file name");
			FileWriter outStream = new FileWriter(outFileName);
			PrintWriter outs = new PrintWriter(outStream);

			for (int d = 0; d < DAYS; d++) {
				for (int h = 0; h < HOURS; h++) {
					if (timeTable[d][h] != null) {
						outs.println(d);
						outs.println(h);

					timeTable[d][h].writeToFile(outs);
					}
				}
			}

			outs.close();
		}
		catch (IOException ex) {
			System.out.println("IO Error:  " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/* load a timetable from a file */
	public void loadTimeTable() {
	/* Your Code Here */
		try {
			String inFileName = JOptionPane.showInputDialog("Input file name");
			FileReader inStream = new FileReader(inFileName);
			BufferedReader ins = new BufferedReader(inStream);

			String dataLine = ins.readLine();

			while (dataLine != null) {
				int day = Integer.parseInt(dataLine);
				dataLine = ins.readLine();
				int hour = Integer.parseInt(dataLine);

				Activity temp = new Activity();
				temp.readFromFile(ins);
				timeTable[day][hour] = temp;
				dataLine = ins.readLine();
			}
				ins.close();
		}

		catch (IOException ex) {
			System.out.println("IO Error:  " + ex.getMessage());
			ex.printStackTrace();
		}
		printTimeTable();
	}


	/* Search for, and print times of, all activities associated with a course
	* Ask user for the course
	* EXTENSION ONLY */
	public void searchCourse() {
	/* Your Code Here */

		String course = JOptionPane.showInputDialog("Enter the course eg.COMP102");
		for (int d=0; d<DAYS; d++) {
			for (int h=0; h<HOURS; h++) {
				if (timeTable[d][h] != null) {
					if ((timeTable[d][h].getCourse()).equals(course)) {
						System.out.print("Found at ");
						printHour(h);
						System.out.print("on ");
						printDay(d);
						System.out.println();
					}
						else {
							System.out.println(course + " not found");
						}
				}
			}
		}
		System.out.println("Done");
	}

	/* Search for blocks of free time in the timetable 
	* EXTENSION ONLY*/
	public void freeTime() {
	/* Your Code Here */
		String inputStr = JOptionPane.showInputDialog("Number of free hours to find");
		int input = Integer.parseInt(inputStr);
		int counter = 0;
		while (true) {
			for (int d=0; d<DAYS; d++) {
				for (int h=0; h<HOURS; h++) {
					while (timeTable[d][h] != null && counter <= input) {
						if (counter == input) {
							System.out.println(input + " free hours found at");
						}
							else {
								counter = counter +1;
							}
					}
				}
			}
			int repeat = JOptionPane.showConfirmDialog(null, "Do you want to find another " + input + " hours?", "Repeat", JOptionPane.YES_NO_OPTION);
			if (repeat == 1) {
				return;
			}
		}
	}


	/* ==========================================================*/
	/*                   PRIVATE METHODS                         */
	/* ==========================================================*/

	/* Print the timetable on the screen */

	private void printTimeTable(){

	/* Your Code Here */
		
		String line = "-------------------------------------------------------";
		System.out.println(line);

		for(int h=-1; h<HOURS; h++) {
			 for (int d=-1; d<DAYS; d++) {
				if (h ==-1 && d ==-1) {
					System.out.print("      ");
				}
				else if (h == -1) {
					printDay(d);
				}
				else if (d == -1) {
					printHour(h);
				}
				else if (timeTable[d][h] != null) {
					timeTable[d][h].print();
				}
				else {
					System.out.print("          ");
				}
			}
			System.out.println();
		}
		System.out.println(line);
	}

	private int readDay(String str){
		String prompt = str;
		while ( true ){
			String d = JOptionPane.showInputDialog(prompt);
			d.toLowerCase();
			if ("monday ".startsWith(d) )         return 0;
			else if ("tuesday ".startsWith(d) )   return 1;
			else if ("wednesday ".startsWith(d) ) return 2;
			else if ("thursday ".startsWith(d) )  return 3;
			else if ("friday ".startsWith(d) )    return 4;
			prompt = str + "(one of mon, tue, wed, thu, fri)";
		}
	}


	private int readHour(String str){
	/* Returns an index into the timetable array corresponding to the hour
	* that the user types in: 8am -> 0,  12 noon -> 4, 1pm -> 5, 7pm ->12
	*/
		String prompt = str;
		while ( true ){
			int h = promptInt(prompt);
			if ( h >= 8 && h <= 12 )
				return h - 8;
			else if ( h >=1 && h <= 7 )
				return h + 4;
			prompt = str + "(between 8 and 7)";
		}
	}

	private void printHour (int hour){
		if ( hour  <= 1 )
			System.out.print(" " + (hour + 8) + "am ");
		else if ( hour > 1 && hour < 4 )
			System.out.print((hour + 8) + "am ");
		else if ( hour == 4 )
			System.out.print("noon ");
		else if ( hour > 4 )
			System.out.print(" " + (hour - 4) + "pm ");
	}


	private void printDay (int day){
		if ( day == 0 )      System.out.print("Monday    ");
		else if ( day == 1 ) System.out.print("Tuesday   ");
		else if ( day == 2 ) System.out.print("Wednesday ");
		else if ( day == 3 ) System.out.print("Thursday  ");
		else if ( day == 4 ) System.out.println("Friday    ");
	}


	private int promptInt(String m){
		String s = JOptionPane.showInputDialog(m);
		return Integer.parseInt(s);
	}

}


/** Class:       Activity
 *
 *  Description: The activity class represents an activity which is a course
 *               and kind pair.
 *               The class allows courses to be read and written to files, as
 *		 well as read from the user and output to the text pane
 */

class Activity {
  	private String course = "";    /* the course of the activity */
  	private String kind = "";      /* the kind of activity */
	
/* return the course of the activity */
/* Your Code Here */
	public String getCourse() {
		return course;
	}

/* prompt the user to enter an activity: course and kind */
/* Your Code Here */
	public void prompt() {
		course = JOptionPane.showInputDialog("Enter the course eg.COMP102");
		if (course == null || course.equals("")) {
			return;
		}
		kind =JOptionPane.showInputDialog("Enter the kind eg. lecture");
		if (kind == null || kind.equals("")) {
			return;
		}
	}


/* print the activity (course and kind) on the screen */
/* Your Code Here */
	public void print() {
		course = course + "      ";
		course = course.substring(0,7);
		
		kind.toLowerCase();
		if ("lecture ".startsWith(kind)) {
			kind = "lec";
		}
		else if ("tutorial ".startsWith(kind)) {
			kind = "tut";
		}
		else if ("laboratory ".startsWith(kind)) {
			kind = "lab";
		}
		else if ("study ".startsWith(kind)) {
			kind = "stu";
		}
			else {
				kind = "oth";
			}
		System.out.print(course + kind);
	}

/* write the activity to file so that it can be read back in*/
/* Your Code Here */
	public void writeToFile(PrintWriter file) throws NumberFormatException, IOException {


		file.println(course);
		file.println(kind);
		
		System.out.println("File Written");

	}

/* read the activity from file */
/* Your Code Here */
	public void readFromFile(BufferedReader file) throws NumberFormatException, IOException {

		course = file.readLine();
		kind = file.readLine();

	}
}
