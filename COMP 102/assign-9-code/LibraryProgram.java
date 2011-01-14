/*	Author:	David Harris
 *	Date: 28/05/03
 *	ID:	300069566
 *	Description: Assignment	9 of COMP102 for 2003.
 *
 */

//======================================================================
//======================================================================

import javax.swing.*;
import java.io.*;


public class LibraryProgram	{
  public static	void main(String[] args) {

	  Librarian libby = new Librarian();

	  String[] menu={
		"Book",
		"Lookup",
		"Calalogue",
		"New Client",
		"Show client",
		"Show People",
		"Borrow",
		"Return",
		"Open",
		"Save",
		"Exit"
	  };

	  while(true){
		  int choice = JOptionPane.showOptionDialog(null,
			  "Library Program Version 0.1: Command?", "Menu",
			  JOptionPane.YES_NO_CANCEL_OPTION,
			  JOptionPane.QUESTION_MESSAGE,	null, menu,	menu[2]);


		  if (choice < 0) System.exit(0);

		  System.out.println("********************************");
		  System.out.println("Your command:	" + menu[choice]);
		  System.out.println("********************************");

		  switch (choice){
		  case 0: libby.newbook();
		   break;
		  case 1: libby.showbook();
		   break;
		  case 2: libby.showcatalogue();
		   break;
		  case 3: libby.newclient();
			break;
		  case 4: libby.showclient();
		   break;
		  case 5: libby.showpeople();
			break;
		  case 6:	libby.Borrow();
			break;
		 case 7: libby.Return();
			break;
		 case 8: libby.open();
		   break;
		  case 9: libby.save();
		   break;
		  case 10: System.exit(0);
		   break;
	  }
	  System.out.println("********************************");
	  }
  }
}

class Librarian	{
	private Map catalogue;
	private Map people;

	public Librarian() {
		catalogue = new	Map(200);
		people = new Map(200);
	}


	public void open() {
		int	nb = 0;
		Book b;

		try	{
			String inFileName = JOptionPane.showInputDialog("Input file	name:");
			FileReader inStream = new FileReader(inFileName);
			BufferedReader ins = new BufferedReader(inStream);

			catalogue.reset();

			while (true) {
				String line = ins.readLine();
				if (line == null)
					break;
				else if (line.equals("BOOK")) {
					b = new	Book(ins);
					if (!catalogue.insert(b.GetID(), b))
					System.out.println("Unable to install book loaded from file!");
					nb++;
				}

				else {
						System.out.println("Load halted: unexpected	data in file!");
						break;
				}
		}
		ins.close();

		}
		catch (IOException ex) {
			System.out.println("Oops! Error with file!");
		}

		System.out.println("Number of books loaded:" + nb);
	}


	public void save() {
		int	nb = 0;
		Book b;

		try	{
			String outFileName = JOptionPane.showInputDialog("Output file name:");
			FileWriter outStream = new FileWriter(outFileName);
			PrintWriter	outs = new PrintWriter(outStream);

			catalogue.getInit();
			while (catalogue.getNext())	{
				b =	(Book) catalogue.getValue();
				b.Save(outs);
				nb++;
			}
			System.out.println("");
			System.out.println("Number of books saved: " + nb);

			outs.close();

		}
		catch (IOException ex) {
			System.out.println("Oops! Error with file!");
		}
	}


	public void	newbook() {
		String id;
		Book b;
		id = JOptionPane.showInputDialog("ID for the new book:");
		if (catalogue.lookup(id) == null){
			b = new Book(id);
			System.out.println("New book created:");
			b.Print();
			System.out.println("");
			if (catalogue.insert(id, b))
			System.out.println("New book inserted in library.");
			else
			System.out.println("Unable to insert new book!");
		}
			else {
			System.out.println("Requested book ID is already in use!");
			}
	}

	public void	showbook() {
		String keyID = JOptionPane.showInputDialog("ID to look up:");
		System.out.println("Looking up book: " + keyID);
		Book b = (Book)
		catalogue.lookup(keyID);
		if (b == null){
			System.out.println("There is no book with that ID!");
		}
		else {
			b.Print();
		}
	}

