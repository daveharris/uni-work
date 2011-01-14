/* bookingschedule.cc - BookingSchedule Class
 * Created originally by Neil Ramsay  ramsayneil 300069252
 * Contributions made by: 
 *   David Keane  keanedavi  300069137
 *   David Harris harrisdavi 300069566
 *   Vipul Delwadia delwadvipu 300069307
 *
 *  Holds Bookings, and handles File IO and User Interaction
 */

#include <iostream>
#include <fstream>
#include <map>
#include <sstream>
#include <iomanip>

#include "bookingschedule.h"
#include "date.h"
#include "booking.h"
#include "room.h"
#include "guests.h"

extern int length;

using namespace std;

//The default constructor
BookingSchedule::BookingSchedule(void) {
  bookings = map<int,Booking*>();
  counter = 1;
}

//The alternative constructor, needed for reading files in
BookingSchedule::BookingSchedule(ifstream& handle, Room& room):bookings() {
  int ID;
  char buffer[256];
  handle.getline(buffer, 256);
  while(!strncmp(buffer, "<booking>", 256)) {
    handle.getline(buffer, 256);
    if(!strncmp(buffer, "<ID>", 256)) {
      handle >> ID;
      handle.ignore();
      handle.clear();

      handle.getline(buffer, 256);
      if(!strncmp(buffer, "</ID>", 256)) {
	bookings[ID] = new Booking(handle, ID, room);
      }
    }
    handle.getline(buffer, 256);
  }
  if(!strncmp(buffer, "<counter>", 256)) {
    handle >> counter;
    handle.ignore();
    handle.clear();
         
    handle.getline(buffer, 256);
    if(!strncmp(buffer, "</counter>", 256)) {
      handle.getline(buffer, 256);      
    }
  }
}

//Destructor
BookingSchedule::~BookingSchedule(void) {
  map<int,Booking*>::iterator iter;
  for(iter = bookings.begin(); iter != bookings.end(); iter++) {
      delete iter->second;
    }
}

//Copy constructor
BookingSchedule::BookingSchedule(const BookingSchedule& original) {
  this->counter = original.counter;
  
  map<int,Booking*>::const_iterator iterC;
  for(iterC = original.bookings.begin(); iterC != original.bookings.end(); iterC++) {
    bookings[iterC->first] = iterC->second;
  }
}

//Assignment operator (ie. = overloaded)
BookingSchedule& BookingSchedule::operator=(const BookingSchedule& original) {
  this->counter = original.counter;
  
  map<int,Booking*>::const_iterator iterC;
  for(iterC = original.bookings.begin(); iterC != original.bookings.end(); iterC++) {
    this->bookings[iterC->first] = iterC->second;
  }

  return *this;
}

//Create a new Booking with the dates and rooms
Booking* BookingSchedule::newBooking(Date& checkIn, Date& checkOut, Room& room) {
  bookings[counter] = new Booking(counter, checkIn, checkOut, room);
  return getBooking(counter++);
}

//Checks if the room is being used between the dates given
bool BookingSchedule::isFree(Date& beginning, Date& end) {
  map<int,Booking*>::const_iterator iterC;

  for(iterC = bookings.begin(); iterC != bookings.end(); iterC++) {
    if((iterC->second->getCheckIn()->getDate() < end.getDate() && iterC->second->getCheckOut()->getDate() >= end.getDate()) 
       ||
       (iterC->second->getCheckIn()->getDate() >= beginning.getDate() && iterC->second->getCheckOut()->getDate() < beginning.getDate())) {
      return false;
    }
  }
  return true;
}

