/* room.cc - Room Class
 * Created originally by Vipul Delwadia delwadvipu 300069307
 * Contributions made by:
 *   Neil Ramsay  ramsayneil 300069252
 *   David Harris harrisdavi 300069566
 * 
 * Holds information needed to track a room, and FileIO and User Interaction
 */

#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <iomanip>

#include "room.h"
#include "bookingschedule.h"
#include "categories.h"

extern int length;

using namespace std;

//construct a new room
Room::Room(const int ID) : ID(ID) {
  bookingSchedule = new BookingSchedule();
  state = true;
  char line[100];
  cout << "Please enter a name for the new room: ";
  cin.getline(line,100);
  name = line;
  cout << "Please enter the capacity of the new room: ";
  cin >> capacity;
  cout << "Please enter the rate per night for the new room: ";
  cin >> rate;
  cout << "Please select a category for the new room: " << endl;
  Categories::printCategories();
  cin >> category;
  cout << "The new room " << name << " has been created successfully" << endl;
  cin.ignore();
}

//take a input filestream and constuct room object
Room::Room(ifstream& handle, const int ID) : ID(ID) {
  state = true;
  char buffer[256];
  handle.getline(buffer,256);
  while(1) {
    if(!strncmp(buffer, "<name>", 256)) { //if name field - store name
      handle.getline(buffer,256);
      name = buffer;
      handle.getline(buffer,256);
    }
    else if(!strncmp(buffer, "<capacity>", 256)) { //if capacity field - store capacity
      handle >> capacity;
      handle.ignore();
      handle.clear();
      handle.getline(buffer,256);
    }
    else if(!strncmp(buffer, "<rate>", 256)) { //if rate field - store rate
      handle.getline(buffer,256);

      stringstream line;
      line << buffer;
      line >> rate;

      handle.getline(buffer,256);
    
    }
    else if(!strncmp(buffer, "<category>", 256)) { //if category field - store category
      handle.getline(buffer,256);

      stringstream line;
      line << buffer;
      line >> category;

      handle.getline(buffer,256);
    }
    else if(!strncmp(buffer, "<bookingschedule>", 256)) { //if start of bookingschedule - call bookingschedule constructor
      bookingSchedule = new BookingSchedule(handle, (*this));
    }
    else if(!strncmp(buffer, "</room>", 256)) { // end of room XML data - return
      return;
    }
    handle.getline(buffer, 256);
  }
}

//delete bookingSchedule pointer
Room::~Room(void) {
  delete bookingSchedule;
  bookingSchedule = 0;
}

Room::Room(const Room& original) : ID(original.ID) {
  this->name = original.name;
  this->capacity = original.capacity;
  this->rate = original.rate;
  this->category = original.category;

  *(this->bookingSchedule) = *(original.bookingSchedule);
}

//assignment operator - this has a problem as an already constructed object cannot change ID
Room& Room::operator = (const Room& original) {
  if(this->ID != original.ID)
    cout << "WARNING: cannot modify ID. ID has remained the same!" << endl;

  this->name = original.name;
  this->capacity = original.capacity;
  this->rate = original.rate;
  this->category = original.category;

  *(this->bookingSchedule) = *(original.bookingSchedule);  

  return *this;
}

//extraction operator - prints out information about a room
ostream& operator << (ostream& os, const Room& right) {
  int w1 = 21;
 
  string cat = "";
  cat.append(Categories::getCategory(right.category));
  cat.append(" (");
  stringstream num;
  num << right.category;
  cat.append(num.str());
  cat.append(")");

  int w2 = cat.length() + 3;
  if(right.name.length() > cat.length())
    w2 = right.name.length() + 3;
  
  if(w2 < w1)
    w2 = w1;

  length = w1 + w2 + 1;
  
  os << "+" << setw(w1+w2+1) << setfill('-') << "+\n"
     << "| Room Details" << setfill(' ') << setw(w2+9) << "|\n"
     << "+" << setw(w1+w2+1) << setfill('-') << "+\n"
     << setfill(' ') << setw(w1) << setiosflags(ios_base::left) << "| ID: " <<setw(w2) << right.ID << "|\n"
     << setw(w1) << "| Name: "<< setw(w2) << right.name << "|\n"
     << setw(w1) << "| Capacity: " << setw(w2) << right.capacity << "|\n"
     << setw(w1) << "| Rate: " << setw(w2) << right.rate << "|\n"
     << setw(w1) << "| Category: " << setw(w2) << cat << "|\n"
     << setw(w1) << "| Room Takings ($): " << setw(w2) << right.bookingSchedule->getTakings() << "|\n"
     << resetiosflags(ios_base::left) << "+" << setw(w1+w2) << setfill('-') << "+" << setfill(' ') << resetiosflags(ios_base::showpoint);
 
  return os;
}

Room::Room(void) {}

void Room::setCategory(int category) {
  this->category = category;
}

void Room::setName(string name) {
  this->name = name;
}

void Room::setCapacity(int capacity) {
  this->capacity = capacity;
}

void Room::setRate(double rate) {
  this->rate = rate;
}

void Room::setState(bool state) {
  this->state = state;
}

int Room::getID(void) {
  return ID;
}

int Room::getCategory(void) {
  return category;
}

string Room::getName(void) {
  return name;
}

int Room::getCapacity(void) {
  return capacity;
}

double Room::getRate(void) {
  return rate;
}

bool Room::getState(void) {
  return state;
}

BookingSchedule* Room::getBookingSchedule() {
  return bookingSchedule;
}

void Room::saveToFile(ofstream& handle) {
  handle << "<name>\n" << name << "\n</name>" << endl;
  handle << "<capacity>\n" << capacity << "\n</capacity>" << endl;
  handle << "<rate>\n" << rate << "\n</rate>" << endl;
  handle << "<category>\n" << category << "\n</category>" << endl;
  handle << "<bookingschedule>" << endl;
  bookingSchedule->saveToFile(handle);
  handle << "</room>" << endl;
}
