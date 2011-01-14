import javax.swing.*;

public class Tester {

	public static void main (String[] args) {

	int option = JOptionPane.showConfirmDialog(null, "Use BST?", "choose one", JOptionPane.YES_NO_OPTION);
	if(option == 0)
		JOptionPane.showMessageDialog(null, "BST Used (0)");
	else if(option == 1)
		JOptionPane.showMessageDialog(null, "BST Not Used (1)");
	}
}