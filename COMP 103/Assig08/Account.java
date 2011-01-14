/**
	* A class to represent a bank account
	*/

public class Account {

	AccountNumber number;
	double balance = 0.0;

	/**
		* Create a new bank account with the given account number.
		* @param accNumber may be	or more decimal digits in length
		*/
	protected Account(AccountNumber accNumber) {
		number = accNumber;
		return;
	}

	/** Add an amount to this account's balance
		* Return the new balance 
		*/
	public double deposit(double amount) {
		balance += amount;
		return (balance);
	}

	/** Subtract an amount from this account's balance
		* Return the new balance 
		*/
	public double withdraw(double amount) {
		balance -= amount;
		return (balance);
	}

	/** Return this account's current balance. */
	public double balance() {
		return (balance);
	}

	/** compare this account with another for equality */
	public boolean equals(Object acc) {
		Account other = (Account)acc;
		return number.equals(other.number);
	}

	/** Return a string representation of this account. */
	public String toString() {
		return (number.toString() + " balance =" + balance);
	}


}
