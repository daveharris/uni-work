import javax.swing.*;

public class GradeChooser {

    public static void main (String[] args) {

	String markStr = JOptionPane.showInputDialog("Enter a mark");
	int mark;
	char msg = ' ';

		while (true) {

			if (markStr == null) {
				break;
			}

			mark = Integer.parseInt(markStr);
			if (mark>=75&&mark<=100) {
				msg = 'A';
			}
			else if (mark>=60) {
				msg ='B';
			}
			else if (mark>=50) {
				msg = 'C';
			}
			else if (mark>=40) {
				msg = 'D';
			}
			else if (mark <40&&mark>=0) {
				msg = 'E';
			}
				else {
					msg = 'X';
				}
			JOptionPane.showMessageDialog(null, "Grade is " + msg);
			markStr = JOptionPane.showInputDialog("Enter a mark");
		}

	}
 }
