
/* Program:       Shopping List Program
 * Author: David Harris
 * ID: 300069566
 * Date: 7/05/03
 * Description:  Allows a user to edit shopping lists.
 *               A shopping list is a collection of items to buy (String).
 *               After each change to the list, the program redisplays the list,
 *               one item per line, with an item number.
 *               The program will also check for and report any duplicates.
 *               The program must cope with lists of up to 40 items
 *                 and can display them in one column.
 */

import javax.swing.JOptionPane;
public class ShoppingListProgram {
  public static void main(String[] args) {
    ShoppingList s = new ShoppingList();
        String[] menu={"Read", "Display", "Replace Item", "Add Item", "Delete Item", "Check Duplicates", "Exit" };

        while(true){
          int choice = JOptionPane.showOptionDialog(null, "select a function", "Menu",
           JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, menu, menu[0]);

          if (choice==6) System.exit(0);
          switch (choice){
            case 0: s.read();break;
            case 1: s.display();break;
            case 2: s.replaceItem();break;
            case 3: s.addItem();break;
            case 4: s.deleteItem();break;
            case 5: s.checkDuplicates();break;
          }
        }
  }
}

class ShoppingList {
  //Data fields
  //The program must cope with lists of up to 40 items
	private String [] list=new String [40];
	private String input;
	private int count;

  public void read() {
	count = 0;
	for (int i=0; i<40; i++) {
		input = JOptionPane.showInputDialog("Enter an item");
			if (input == null || input.equals("")) {
				return;
			}
				else {
					list[i] = input;
					count++;
				}
	}
  }

  public void display() {
	for (int j=0; j<count; j++) {
		System.out.println((j+1) + ":  " + list[j]);
	}
  }

  public void replaceItem() {
	input = JOptionPane.showInputDialog("Enter the number of an item you want to replace");
		if (input == null || input.equals("")) {
			return;
		}
	int inputIn = (Integer.parseInt(input)) -1;

	String replace = JOptionPane.showInputDialog("What item would you like to replace it with?");
	if (replace == null || replace.equals("")) {
		return;
	}
	list[inputIn] = replace;

	this.display();
  }

  public void addItem() {
	input = JOptionPane.showInputDialog("Enter a new item to add to list");
	if (input == null || input.equals("")) {
		return;
	}
		else {
			list[count] = input;
			count++;
			this.display();
		}
  }

  public void deleteItem() {
	input = JOptionPane.showInputDialog("Enter the number of an item to delete from list");
	if (input == null || input.equals("")) {
		return;
	}
	else {
		int inputIn=((Integer.parseInt(input)) -1);
		for( int k=inputIn; k<39; k++) {
			list[k] = list[(k+1)];
		}
	}
	count--;
	this.display();
  }

  public void checkDuplicates() {
    /** Checks if any items in the array are duplicates and prints them out.
      *   Prints out "no duplicates found" if there were no duplicates
      */
	int countDup=0;
	for (int l=0; l<count; l++) {
 		for (int m=(l+1); m<count; m++) {
			if (((list[l]).equals(list[m])) && (l != m)) {
				countDup++;
				System.out.println("Items " + (l+1) +" and " + (m+1) + " are the same " + "(" + list[m] + ")");
			}
		}
	}
  	if (countDup==0) {
		System.out.println("There are no duplicates");
	}
  }






 }
