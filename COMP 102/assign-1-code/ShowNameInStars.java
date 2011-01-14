/*
 *
 * ShowNameInStars: demonstration program for COMP102 Assignment 1
 *
 * Modified from Figures 1.11 and 1.12 in K&W, p25-6.
 *
 * Description: this program asks for the user name, and then displays
 *              it surrounded by stars.
 *
 * Student Name:David Harris
 * Student ID:300069566
 * Date: 4/03/03
 *
 * 
*/

import javax.swing.*;

public class ShowNameInStars {

    public static void main (String[] args) {
	// Get the user's name
	String userName = JOptionPane.showInputDialog("Please type in your ID number?");

	// Create a NameInStars object that stores the user's name.
	NameInStars you = new NameInStars(userName);

	// Diplsay your name in starts three times
	JOptionPane.showMessageDialog(null, you.surroundNameInStars());

    }

}

class NameInStars {		// Not "public" when in same file!
    // data fields
    private String name;

    // methods


    public NameInStars(String n) {
	name = n;	        // name = nam; is a typo in the book!
    }

    public String surroundNameInStars() {
	return "*****" + name + "*****\n" +
	    "*****" + name + "*****\n" +
	    "*****" + name + "*****\n";
    }

}
