/* guests.cc - Guests Class
 * Created originally by Vipul Delwadia delwadvipu 300069307
 * Contributions made by:
 *   David Keane  keanedavi  300069137
 *  
 * Contains a list of Guests and File and User Interaction methods.
 */

#include <string>
#include <iostream>
#include <fstream>
#include <map>
#include <sstream>
#include <iomanip>

#include "guests.h"

using namespace std;

// The default constructor, needed for some cases
Guests::Guests() {
  counter = 1;
}

// The file constructor, used when loading from the file
Guests::Guests(ifstream& handle) {
  int ID;
  string name;
  char buffer[256];
  handle.getline(buffer, 256);
  if(!strncmp(buffer, "<guests>", 256)) { //if there are guests
    handle.getline(buffer, 256); 
    while(!strncmp(buffer, "<guest>", 256)) { //open first guest
      handle >> ID;
      handle.ignore();
      handle.clear();
      handle.getline(buffer, 256);
      name = buffer;
      guests_map[ID] = name;
      handle.getline(buffer, 256);
      if(strncmp(buffer, "</guest>", 256)) { //end of this constructor's run
	return;
      }
      handle.getline(buffer, 256);
      if(!strncmp(buffer, "</guests>", 256)) {
	handle.getline(buffer, 256);
	if(!strncmp(buffer, "<counter>", 256)) {
	  handle >> counter;
          handle.ignore();
          handle.clear();
	  handle.getline(buffer, 256);
	  if(!strncmp(buffer, "</counter>", 256)) {
	    handle.getline(buffer, 256);
	    if(!strncmp(buffer, "</guests>", 256)) { //end of this constructor's run
	      return;
	    }
	  }
	}
      }
    }
  }
}

// << operator, for pretty output to cout
ostream& operator << (ostream& os, const Guests& right) {
  int w1 = 40;

  os << "+" << setw(w1) << setfill('-') << "+\n" << setiosflags(ios_base::left)
     << "| " << setfill(' ') << setw(w1-3) << "Guest List" << "|\n" << resetiosflags(ios_base::left)
     << "+" << setw(w1-1) << setfill('-') << "+" << setiosflags(ios_base::left) << endl;

  map<int,string>::const_iterator iterC;

  for(iterC = right.guests_map.begin(); iterC != right.guests_map.end(); iterC++) {
    os << "| " << iterC->first << " : " << setfill(' ') << setw(w1-7) << iterC->second << "|" << endl;
  }

  os << resetiosflags(ios_base::left) << "+" << setw(w1-1) << setfill('-') << "+";
}

// creats a new guest with the string givin as the name
void Guests::newGuest(string name) {
  guests_map[counter++] = name;
}

//renames the guest with the givin ID to the givin name
void Guests::editGuest(int ID, string name) {
  map<int,string>::iterator iter = guests_map.find(ID);
  if(iter!=guests_map.end())
    iter->second = name;
  else
    cout << "Guest# " << ID << " does not exist" << endl;
  //the ID givin is invalid
}

pair<map<int,string>::const_iterator,map<int,string>::const_iterator> Guests::getIterator(void) const {
  map<int,string>::const_iterator iterCB, iterCE;
  iterCB = guests_map.begin();
  iterCE = guests_map.end();
  pair<map<int,string>::const_iterator, map<int,string>::const_iterator> ans(iterCB, iterCE);
  return ans;
}

void Guests::removeGuest(int ID) {
  cout << "Are you sure you want to delete Guest #" << ID << ": " << guests_map[ID] << "? [YN]" <<endl;
  char input[256];
  cin.getline(input, 256);
  input[0] = tolower(input[0]);
  if (input[0] == 'y') {
    cout << "Guest #" << ID << " " << guests_map[ID] << " has been deleted" << endl;
    guests_map.erase(ID);
  }
  else
    cout << "Guest #" << ID << " " << guests_map[ID] << " has not been deleted" << endl;
  //the ID givin is invalid
}

// Saves to file handle passed by reference
void Guests::saveToFile(ofstream& handle) {
  handle << "<guests>" << endl;
  for (map<int,string>::const_iterator iterC = guests_map.begin(); iterC != guests_map.end(); iterC++) {
    handle << "<guest>\n" << iterC->first << "\n" << iterC->second << "\n</guest>" << endl; 
  }
  handle << "</guests>" << endl;
  handle << "<counter>" << endl;
  handle << counter << endl;
  handle << "</counter>" << endl;
  handle << "</guests>" << endl;
}
