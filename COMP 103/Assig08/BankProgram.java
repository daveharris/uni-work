import javax.swing.*;
import java.awt.BorderLayout;
import java.io.*;
import java.awt.event.*;

/**
 * GUI for the Bank program.<br />
 The user interface provides buttons for performing the actions of
 <I>Create</I>, <I>Close</I>, <I>Deposit</I>, <I>Withraw</I> and <I>Balance</I>
 for creating an account, closing an account, depositing money in an account 
 withdrawing money from an account and checing the balance of an account 
 respectively. It also provides buttons which allow creating a random 
 transaction file and processing a transaction file. Also there is a button 
 for quiting the program
*/
public class BankProgram implements ActionListener {

	/** the output area */
	private JTextArea textArea; 

	/** the Bank object which administers the accounts */
	private Bank theBank;

	/** main method creats a new Bank Object user interface */
	public static void main(String args[]){
		new BankProgram();
	}


	/** constructor for the bank user interface constructs an interface with an
			output area and sets up all the buttons */
	public BankProgram() {
		JFrame frame = new JFrame("Account Manager");
		frame.setSize(800,600);

		//The text area
		textArea = new JTextArea(60, 60);
		frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
		textArea.setTabSize(5);


		//The buttons
		JPanel buttonPanel = new JPanel();
		JPanel fileButtonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
		frame.getContentPane().add(fileButtonPanel, BorderLayout.SOUTH);

		// I made a method below to make it easier to add the buttons
		addButton(buttonPanel, "Create");
		addButton(buttonPanel, "Close");
		addButton(buttonPanel, "Deposit");
		addButton(buttonPanel, "Withdraw");
		addButton(buttonPanel, "Balance");
		addButton(buttonPanel, "List A/Cs");
		addButton(fileButtonPanel, "Make File");
		addButton(fileButtonPanel, "Process File");
		addButton(buttonPanel, "Quit");
		frame.setVisible(true);

		theBank= new Bank();
	}

	/** easy button adder */
	private void addButton(JPanel panel, String name){
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
	} 

	/* Respond to button presses */
	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand().equals("Create"))
			createAcount();
		else if (e.getActionCommand().equals("Close"))
			closeAccount();
		else if (e.getActionCommand().equals("Deposit"))
			deposit();
		else if (e.getActionCommand().equals("Withdraw"))
			withdraw();
		else if (e.getActionCommand().equals("Balance"))
			balance();
		else if (e.getActionCommand().equals("List A/Cs"))
			list();
		else if (e.getActionCommand().equals("Make File"))
			makeFile();
		else if (e.getActionCommand().equals("Process File"))
			processFile();
		else // action must be quit
			System.exit(0);
	}

	/** function to get a <I>long</I> number, with error checking */
	private AccountNumber askAccountNumber(String msg) {
		boolean flag = true;
		String str;
		long num = 0;
		while (flag) {
			try {
	str = JOptionPane.showInputDialog(msg);
	num =	Long.parseLong(str);
	flag = false;
			}
			catch (NumberFormatException e) {
	textArea.append("invalid number, please try again"+"\n");
			}
		}
		return new AccountNumber(num);
	}

	/** function to get a <I>double</I> number, with error checking */
	private double askAmount(String msg) {
		String str;
		double amt = 0;
		while (true) {
			try {
	str = JOptionPane.showInputDialog(msg);
	amt =	Double.parseDouble(str);
	break;
			}
			catch (NumberFormatException e) {
	textArea.append("invalid number, please try again"+"\n");
			}
		}
		return amt;
	}

	/** create a new account */
	private void createAcount() {
		AccountNumber accNum = askAccountNumber("Enter your account number");
	
		textArea.append(theBank.create(accNum)+"\n");
	}

	/** close an account */
	private void closeAccount() {
		AccountNumber accNum = askAccountNumber("Enter your account number");
		textArea.append(theBank.close(accNum)+"\n");
	}

	/** deposit an amount in an account */
	private void deposit() {
		AccountNumber accNum = askAccountNumber("Enter your account number");
		double amount = askAmount("Enter the amount to deposit");
		textArea.append(theBank.deposit(accNum,amount)+"\n");
	}

	/** withdraw an amount from an account */
	private void withdraw() {
		AccountNumber accNum = askAccountNumber("Enter your account number");
		double amount = askAmount("Enter the amount to withdraw");
		textArea.append(theBank.withdraw(accNum,amount)+"\n");
	}

	/** print the balance of an account */
	private void balance() {
		AccountNumber accNum = askAccountNumber("Enter your account number");
		textArea.append(theBank.balance(accNum)+"\n");
	}

	/** list all the accounts */
	private void list() {
		textArea.append(theBank.listAccounts()+"\n");
	}


	/** make a file of random transactions - for testing */
	private void makeFile() {
		String fname = JOptionPane.showInputDialog("Enter the name of the file to process");
		TransFileGenerator gen = new TransFileGenerator(fname);
		try {
			gen.makeTransactions();
			textArea.append("Transaction file " + fname + " created\n");
		}
		catch( IOException e ) {
			textArea.append("Trouble opening/writing transaction file "+ fname +": " + e +"\n");
		} 
	}

	/** process a file of transaction discriptions */
	private void processFile() {
		String fname = JOptionPane.showInputDialog("Enter the name of the file to process");
		theBank = new Bank();
		textArea.setText("Processing File\n");
		try {
			BufferedReader f = new BufferedReader(new FileReader(fname));

			StopWatch s1 = new StopWatch();
			s1.start();
			String result = (theBank.processTransactions(f));
			s1.stop();
			textArea.append(result);
			textArea.append( "Processing the actions took: " +
					 s1.getTotal() + " milliseconds.\n" );
		}
		catch ( FileNotFoundException fnfe ) {
			textArea.append( "File " + fname + " not found: " + fnfe +"\n");
		}
		catch( IOException ioe ) {
			textArea.append( "Problem reading from file " + fname + ": " + ioe +"\n");
		}


	}

}
