/*
 * SqlModel.java
 * Author: David Harris
 * Created on: 09/05/05
 */
import java.sql.*;

import javax.swing.JOptionPane;

import sun.security.jca.GetInstance;

public class SqlModel302 {
	
	private Connection connection = null;
	private boolean subCall = false;
	
	/** Creates a new instance of SqlModel */
	public SqlModel302(String userId, String password) {
		try {
			Class.forName("org.postgresql.Driver");
		}
		
		catch (ClassNotFoundException cnfe) {
			System.out.println("Cannot find the driver class: \nEither not installed properly or not in CLASSPATH");
		}
		
		String url = "jdbc:postgresql://db.mcs.vuw.ac.nz/" + userId + "_jdbc";
		try {
			connection = DriverManager.getConnection(url, userId, password);
		}
		catch (SQLException sqlex) {
			System.out.println("Cannot connect to database\n" + sqlex.getMessage());
		}
	}
	
	public String showAllCustomer() {
		Statement s = null;
		try {
			s = connection.createStatement();
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while creating a statement, probably means connection to database was lost");
		}
		
		ResultSet resultSet = null;
		String allCustomers = "";
		
		try {
			resultSet = s.executeQuery("SELECT * FROM customer");
			String city;
			while(resultSet.next()) {
				int customerId = resultSet.getInt(1);
				String fName = resultSet.getString(2).trim();
				String lName = resultSet.getString(3).trim();
				if(resultSet.getString(4) == null)
					city = "";
				else
					city = resultSet.getString(4).trim();
				allCustomers += (customerId + " - " + lName + " - " + fName + " - " + city + "\n");
			}
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
		}
		return "Show All Customers\n" + allCustomers + "\n\n";
	}
	
