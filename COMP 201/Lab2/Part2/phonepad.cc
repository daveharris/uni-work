// Phonepad:
// - allows the user to enter names and phone extension numbers
//   and stores them in a table
// - allows the user to look up names in the table and find the
//   matching number
// - prints the whole table of names and numbers

#include <iostream> // for I/O stream operations
#include <string.h>   // for strcmp: string compare
#include "table.h"    // for table class

using namespace std;

#define MAX_ANSCHARS 256

int
confirm(char question[]); // pre-declaration so function can be put at end

main()
{
  char name[MAX_KEYCHARS];
  int number;
  int findnumber;
  Table *tab = 0;
  tab = new Table();

  cout << "Phonepad entry and lookup program:\n";
  tab->init();

  while (confirm("\nNew entry?")) {
    cout << "Enter name (without blanks):\n"; // string input stops at a blank
    cin >> name;
    cout << "Enter number:\n";
    cin >> number;
    if (tab->insert(name, number))
      cout << "Entry inserted.\n";
    else
      cout << "Entry rejected.\n";
  }

  while (confirm("\nLook up entry?")) {
    cout << "Enter name:\n";
    cin >> name;
    if (tab->find(name, &findnumber))
      cout << "Entry found. Number is " << findnumber << ".\n";
    else
      cout << "Entry not found.\n";
  }

  cout << "\nComplete Phonepad:\n";
  tab->print();
}

int
confirm(char question[])
{
  char answer[MAX_ANSCHARS];
  answer[0] = 0;
  cout << question << ' ';
  
  // Note that strcmp returning 0 means strings are the same!
  while (!(strcmp(answer, "yes") == 0 || strcmp(answer, "no") == 0)) {
    cout << "[yes or no?]\n";
    cin >> answer;
  }
  return strcmp(answer, "yes") == 0;
}

