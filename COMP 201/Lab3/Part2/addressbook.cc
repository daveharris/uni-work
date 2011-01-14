//Include the entry header
#include "entry.h"

//Include libraries for i/o, files, strings and converting char to int
#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
using namespace std;


int main() {
  //Create a new array of length 100 and type Entry
  Entry entryArray[100];
  char nme[256];
  char phNum[256];
  int count = 0;  //Element count
  int num = 0;
  //Create the input and output objects for files
  ifstream inputFile;
  ofstream outputFile;
  //Open the file to read from
  inputFile.open("phonelist.txt");
  
  //If something goes wrong, say so, and exit
  if(inputFile.fail()) {
    cout << "Unable to open the file phonelist.txt" << endl;
    exit(1);
  }

  //Read in the lines to nme and phNum
  inputFile.getline(nme, 100);
  inputFile.getline(phNum, 100);
  //Print out the header 
  cout << " -----------------------------------" << endl;
  cout << " | Name \t \t | Phone # | " << endl;
  cout << " -----------------------------------" << endl;
  //Make sure that nothing goes wrong while reading file
  while(inputFile.good()) {
      //char array to int
      stringstream stnstr;
      stnstr << phNum;
      stnstr >> num;
      //Create a new Entry object with the values from the file
      Entry entry(nme, num);
      //Add to the array and increase element count
      entryArray[count++] = entry;
      //Print out the formatted file to the screen
      cout << " | " << nme << " \t \t | " << phNum << " | "<< endl;
      //Read in the next two lines (name and phone number)
      inputFile.getline(nme, 100);
      inputFile.getline(phNum, 100);
    }
  cout << " -----------------------------------" << endl;
  
  char answer[20];
  while(1) {
    //Ask the user what they want to do
    cout << "What do you want to do ...? [q]uit, [a]dd, [f]ind" << endl;
    cin >> answer;
    if(answer[0] == 'q') {
      char save[20];
      //Ask if they want to save the file
      cout << "Save the file...? [y]es or [n]o" << endl;
      cin >> save;
      if(save[0] == 'n')
	break;
      
      char fileName[50];
      cout << "Enter the filename to write to?" << endl;
      cin >> fileName;
      //Open the ouput file
      outputFile.open(fileName);
      if(outputFile.fail()) {
	cout << "Unable to open " << fileName << endl;
	return 0;
      }
      char buffer[256];
      //Store the contents of the array to the file
      for(int i=0; i<count; i++)
	outputFile << entryArray[i].getName() << "\n" << entryArray[i].getNum() << endl;
      break;
      
    }
    else if(answer[0] == 'a') {
      string newNme;
      int newNum;
      cout << "Enter the name of contact: " << endl;
      cin >> newNme;
      cout << "Enter the phone number of " << nme << endl;
      cin >> newNum;
      //Create a new Entry object with given data
      Entry newEntry(newNme, newNum);
      //Stroe object in array and update counter
      entryArray[count++] = newEntry;
      cout << newNme << ", (" << newNum << ") added to the list" << endl;
    }
    else if(answer[0] == 'f') {
      string search;
      cout << "Enter the name of the contact: " << endl;
      cin >> search;
      //Search the array for the name
      for(int i=0; i<count; i++) {
	if(entryArray[i].getName() == search)
	  cout << "Phone number for " << search << " is: " << entryArray[i].getNum() << endl;
      }
    }
  }
  inputFile.close();
  outputFile.close();
  return 0;
 }
