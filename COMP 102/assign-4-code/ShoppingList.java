/*
	ShoppingList is a class that provides methods for creating and displaying shopping lists and for checking input
	Method 'main' creates a new shopping list and calls on other methods to do the actual work
		- it calls on the 'CreateList' method to put the list together
		- it calls on the 'DisplayList' method to output the list to the screen
	Method 'CreateList' handles the input and checking of data
		- it calls on the 'CheckItem' and 'CheckUnit' methods to check the input data
	Method 'DisplayList' displays the data on screen
	Method 'CheckItem' checks that an item is 'flour' or 'sugar' or 'rice'
	
 */

import javax.swing.*;

//A class that provides methods for creating and displaying shopping lists and 
//for checking input
public class ShoppingList {

//Main method, creates a new shopping list and calls on methods to do the 
//actual work
	public static void main (String[] args) {
	
	//Create a new List object
	List list1 = new List();
	
	//Call the CreateList method to add items to the list
	list1.CreateList();
	
	//Call the DisplayList method to display the list on the screen
	JOptionPane.showMessageDialog(null, (list1.DisplayList()));
	}
}

class List {
	
	//data fields
	String msg = "";
	String contents;
	String foodType;
	String items;
	boolean result;

	//The default constructor, which is a constructor with no parameters, no body
	public List() { }
	
	
	//METHOD CreateList - Handles the input and checking of data
	public String CreateList() {
	
	//Define variables
	String error;
		
	//Define and initialise an error message
	error = "ERROR! Enter VALID item (flour, sugar, rice) and quantity separated by a space.\n (Press cancel to terminate input)";
	
	//Define and initialise the output string

	//Get input from user; extract item, quantity and unit; stop when user clicks CANCEL
	//For Version 2, call on the CheckItem and CheckUnit methods
	while (true) {	
		//Your code here
		contents = JOptionPane.showInputDialog("Enter item (flour, sugar, rice) and quantity separated by a space." + "\n (Press cancel to terminate input)");
			if (contents == null) {
			break;
			}
	     
		 		else {
				foodType = contents.substring(0,contents.indexOf(" "));
				if (this.CheckItem(foodType)){
				msg = msg + contents + "\n";
				}
					else {
					contents = JOptionPane.showInputDialog(error);
						if (contents == null) {
						break;
						}
							else {
							msg=msg + "\n" + contents;
							}
					}
				}
	}
          
	 
	//Return the output variable
	return msg;

	}
	
	//METHOD DisplayList - Handles the display of data
	public String DisplayList() {
	
	//Your code here
	
	return msg;
	
	}
	
/*		
	//METHODS FOR CHECKING INPUT - Version 2
		
	//METHOD CheckItem - if an item is 'flour' or 'sugar' or 'rice' return 'true', otherwise return 'false
*/	
private boolean CheckItem (String check_item) {
	
		//Your code here
		if (check_item.equalsIgnoreCase("flour") || check_item.equalsIgnoreCase("rice") || check_item.equalsIgnoreCase("sugar")){
		result = true;
		}
		
		else {
		result = false;
		}
		return result;
}

}
