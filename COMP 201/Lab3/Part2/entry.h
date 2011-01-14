#ifndef _ENTRY_HEADER_
#define _ENTRY_HEADER_

#include <string>
using namespace std;

class Entry {

 public:
  Entry();
  Entry(string nme, int num);
  void setNum(int newPhone);
  void setName(string newName);
  int getNum();
  string getName();

 private:
  int phone;
  string name;
 
};

#endif