	public void showcatalogue()	{
		int	n;
		Book b;
		n = 0;
		System.out.println("List of catalogue:");
		catalogue.getInit();

		while (catalogue.getNext())	{
			b = (Book) catalogue.getValue();
			b.Print();
			n++;
		}
		System.out.println("");
		System.out.println("Number of entries shown: " + n);
	}
	
	public void	newclient() {
		String id;
		Client c;
		id = JOptionPane.showInputDialog("ID for the new client");

		if (people.lookup(id) == null){
			c = new Client(id);
			System.out.println("New book created:");
			c.Print();
			System.out.println("");
				if (people.insert(id, c))
				System.out.println("New client inserted in library.");
				else
				System.out.println("Unable to insert new client!");
		}
		else {
		  System.out.println("Requested	client ID is already in use!");
		}
	  }

	  public void showclient() {
		String id =	JOptionPane.showInputDialog("ID to look up:");
		System.out.println("Looking up client: " + id);
		Client c = (Client) people.lookup(id);
		if (c == null){
			System.out.println("There is no client with that ID!");
		}
			else {
				c.Print();
			}
	}
	
	 public void showpeople() {
		int	n = 0;
		Client c;
		System.out.println("List of people:");
		people.getInit();

		while (people.getNext()) {
			c = (Client) people.getValue();
			c.Print();
			n++;
		}
		
		System.out.println("");
		System.out.println("Number of entries shown: " + n);
	}

	public void Borrow() {
  String clientCode = JOptionPane.showInputDialog("Enter client code");
		Client c = (Client)people.lookup(clientCode);
		
		
		if (c == null){
			System.out.println("Client code is " + clientCode +
									"\n Client code is unknown!");
			return;
		}
		c.Print();
		String bookid = JOptionPane.showInputDialog("Enter book code");
		Book b = (Book) catalogue.lookup(bookid);

		if (b == null) {
			System.out.println("Book code is " + bookid +
									"\n Book code is unknown!");
			return;
		}
		b.Print();
		if (b.getBorrower() !=null) {
			System.out.println(clientCode + " already has a book borrowed!");
		}
		if (c.Borrow(clientCode) == false) {
			System.out.println(clientCode + " can only have one book aout at once");
			return;
		}
		b.Borrow(clientCode);
		System.out.println(clientCode + " has borrowed the book");
	}

	public void Return() {
		String bookCode = JOptionPane.showInputDialog("Enter client code");
		Book b = (Book) catalogue.lookup(bookCode);
		if ( b != null)
			showbook();
		else {
			System.out.println("Book code is " + bookCode +
									"\n Book code is unknown!");
			return;
		}


		String clientCode = b.getBorrower();
		Client c = (Client) people.lookup(clientCode);

		c.Return();
		b.Return();
		System.out.println("Book has been returned");
	}

}

//======================================================================
/**	Book: represents a single book in the library.
  */
//======================================================================

class Book{
  private String id;
  private String author;
  private String title;
  private String borrowercode = null;
  

  public Book(String newname){
	id = newname;
	author = JOptionPane.showInputDialog("Enter book author: ");
	title = JOptionPane.showInputDialog("Enter book title: ");
  }

  public Book(BufferedReader f) throws IOException {
	  // Note that the throws line above means any exception
	  // causes	this method	to return, passing the exception
	  // to the place where	this method	was called from.
	  // This is a handy way to	avoid dealing with exceptions
	  // in	a lower	level class.

	id = f.readLine();
	title = f.readLine();
	author = f.readLine();
	String extraline = f.readLine(); // In later steps this will be the borrower code

  }

  public void Print() {
	if (id != null) {
	  System.out.print("Book - Author: " + author);
	  System.out.print(" Title: "	+ title);
	  System.out.println(" ID: " + id);
    }
	else 
	  System.out.println("*IN*");
	// In later steps this will print the borrower code
  }
	
