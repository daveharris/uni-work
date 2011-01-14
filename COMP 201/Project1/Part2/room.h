#ifndef __ROOM_HEADER__
#define __ROOM_HEADER__
class Room;

#include <fstream>
#include <string>
#include "bookingschedule.h"

using namespace std;

class Room
{
 public:
  Room(const int ID);
  Room(ifstream& handle, const int ID);

  ~Room(void);
  Room(const Room& original);
  Room& operator = (const Room& original);

  friend ostream& operator << (ostream& os, const Room& right);

  void setCategory(int category);
  void setName(string name);
  void setCapacity(int capacity);
  void setRate(double rate);
  void setState(bool state);
  int getCategory(void);
  string getName(void);
  int getCapacity(void);
  double getRate(void);
  int getID(void);
  bool getState(void);
  BookingSchedule* getBookingSchedule(void);
  void saveToFile(ofstream& handle);

 private:
  Room(void);
  const int ID;
  string name;
  int capacity;
  double rate;
  int category;
  bool state; //true if active, false if inactive
  BookingSchedule* bookingSchedule;
    
};

#endif