//Prints out the booking details, properly alligned. Room passed by pointer so no 'shallow copy'
void BookingSchedule::printSchedule(Room* room) {
  char buffer[256];
  bool showPrev;
  //keep asking if they want to show old bookings until they give a good answer
  while (1){
    cout << "Do you want to view bookings ending before today? [YN]" << endl;
    cin.getline(buffer,255);
    buffer[0] = tolower(buffer[0]);
    switch (buffer[0]) {
	//if yes, set flag, and jump out of loop
    case 'y': {
      showPrev = true;
      goto outside;
    }
	//if no, set flag, and jump out of loop
    case 'n': {
      showPrev = false;
	  goto outside;
    }
	//otherwise, loop and ask again
    default: {
      break;
    }
    }
  }
 outside:
  int max = 48;
  string title = "Bookings for Room ";
  Date today;
  stringstream num;
  num << room->getID();
  title.append(num.str());
  title.append(": ");
  title.append(room->getName());
  //Print out the title, properly alligned
  cout << "+" << setw(max) << setfill('-') << "+\n" << setiosflags(ios_base::left)
       << "| " << setw(max-3) << setfill(' ') << title << "|\n" << resetiosflags(ios_base::left)
       << "+" << setw(max-1) << setfill('-') << "+" << setiosflags(ios_base::left) << endl;

  //Go through the map and create a string that holds the dates
  map<int,Booking*>::const_iterator iterC;
  for(iterC = bookings.begin(); iterC != bookings.end(); iterC++) {
    string data = "";
    if (showPrev){
      stringstream id[3];
      id[0] << iterC->first;
      data.append(id[0].str());
      data.append(" ");
      id[1] << iterC->second->getCheckIn()->getDate();
      data.append(id[1].str());
      data.append("-");
      id[2] << iterC->second->getCheckOut()->getDate();
      data.append(id[2].str());
      data.append(" ");
      data.append(iterC->second->getContact()->getName());
      cout << "| " << setw(max-4) << setfill(' ') << data << " |" << endl;
    }
    else if(!(iterC->second->getCheckOut()->getDate() < (today.getDate()))){
      stringstream id[3];
      id[0] << iterC->first;
      data.append(id[0].str());
      data.append(" ");
      id[1] << iterC->second->getCheckIn()->getDate();
      data.append(id[1].str());
      data.append("-");
      id[2] << iterC->second->getCheckOut()->getDate();
      data.append(id[2].str());
      data.append(" ");
      data.append(iterC->second->getContact()->getName());
      cout << "| " << setw(max-4) << setfill(' ') << data << " |" << endl;
    }
  }    
  cout << resetiosflags(ios_base::left) << "+" << setw(max) << setfill('-') << "+\n";

}

//Return the booking associated with the ID
Booking* BookingSchedule::getBooking(int ID) {
  map<int, Booking*>::iterator iter;

  iter = bookings.find(ID);
  if(iter != bookings.end()) {
    return bookings[ID];
  }
  else {
    cout << "\aBooking# " << ID << " does not exist!" << endl;
    return NULL;
  }
}

void BookingSchedule::deleteBooking(int ID) {
  bookings.erase(ID);
}

//Save to file method, needed so program can continue from any point
void BookingSchedule::saveToFile(ofstream& handle) {
  map<int,Booking*>::const_iterator iterC;
  for(iterC = bookings.begin(); iterC != bookings.end(); iterC++){
    handle << "<booking>" << endl;
    iterC->second->saveToFile(handle);
  }
  handle << "<counter>" << endl;
  handle << counter << endl;
  handle << "</counter>" << endl;
  handle << "</bookingschedule>" << endl;
}

