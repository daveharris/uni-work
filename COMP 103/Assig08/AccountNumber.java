/* A class to represent an account number */

public class AccountNumber implements Comparable {

// MINVAL and MAXVAL are used to ensure acc# are just 8 digits 
  private static final long MINVAL = 10000000;  
  private static final long MAXVAL = 100000000;  


  long num;  // The account number

  AccountNumber( long n ) {
    while (n < MINVAL)
      n = n+MINVAL;

    while (n >= MAXVAL)
      n = n-MINVAL;
    
    num = n;
  }

  public int compareTo( Object other ) {
    AccountNumber o = (AccountNumber) other;
    if (num > o.num) return 1;
    else if (num < o.num) return -1;
    else return 0;
  }

  public boolean equals( Object other ) {
    return ( num == ((AccountNumber) other).num );
  } 

  public long getNum(){
    return num;
  }


  /** return a string representation of this account.
   * just shows the Account Number. */

  public String toString() {
    return( "Acc# " + num );
  }

}