  public String GetID(){
	return id;
  }

  public void Save(PrintWriter f) {
	f.println("BOOK");
	f.println(id);
	f.println(title);
	f.println(author);
	f.println("*END*");	// In later	steps this will	be the borrower	code

  }

  // For Step 3	...
  public void Borrow(String	clientcode){
    borrowercode = clientcode;
  }
  
  public void Return(){
     borrowercode = null;
  }

  public String getBorrower(){
    return borrowercode;
  }

}


//======================================================================
//======================================================================

// For Step	2 ...
class Client {
// Fields should go	here ...
	private String client;
	private String phone;
	private String clientCode;
	private String borrowedid = null;

// Methods should be as	follows	...
	public Client (String clientcode) {
		clientCode = clientcode;
		client = JOptionPane.showInputDialog("Enter your name");
		phone =	JOptionPane.showInputDialog("Enter your phone number");
	}

	public Client (BufferedReader f) throws	IOException	{
		clientCode = f.readLine();
		client = f.readLine();
		phone = f.readLine();
		String extraline = f.readLine();
	}

	public void Print() {
		if (borrowedid == null) {
			String msg = "List of all clients of library:" +
							"\n	Name: " + client +
							"\n	Client Code: " + clientCode +
							"\n	Phone: " + phone +
							"\n	Number of books	borrowed " +
							"\n + \n" +"Number of clients shown: ";
			System.out.println(msg);
		}
	}

	public String GetCode() {
		return clientCode;
	}

	public void Save (PrintWriter f) {
		f.println("CLIENT");
		f.println(clientCode);
		f.println(client);
		f.println(phone);
		f.println("*END*");
	}


// For Step	3 ...
	public boolean Borrow(String id) {
		if ( borrowedid != null)
			return false;
		else {
			borrowedid = id;
			return true;
		}
	}
	
	public void Return(){
		borrowedid = null;
	}
}


//======================================================================
/**	Generic	Map	class, as discussed	in lectures.  This class maps
 *	 Objects to	Objects, using two arrays.	The	array "keys" is	used
 *	 to	store product codes, and the array "values"	is used	to store
 *	 actual	products.  The arrays are always used so that when you
 *	 find a	code in	position i in the keys array, the matching product
 *	 is	in position	i of the values	array.
 *		
 * This	version	also has a simple "traversal" methods getInit, and
 * getNext.	You	first call getInit,	and	then each call to getNext
 * positions itself	at the next	entry in the map, returning	true when
 * there is	one, and false when	there are none left. The methods
 * getKey and getValue are used	to get the details of the current
 * entry.
 **/
//======================================================================


class Map {
	private	Object keys[];
	private	Object values[];
	private	int	MAX;
	private	int	pos;
		
	public Map() {
		MAX	= 100;
		keys = new Object[MAX];
		values = new Object[MAX];
	}

	public Map(int n) {
		MAX	= n;
		keys = new Object[MAX];
		values = new Object[MAX];
	}
	
	public void	reset() {
		  for (int i = 0; i	< MAX; i++)	{
			  keys[i] = null;
			  values[i] = null;
		  }
	}

	public boolean insert(Object k,	Object v) {
		if (lookup(k) != null) return false;
		int	i;
		i =	0;
		while (i < MAX) {
			if (keys[i] == null) {
				keys[i] = k;
				values[i] =	v;
				return true;
			}
			i++;
		}
		return false;
	}

	public Object lookup(Object x) {
		int	i;
		i = 0;
		while (i < MAX) {
			if (keys[i] != null && keys[i].equals(x)) {
				return values[i];
			}
			i++;
		}
		return null;
	}
	

	public void	getInit() {
		pos	= -1;
	}


	public boolean getNext() {
	pos++;
		while (pos < MAX) {
		if (keys[pos] != null) {
				return true;
			}
		pos++;
		}
		return false;
	}

	public Object getValue() {
	return values[pos];
	}

	public Object getKey() {
	return keys[pos];
	}
}