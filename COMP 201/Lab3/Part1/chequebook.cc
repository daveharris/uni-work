#include <iostream>

using namespace std;

#include "account.h"

#define MAX_ANSCHARS 256

main(){
  int value;
  char answer[MAX_ANSCHARS];

  cout << "Chequebook Balancing Program...\n";
  cout << '\n';

  cout << "Initial balance?\n";
  cin >> value;
  //Create an new Account object
  Account newAccount(value);

  while (1) {
    cout << '\n';
    cout << "Cheque [c], Deposit [d] or Quit [q] ?\n";
    cin >> answer;

    if(!strcmp(answer, "c")) {
      cout << "Cheque Amount?\n";
      cin >> value;
      //Debit the account by the given amount
      newAccount.debit(value); 
    }
    else if(!strcmp(answer, "d")) {
      cout << "Deposit Amount?\n";
      cin >> value;
      //Credit the account by the given amount
      newAccount.credit(value);
    }
    else if(!strcmp(answer, "q"))
      break;
    else 
      cout << "Sorry, I didn't understand that!\n";
  }

  cout << '\n';
  cout << "Final State of Cheque Account:\n";
  
  //Print out the account at the end
  newAccount.print();

  cout << '\n';
  cout << "Chequebook Program Done.\n";

}
