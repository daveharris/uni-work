#ifndef __BOOKINGSCHEDULE_HEADER__
#define __BOOKINGSCHEDULE_HEADER__
class BookingSchedule;

#include <string>
#include <iostream>
#include <fstream>
#include <map>

#include "date.h"
#include "booking.h"
#include "room.h"

using namespace std;

class BookingSchedule {
 public:
  BookingSchedule(void);
  BookingSchedule(ifstream& handle, Room& room);

  ~BookingSchedule(void);
  BookingSchedule(const BookingSchedule& original);
  BookingSchedule& operator=(const BookingSchedule& original);
  
  Booking* newBooking(Date& checkIn, Date& checkOut, Room& room);
  bool isFree(Date& beginning, Date& end);
  void printSchedule(Room* room);
  Booking* getBooking(int ID);
  void deleteBooking(int ID);
  double getTakings(void) const;
  void saveToFile(ofstream& handle);
  void displayMenu(char* border, Room* current);
  void editMenu(int ID);

 private:
  map<int,Booking*> bookings;
  int counter;
};

#endif
