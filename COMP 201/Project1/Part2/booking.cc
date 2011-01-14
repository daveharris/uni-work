/* booking.cc - Bookings Class
 * Created originally by Neil Ramsay  ramsayneil 300069252
 * Contributions made by: 
 *   David Keane  keanedavi  300069137
 *   David Harris harrisdavi 300069566
 *   Vipul Delwadia delwadvipu 300069307
 *
 *  Holds and Calculates Material required for a Booking
 */

#include <string>
#include <sstream>
#include <iostream>
#include <fstream>
#include <iomanip>
#include <map>

#include "booking.h"
#include "contact.h"
#include "date.h"
#include "room.h"
#include "guests.h"

//Needed for menu allignment
extern int length;

using namespace std;

//Booking constructor with assignment list for const variables
Booking::Booking(int ID, Date& checkIn, Date& checkOut, Room& room) : ID(ID), checkIn(checkIn), checkOut(checkOut), room(&room), guests() {
  //make a new Contact object, and stores all the personal details in Contact class
  contactPerson = new Contact();

  char line[256];
  int person = 0;
  cout << "How many guests are there? ";
  cin >> person;
  cin.ignore();
  cin.clear();
  if (person >= room.getCapacity()){
    cout << "Room does not fit " << person + 1 << " people" << endl;
  }
  if(person) {
    cout << "Please enter the names of the guests (one per line) but not the contact person:" << endl;
    while(cin.good() && person) {
      cin.getline(line, 256);
      guests.newGuest(line);
      person--;
    }
  }
}

//Alternative Booking constructor, needed for reading the file. Recursivley calls child constructors
Booking::Booking(ifstream& handle, int ID, Room& room) : ID(ID), checkIn(Date(0)), checkOut(Date(0)), room(&room), guests() {
  char buffer[256];

  handle.getline(buffer, 256); //<contact>
  contactPerson = new Contact(handle);
  
  handle.getline(buffer, 256); //<date>
  checkIn = Date(handle);
  handle.getline(buffer, 256); //<date>
  checkOut = Date(handle);
  
  handle.getline(buffer, 256); // <guests>
  guests = Guests(handle);

  handle.getline(buffer, 256); //</booking>
}

//Overloading the << operator so we can print out, with allignment
ostream& operator << (ostream& os, const Booking& right) {
  int w1 = 21;
  
  //Create the room string to find out it's length
  string title = "Room ";
  stringstream num;
  num << right.room->getID();
  title.append(num.str());
  title.append(": ");
  title.append(right.room->getName());
  title.append(", Booking ");
  title.append(num.str());
  int titleLength = title.length();

  //Create the name string to find out it's length
  string name = right.contactPerson->getName();
  int nameLength = name.length();
  
  //Create the address string to find out it's length  
  string address = right.contactPerson->getAddress();
  int addressLength = address.length();

  int w2 = 9;
  if (nameLength>w2)
    w2 = nameLength + 1;
  if(addressLength>w2)
    w2 = addressLength + 1;

  int max = w1 + w2 + 1;

  length = max;
	//Print out the strings with allignment
  os << "+" << setw(max) << setfill('-') << "+\n" << setiosflags(ios_base::left)
     << "| " << setfill(' ') << setw(max-3) << title << "|\n" << resetiosflags(ios_base::left)
     << "+" << setw(max) << setfill('-') << "+\n" << setiosflags(ios_base::left)
     << setfill(' ') << setw(w1) << setiosflags(ios_base::left) << "| ID: " <<setw(w2) << right.ID << "|\n"
     << setw(w1) << "| Checkin: "<< setw(w2) << right.checkIn.getDate() << "|\n"
     << setw(w1) << "| Checkout: " << setw(w2) << right.checkOut.getDate() << "|\n"
     << (*right.contactPerson)
     << setw(w1) << "| Guests: " << setw(w2) << " " << "|" << endl;

  pair<map<int,string>::const_iterator,map<int,string>::const_iterator> iters = right.guests.getIterator();
  
  int counter = 0; 
  
  for(map<int,string>::const_iterator iterC = iters.first; iterC != iters.second; iterC++) {
    os << "|    " << setfill(' ') << setw(max-6) << iterC->second << "|" << endl;
    counter++;
  }

  os << setw(w1) << "| Number of Guests: " << setfill(' ') << setw(w2) << counter+1 << "|" << endl;

  os << resetiosflags(ios_base::left) << "+" << setw(max-1) << setfill('-') << "+" << endl;

  return os;
}


Date* Booking::getCheckIn(void) {
  return &checkIn;
}

Date* Booking::getCheckOut(void) {
  return &checkOut;
}

Contact* Booking::getContact(void) {
  return contactPerson;
}

double Booking::getCost(void) {
  double cost = room->getRate() * (checkOut.difference(checkIn));
  return cost;
}

Guests* Booking::getGuests() {
  return (&guests);
}

void Booking::setCheckIn(Date& checkIn) {
  this->checkIn = checkIn;
}

void Booking::setCheckOut(Date& checkOut) {
  this->checkOut = checkOut;
}

//The save to file method, needed so that the data can be read back in
//Calls child saveToFile methods.
void Booking::saveToFile(ofstream& handle) {
  handle << "<ID>" << endl << ID << endl << "</ID>" << endl;

  handle << "<contact>" <<endl;
  contactPerson->saveToFile(handle);

  handle << "<date>" <<endl;
  checkIn.saveToFile(handle);

  handle << "<date>" <<endl;
  checkOut.saveToFile(handle);

  handle << "<guests>" << endl;
  guests.saveToFile(handle);

  handle << "</booking>" << endl;
}
