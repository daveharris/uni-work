/* contact.cc - Contact Class
 * Created originally by David Harris harrisdavi 300069566
 * Contributions made by:
 *   David Keane  keanedavi  300069137
 *   Vipul Delwadia delwadvipu 300069307
 *   Neil Ramsay  ramsayneil 300069252
 *  
 * Contains Data for the Designated Contact Person for a Booking
 */

#include <string>
#include <iostream>
#include <fstream>
#include <iomanip>

#include "contact.h"

extern int length;

using namespace std;

//Default contact constructor, reads in the name, address, number
Contact::Contact(void) {
  char buffer[80] = "";

  cout << "Contact name: ";
  cin.getline(buffer, 80);
  name = buffer;

  cout << "Contact address: ";
  cin.getline(buffer, 80);
  address = buffer;

  cout << "Contact number: ";
  cin.getline(buffer, 80);
  number = buffer;
}

//Alternative contact constructors, needed for reading xml files
Contact::Contact(ifstream& handle) {
  char buffer[100];

  handle.getline(buffer, 100); //<name>
  handle.getline(buffer, 100); //name data
  name = buffer;
  handle.getline(buffer, 100); //</name>

  handle.getline(buffer, 100);//<address>
  handle.getline(buffer, 100);//address data
  address = buffer;
  handle.getline(buffer, 100);//</address>

  handle.getline(buffer, 100);//<number>
  handle.getline(buffer, 100);//number data
  number = buffer;
  handle.getline(buffer, 100);//</number>

  handle.getline(buffer, 100);//</contact>
}

//The overloaded operator << for printing out correctly
ostream& operator << (ostream& os, const Contact& right) {
  int w1 = 21;
  string name = right.name;
  int nameLength = name.length();
  
  string address = right.address;
  int addressLength = address.length();
  int w2 = 9;
  if (nameLength>w2)
    w2 = nameLength + 1;
  if(addressLength>w2)
    w2 = addressLength + 1;
  
  int max = w1 + w2 + 1;

  length = max;

  os << setw(w1) << "| Contact Name: " << setw(w2) << name << "|\n"
     << setw(w1) << "| Contact Address: " << setw(w2) << address << "|\n"
     << setw(w1) << "| Contact Phone: " << setw(w2) << right.number << "|" << endl;
    
  return os;
}

string Contact::getAddress(void) {
  return address;
}

string Contact::getName(void) {
  return name;
}

string Contact::getNumber(void) {
  return number;
}

void Contact::setAddress(string address) {
  this->address = address;
}

void Contact::setName(string name) {
  this->name = name;
}

void Contact::setNumber(string number) {
  this->number = number;
}

//the save to file method, needed to save it in an xml file
//continues above and below
void Contact::saveToFile(ofstream& handle) {
  handle << "<name>" << endl << name << endl << "</name>" << endl;
  handle << "<address>" << endl << address << endl << "</address>" << endl;
  handle << "<number>" << endl << number << endl << "</number>" << endl;

  handle << "</contact>" << endl;
}