//Method to display the menu of bookings
//Uses a switch to get the option wanted
void BookingSchedule::displayMenu(char* border, Room* current) {
  cout << border << endl;
  cout << "| Bookings Menu                                |" << endl;
  cout << border << endl;
  cout << "|  (L)ist Bookings                             |" << endl;
  cout << "|  (A)dd Booking                               |" << endl;
  cout << "|  (R)emove Booking                            |" << endl;
  cout << "|  (E)dit Booking                              |" << endl;
  cout << "|  (V)iew Booking                              |" << endl;
  cout << "|  (D)isplay this Menu                         |" << endl;
  cout << "|  E(X)it to Main Menu                         |" << endl;
  cout << border << endl;

  while (1) {
    char input[100];
    cout << "Enter Command (LAREVDX): ";
    cin.getline(input, 100);
    input[0] = tolower(input[0]);
    switch (input[0]) {
    case 'l' : {
      printSchedule(current);
      break;
    }
    case 'a' : {
      int in, out;
      cout << "New Booking" << endl;
      cout << "Check In Date (yyyymmdd): ";
      cin >> in;
      cout << "Check Out Date (yyyymmdd): ";
      cin >> out;
      cin.ignore();
      cin.clear();
      Date inDate(in);
      Date outDate(out);
      if (inDate.difference(outDate)> -1) {
	cout << "You cannot Check Out before you Check In" << endl;
        break;
      }
      if (isFree(inDate, outDate)) {
        cout << (*newBooking(inDate, outDate, *current)) << endl;
      }
      else{
        cout << "Room is not free between " << inDate.getDate() << " and " << outDate.getDate() << endl;
      }
      break;
    }
    case 'r' : {
      int bookingID;
      cout << "Remove which Booking (by ID)?" << endl;
      printSchedule(current);
      cin >> bookingID;
      cin.ignore();
      cin.clear();
      if(getBooking(bookingID) != NULL)
	deleteBooking(bookingID);
      break;
    }
    case 'e' : {
      int bookingID;
      cout << "Edit which Booking(by ID)?" << endl;
      printSchedule(current);
      cin >> bookingID;
      cin.ignore();
      cin.clear();
      if(getBooking(bookingID) != NULL)
	editMenu(bookingID);
      break;
    }
    case 'v' : {
      int bookingID;
      cout << "View which Booking (by ID)?" << endl;
      printSchedule(current);
      cin >> bookingID;
      cin.ignore();
      cin.clear();
      if(getBooking(bookingID) != NULL)
	cout << (*getBooking(bookingID)) << endl;
      break;
    }
    case 'd' : {
      system("clear");
      cout << border << endl;
      cout << "| Bookings Menu                                |" << endl;
      cout << border << endl;
      cout << "|  (L)ist Bookings                             |" << endl;
      cout << "|  (A)dd Booking                               |" << endl;
      cout << "|  (R)emove Booking                            |" << endl;
      cout << "|  (E)dit Booking                              |" << endl;
      cout << "|  (V)iew Booking                              |" << endl;
      cout << "|  (D)isplay this Menu                         |" << endl;
      cout << "|  E(X)it to Main Menu                         |" << endl;
      cout << border << endl;
      break;
    }
    case 'x' : return;
    default : cout << "Invalid option" << endl;
    }
  }
}

