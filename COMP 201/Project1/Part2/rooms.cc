/* rooms.cc - Rooms Class
 * Created originally by Vipul Delwadia delwadvipu 300069307
 * Contributions made by: 
 *   David Keane  keanedavi  300069137
 *   David Harris harrisdavi 300069566
 *   Neil Ramsay  ramsayneil 300069252
 *
 * This class holds Room Objects in a Map, and deals with the File IO, and User Interaction for Rooms
 */

#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <sstream>
#include <iomanip>

#include "rooms.h"
#include "date.h"
#include "categories.h"

extern int length;

using namespace std;

Rooms::Rooms(void){
  counter = 1;
}

//takes a input filestream and creates a new object
//this parses a section of a XML file and stores in the objects local vars
Rooms::Rooms(ifstream& handle) {
  int ID;
  char buffer[256];
  handle.getline(buffer, 256);
  //while more rooms in XML create new rooms and pass file handle
  while(!strncmp(buffer, "<room>", 256)) {
    handle.getline(buffer, 256);
    if(!strncmp(buffer, "<ID>", 256)) { //room constuctor needs ID as is const in room
      handle >> ID;
      handle.ignore();
      handle.clear();
      handle.getline(buffer, 256);
      if(!strncmp(buffer, "</ID>", 256))
	rooms[ID] = new Room(handle, ID); //create new room passing handle and ID
    }
    handle.getline(buffer, 256);
  }
  //restore rooms counter
  if(!strncmp(buffer, "<counter>", 256)) {
    handle >> counter;
    handle.ignore();
    handle.clear();
    handle.getline(buffer, 256);
    if(!strncmp(buffer, "</counter>", 256))
      handle.getline(buffer, 256);
  }
}

//destruct each room as each room is stored as pointers in a map
Rooms::~Rooms(void) {
  map<int, Room*>::iterator iter;
  
  //iterate through map destroying all rooms'
  for(iter = rooms.begin(); iter!=rooms.end(); iter++)
    delete iter->second;
}

Rooms::Rooms(const Rooms& original) {
  rooms = original.rooms;
  counter = original.counter;
}

Rooms& Rooms::operator=(const Rooms& original) {
  rooms = original.rooms;
  counter = original.counter;
  return *this;
}

//create a new room and store in map
Room* Rooms::newRoom(void) {
  Room *temp = new Room(counter);
  rooms[counter++] = temp;
  return temp;
}

//check if room exists and get if exists
Room* Rooms::getRoom(const int ID) {
  map<int, Room*>::const_iterator iterC;

  //find room
  iterC = rooms.find(ID);
  //if exists - return
  if (iterC!=rooms.end()&&iterC->second->getState()) {
    return iterC->second;
  }
  //else alert user and return NULL
  else {
    cout << "\aRoom# " << ID << " does not exist!" << endl;
    sleep(3);
    return NULL;
  }
}

//iterate through rooms' and print out rooms that are free
bool Rooms::locateRooms(int capacity, int category, Date& checkIn, Date& checkOut) {
  map<int, Room*>::const_iterator iterC;
  bool found = false;

  //iterate through rooms'
  for(iterC = rooms.begin(); iterC!=rooms.end(); iterC++) {
    //if is free print out using << operator
    if (iterC->second->getState() &&
	capacity <= iterC->second->getCapacity() &&
	category == iterC->second->getCategory() &&
	iterC->second->getBookingSchedule()->isFree(checkIn, checkOut)) {
      found = true;
      cout << iterC->first << " : " << iterC->second->getName() << endl;
    }
  }
  if(!found)
    cout << "We could not find a room that matches your criteria, please try again" << endl;
  return found;
}

//iterate through rooms' and print out using << operator
void Rooms::listRooms(void) {
  map<int, Room*>::const_iterator iterC;
  cout << "List of Rooms:" << endl;
  for(iterC = rooms.begin(); iterC!=rooms.end(); iterC++) {
    if(iterC->second->getState()) {
      string temp = iterC->second->getName();
      cout << iterC->first << " : " << iterC->second->getName() << endl;
    }
  }
}

void Rooms::printSummary(const int ID) {
  if(getRoom(ID) != NULL)
    cout << (*getRoom(ID)) << endl;
}

void Rooms::deleteRoom(const int ID) {
  if(getRoom(ID) != NULL)
    getRoom(ID)->setState(false);
}

//save rooms' to XML file
void Rooms::saveToFile(ofstream& handle) {
  map<int, Room*>::const_iterator iterC;
  for(iterC = rooms.begin(); iterC != rooms.end(); iterC++) {
    handle << "<room>" << endl;
    handle << "<ID>\n" << iterC->first << "\n</ID>" << endl;
    iterC->second->saveToFile(handle);
  }
  handle << "<counter>\n" << counter << "\n</counter>" << endl;
  handle << "</rooms>" << endl;

  cout << "rooms successfully saved to file" << endl; 
}

