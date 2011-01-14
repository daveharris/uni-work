#include <iostream>

using namespace std;

#include "account.h"

void
demoprint(Account z);

main(){

  cout << "Demo: Demonstration Program for Account Class...\n";

  cout << "Demo: Declaring Account Now...\n";
  Account a(0);
  cout << "Demo: Declaring Account Done.\n";

  demoprint(a);

  cout << "Demo: Creditting 72 to Account...\n";
  a.credit(72);
  cout << "Demo: Creditting Account Done.\n";

  demoprint(a);

  cout << "Demo: Debitting 27 from Account Now...\n";
  a.debit(27);
  cout << "Demo: Declaring Account Done.\n";

  demoprint(a);
  
  cout << "Demo: Getting  Balance of Account...\n";
  int num;
  num = a.balance();
  cout << "Demo: The Account Balance is <" << num << ">\n";
  cout << "Demo: Declaring Account Done.\n";

  demoprint(a);
  
  cout << "Demo: Demonstration Program Done.\n";
}

void
demoprint(Account z)
{
  cout << '\n';
  cout << "Demo: Printing Account Now...\n";
  z.print();
  cout << "Demo: Printing Account Done.\n";
  cout << '\n';
}
