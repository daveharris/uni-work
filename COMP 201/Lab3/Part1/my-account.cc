//Include the account the header file
#include "account.h"
//So we can use the cout and cin functions
#include <iostream>
using namespace std;

Account::Account(int x){
  //Create a new account with initial balance as given
  transcount = 0;
  amount = x;
}
Account::Account() {
  //Create a new account with initial balance as 0
  transcount = 0;
  amount = 0;
}

void 
Account::credit(int x){
  //Add money to account and update the transactions counter
  amount += x;
  transcount++;
}

void 
Account::debit(int x){
  //Take money out of account and update the transactions counter
  amount -= x;
  transcount++;
}

int 
Account::balance(){
  return amount;
}

void 
Account::print(){
  //Print out the balance and transactions counter
  cout << "------------------------\n";
  cout << "Account: Balance is $" << amount << "\n";
  cout << "Account: Number of Transactions was " << transcount << "\n";
  cout << "------------------------" << endl;
}

