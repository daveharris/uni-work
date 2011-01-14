import javax.swing.*;
import jds.Map;
import java.io.*;
import java.util.Enumeration;
import java.util.StringTokenizer;

	/**
	* a (very simple) Bank simulator.
	* In this simulator, the Bank maintains a list of Accounts.
	* Accounts may be Created,Closed,Deposited to,Withdrawn from and balance
	* queried. Transactions may be supplied in a batch in a file.
	* Each of the transaction methods returns a string reporting the result of
	* the method
	*/
public class Bank {

	/** the collection which contains the accounts */
	private Map accounts;

	//constructor to create a Bank object

	public Bank() {
		//Gives the user the option of using BSTMap or SortedArrayMap
		int option = JOptionPane.showConfirmDialog(null, "Use BST?", "choose one", JOptionPane.YES_NO_OPTION);
		if(option == 0)
			accounts = new BSTMap();
		else
			accounts = new SortedArrayMap();
	}

	/** Open an account
	*	Returns a String reporting the action performed or the error.
	*/
	public String create(AccountNumber accNum) {
		//Check that the key isn't in the map already
		if(accounts.containsKey(accNum))
			return (accNum + " already exists!");
		else {
			//Add it to the map
			accounts.set(accNum, new Account(accNum));
			return (accNum + " has been added");
		}
	}

	/** Close an account
	*	Returns a String reporting the action performed or the error.
	*/
	public String close(AccountNumber accNum) {
		//Check that the key is in the map, and remove the key
		if(accounts.containsKey(accNum)) {
			accounts.removeKey(accNum);
			return (accNum + " has been closed");
		}
		//Say that the key isn't in the map
		else
			return (accNum + " doesn't exist!");
	}

	/** Perform a deposit in an account
	*	Returns a String reporting the action performed or the error.
	*/
	public String deposit(AccountNumber accNum, double amount) {
		//Check that the key is in the map
		if(accounts.containsKey(accNum)) {
			Account acc = (Account)accounts.get(accNum);
			//Call on the deposit() method to update the account balance
			acc.deposit(amount);
			//Say that the deposit has taken place and print out the current balance
			return (amount + " has been deposited in " + accNum
				+ ", new balance is " + acc.balance());
		}
		//say that the deposit didn't take place
		else
			return (amount + " couldn't be deposited in " + accNum);
	}

	/** perform a withdrawal from an account
	*	Returns a String reporting the action performed or the error.
	*/
	public String withdraw(AccountNumber accNum, double amount) {
		//Check that the key is in the map
		if(accounts.containsKey(accNum)) {
   		Account acc = (Account)accounts.get(accNum);
   		//Call on the withdraw() method to update the account balance
			acc.withdraw(amount);
			//Say that the withdraw has taken place and print out the current balance
			return (amount + " has been withdrawn in " + accNum
				+ ", new balance is " + acc.balance());
		}
		//say that the withdraw didn't take place
		else
			return (amount + " couldn't be withdrawn in " + accNum);
	}

	/** perform a balance query for an account
	*	Returns a String reporting the account and its balance.
	*/
	public String balance(AccountNumber accNum) {
		//Check that the key is in the map
		if(accounts.containsKey(accNum)) {
   		Account acc = (Account)accounts.get(accNum);
   		//return the account number and the balance, so it can be printed
			return ("Balance of " + accNum + " is " + acc.balance());
		}
		//Say that the account number is not in the map
		else
			return ("Balance of " + accNum + " couldn't be found");
	}

	/** list all the accounts */
	public String listAccounts() {
		StringBuffer ans = new StringBuffer("All Accounts:\n");
		for (Enumeration e = accounts.elements(); e.hasMoreElements(); ) {
			Account acc = (Account)(accounts.get(e.nextElement()));
			ans.append(acc.toString()).append("\n");
		}
		return ans.toString();
	}


	/**
	* read transactions from the given file and pass them on to the teller.
	* No attempt is made to check the file for validity,
	*
	* @param f BufferedReader from which to read transactions.
	*/
	public String processTransactions(BufferedReader f) {
		try {
			while (true) {
				String line = f.readLine();
				if (line == null)
					break;							//end of file

				for (Enumeration e = new StringTokenizer(line); e.hasMoreElements(); )
				{
					String action = ((String)e.nextElement()).toLowerCase();
					long num = Long.parseLong((String)e.nextElement());
					AccountNumber accNum = new AccountNumber(num);
					if (action.equals("n")) {
						create(accNum);
						break;
					}
					else if (!accounts.containsKey(accNum))
						break;
					else if (action.equals("c"))
						close(accNum);
					else if (action.equals("d")) {
						double amt = Double.parseDouble((String)e.nextElement());
						deposit(accNum, amt);
					}
					else if (action.equals("w")) {
						double amt = Double.parseDouble((String)e.nextElement());
						withdraw(accNum, amt);
					}
				}
			}
			return ("Finished Processing file\n");
		}
		catch (IOException ioe) {
			return ("Problem reading from file: " + ioe);
		}
	}													 // end processTransactions
}
