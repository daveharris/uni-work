#ifndef __DATE_HEADER__
#define __DATE_HEADER__
class Date;

#include<iostream>
#include<fstream>
using namespace std;

class Date
{
 public:
  Date(void);
  Date(int day, int month, int year, int time);
  Date(int yyyymmdd);
  Date(ifstream& handle);
  int difference(Date& secondDate);
  int getTime(void);
  int getDate(void) const;
  void setTime(int time);
  void setDate(int yyyy_mm_dd);
  void saveToFile(ofstream& handle);

 private:
  int day;
  int month;
  int year;
  int time;
};

#endif
