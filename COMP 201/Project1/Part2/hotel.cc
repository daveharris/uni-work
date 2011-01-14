/* hotel.cc - Hotel Class
 * Created originally by Vipul Delwadia delwadvipu 300069307
 * Contributions made by:
 *   David Keane  keanedavi  300069137
 *   Neil Ramsay  ramsayneil 300069252
 *   David Harris harrisdavi 300069566
 *  
 * Main Hub of Program, holding main Method, Main Menu, and Master File Operations.
 */

#include <string>
#include <iostream>
#include <fstream>
#include <ctime>

#include "hotel.h"
#include "date.h"
#include "categories.h"
#include "rooms.h"

using namespace std;

char* border = "+----------------------------------------------+"; //used for pretty output
int length = 15; //global variable used to align tokens pretty

// Main method, just displays about information and calls appropriate methods
int main(void) {
  cout << border << endl;
  cout << "| SAAHBS - SAAHBS Ain't A Hotel Booking System |" << endl;
  cout << "| Version 1.21                                 |" << endl;
  cout << "| N Ramsay, V Delwadia, D Harris, D Keane      |" << endl;
  cout << border << endl;

  Hotel hotel;
  hotel.readFromFile();
  hotel.displayMenu();
}

// default constructor for new hotel, required by main
Hotel::Hotel(void) {
  currentDate = new Date();
  rooms = new Rooms();
}

void Hotel::saveToFile(void) {
  system("mv hotel.htl hotel.htl.bak");
  ofstream handle("hotel.htl");
  if(!handle) {
    cout << "unable to open the hotel data file to save" << endl;
    return;
  }
  
  handle << "<hotel>" << endl;
  handle << "<categories>" << endl;
  Categories::saveToFile(handle);
  
  handle << "<rooms>" << endl;
  rooms->saveToFile(handle);
  
  handle << "</hotel>" << endl;
  handle.close();
    
}

void Hotel::readFromFile(void) {
  char buffer[256];
  ifstream handle("hotel.htl");
  if(!handle) {
    ifstream handlebak("hotel.htl.bak");
    if(!handlebak) {
      cout << "unable to open the hotel data file to load, please contact the system administrator" << endl;
      return;
    }
    system("cp hotel.htl.bak hotel.htl");
    cout << "unable to open the hotel data file to load, trying to load the backup file" << endl;    
    readFromFile();
    return;
  }
  handle.getline(buffer, 256);
  if(!strncmp(buffer, "<hotel>", 256)) {
    handle.getline(buffer, 256);
    if(!strncmp(buffer, "<categories>", 256)){
      Categories::loadFromFile(handle);
      handle.getline(buffer, 256);
      if(!strncmp(buffer, "<rooms>", 256)){
	rooms = new Rooms(handle);   
	handle.getline(buffer, 256);
	if(!strncmp(buffer, "</hotel>", 256)){
	  cout << "the hotel data file was loaded successfully" << endl;
	}
      }
    }
  }
  else {
    cout << "unable to open the hotel data file to load" << endl;
  }
  handle.close();
}

// Display menu of options
void Hotel::displayMenu(void) {
  cout << border << endl;
  cout << "| Main Menu                                    |" << endl;
  cout << border << endl;
  cout << "|  (L)oad from File                            |" << endl;
  cout << "|  (S)ave to File                              |" << endl;
  cout << "|  (R)ooms Menu                                |" << endl;
  cout << "|  (B)ookings Menu                             |" << endl;
  cout << "|  (N)ew Booking                               |" << endl;
  cout << "|  (D)isplay this Menu                         |" << endl;
  cout << "|  E(X)it                                      |" << endl;
  cout << border << endl;
  while (1) {
    char input[100];
    cout << "Enter Command (LSRBNDX): ";
    cin.getline(input, 100);
    input[0] = tolower(input[0]);
    if (cin.eof()){
      cout << endl;
      exit(0);
    }
    switch (input[0]) {
    case 'l' : readFromFile(); break;
    case 's' : saveToFile(); break;
    case 'r' : rooms->displayMenu(border); goto menu;
    case 'b' : {
      cout << "Bookings for which room (by ID)?" << endl;
      rooms->listRooms();
      int roomID;
      cin >> roomID;
      cin.ignore();
      cin.clear();
      Room* current = rooms->getRoom(roomID);
      if (current != NULL){
        current->getBookingSchedule()->displayMenu(border, current);
        goto menu;
      }
      break;
    }
    case 'n': {
      int capacity, category, datein, dateout, roomID;
      cout << "How many people to stay?:" << endl;
      cin >> capacity;
      cout << "Preferred Room Category? (by ID):" << endl;
      Categories::printCategories();
      cin >> category;
      cout << "Checkin Date: (yyyymmdd) " << endl;
      cin >> datein;
      Date checkIn(datein);
      cout << "Checkout Date: (yyyymmdd) " << endl;
      cin >> dateout;
      Date checkOut(dateout);
      if (checkIn.difference(checkOut)> -1) {
	cout << "You must stay at least 1 day" << endl;
        cin.ignore();
        cin.clear();
        break;
      }
      bool found = rooms->locateRooms(capacity, category, checkIn, checkOut);
      if (!found){
        break;
      }
      cout << "which room do you wish to book? (by ID) " << endl;
      cin >> roomID;
      cin.ignore();
      cin.clear();
      Booking* b = rooms->getRoom(roomID)->getBookingSchedule()->newBooking(checkIn, checkOut, *(rooms->getRoom(roomID)));
      cout << (*b) << endl;
      sleep(3);
      goto menu;
    }
    menu:
    case 'd': {
      system("clear");
      cout << border << endl;
      cout << "| Main Menu                                    |" << endl;
      cout << border << endl;
      cout << "|  (L)oad From File                            |" << endl;
      cout << "|  (S)ave to File                              |" << endl;
      cout << "|  (R)ooms Menu                                |" << endl;
      cout << "|  (B)ookings Menu                             |" << endl;
      cout << "|  (N)ew Booking                               |" << endl;
      cout << "|  (D)isplay this Menu                         |" << endl;
      cout << "|  E(X)it                                      |" << endl;
      cout << border << endl;
      break;
    }
    case 'x' : exit(0);
    default : break;
    }
  }
  cout << "Would you like to save your changes? [YN]";
  char buffer[256];
  cin.getline(buffer,255);
  buffer[0] = tolower(buffer[0]);
  if(buffer[0] != 'n')
    saveToFile(); // save to file on any input except for any string starting with "n"
  delete rooms;
  delete currentDate;
  // since this is the last lines of the program, clear up memory
}
