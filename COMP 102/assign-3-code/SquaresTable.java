/**
  * SquaresTable program: for COMP102 assignment 3.
  * Author: David Harris
  * ID: 30069566
  * Date: 19/03/03
  * Description: SquaresTable prompts the user to enter two numbers - the
  * minimum and maximum values in a table, then prints a table of numbers
  * between minimum and maximum (inclusive), along with their squares and 
  * their square roots.  Checks that the numbers are both non-negative, and 
  * that min<max.
  */

import javax.swing.*;

public class SquaresTable {

  public static void main(String[] args) {

    String low = 
        JOptionPane.showInputDialog("Minimum value in table: (Must be non-negative)");
          //Enter minimum number
    String high = 
        JOptionPane.showInputDialog("Maximum value in table: (Must be greater than one)");
          //Enter maximum number

    int min = Integer.parseInt(low);            //Change string type to int type
    int max = Integer.parseInt(high);

    while (min < 0) {
      min = Integer.parseInt(JOptionPane.showInputDialog(null, 
                                                         "Please re-enter minimum (non-negative)"));
            //If miminum number is negative, re-enter 
    }

    while (max < 0 || max <= min) {
      max = Integer.parseInt(JOptionPane.showInputDialog(null, 
                                                         "Please re-enter maximum (non-negative)"));
            //If maximum number is negative or less than min, re-enter
    }

    String msg = "";                              //Creates blank string
    String header = "Num" + "   " + "Square" + "    " + "Square Root";
          //Creates header

    while (min <= max) {                          //Does the maths
      double squareRoot = Math.sqrt(min);
      int square = min * min;
      msg = msg + min + "  " + square + "  " + squareRoot + "\n";
      min++;
    }

    JOptionPane.showMessageDialog(null, header + "\n" + msg);
    	//Prints data




  }

}
