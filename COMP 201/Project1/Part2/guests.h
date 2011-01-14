#ifndef __GUESTS_HEADER__
#define __GUESTS_HEADER__
class Guests;

#include <string>
#include <iostream>
#include <fstream>
#include <map>

using namespace std;

class Guests {
 public:
  Guests();
  Guests(ifstream& handle);
  friend ostream& operator << (ostream& os, const Guests& right);

  void newGuest(string name);
  void editGuest(int ID, string name);
  pair<map<int,string>::const_iterator,map<int,string>::const_iterator> getIterator(void) const;
  void removeGuest(int ID);
  void saveToFile(ofstream& handle);

 private:
  map<int,string> guests_map;
  int counter;
};

#endif
