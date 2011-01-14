/**
 * Program: StudentData1, for Comp102, Assignment 3.
 * Author:David Harris
 * ID: 30006956
 * Date: 19/03/03
 * StudentData1 reads a student's name and mark, and print a message
 * indicating whether the student passed or failed.
 */

import javax.swing.*;

public class StudentData {

    public static void main (String[] args) {

	String namark = JOptionPane.showInputDialog("Enter name and mark");
	
int space = namark.indexOf(" ");
String name = namark.substring(0,space);
int mark = Integer.parseInt(namark.substring(space+1));


if (mark >= 50) {
JOptionPane.showMessageDialog(null, name + " has passed");
}
else {
JOptionPane.showMessageDialog(null, name + " has failed");
}

    }

}