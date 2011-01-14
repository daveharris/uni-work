#include <iostream> // for I/O stream operations
#include <string.h>   // for string copy and compare operations
#include "table.h"    // for table operations

using namespace std;

void Table::init() {
  num_entries = 0;
}

int Table::insert(char key[], int value) {
  
   values[num_entries] = value;
  strcpy(keys[num_entries], key);
  cout << "num of entries = " << num_entries << '\n';
  // Note strcpy copies 2nd argument to the 1st!
  num_entries++;
  return 1;
}

int Table::find(char key[], int *value) {
  int i;
  //Note strcmp returns 0 when strings are the same!
  for (i = 0; i < num_entries; i++) {
    if (strcmp(key, keys[i]) == 0) {
      (*value) = values[i];
      return 1;
    }
  }

  return 0;
}

void Table::print() {
  int i;

  cout << "Table contains " << num_entries << " entries.\n";
  for (i = 0; i < num_entries; i++)
    cout << keys[i] << ": " << values[i] << "\n";
}
