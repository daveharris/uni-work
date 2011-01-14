#include "account.h"
#include <iostream>
#include <string>

using namespace std;

Account::Account(int id, char *n, int init){
  idnumber = id;
  strncpy(name, n, 32);
  amount = init;
  transcount = 0;
}

Account::Account(){
  idnumber = 0;
  name[0] = '\0';
  amount = 0;
  transcount = 0;
}

void Account::credit(int x){
  amount += x;
  ++ transcount;
}

void Account::debit(int x){
  amount -= x;
  ++ transcount;
}

int Account::getid(){
  return idnumber;
}
int Account::getbalance(){
  return amount;
}

void Account::print(){
  cout << "-----------------------------------------\n";
  cout << "Account ID number : " << idnumber << '\n';
  cout << "Account: Name is: " << name << '\n';
  cout << "Account: Balance is $" << amount << '\n';
  cout << "Account: Number of Transactions was " << transcount << '\n';
  cout << "-----------------------------------------\n";
}