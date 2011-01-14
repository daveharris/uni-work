#include "stats.h"
#include <iostream>
using namespace std;

Stats::Stats() {
  //Create a new stats object, and set all stats to 0
  count = 0;
  sum = 0;
  max = 0;
  min = 0;
}

void Stats::data(int x) {
  //Add data to the collection
  count++;
  sum += x;
  //If first data entered set the max and min to that
  if(count == 1) {
    max = x;
    min = x;
    return;
  }
  //Set the new max if larger
  if( x > max)
    max = x;
  //Set the new min if smaller
  if(x < min)
    min = x;
}

void Stats::print() {
  //Print out the statistics
  int mean;
  if(count != 0) {
     mean = sum / count;
    cout << "\n-----------------------\n";
    cout << "Statistics: Count = " << count << "\n";
    cout << "Statistics: Minimum = "<< min << "\n";
    cout << "Statistics: Maximum = " << max << "\n";
    cout << "Statistics: Mean = " << mean <<"\n";
    cout << "-----------------------" << endl;
  }
  else {
  //If there are no stats, then print '?'
    char min = '?';
    char max = '?';
    char mean = '?';
    cout << "\n-----------------------\n";
    cout << "Statistics: Count = " << count << "\n";
    cout << "Statistics: Minimum = ?\n";
    cout << "Statistics: Maximum = ?\n";
    cout << "Statistics: Mean = ?\n";
    cout << "-----------------------" << endl;
  }
}

void Stats::reset () {
  count = 0;
  sum = 0;
  max = 0;
  min = 0;
}
