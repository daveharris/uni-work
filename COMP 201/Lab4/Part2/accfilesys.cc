#include <iostream>
#include <string>
#include "account.h"
#include "accountfile.h"

using namespace std;

main() {

  AccountFile af;
  int resultok;
  string command;
  char username[32];
  int idn, money;
  
  cout << "\nWelcome to the account file system!\n";

  resultok = af.afopen("afs.data");
  if (resultok) 
    cout << "Account file opened ok.\n";
  else {
    cout << "Unable to open Account File!\n";
    exit(1);
  }

  while (1) {
    cout << "Command: [list, create, credit, debit, print, quit]\n";
    cin >> command;
    if (command == "quit") break;

    else if (command == "list") {
      af.list();
    }
    else if (command == "create") {
      cout << "Account ID number?\n"; cin >> idn;
      cout << "Account user name?\n"; 
      cin.getline(username, 32);
      cin.getline(username, 32);
      cout << "Account initial balance?\n"; cin >> money;
      Account a(idn, username, money);
      resultok = af.put(a);
      if (resultok)
	cout << "Account created ok.\n";
      else
	cout << "Unable to create account.\n";
    }
    else if (command == "credit") {
      cout << "Account ID number?\n"; cin >> idn;
      Account a;
      resultok = af.get(idn, a);
      if (resultok) {
	cout << "Account got ok.\n";
	cout << "Amount to credit?\n"; cin >> money;
	a.credit(money);
	cout << "Credit done.\n";
	resultok = af.put(a);
	if (resultok) cout << "Account put back ok.\n";
	else cout << "Unable to put back account.\n";
      }
      else cout << "Unable to get account.\n";
    }
    else if (command == "debit") {
      cout << "Account ID number?\n"; cin >> idn;
      Account a;
      resultok = af.get(idn, a);
      if (resultok) {
	cout << "Account got ok.\n";
	cout << "Amount to debit?\n"; cin >> money;
	a.debit(money);
	cout << "Debit done.\n";
	resultok = af.put(a);
	if (resultok) cout << "Account put back ok.\n";
	else cout << "Unable to put back account.\n";
      }
      else cout << "Unable to get account.\n";
    }
    else if (command == "print") {
      Account a;
      cout << "Account ID number?\n"; cin >> idn;
      resultok = af.get(idn, a);
      if (resultok) {
	cout << "Account got ok.\n";
	cout << "Account printout:\n";
	a.print();
	cout << "Print done.\n";
      }
      else cout << "Unable to get account.\n";
    }
    else
      cout << "Unrecognised command!\n";
  }

  af.afclose();
  cout << "Account file system done.\n";
}
