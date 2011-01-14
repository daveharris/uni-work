/* Name: David Harris 
* Usercode: daviharris3
* ID: 300069566

	MovieTheatre
	A program that
		- allows Movies to be added, deleted, and listed
		- allows tickets to movies on the list to be booked
		- prints out ticket purchases
*/

import javax.swing.*;

public class MovieTheatreCore {

	public static void main(String args[]) {

		//Initialisation of local variables
	int choice = 0;
				
		//Create new BoxOffice object
	BoxOffice bookings = new BoxOffice();
	
		//Set up a string to print out the main menu
	final String menu = "Movie Theatre" + 
					    	"\nMAIN MENU" + 
					    	"\n1. Add a Movie" +
					    	"\n2. Delete a Movie" +
					    	"\n3. List all Movies" +
					    	"\n4. Book Tickets" +
					    	"\n5. Print an Order" +
					    	"\n6. Quit";

		//Loop until the user selects 'Quit'
	while (true) {
		String s = JOptionPane.showInputDialog(menu);
			if (s == null ) {																// If no string is entered break out of loop
				break;
			}
			
			choice = Integer.parseInt(s.trim());
			
	switch (choice) {
		case 1: bookings.addMovie();
			break;
		case 2: bookings.deleteMovie();
			break;
		case 3: bookings.listMovies();
			break;
		case 4: bookings.bookTickets();
			break;
		case 5: bookings.printTickets();
			break;
		case 6: System.exit(0);
			break;
	}	
			
		}
	}
}


	class BoxOffice {

		//Declare class data fields
		private Movie movie1;																// Initalises the movie object
		String name;
		String userName;
		String output;	
		String delivery;
		double pD;
		String bkTick;
		String bkName;
		
		public void addMovie () {																// A method the adds movies to the list
		
		//Declare local variables
			
		//Get the movie name 
		
		String n = JOptionPane.showInputDialog("Enter the Movie name");
		if (n == null || n.equals("")) {															// If no string is entered break out of loop
			return;
		}	
				else {
				name = n;
				}
		
		
		//Get the price of tickets for the movie 
		String p = JOptionPane.showInputDialog("Enter the ticket price for "+ n);
		if (p == null || p.equals("")) {															// If no string is entered break out of loop
			return;
		}
		pD = Double.parseDouble(p);															// IChanges string type to double
		
		//Create a new Movie object with the movie name and ticket price
		if (movie1 == null) {
		movie1 = new Movie(name, pD);															// Creates a new movie object
		}
			else {
				JOptionPane.showMessageDialog(null, "Sorry, list is full");								// Cannot add more than one movie at once
				return;
			}
		
		//Print out a message saying that the movie has bee added to the list
		JOptionPane.showMessageDialog(null, n + " has been added to the list");
	}
	
	public void deleteMovie () {																// A method the deletes the movies in list
		
		//Your code goes here
		String input = JOptionPane.showInputDialog("Which movie would you like to delete?");
		if (input == null || input.equals("")) {														// If no string is entered break out of loop
			return;
		}
		
		else {
		if (movie1 == null) {
			JOptionPane.showMessageDialog(null, "The movie is not in the list");
			return;
		}
		
		if (input.equalsIgnoreCase(movie1.getName())) {
			JOptionPane.showMessageDialog(null, movie1.getName() + " has been deleted from the list");
			movie1 = null;																	// Sets the movie object to null (deleting it)
		}
			else {
				JOptionPane.showMessageDialog(null, "The movie is not in the list");						// Error message
			}
		
		}
	}	
	
	public void listMovies () {																	// A method the adds movies to the list
		
		//Your code goes here
		if (movie1 != null) {
			JOptionPane.showMessageDialog(null, "Movies in List" +
										"\n ======" +
										"\n" + movie1.getName());
		}
		
			else {
				JOptionPane.showMessageDialog(null, "No Movies currently in list");
			}

	}
	
	public void bookTickets () {																// A method that creates a booking for movies
		
		//Your code goes here
		userName = JOptionPane.showInputDialog("Enter your name");
		if (userName == null || userName.equals("")) {												// If no string is entered break out of loop
			return;
		}
		
		String bkMovie = JOptionPane.showInputDialog("What movie would you like to buy tickets for?");
		if (bkMovie == null || bkMovie.equals("")) {													// If no string is entered break out of loop
			return;
		}	
		
		if (bkMovie.equals(movie1.getName())) {
			bkTick = JOptionPane.showInputDialog("How many tickets would you like?");
			if (bkTick == null || bkTick.equals("")) {												// If no string is entered break out of loop
				return;
			}	
			delivery = JOptionPane.showInputDialog("Would you like the tickets to be delivered (y/n)?");
			if (delivery == null || delivery.equals("")) {												// If no string is entered break out of loop
				return;
			}
			output = userName + " has purchased " + bkTick + " tickets to " + movie1.getName();				// Output message
			JOptionPane.showMessageDialog(null, output);
		}
		
			else {	
				JOptionPane.showMessageDialog(null, "Movie not in list");
			}
	}

	public void printTickets () {																	// Prints the customer's order

		//Your code goes here
		String tickDelivery = "";
		double totalCost = 0;
		String customer = JOptionPane.showInputDialog("Which customer would you like to print a ticket list for?");
		if (customer == null || customer.equals("")) {												// If no string is entered berak out of loop
					return;
		}
		double bkDTick = Double.parseDouble(bkTick);
		
		if (delivery.equals("y")) {
			tickDelivery = "delivered";															//  Set tickDelivery to'delivered'
			totalCost = pD * bkDTick + 5;														//  Sets totalCost plus delivery fee
		}
		else if (delivery.equals("n")) {
			tickDelivery = "collected";															//  Set tickDelivery to'collected'
			totalCost = movie1.getPrice() * bkDTick;												//  Sets totalCost minus delivery fee
		}
		else {
			return;
		}
		
		if (customer.equalsIgnoreCase(userName)) {
			JOptionPane.showMessageDialog(null, output +
									"\nThese tickets will be " + tickDelivery +
									"\nTotal Cost is $" + totalCost);
		}
			else {
			JOptionPane.showMessageDialog(null, "The record for the customer is not found");				// Error messaage
			}
	}		
}
class Movie {

	//Declare class data fields
	String movieName;
	double moviePrice;
	
	//Constructor
	public Movie(String n, double pD) {
		movieName = n;
		moviePrice = pD;
	}
	
	//Returns the movie name
	public String getName() {																	// Creates method for finding the name of each movie
		return movieName;
	}
	
	//Returns the movie ticket price
	public double getPrice() {																	// Creates method for finding the price of each movie
		return moviePrice;
	}
}
