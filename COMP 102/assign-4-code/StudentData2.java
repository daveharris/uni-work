/**
* Program: StudentData2, for Comp102, Assignment 4
* Author: 
* StudentData2 reads a student's name and mark, and prints a message
* indicating whether the student passed or failed.
* This version assumes just one name is entered, and does no error checking.
*/

import javax.swing.*;

public class StudentData2 {

	public static void main (String[] args) {

		String nameMark = JOptionPane.showInputDialog("Enter name and mark");
		int space = nameMark.indexOf(" ");
		String name = nameMark.substring(0,space);
		int mark = Integer.parseInt(nameMark.substring(space+1));
		
		String nameMarkTwo = JOptionPane.showInputDialog("Enter another name and mark");
		int spaceTwo = nameMarkTwo.indexOf(" ");
		String nameTwo = nameMarkTwo.substring(0,spaceTwo);
		int markTwo = Integer.parseInt(nameMarkTwo.substring(spaceTwo+1));
		
		//Create a new Student object
		Student stuOne = new Student(name, mark);
		Student stuTwo = new Student(nameTwo, markTwo);
		
		//Print student's result
		JOptionPane.showMessageDialog(null, name + stuOne.GetResult() + 
								 "\n" + nameTwo + stuTwo.GetResult());
	}
	
}


class Student {

	//Data fields
	String nameStu;
	int markStu;
	String result;
	
	//Constructor, needs to read in the name and mark and 
	public Student (String name,int mark) {
	nameStu = name;
	markStu = mark;
	}

		
	//METHOD GetResult
	//calculate pass or fail
	//think carefullString y about how to set up
		//needs to called from main method in StudentData2 class (ie: from outside the Student class)
		//needs to return the pass or fail message
	public String GetResult () {
		
		//Your code here
		if (markStu >= 50) {
		result = " has passed";  
		}
		else {
		result = " has failed";  
		}
	
		return result;
	
						}
	
			}
