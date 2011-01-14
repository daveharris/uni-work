#include <iostream>
using namespace std;

#define MAXITEMS 100

main()
{ 
  int items[MAXITEMS];
  int numitems;
  int numin;
  int searchNum = 0;
  int found = 0;

  numitems = 0;

  cout << "Enter a list of integers, one per line, zero to stop:\n";

  while (1) {
    cin >> numin;
    if (numin == 0) break;

    items[numitems] = numin;
    numitems++;
  }
  
  int i;

  cout << "Here are the integers you entered:\n";

  for (i = 0; i < numitems; i++)
    cout << items[i] << '\n';
    
  cout << "Enter a number to search for:";
  cin >> searchNum;

  int n;
  
  for(n = 0; n < numitems; n++) {
    if(items[n] == searchNum) {
      cout << searchNum << " was found at " <<  n << endl;
      found = 1;
    }
  }
    if(found == 0 && n == numitems)
      cout << searchNum << " was not found!! " << endl;
  
}
