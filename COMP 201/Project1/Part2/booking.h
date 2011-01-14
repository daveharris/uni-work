#ifndef __BOOKING_HEADER__
#define __BOOKING_HEADER__
class Booking;

#include<string>
#include <iostream>
#include <fstream>
#include <vector>

#include "contact.h"
#include "date.h"
#include "room.h"
#include "guests.h"

using namespace std;

class Booking
{
 public:
  Booking(int ID, Date& checkIn, Date& checkOut, Room& room);
  Booking(ifstream& handle, int ID, Room& room);
  friend ostream& operator << (ostream& os, const Booking& right);
  
  Date* getCheckIn(void);
  Date* getCheckOut(void);
  Contact* getContact(void);
  double getCost(void);
  Guests* getGuests();
  void setCheckIn(Date& checkIn);
  void setCheckOut(Date& checkOut);
  void saveToFile(ofstream& handle);
 
 private:
  const int ID;
  Contact* contactPerson;
  Date checkIn;
  Date checkOut;
  Guests guests;
  Room* room;
  Booking(void);
};

#endif
