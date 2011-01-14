#include <iostream>

#include "customer.h"

using namespace std;

main(int argcount, char *argarray[])
{
  cout << "Customer Checking Program...\n\n";

  if (argcount != 2) {
    cout << "Usage: checkout name\n";
    return 1;
  }

  cout << "Customer Name is \"" << argarray[1] << "\"\n\n";

  cout << "Now Input Transactions in lines as follows:\n";
  cout << "name payment amount\n";
  cout << "name purchase amount\n\n";
  
  cout << "Only transactions for \"" << argarray[1] << "\" will be used.\n\n";

  Customer who(argarray[1]);

  char name[50];
  char option[50];
  int value;

  while (cin >> name) {
    cin >> option;
    cin >> value;

    if (!strcmp(name, argarray[1])) {
      cout << "Customer Does Match.\n";
      if (!strcmp(option, "payment")) {
	cout << "Doing Payment Transaction.\n";
	who.payment(value);
      } else if (!strcmp(option, "purchase")) {
	cout << "Doing Purchase Transaction.\n";
	who.purchase(value);
      } else {
	cout << "Sorry: Invalid Transaction Option!\n";
      }
    } else {
      cout << "Customer Does Not Match.\n";
    }
  }

  cout << "End of Input.\n\n";
  cout << "Customer Report Follows:\n";

  who.report();

  cout << "Customer Checking Program Done.";

}
