#ifndef __CONTACT_HEADER__
#define __CONTACT_HEADER__
class Contact;

#include <string>
#include <iostream>
#include <fstream>
using namespace std;

class Contact
{
 public:
  Contact(void);
  Contact(ifstream& handle);
  friend ostream& operator << (ostream& os, const Contact& right);

  string getAddress(void);
  string getName(void);
  string getNumber(void);
  void setAddress(string address);
  void setName(string name);
  void setNumber(string number);
  void saveToFile(ofstream& handle);

 private:
  string name;
  string address;
  string number;
};

#endif
