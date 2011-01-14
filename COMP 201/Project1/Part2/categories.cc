/* categories.cc - Categories Class
 * Created originally by David Harris harrisdavi 300069566
 * Contributions made by: 
 *   David Keane  keanedavi  300069137
 *   Vipul Delwadia delwadvipu 300069307
 *   Neil Ramsay  ramsayneil 300069252
 *  
 * Static Class containing Room Categories categories for Room Objects
 */

#include <map>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include "categories.h"

using namespace std;

int Categories::counter = 1;
map<int, string> Categories::categories;

//Creates a new category with a given name
int Categories::newCategory(string name) {
  categories[counter++] = name;
}

//Returns the name of the category from the ID
string Categories::getCategory(const int ID) {
  map<int, string>::iterator iter;
  string ans;
  iter = categories.find(ID);
  if(iter!=categories.end()) {
    ans = iter->second;
  }
  else {
    cout << "Category# " << ID << " does not exist!" << endl;
    return (string)((char[]){0xFF, '\0'});
  }
  return ans;

}

//Goes through the map, and prints out the id and name, 
//using the overloaded operator <<
void Categories::printCategories(void) {
  map<int, string>::const_iterator iterC;
  for(iterC = categories.begin(); iterC!=categories.end(); iterC++)
    cout << iterC->first << " : " << iterC->second << endl;
}

//rename method, used for edit, given an ID, change the name to 'name'
void Categories::renameCategory(const int ID, string name) {
  map<int, string>::iterator iter;
  iter = categories.find(ID);
  if(iter!=categories.end()) {
    iter->second = name;
  }
  else {
    cout << "category " << ID << " does not exist" << endl;
  }
}

void Categories::deleteCategory(const int ID) {
  categories.erase(ID);
}

//load from file method, needed so that the program can be re-made after quiting
//makes part of a xml file. continues above and below
void Categories::loadFromFile(ifstream& handle) {
  int ID;
  string name;
  char buffer[256];
  handle.getline(buffer, 256);//gets <categories>
  handle.getline(buffer, 256); //gets <category>
  while(!strncmp(buffer, "<category>", 256)) {
    handle >> ID;  //gets Category ID
    handle.ignore();
    handle.clear();
    handle.getline(buffer, 256);
    name = buffer;
    categories[ID] = name; //get/set category name
    handle.getline(buffer, 256); //gets </category>
    handle.getline(buffer, 256); //gets either <category> or </categories>
  }
  handle.getline(buffer, 256);// gets <counter>
  handle >> counter;
  handle.ignore();
  handle.clear();
  handle.getline(buffer, 256);//gets </counter>
  handle.getline(buffer, 256);//gets </categories>
}

void Categories::saveToFile(ofstream& handle) {
  map<int, string>::const_iterator iterC;
  handle << "<categories>" << endl;
  for(iterC = categories.begin(); iterC!= categories.end(); iterC++) {
    handle << "<category>\n" << iterC->first << "\n" << iterC->second << "\n</category>" << endl;
  }
  handle << "</categories>" << endl;
  handle << "<counter>\n" << counter << "\n</counter>" << endl;

  handle << "</categories>" << endl;
  
}

//Displays the menu structure correctly, uses a switch to use the menu
void Categories::displayMenu(char* border) {
  cout << border << endl;
  cout << "| Categories Menu                              |" << endl;
  cout << border << endl;
  cout << "|  (L)ist Categories                           |" << endl;
  cout << "|  (A)dd Category                              |" << endl;
  cout << "|  (R)emove Category                           |" << endl;
  cout << "|  (E)dit Category                             |" << endl;
  cout << "|  (D)isplay this Menu                         |" << endl;
  cout << "|  E(X)it to Main Menu                         |" << endl;
  cout << border << endl;

  while (1) {
    char input[100];
    cout << "Enter Command (LAREDX): ";
    cin.getline(input, 100);
    input[0] = tolower(input[0]);
    switch (input[0]) {
    case 'l' : {
      Categories::printCategories();
      break;
    }
    case 'a' : {
      char line[256];
      cout << "Category Name: ";
      cin.getline(line, 256);
      string name = line;
      Categories::newCategory(name);
      Categories::printCategories();
      break;
    }
    case 'r' : {
      int categoryID;
      cout << "Remove which Category (by ID)?" << endl;
      Categories::printCategories();
      cin >> categoryID;
      cin.ignore();
      if(getCategory(categoryID) != (string)(char[]){0xFF, '\0'})
	Categories::deleteCategory(categoryID);
      break;
    }
    case 'e' : {
      char line[256];
      int categoryID;
      cout << "Rename which Category (by ID)?" << endl;
      Categories::printCategories();
      cin >> categoryID;
      cin.ignore();
      if(getCategory(categoryID) != (string)((char[]){0xFF, '\0'})) {
	cout << "What shall be the new name? ";
	cin.getline(line, 256);
	string name = line;
	Categories::renameCategory(categoryID, name);
      }
      break;
    }
    case 'd' : {
      system("clear");
      cout << border << endl;
      cout << "| Categories Menu                              |" << endl;
      cout << border << endl;
      cout << "|  (L)ist Categories                           |" << endl;
      cout << "|  (A)dd Category                              |" << endl;
      cout << "|  (R)emove Category                           |" << endl;
      cout << "|  (E)dit Category                             |" << endl;
      cout << "|  (D)isplay this Menu                         |" << endl;
      cout << "|  E(X)it to Main Menu                         |" << endl;
      cout << border << endl;
      break;
    }
    case 'x' : return;
    default : cout << "Invalid option" << endl;
    }
  }
}
