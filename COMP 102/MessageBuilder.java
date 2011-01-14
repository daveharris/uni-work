import javax.swing.*;

// Read lines of text from the user and build them into a string with
// separating newlines, until the user presses Cancel, then print the whole
// message.

public class MessageBuilder {
  public static void main(String[] args) {
      String msg = "";
      while (true) {
	  String str = JOptionPane.showInputDialog(
                     "Enter string (or press Cancel"); 
	  if ( str == null ) break;
	  msg = msg + '\n' + str;
      }
      JOptionPane.showMessageDialog(null, msg);
  }
}