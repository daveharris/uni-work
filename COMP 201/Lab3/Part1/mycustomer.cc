//Include the customer header file
#include "customer.h"

//So we can use cout and cin
#include <iostream>
using namespace std;

Customer::Customer(char *newname){
  //Create an new customer with a name 
  for(int i=0; i<50; i++) {
      name[i] = newname[i];
  }
  //Reset the two instances of the stats class
  purchase_stats.reset();
  payment_stats.reset();
}

void Customer::purchase(int x){
  //When a customer purchases something, update the stats and account data
  purchase_stats.data(x);
  acct.debit(x);
}
  
void Customer::payment(int x){
  //When a customer makes a payment, update the stats and account data
  payment_stats.data(x);
  acct.credit(x);
}

void Customer::report(){
  //Print out the stastics from stats and account classes
  cout << "================================" << endl;
  cout << "Report on customer \"" <<  name << "\" :" << endl;
  cout << "Balance is $" << acct.balance() << endl;
  cout << "Purchase Statistics Since Last Report:" << endl;
  purchase_stats.print();
  cout << "Payment Statistics Since Last Report:" << endl;
  payment_stats.print();
  cout << "================================" << endl;
  //reset the data in stats class
  purchase_stats.reset();
  payment_stats.reset();
}


