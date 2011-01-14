/**
  * Program: LabPlanner, for Comp102, Assignment 2
  * Author:David Harris
  * ID: 300069566
  * Date: 11/03/03
  * LabPlanner calculates the number of lab sessions needed for COMP 102. 
  */
import javax.swing.*;
public class LabPlanner {

  public static void main(String[] args) {


    String num1 = 
        JOptionPane.showInputDialog("Enter expected number of enrolments");
    String num2 = 
        JOptionPane.showInputDialog("Enter actual number of enrolments");
    String num3 = JOptionPane.showInputDialog("Enter lab size");


    double num4 = Double.parseDouble(num1);
    double num5 = Double.parseDouble(num2);
    double num6 = Double.parseDouble(num3);


    int num7 = (int)Math.floor(num4 / num6);
    int num8 = (int)Math.ceil(num5 / num6);


    JOptionPane.showMessageDialog(null, "Number of expected enrolments:" +
                                  " " + num1 + 
                                  "\nNumber of actual enrolments:" + " " +
                                  num2 + 
                                  "\nNumber of labs planned: " + " " + num3 + 
                                  "\nNumber of labs planned:" + " " + num7 + 
                                  "\nNumber of labs needed:" + " " + num8);







  }

}
