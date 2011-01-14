/*
	* Name: David Harris 
	* Usercode: harrisdavi3
	* ID: 300069566

	MovieTheatre
	A program that
		- allows Movies to be added, deleted, and listed
		- allows tickets to movies on the list to be booked
		- prints out ticket purchases
*/

import javax.swing.*;

public class MovieTheatreExt {

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
			if (s == null ) {																			// If no string is entered berak out of loop
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
		private Movie movie1;																		// Initialises the movie1 object
		private Movie movie2;																		// Initialises the movie2 object
		private Movie movie3;																		// Initialises the movie3 object
		String name;
		String userName;
		String output;	
		String delivery;
		String movieName;
		int adultQuan;																				// Initalises quantity of adult tickets wanted
		int studentQuan;																			// Initalises quantity of student tickets wanted
		int childQuan;																				// Initalises quantity of child tickets wanted
		double moviePrice;																			// Initalises generic movieproce
		
		public void addMovie () {																		// A method that adds movies to the list
		
		//Declare local variables
			
		//Get the movie name 
		
		String n = JOptionPane.showInputDialog("Enter the Movie name");
		
		if (n == null || n.equals("")){																	// If nothing is entered, then return
			return;
		   }	
			else {
				name = n;
			}
		
		
		//Get the price of tickets for the movie 
		String p = JOptionPane.showInputDialog("Enter the ticket price for "+ n);
		if (p == null || p.equals("")) {																	// If nothing is entered, then return
			return;
		}	
		double pD = Double.parseDouble(p);																// Changing the string into a double
		
		//Create a new Movie object with the movie name and ticket price
		if (n == null || n.equals("")) {																	// If nothing is entered, then return
			return;
		}	
		else {
			if (movie1 == null) {
			movie1 = new Movie(name, pD);																// New movie object created
			}
				else if (movie2 == null) {	
				movie2 = new Movie(name, pD);															// New movie object created
				}
					else if (movie3 == null) {
					movie3 = new Movie(name, pD);														// New movie object created
					}
						else {
						JOptionPane.showMessageDialog(null, "Sorry, list is full");								// Cannot add more than 3 movies at once
						return;
						}					
		}
	
		//Print out a message saying that the movie has bee added to the list
		JOptionPane.showMessageDialog(null, n + " has been added to the list");
	}
	
	public void deleteMovie () {																		// A method the deletes the movies in list
		
		//Your code goes here
		String input = JOptionPane.showInputDialog("Which movie would you like to delete?");
		
		if (input == null || input.equals("")) {																// If nothing is entered, then return
			return;
		}	
		else{
			if (movie1 != null) {
				if (input.equalsIgnoreCase(movie1.getName())) {
					JOptionPane.showMessageDialog(null, movie1.getName() + " has been deleted from the list");
					movie1 = null;																	// Sets the movie object to null (deleting it)
					return;
				}
			}
			if (movie2 != null) {
				if (input.equalsIgnoreCase(movie2.getName())) {
					JOptionPane.showMessageDialog(null, movie2.getName() + " has been deleted from the list");
					movie2 = null;																	// Sets the movie object to null (deleting it)
					return;
				}
			}
			if (movie3 != null) {
				if (input.equalsIgnoreCase(movie3.getName())) {
					JOptionPane.showMessageDialog(null, movie3.getName() + " has been deleted from the list");
					movie3 = null;																	// Sets the movie object to null (deleting it)
				return;
				}
			}
		JOptionPane.showMessageDialog(null, "The movie is not in the list");										// Movie entered didn't match any listed
					
		}
	}	
	
	public void listMovies () {																			// A method the list the movies in the list
		
		//Your code goes here
		
		String movieList =  "Movies in List" +
									"\n ===============";
		boolean found  = false;																		// Sets boolean variable to false
		
		if (movie1 != null) {
			movieList = movieList + "\n" + movie1.getName();												// Adds to the movieList string
			found = true;																			// Sets found to true when found the movie
		}
		if (movie2 != null) {
			movieList = movieList + "\n" + movie2.getName();												// Adds to the movieList string
			found = true;																			// Sets found to true when found the movie
		}
		if (movie3 != null) {
			movieList = movieList + "\n" + movie3.getName();												// Adds to the movieList string
			found = true;																			// Sets found to true when found the movie
		}
		if ( found == true) {
			JOptionPane.showMessageDialog(null, movieList);												// If found is true, then print list
		}
		else {
		JOptionPane.showMessageDialog(null, "No Movies currently in list");										// If no movies found, print error message
		}
	}
	
	public void bookTickets () {																		// A method that creates a booking for movies
		
		//Your code goes here
		boolean found = false;																		// Sets boolean variable to false
		
		userName = JOptionPane.showInputDialog("Enter your name");
		if (userName == null || userName.equals("")) {														// If nothing is entered, then return
			return;
		}	
		movieName = JOptionPane.showInputDialog("What movie would you like to buy tickets for?");
		if (movieName == null || movieName.equals("")) {													// If nothing is entered, then return
			return;
		}	

		if ( movie1 != null && movieName.equals(movie1.getName())) {
			found = true;
		}
		if (movie2 != null && movieName.equals(movie2.getName())) {
			found = true;
		}
		if (movie3 != null && movieName.equals(movie3.getName())) {
			found = true;
		}
		
				
		if (found == true) {
			
			String adultQuanStr = JOptionPane.showInputDialog("How many adult tickets would you like?");
				if (adultQuanStr == null || adultQuanStr.equals("")) {												// If nothing is entered, then return
					return;
				}	
			adultQuan = Integer.parseInt(adultQuanStr);														// Change string into an int
			
			String studentQuanStr = JOptionPane.showInputDialog("How many student tickets would you like?");
				if (studentQuanStr == null || studentQuanStr.equals("")) {											// If nothing is entered, then return
					return;
				}	
			studentQuan = Integer.parseInt(studentQuanStr);													// Change string into an int
			
			String childQuanStr = JOptionPane.showInputDialog("How many child tickets would you like?");
				if (childQuanStr == null || childQuanStr.equals("")) {												// If nothing is entered, then return
					return;
				}	
			childQuan = Integer.parseInt(childQuanStr);															// Change string into an int
			
			delivery = JOptionPane.showInputDialog("Would you like the tickets to be delivered (y/n)?");
			if (delivery == null || delivery.equals("")) {															// If nothing is entered, then return
				return;
			}
			else {
				int ticketQuan = (adultQuan + studentQuan + childQuan);
				output =  userName + " has purchased " + ticketQuan + " tickets to " + movieName;						// Output message
				JOptionPane.showMessageDialog(null, output);	
			}
		 
		 	if (found == false) {
			JOptionPane.showMessageDialog(null, "Movie is not found in list");
			}
		
			if (!delivery.equals("y") || !delivery.equals("n")) {
				return;
			}
		
		}
		
	}

	public void printTickets () {																				// Prints the customer's order

		//Your code goes here
		double totalCost;
		boolean found = false;
		
		
		String customer = JOptionPane.showInputDialog("Which customer would you like to print a ticket list for?");
		if (customer == null || customer.equals("")) {															// If nothing is entered, then return
			return;
		}	
		
		if (customer.equalsIgnoreCase(userName)) {
		}
		else {
			JOptionPane.showMessageDialog(null, "Customer record not found");										// Customer enter is not the same as above
			return;
		}
		
		
		if (movie1 != null && movieName.equals(movie1.getName())) {
			moviePrice = movie1.getPrice();																	// Sets the price of the movieto a variable
			found  = true;
		}
		if (movie2 != null && movieName.equals(movie2.getName())) {
			moviePrice = movie2.getPrice();																	// Sets the price of the movieto a variable
			found  = true;
		}
		if (movie3 != null && movieName.equals(movie3.getName())) {
			moviePrice = movie3.getPrice();																	// Sets the price of the movieto a variable
			found  = true;
		}
		
		double adultTotal = adultQuan * moviePrice;																// Initalises prices for adults
		double studentTotal = ((studentQuan * (2.0/3.0) * moviePrice));												// Initalises prices for students
		double childTotal = ((childQuan * 0.5) * moviePrice);														// Initalises prices for children
		
		
		
		if (found == true) {
			
			if (delivery.equals("y")) {
			
				totalCost = (adultTotal + studentTotal + childTotal) + 5;												// Initalises combined cost plus delivery fee
				JOptionPane.showMessageDialog(null, output +													// Prints information on tickets ordered,
											"\nThese tickets will be delivered" +								// whether delivered or not and total cost
											"\nTotal Cost is $" + totalCost);
			}
		
			else if (delivery.equals("n")) {
				
				totalCost = (adultTotal + studentTotal + childTotal);												// Initalises combined cost with no delivery fee
				JOptionPane.showMessageDialog(null, output +													// Prints information on tickets ordered,
											"\nThese tickets will be collected"  +								// whether delivered or not and total cost
											"\nTotal Cost is $" + totalCost);
			}	
				
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
	public String getName() {																				// Creates method for finding the name of each movie
		return movieName;
	}
	
	//Returns the movie ticket price
	public double getPrice() {																				// Creates method for finding the price of each movie
		return moviePrice;
	}
}
