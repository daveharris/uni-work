/* date.cc - Date Class
 * Created originally by Neil Ramsay  ramsayneil 300069252
 * Contributions made by:
 *   David Keane  keanedavi  300069137
 *   Vipul Delwadia delwadvipu 300069307
 *  
 * Contains Date information, and provides a difference operation
 */

#include<iostream>
#include<fstream>
#include<ctime>
#include<sstream>
#include"date.h"

using namespace std;

//create date object using the current time as default
Date::Date(){
  time_t now_utc;
  tm * now_local;

  std::time(&now_utc); //prevents confusion with var - time
  now_local = localtime(&now_utc);

  //convert 12hr time to 24hr
  time = (now_local->tm_hour*100) + now_local->tm_min;
  
  //convert ctime time_t struct to absolute date
  day = now_local->tm_mday;
  month = now_local->tm_mon + 1;
  year = now_local->tm_year + 1900;
}

//create date object using given absolute date values
Date::Date(int day, int month, int year, int time)
{
  this->day = day;
  this->month = month;
  this->year = year;
  this->time = time;
}

//create date object using int formated date value
Date::Date(int yyyymmdd){
  year = yyyymmdd/10000;
  month = (yyyymmdd - (year*10000))/100;
  day = (yyyymmdd - year*10000 - month*100);
}

//create date object from file stream
Date::Date(ifstream& handle) {
  char buffer[100];
  
  handle.getline(buffer, 100); //<day>
  handle >> day;//day data
  handle.ignore();
  handle.clear();
  handle.getline(buffer, 100); //</day>

  handle.getline(buffer, 100);//<month>
  handle >> month;//month data
  handle.ignore();
  handle.clear();
  handle.getline(buffer, 100);//</month>

  handle.getline(buffer, 100);//<year>
  handle >> year;//year data
  handle.ignore();
  handle.clear();
  handle.getline(buffer, 100);//</year>

  handle.getline(buffer, 100);//<time>
  handle >> time;//time data
  handle.ignore();
  handle.clear();
  handle.getline(buffer, 100);//</time>

  handle.getline(buffer, 100);//</date>
}

//difference between two dates in days
int Date::difference(Date& secondDate) {

  //put two dates in ctime tm stucts
  struct tm* date1 = new tm();
  struct tm* date2 = new tm();

  //copy each date objects data to ctime struct
  date1->tm_hour = 0;
  date1->tm_isdst = 0;
  date1->tm_mday = this->day;
  date1->tm_min = 0;
  date1->tm_mon = this->month;
  date1->tm_sec = 0;
  date1->tm_year = this->year - 1900;

  date2->tm_hour = 0;
  date2->tm_isdst = 0;
  date2->tm_mday = secondDate.day;
  date2->tm_min = 0;
  date2->tm_mon = secondDate.month;
  date2->tm_sec = 0;
  date2->tm_year = secondDate.year - 1900;

  //convert to ctime time_t types so can be compared - ie EPOC
  time_t epoc1 = mktime(date1);
  time_t epoc2 = mktime(date2);

  //convert seconds difference to days difference and return
  return (int)difftime(epoc1, epoc2)/60/60/24;
}

int Date::getTime(void)	{
  return time;
}

//return int formated date
int Date::getDate(void) const {
  return (year*10000) + (month*100) + (day);
}
						
void Date::setTime(int time) {
  this->time = time;
}

//set date from formated int
void Date::setDate(int yyyy_mm_dd) {
  year = yyyy_mm_dd/10000;
  month = (yyyy_mm_dd - (year*10000))/100;
  day = (yyyy_mm_dd - year*10000 - month*100);
}

void Date::saveToFile(ofstream& handle) {
  handle << "<day>\n" << day << "\n</day>\n"
	 << "<month>\n" << month <<  "\n</month>\n"
	 << "<year>\n" << year << "\n</year>\n"
	 << "<time>\n" << time << "\n</time>\n"
	 << "</date>" << endl;
}
