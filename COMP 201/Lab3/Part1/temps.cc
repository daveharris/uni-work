//Iclude the stats header
#include "stats.h"
//So we can use cout and cin
#include <iostream>
using namespace std;

int main() {
  //Create a new stat object
  Stats stat;
  //Loop around to collect the temperatures
  while(1) {
    int temp = 0;
    cout << "Enter the temperature: " << endl;
    cin >> temp;
    //Add the temp so we can calculate the stats
    stat.data(temp);
    char cont;
    cout << "Add another temperature...? [y]es OR [n]o" << endl;
    cin >> cont;
    if(cont =='n')
      break;
  }
  //Print out the results
  stat.print();
    
}