//The edit menu, uses a switch again to use the menu
void BookingSchedule::editMenu(int ID) {
   Booking* booking = getBooking(ID);
   cout << *booking;
   while (1) {
     int w = length-1;
     cout << setiosflags(ios_base::left) << setfill(' ')
	  << setw(w) << "| Edit Check(I)n" << "|\n"
	  << setw(w) << "| Edit Check(O)ut" << "|\n"
	  << setw(w) << "| Edit (C)ontact" << "|\n"
	  << setw(w) << "| Edit (G)uests" << "|\n"
	  << setw(w) << "| E(X)it Edit Menu" << "|\n"
	  << "+" << setfill('-') << setw(w-1) << "-" << "+" << setfill(' ') << endl;

     char input[256];
     cout << "Enter Command (IOCGX): ";
     cin.getline(input, 256);
     input[0] = tolower(input[0]);
     switch (input[0]) {
     case 'i' : {
       cout << "Check in date (currently " << booking->getCheckIn()->getDate() << "): ";
       int date;
       cin >> date;
       booking->getCheckIn()->setDate(date);
       cout << "\n" << (*booking) << endl;
       break;
     }
     case 'o' : {
       cout << "Check out date (currently " << booking->getCheckOut()->getDate() << "): ";
       int date;
       cin >> date;
       booking->getCheckOut()->setDate(date);
       cout << "\n" << (*booking) << endl;
       break;
     }
     case 'c' : {
       Contact* contact = booking->getContact();
       
       while (1) {
	 int w = length-2;
	 cout << "+" << setfill('-') << setw(w) << "-" << "+" << setfill(' ') << "\n"
	      << (*contact) << "+" << setfill('-') << setw(w) << "-" << "+\n"
	      << setfill(' ') << endl;
	 cout << "+---------------------------------+" << endl;
	 cout << "| Change the (N)ame               |" << endl;
	 cout << "| Change the (A)ddress            |" << endl;
	 cout << "| Change the N(U)mber             |" << endl;
	 cout << "| E(X)it Edit Menu                |" << endl;
	 cout << "+---------------------------------+" << endl;
	 
	 char input[100];
	 cout << "Enter Command (ANUX): ";
	 cin.getline(input, 100);
	 input[0] = tolower(input[0]);
	 switch (input[0]) {
	 case 'n' : {
	   cout << "Name (currently " << contact->getName() << "): ";
	   char buffer[256];
	   cin.getline(buffer,256);
	   contact->setName(buffer);
	   break;
	 }
	 case 'a' : {
	   cout << "Address (currently " << contact->getAddress() << "): ";
	   char buffer[256];
	   cin.getline(buffer,256);
	   contact->setAddress(buffer);
	   break;
	 }
	 case 'u' : {
	   cout << "Number (currently " << contact->getNumber() << "): ";
	   char buffer[256];
	   cin.getline(buffer,256);
	   contact->setNumber(buffer);
	   break;
	 }
	 case 'x' : goto cdone;
	 }
       }
     cdone:
       break;
     }
     case 'g' : {
       Guests* guests = booking->getGuests();
       cout << "\n" << (*guests) << endl;
       while (1) {
	 cout << "+---------------------------------+" << endl;
	 cout << "| (A)dd a Guest                   |" << endl;
	 cout << "| Re(N)ame a Guest                |" << endl;
	 cout << "| (R)emove a Guest                |" << endl;
	 cout << "| E(X)it Edit Menu                |" << endl;
	 cout << "+---------------------------------+" << endl;
	 
	 char input[100];
	 cout << "Enter Command (ANRX): ";
	 cin.getline(input, 100);
	 input[0] = tolower(input[0]);
	 switch (input[0]) {
	 case 'a' : {
	   cout << "Guest's name: ";
	   char buffer[256];
	   cin.getline(buffer,256);
	   guests->newGuest(buffer);
	   cout << (*guests) << endl;
	   break;
	 }
	 case 'n' : {
	   cout << "Rename which guest?" << endl;
	   cout << (*guests) << endl;
	   int guestID;
	   cin >> guestID;
	   cin.ignore();
	   cin.clear();
	   cout << "Guest's name: ";
	   char buffer[256];
	   cin.getline(buffer,256);
	   guests->editGuest(guestID, buffer);
	   cout << (*guests) << endl;
	   break;
	 }
	 case 'r' : {
	   cout << "Remove which guest?" << endl;
	   cout << (*guests) << endl;
	   int guestID;
	   cin >> guestID;
	   guests->removeGuest(guestID);
	   cout << (*guests) << endl;
	   break;
	 }
	 case 'x' : goto gdone;
	 }
       }
     gdone:
       break;
     }
     case 'x' : return;
     }
   }
 }

//Method to return the amount of money collected for all bookings
double BookingSchedule::getTakings(void) const {
  double total = 0;
  map<int,Booking*>::const_iterator iterC;
  for(iterC = bookings.begin(); iterC != bookings.end(); iterC++) {
    total += iterC->second->getCost();
  }
  return total;
}
