/**
  * Program: UserDetails, for Comp102, Assignment 2
  * Author:David Harris
  * ID: 300069566
  * Date: 12/03/03
  * UserDetails reads a user's given name, family name and student ID number,
  * and prints the user's full nmae, default user name and password, and the
  * two forms of email address.
  */

import javax.swing.*;
public class UserDetails {

  public static void main(String[] args) {


    String namID = 
        JOptionPane.showInputDialog("Enter given name, family name and student ID number" + 
                                    "\nin one line, for example" + 
                                    "\nMark Smith 300069566");

    int firstgap = namID.indexOf(" ");
    int secondgap = namID.indexOf(" ", firstgap + 1);
    String first = namID.substring(0, firstgap);
    String second = namID.substring(firstgap, secondgap);
    String third = namID.substring(secondgap);
    String email = first + "." + second + "@mcs.vuw.ac.nz";
    String full = "Computer User Details" + 
        "\n =====================" + 
        "\n Given name:" + "    " + first + 
        "\n Second name:" + "  " + second + 
        "\n ID #:" + "            " + third + 
        "\n Email:" + "  " + email;

    JOptionPane.showMessageDialog(null, full);













  }

}
