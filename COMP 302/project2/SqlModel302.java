/*
 * SqlModel.java
 * Author:
 * Created on:
 */

 
public class SqlModel302 {
    
    /** Creates a new instance of SqlModel */
    public SqlModel302(String userid, String password) {
    }
    
    public String showAllCustomer() {
        return "Show All Customers\n";
    }
    
    public String showCustomer(int customerId) {
        return "Show Customer\n";
    }
    
    public String showAllAuthor() {
        return "Show All Authors\n";
    }
    
    public String bookLookUp(int isbn) {
        return "Book LookUp\n";
    }    
    
    public String returnBook(int isbn, int cusID) {
        return "Return:\nISBN: " + isbn + " CustomerId: " + cusID + "\n";
    }
    
    public String borrowBook(int isbn, int cusID, int day, int month, int year) {
		return "Borrow:\n ISBN: " +  isbn + " --- CustomerId: " + cusID + 
		" --- Due Date: " + day + " --- " + month + " --- " + year + " ---\n";
    }
    
    public String showAuthor(int authorId) {
        return "Show Author\n";
    }
    
    public String showCatalogue() {
        return "Show Catalogue\n";
    }
    
    public String showLoanedBook() {
        return "Show Loaned Books\n";
    }
    
    public void closeDBConnection() {
    }
    
}
