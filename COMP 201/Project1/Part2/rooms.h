#ifndef __ROOMS_HEADER__
#define __ROOMS_HEADER__
class Rooms;

#include "room.h"
#include "date.h"
#include <fstream>
#include <string>
#include <map>

using namespace std;

class Rooms
{
 public:
  Rooms(void);
  Rooms(ifstream& handle);
  ~Rooms(void);
  Rooms(const Rooms& original);
  Rooms& operator=(const Rooms& original);

  Room* newRoom(void);
  Room* getRoom(const int ID);
  int getRoomID(string name);
  bool locateRooms(int capacity, int category, Date& checkIn, Date& checkOut);
  void listRooms(void);
  void printSummary(const int ID);
  void deleteRoom(const int ID);
  void saveToFile(ofstream& handle);
  void displayMenu(char* border);
  void editMenu(int ID);
  
 private:
  map<int, Room*> rooms;
  int counter;
};

#endif
