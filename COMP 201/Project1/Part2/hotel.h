#ifndef __ENTRY__HEADER__
#define __ENTRY__HEADER__
class Hotel;

#include "categories.h"
#include "rooms.h"
#include "date.h"

class Hotel {

 public:
  Hotel(void);
  void saveToFile(void);
  void readFromFile(void);
  void updateTime(void);
  void displayMenu(void);
  
 private:
  Rooms* rooms;
  Date* currentDate;

};

#endif