	public String showCustomer(int customerId) {
		Statement s = null;
		try {
			s = connection.createStatement();
		}
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while creating a statement, probably means connection to database was lost");
		}
		String allDetails = "";
		try {
			String select = "SELECT * FROM customer WHERE customerid=?";
			PreparedStatement pSmt = connection.prepareStatement(select);
			pSmt.setInt(1, customerId);
			
			ResultSet resultSet = pSmt.executeQuery();
			String city = "";
			resultSet.next();
			
			int customerid = resultSet.getInt(1);
			String fName = resultSet.getString(2);;
			String lName = resultSet.getString(3);
			if(resultSet.getString(4) == null)
				city = "";
			else
				city = resultSet.getString(4).trim();
			allDetails = customerid + " - " + lName.trim() + " - " + fName.trim() + " - " + city.trim();
			
			String customer = "SELECT * FROM cust_book NATURAL JOIN book WHERE customerid=" + customerId + ";";
			PreparedStatement statement = connection.prepareStatement(customer);
			ResultSet borrowedResultSet = statement.executeQuery();
			allDetails += "\nBooks borrowed:\n";
			while(borrowedResultSet.next())
				allDetails += " " + borrowedResultSet.getInt(1) + " - " + borrowedResultSet.getString(4) + "\n";
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
		}
		if(subCall)
			return allDetails;
		else
			return "Show Customer\n" + allDetails + "\n\n";
	}
	
	public String showAllAuthor() {
		Statement s = null;
		try {
			s = connection.createStatement();
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while creating a statement, probably means connection to database was lost");
		}
		
		ResultSet resultSet = null;
		String allCustomers = "";
		
		try {
			resultSet = s.executeQuery("SELECT * FROM author");
			int authorId;
			String FName;
			String LName;
			
			while(resultSet.next()) {
				authorId = resultSet.getInt(1);
				FName = resultSet.getString(2);
				LName = resultSet.getString(3);
				allCustomers += (authorId + " - " + FName.trim() + " - " + LName.trim() + "\n");
			}
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
		}
		return "Show All Authors\n" + allCustomers + "\n\n";
	}
	
	public String bookLookUp(int isbn) {
		Statement s = null;
		try {
			s = connection.createStatement();
		}
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while creating a statement, probably means connection to database was lost");
		}
		String allDetails = "";
		String surname = "";
		
		try {
			String select = "SELECT * FROM (book NATURAL LEFT OUTER JOIN book_author) NATURAL LEFT JOIN author WHERE isbn=? ORDER BY book_author.authorseqno";
			PreparedStatement statement = connection.prepareStatement(select);
			statement.setInt(1, isbn);
			ResultSet resultSet = statement.executeQuery();
			
			String authors = "SELECT surname FROM (book NATURAL LEFT OUTER JOIN book_author) NATURAL LEFT JOIN author WHERE isbn=?";
			PreparedStatement authorsStatement = connection.prepareStatement(authors);
			authorsStatement.setInt(1, isbn);
			ResultSet authorsSet = authorsStatement.executeQuery();
			
			resultSet.next();
			int ISBN = resultSet.getInt(2);
			String title = resultSet.getString(3);
			int edition = resultSet.getInt(4);
			int copies = resultSet.getInt(5);
			int copiesLeft = resultSet.getInt(6);
			
			while(authorsSet.next()) {
				if(authorsSet.getString(1) != null)
					surname += authorsSet.getString(1).trim() + ", ";
			}
			
			allDetails += isbn + " - " + title + "\n" + "Edition: " + edition + " Number of Copies: "
			+ copies + " Copies left " + copiesLeft + "\n" + "Authors: " + surname;			
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
			System.err.println(sqlex);
		}
		
		if(subCall)
			return allDetails;
		else
			return "Book Lookup\n" + allDetails + "\n\n";
	}    
	
	public String returnBook(int isbn, int cusID) {
		try {
			connection.setAutoCommit(false);
		}
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while commit configuration set");
		}
		String details = "";
		try {
			subCall = true;
			if(showCustomer(cusID).equals(""))
				return "Customer " + cusID + " not found!!";
			
			String selectCustomer = "BEGIN;" +
									"SELECT * FROM customer WHERE customerid=" + cusID + " FOR UPDATE;" +
									"COMMIT;";
			PreparedStatement customerStatement = connection.prepareStatement(selectCustomer);
			customerStatement.execute();
			
			if(bookLookUp(isbn).equals(""))
				return "The book with isbn " + isbn + " does not exist!!\n\n";
			else if(!borrowed(isbn, cusID))
				return "No copies of the book with isbn " + isbn + " are currently borrowed by the customer " + cusID + "!!!\n\n";
			else {
				String selectBook = "BEGIN;" +
									"LOCK book IN SHARE ROW EXCLUSIVE MODE;" +
									"DELETE FROM cust_book WHERE isbn=" + isbn + ";" +
									"UPDATE book SET numleft=numleft+1 WHERE isbn=" + isbn + ";" +									
									"COMMIT;";
				
				PreparedStatement bookStatement = connection.prepareStatement(selectBook);
				int bookResultSet = bookStatement.executeUpdate();
				
				details = "The book with isbn " + isbn + " was returned\n\n";
			}
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while creating a statement, probably means connection to database was lost");
			System.out.println(sqlex.getMessage());
			rollback();
			return "Operation failed, book not borrowed";
			
		}
		
		finally {
			try {
				connection.commit();			
				connection.setAutoCommit(true);
			}
			
			catch (SQLException sqlex) {
				System.out.println("An exception occurred while commit configuration set");
				System.out.println(sqlex.getMessage());
				rollback();
				return "Operation failed, book not borrowed";				
			}
		}
		
		subCall = false;
		return details;
	}
	
	public String borrowBook(int isbn, int cusID, int day, int month, int year) {
		try {
			connection.setAutoCommit(false);
		}
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while commit configuration set");
		}
		String details = "";
		try {
			subCall = true;
			if(showCustomer(cusID).equals(""))
				return "Customer " + cusID + " not found!!";
			String selectCustomer = "BEGIN;" +
									"SELECT * FROM customer WHERE customerid=" + cusID + " FOR UPDATE;" +
									"COMMIT;";
			PreparedStatement customerStatement = connection.prepareStatement(selectCustomer);
			customerStatement.execute();
			
			if(bookLookUp(isbn).equals(""))
				return "The book with isbn " + isbn + " does not exist!!";
			
			String select = "SELECT numleft FROM book WHERE isbn="+ isbn + " FOR UPDATE;";
			PreparedStatement update = connection.prepareStatement(select);
			update.executeQuery();
			
			if(!copyAvailable(isbn))
				return "All copies of the book with isbn " + isbn + " are currently borrowed!!!";
			else {
				JOptionPane.showMessageDialog(null, "Locked tuple(s), ready to update.\nClick OK to continue");
				
				String date = year + "-" + month + "-" + day;
				String selectBook = "BEGIN;" +
									"INSERT INTO cust_book VALUES (" + isbn + ", '" + date + "', " + cusID + ");" +
									"UPDATE book SET numleft=numleft-1 WHERE isbn=" + isbn + ";" +
									"COMMIT;";
				
				PreparedStatement bookStatement = connection.prepareStatement(selectBook);
				bookStatement.executeUpdate();
				
				details = "Borrow:\nBook: "+ bookLookUp(isbn) + 
				"\nLoaned to: " + showCustomer(cusID) +
				"\nDue date: " + day + "/" + (month+1) + "/" + year + "\n\n";
			}			
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while creating a statement, probably means connection to database was lost");
			System.out.println(sqlex.getMessage());
			System.out.println(sqlex.getSQLState());
			System.out.println(sqlex.getErrorCode());
			rollback();
			return "Operation failed, book not borrowed";			
		}
		
		finally {
			try {		
				connection.setAutoCommit(true);
			}
			
			catch (SQLException sqlex) {
				System.out.println("An exception occurred while commit configuration set");
				System.out.println(sqlex.getMessage());
				rollback();
				return "Operation failed, book not borrowed";				
			}			
		}
		
		subCall = false;
		return details;
	}
	
	public String showAuthor(int authorId) {
		Statement s = null;
		try {
			s = connection.createStatement();
		}
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while creating a statement, probably means connection to database was lost");
		}
		String allDetails = "";
		try {
			String authors = "SELECT authorid, name, surname FROM book_author NATURAL JOIN author WHERE authorid=?";
			PreparedStatement authorStatement = connection.prepareStatement(authors);
			authorStatement.setInt(1, authorId);
			ResultSet authorSet = authorStatement.executeQuery();
			
			String books = "SELECT isbn, title FROM book_author NATURAL JOIN book WHERE authorid=?";
			PreparedStatement booksStatement = connection.prepareStatement(books);
			booksStatement.setInt(1, authorId);
			ResultSet bookSet = booksStatement.executeQuery();
			
			if (authorSet.next()) {
				int authorid = authorSet.getInt(1);
				String fName = authorSet.getString(2);
				String lName = authorSet.getString(3);
				allDetails += authorid + " - " +  fName.trim() + " - " +  lName +"\nBooks:\n";
			}
			else
				return "The customer was not found!!\n\n";
			
			while(bookSet.next()) {
				int isbn = bookSet.getInt(1);
				String title = bookSet.getString(2);
				allDetails += isbn + " " + title + "\n";
			}
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
		}
		
		return "\n\nShow Author\n" + allDetails;
	}
	
	public String showCatalogue() {
		Statement s = null;
		try {
			s = connection.createStatement();
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occoured while creating a statement, probably means connection to database was lost");
		}
		
		ResultSet resultSet = null;
		String allBooks = "";
		try {
			resultSet = s.executeQuery("SELECT isbn FROM book");
			
			while(resultSet.next()) {
				int isbn = resultSet.getInt(1);
				subCall = true;
				allBooks += bookLookUp(isbn) + "\n";
			}
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
		}
		subCall = false;
		return "Show Catalogue\n\n" + allBooks + "\n\n";
	}
	
	public String showLoanedBook() {
		String allBooks = "";
		try {
			String select = "SELECT * FROM cust_book";
			PreparedStatement statement = connection.prepareStatement(select);
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				int isbn = resultSet.getInt(1);
				int custID = resultSet.getInt(3);
				subCall = true;
				allBooks += bookLookUp(isbn) + "\nBorrowers: " + showCustomer(custID) + "\n\n";
			}
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
			System.out.println(sqlex.getMessage());
			System.out.println(sqlex.getSQLState());
			System.out.println(sqlex.getErrorCode());
		}
		subCall = false;
		return "Show Loaned Books\n\n" + allBooks + "\n\n";
	}
	
	public void closeDBConnection() {
		try {
			connection.close();
		}
		catch (SQLException sqlex) {
			System.out.println("An exception occoured while closing the connection, connection may be already lost");
		}
	}
	
	public boolean copyAvailable(int isbn) {
		Statement s = null;
		try {
			s = connection.createStatement();
		}
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while creating a statement, probably means connection to database was lost");
		}
		
		try {
			String select = "SELECT numleft FROM book WHERE isbn=?";
			PreparedStatement statement = connection.prepareStatement(select);
			statement.setInt(1, isbn);
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.next();
			int numleft = resultSet.getInt(1);
			if(numleft > 0)
				return true;
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
			System.out.println(sqlex.getMessage());
		}
		return false;
	}
	
	public boolean borrowed(int isbn, int cusID) {
		try {
			String select = "SELECT * FROM cust_book WHERE isbn=? AND customerid=?;";
			PreparedStatement statement = connection.prepareStatement(select);
			statement.setInt(1, isbn);
			statement.setInt(2, cusID);
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.getFetchSize() == 0)
				return false;			
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while executing a query, probably means the SQL is invalid");
			System.out.println(sqlex.getMessage());
		}
		return true;
	}
	
	public void rollback() {
		try {
			connection.rollback();
		}
		
		catch (SQLException sqlex) {
			System.out.println("An exception occurred while database");
		}
	}
}