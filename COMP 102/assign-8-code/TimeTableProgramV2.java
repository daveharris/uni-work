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


public class TimeTableProgramV2 {
	public static void main(String[] args){
		/* Creates a new timetable object called 'tTable'*/
		TimeTable tTable = new TimeTable();
		String[] menu={"New", "Load", "Add", "Delete", "Search", "Free Time", "Save", "Exit" };		/* The menu functions*/

		while(true){		/* Keep displaying the menu until a corrects button is pushed*/
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
					/* Switch statement to control the menu functions */
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
	/* the total number of days in the timetable */
	private int DAYS = 5;				
	/* the total number of hours in a day */
	private int HOURS = 12;					

	
	/* Fields */
	/* the time table, indexed by day and hour */
	private Activity[][] timeTable;			

	public TimeTable() {
		newTimeTable();
		printTimeTable();
	}

	public void newTimeTable() {				/* make a new time table */
	/* Your Code Here */
		/*Creates  a new array, 'called timeTable' */
		timeTable = new Activity[DAYS][HOURS];				
	}
 
	public void addActivity() {
	/* add an activity to the timetable. Ask the user for day and time, then for the activity, then add to the timetable*/
	/* Your Code Here */
		/* Prompts the user for the day wanted*/
		int day = readDay("Enter the day");			
		/* Promts the user for the time wanted */
		int hour = readHour("Enter the time");		
		/* Creates a new activity object*/
		Activity temp = new Activity();				

		if (timeTable[day][hour] == null) {
			/* Calls on the prmpt method form the activity class*/
			temp.prompt();
			/* Puts the objects into the array*/
			timeTable[day][hour] = temp;			
			/* prints the changes to the timetable*/
			printTimeTable();						
		}
			else {
				int confirm = JOptionPane.showConfirmDialog(null, "There is an activity there, continue?", 	"Confim", JOptionPane.YES_NO_OPTION);
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
		/* Prompts the user for the day wanted*/
		int day = readDay("Enter the day");					
		/* Promts the user for the time wanted */
		int hour = readHour("Enter the time");				

		if (timeTable[day][hour] == null) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Confim", JOptionPane.YES_NO_OPTION);
			if (confirm == 0) {
				/* Sets the specified place in the array to null ie.deleting */
				timeTable[day][hour] = null;
			}
				else {
					return;
				}
		}
			else{
				/* Sets the specified place in the array to null ie.deleting */
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
			/* Asks the user for the name of the file to be saved*/
			String outFileName = JOptionPane.showInputDialog("Output file name");		
			/* Sets up the file-writing and printing  */
			FileWriter outStream = new FileWriter(outFileName);
			PrintWriter outs = new PrintWriter(outStream);				

			/* For loops control the array */
			for (int d = 0; d < DAYS; d++) {
				for (int h = 0; h < HOURS; h++) {			
					if (timeTable[d][h] != null) {
						/* print the data into a file */
						outs.println(d);
						outs.println(h);					

					/* Calls on the writeToFfile method*/
					timeTable[d][h].writeToFile(outs);			
					}
				}
			}
			/* Close the file */
			outs.close();		
		}
		catch (IOException ex) {		/* catch exceptions*/
			System.out.println("IO Error:  " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/* load a timetable from a file */
	public void loadTimeTable() {
	/* Your Code Here */
		try {
			String inFileName = JOptionPane.showInputDialog("Input file name");
			/* Sets up the reading*/
			FileReader inStream = new FileReader(inFileName);
			BufferedReader ins = new BufferedReader(inStream);

			/* Reads the first line*/
			String dataLine = ins.readLine();

			while (dataLine != null) {
				int day = Integer.parseInt(dataLine);
				/* Reads the second line*/
				dataLine = ins.readLine();
				int hour = Integer.parseInt(dataLine);

				Activity temp = new Activity();
				temp.readFromFile(ins);
				timeTable[day][hour] = temp;
				dataLine = ins.readLine();
			}
				/* Close the file*/
				ins.close();		
		}

		catch (IOException ex) {		/* Catch exceptons*/
			System.out.println("IO Error:  " + ex.getMessage());
			ex.printStackTrace();
		}
		/* Print the timetable*/
		printTimeTable();				
	}


	/* Search for, and print times of, all activities associated with a course
	* Ask user for the course
	* EXTENSION ONLY */
	public void searchCourse() {
	/* Your Code Here */

		/* Asks the user for input*/
		String course = JOptionPane.showInputDialog("Enter the course eg.COMP102");
		/*Creates a counter, set to 0 */
		int count = 0;
		for (int d=0; d<DAYS; d++) {
			for (int h=0; h<HOURS; h++) {
				if (timeTable[d][h] != null) {
					if ((timeTable[d][h].getCourse()).equalsIgnoreCase(course)) {
						/* Sets up the output string*/
						System.out.print("Found at ");
						printHour(h);
						System.out.print("on ");
						printDay(d);
						System.out.println();
						/* Updates the counter */
						count++;
					}
				}
			}
		}
		if (count == 0) {		
			/* Output string if not found  */
			System.out.println(course + " not found");			
		}
		System.out.println("Done");
				
	}

	/* Search for blocks of free time in the timetable 
	* EXTENSION ONLY*/
	public void freeTime() {
	/* Your Code Here */
		/* Asks the user for input and changes it to an integer*/
		int input = promptInt("Number of free hours to find");				
		/* Sets the counter to be 0*/
		int counter = 0;										
		for (int d=0; d<DAYS; d++) {
				/* Resets the counter to be 0, so the time measured doesn't overlap days*/
				counter=0;										
			for (int h=0; h<HOURS; h++) {
					if (timeTable[d][h] == null) {
						counter++;
						if (counter == input) {
							/* The output string*/
							System.out.print(input + " free hours found at ");
							printHour(h+1-input);
							System.out.print("on ");
							printDay(d);
							System.out.println();			
							/* Resests the counter to 0*/
							counter=0;							
							int repeat = JOptionPane.showConfirmDialog(null, "Do you want to find another " + input + " hours?", "Repeat", JOptionPane.YES_NO_OPTION);
							if (repeat == 1) {
								return;
							}
						}
					}
					else {
						/* Resets the counter to 0*/
						counter = 0;				
					}
			}
		}
		
		
	}


	/* ==========================================================*/
	/*                   PRIVATE METHODS                         */
	/* ==========================================================*/

	/* Print the timetable on the screen */

	private void printTimeTable(){

	/* Your Code Here */
		
		String line = "----------------------------------------------------------";
		System.out.println(line);

		for(int h=-1; h<HOURS; h++) {
			 for (int d=-1; d<DAYS; d++) {
				if (h ==-1 && d ==-1) {
					/* Prints an emplty string for the hours to go under*/
					System.out.print("      ");			
				}
				else if (h == -1) {
					/* Prints the day*/
					printDay(d);						
				}
				else if (d == -1) {
					/* Prints the hour */
					printHour(h);						
				}
				else if (timeTable[d][h] != null) {
					/* Prints the contents of the array using the above 'for' loop*/
					timeTable[d][h].print();				
				}
				else {
					System.out.print("          ");
				}
			}
			System.out.println();
		}
		/* Prints the line separating timetables*/
		System.out.println(line);				
	}

	private int readDay(String str){
		String prompt = str;
		while ( true ){
			String d = JOptionPane.showInputDialog(prompt);
			d.toLowerCase();
			/* Returns the day to help with menus*/
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
			/* Returns the time in the proper format */
			if ( h >= 8 && h <= 12 )
				return h - 8;
			else if ( h >=1 && h <= 7 )
				return h + 4;
			prompt = str + "(between 8 and 7)";					
		}
	}

	private void printHour (int hour){
		/* Prints the time ready for printing */
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
		/* Prints the day for the top of the array*/
		if ( day == 0 )      System.out.print("Monday    ");
		else if ( day == 1 ) System.out.print("Tuesday   ");
		else if ( day == 2 ) System.out.print("Wednesday ");
		else if ( day == 3 ) System.out.print("Thursday  ");
		else if ( day == 4 ) System.out.println("Friday    ");				
	}


	private int promptInt(String m){
		/* Gets user input and makes it into a intger*/
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
	/* the course of the activity */
	private String course = "";   		 
	/* the kind of activity */
	private String kind = "";     		 
	
/* return the course of the activity */
/* Your Code Here */
	public String getCourse() {
		/* Returns the course type*/
		return course;					
	}

/* prompt the user to enter an activity: course and kind */
/* Your Code Here */
	public void prompt() {
		/* Asks the user for the course name*/
		course = JOptionPane.showInputDialog("Enter the course eg.COMP102");			
		if (course == null || course.equals("")) {
			return;
		}
		/* Asks the user for the kind of activity*/
		kind = JOptionPane.showInputDialog("Enter the kind eg. lecture");				
		if (kind == null || kind.equals("")) {
			return;
		}
	}


/* print the activity (course and kind) on the screen */
/* Your Code Here */
	public void print() {
		course = course + "      ";
		/* Makes the course into a string of max letters of 7*/
		course = course.substring(0,7);					
		
		kind.toLowerCase();
		/* Makes the kind into a lec if the correct letters are inputed*/
		if ("lecture ".startsWith(kind)) {					
			kind = "lec";
		}
		/* Makes the kind into a tut if the correct letters are inputed*/
		else if ("tutorial ".startsWith(kind)) {				
			kind = "tut";
		}
		/* Makes the kind into a lab if the correct letters are inputed*/
		else if ("laboratory ".startsWith(kind)) {			
			kind = "lab";
		}
		/* Makes the kind into a stu if the correct letters are inputed*/
		else if ("study ".startsWith(kind)) {					
			kind = "stu";
		}

			else {
				kind = "oth";
			}
		/* Prints the updated course and kind*/
		System.out.print(course + kind + " ");				
	}

/* write the activity to file so that it can be read back in*/
/* Your Code Here */
	public void writeToFile(PrintWriter file) throws NumberFormatException, IOException {


		/* Prints the course and kind to the file*/
		file.println(course);
		file.println(kind);					
		
		/* Confirms to the user that the file has been sucessful*/
		System.out.println("File Written");			

	}

/* read the activity from file */
/* Your Code Here */
	public void readFromFile(BufferedReader file) throws NumberFormatException, IOException {

		/* Reads the course and kind from the specified file*/
		course = file.readLine();
		kind = file.readLine();					

	}
}