//display menu
void Rooms::displayMenu(char* border) {
  cout << border << endl;
  cout << "| Rooms Menu                                   |" << endl;
  cout << border << endl;
  cout << "|  (C)ategories Menu                           |" << endl;
  cout << "|  (L)ist all Rooms                            |" << endl;
  cout << "|  (A)dd a Room                                |" << endl;
  cout << "|  (R)emove a Room                             |" << endl;
  cout << "|  (E)dit a Room                               |" << endl;
  cout << "|  (V)iew a Room                               |" << endl;
  cout << "|  (D)isplay this Menu                         |" << endl;
  cout << "|  E(X)it to Main Menu                         |" << endl;
  cout << border << endl;

  while (1) {
    //get option
    char input[100];
    cout << "Enter Command (CLAREVDX): ";
    cin.getline(input, 100);
    input[0] = tolower(input[0]);
    switch (input[0]) {
    case 'l' : { //list all rooms
      listRooms();
      break;
    }
    case 'a' : { //add a room
      newRoom();
      break;
    }
    case 'r' : { //remove a room
      cout << "Remove which room (by ID)?" << endl;
      listRooms();
      int roomID;
      cin >> roomID;
      cin.ignore();
      deleteRoom(roomID);
      break;
    }
    case 'e' : { //edit a room
      cout << "Edit which room (by ID)?" << endl;
      listRooms();
      int roomID;
      cin >> roomID;
      cin.ignore();
      if(getRoom(roomID) != NULL)
	editMenu(roomID);
      goto menu;
    }
    case 'v' : { //view a room
      cout << "View which room (by ID)?" << endl;
      listRooms();
      int roomID;
      cin >> roomID;
      cin.ignore();
      printSummary(roomID);
      break;
    }
    case 'c' : { //goto categories menu
      Categories::displayMenu(border);
    }
    menu:
    case 'd' : { //redisplay menu
      system("clear");
      cout << border << endl;
      cout << "| Rooms Menu                                   |" << endl;
      cout << border << endl;
      cout << "|  (C)ategories Menu                           |" << endl;
      cout << "|  (L)ist all Rooms                            |" << endl;
      cout << "|  (A)dd a Room                                |" << endl;
      cout << "|  (R)emove a Room                             |" << endl;
      cout << "|  (E)dit a Room                               |" << endl;
      cout << "|  (V)iew a Room                               |" << endl;
      cout << "|  (D)isplay this Menu                         |" << endl;
      cout << "|  E(X)it to Main Menu                         |" << endl;
      cout << border << endl;
      break;
    }
    case 'x' : return;
    default : cout << "Invalid option" << endl; break;
    }
  }
}

//edit a room
void Rooms::editMenu(int ID) {
  printSummary(ID);
  Room* current = getRoom(ID);

  while (1) {
    int w = length-1;
    cout << setiosflags(ios_base::left) << setfill(' ')
	 << setw(w) << "| Edit (N)ame" << "|\n"
	 << setw(w) << "| Edit (C)apacity" << "|\n"
	 << setw(w) << "| Edit (R)ate" << "|\n"
	 << setw(w) << "| Edit C(A)tegory" << "|\n"
	 << setw(w) << "| (F)inish Editing" << "|\n"
	 << "+" << setfill('-') << setw(w-1) << "-" << "+" << setfill(' ') << endl;
  
    char input[100];
    cout << "Enter Command (NCRAX): ";
    cin.getline(input, 100);
    input[0] = tolower(input[0]);
    switch (input[0]) {
    case 'n' : { //change a room name
      cout << "Room name (currently " << current->getName() << "): ";
      char buffer[256];
      cin.getline(buffer,256);
      current->setName(buffer);
      cout << "\n" << (*current) << endl;
      break;
    }
    case 'c' : { //change a room's category
      cout << "Room capacity (currently " << current->getCapacity() << "): ";
      int num;
      cin >> num;
      cin.ignore();
      cin.clear();
      current->setCapacity(num);
      cout << "\n" << (*current) << endl;
      break;
    }
    case 'r' : { //change the rate of a room
      cout << "Room rate (currently " << current->getRate() << "): ";
      double num;
      cin >> num;
      cin.ignore();
      cin.clear();
      current->setRate(num);
      cout << "\n" << (*current) << endl;
      break;
    }
    case 'a' : { //change the category of a room
      cout << "Room category (currently " << current->getCategory() << "): ";
      int num;
      cin >> num;
      cin.ignore();
      cin.clear();
      current->setCategory(num);
      cout << "\n" << (*current) << endl;
      break;
    }
    case 'f' : return;
    }
  }
}
