//Include entry header file, string and i/o libraries
#include "entry.h"

#include <string>
#include <iostream>
using namespace std;

Entry::Entry() {
  //Create a new Entry object with no name or phone number
  name = "";
  phone = 0;
}

Entry::Entry(string nme, int num) {
  // Create a new Entry object with given name and phone number
  name = nme;
  phone = num;
}

int Entry::getNum() {
  return phone;
}

string Entry::getName() {
  return name;
}

void Entry::setNum(int newPhone) {
  phone = newPhone;
}

void Entry::setName(string newName) {
  name = newName;
}
