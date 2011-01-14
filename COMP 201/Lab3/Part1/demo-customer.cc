#include <iostream>
#include "customer.h"

using namespace std;

main()
{
  cout << "Demonstration Program for Customer Class...\n\n";

  cout << "Now Declaring Customer Variable...\n";
  Customer me("Robert!!!!");
  cout << "\nDone Declaring Customer Variable.\n\n";

  cout << "Now Doing Purchases...\n";
  me.purchase(50);
  me.purchase(100);
  cout << "Done Purchases.\n\n";

  cout << "Now Doing Payment\n";
  me.payment(100);
  cout << "Done Payment\n\n";

  cout << "Now Doing Report\n";
  me.report();
  cout << "Done Report\n\n";

  cout << "Now Doing Purchases\n";
  me.purchase(100);
  me.purchase(30);
  me.purchase(70);
  cout << "Done Purchases.\n\n";

  cout << "Now Doing Payment\n";
  me.payment(250);

  cout << "Now Doing Report\n";
  me.report();
  cout << "Done Report\n\n";

  cout << "Now Doing Purchase\n";
  me.purchase(35);
  me.purchase(40);
  cout << "Done Purchases.\n\n";  

  cout << "Now Doing Report\n";
  me.report();
  cout << "Done Report\n\n";

  cout << "Demonstration Program Done.\n";
}
