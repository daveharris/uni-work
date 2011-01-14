import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * gets some configuration parameters from the user then writes
 * random transactions to a file.
 *
 */
public class TransFileGenerator {

  long currentAccNum    =  0;  // starting point for sequential acc#s
  Vector accNumbers = new Vector();    // used to record acc#s used
  Random r          = new Random();    // uniform random number generator

  String outfile;
  int    numAccounts;
  int    numTransactions;
  int    freqNew;
  int    freqClose;
  int    order;

  /**
   * set up a instance of this object, prompiting for parameters
   */
  public TransFileGenerator(String fname) {

    outfile = fname;
    numAccounts = Integer.parseInt
      ( JOptionPane.showInputDialog
	( null, "How many initial accounts?" )
	);
    numTransactions = Integer.parseInt
      ( JOptionPane.showInputDialog
	( null, "Generate how many additional transactions ?" )
	);
    // Frequencies are a percentage of total transactions
    // 50 entered would mean 50% of the total number of transactions
    freqNew = Integer.parseInt
      ( JOptionPane.showInputDialog
	( null, "Frequency of New Accounts (% of transactions)" )
	);

    freqClose = Integer.parseInt
      ( JOptionPane.showInputDialog
	( null, "Frequency of Closure (% of transactions)" )
	);

    order = JOptionPane.showOptionDialog
      ( null,
	"Generate Account numbers ascending, descending or random ?",
	"generate account numbers choice",
	JOptionPane.DEFAULT_OPTION,
	JOptionPane.PLAIN_MESSAGE,
	null,
	new String[] { "Ascending", "Descending", "Random" },
	null );
    if (order==1) currentAccNum = numAccounts;

  }



  /**
   * get the next new account number according to the
   * specified ordering: Random, Ascending or Descending
   */
  private long nextAcc() {

    if( order == 2 ) { //   Random 
      while (true){
	AccountNumber ac = new AccountNumber(r.nextInt(numAccounts * 2 ));
	if (!accNumbers.contains(ac))
	  return ac.getNum();
      }
    }
    else if( order == 0 ) { // Ascending
      currentAccNum++;
      return ( currentAccNum );
    }
    else {  // Descending 
      currentAccNum--;
      return ( currentAccNum );
    }
  }

  private char[] choices    = new char[] {'D','W','B' };

  /**Generate the transactions. Write to file.
   * @throws IOException if there is any trouble opening/writing the specified file
   */
  public void makeTransactions() throws IOException {
    PrintWriter p = new PrintWriter( new FileWriter( outfile ) );
    

    for (int i=0; i<numAccounts; i++){
      AccountNumber accNum =  new AccountNumber(nextAcc());    // get a new Acc# 
      p.println( "N " + accNum.getNum());
      accNumbers.add( accNum );
    }

    for( int i = 0; i < numTransactions; i++ ) {
      int index = r.nextInt(accNumbers.size() );
      long num = ((AccountNumber)accNumbers.elementAt(index)).getNum() ;
      int amt = r.nextInt( 1000 );

      char action = choices[r.nextInt(3)];

      int ran = r.nextInt(100);
      if (accNumbers.size()==0) 
	action = 'N';
      else if(ran < freqNew )
	action = 'N';
      else if (ran < (freqNew+freqClose)){
	if (accNumbers.size()>1) 
	  action = 'C';
      }

      switch( action ) {
      case 'C':
	p.println( "C " + num);
	accNumbers.removeElementAt( index );
	break;
      case 'N':
	AccountNumber accNum =  new AccountNumber(nextAcc());    // get a new Acc# 
	p.println( "N " + accNum.getNum());
	accNumbers.add( accNum );
	break;
      case 'D':
	p.println( "D " + num +" "+ amt);
	break;
      case 'W':
	p.println( "W " + num + " " + amt );
	break;
      case 'B':
	p.println( "B " + num);
	break;
      }
    }
    p.flush();    
  }


  /**
   * get configuration options from the user and then write
   * out transactions to a file
   */
  public static void main( String[] args ) {

    String fname;
    if (args.length>0)
      fname = args[0];
    else
      fname = JOptionPane.showInputDialog ( null, "Filename for Output ?" );

    TransFileGenerator mt = new TransFileGenerator(fname);
    try {
      mt.makeTransactions();
    }
    catch( IOException e ) {
      System.err.println( "Trouble opening/writing output file " + e );
    } 

    System.exit(0);
  }
}
