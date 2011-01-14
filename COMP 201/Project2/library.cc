#include <iostream>
#include <fstream>

#include "book.h"
#include "bucket.h"
#include "mapfile.h"

using namespace std;

void displayMenu();
void loadText(char* filename);
void leave(int quit = 0);
void newMapfile();
void loadBinary();

Mapfile* mapfile;
int resultok;

int main() {

  char command;
  int resultok;

  newMapfile();

  displayMenu();

  while(1) {

    cin >> command;
    cin.ignore();
    cin.clear();
    command = tolower(command);

    if(command == 'e') {
      leave();
    }
    
    else if(command == 'c') {
      Book book;
	  book.initalise();
      mapfile->insert(book);
	  displayMenu();
    }

    else if(command == 'f') {
      char bookId[7];
      cout << "Enter a book id to find" << endl;
      cin >> bookId;
      cin.ignore();
      cin.clear();
      bookId[6] = '\0';
      Book *foundBook;
      mapfile->retrieve(foundBook, bookId);
	  foundBook->print();
	  displayMenu();
    }

    else if(command == 'd') {
      char bookId[7];
      cout << "Enter a book id to delete" << endl;
      cin >> bookId;
      cin.ignore();
      cin.clear();
      bookId[6] = '\0';
	  mapfile->remove(bookId);
	  displayMenu();
    }

    else if(command == 'p') {
      mapfile->print();
      displayMenu();
    }
 
    
    else if(command == 'r') {
      char filename[256];
      cout << "Enter the filename to read from" << endl;
      cin >> filename;
      loadText(filename);
      displayMenu();
    }

    else
      displayMenu();
  }
}

void displayMenu() {

  cout <<"\n----------------------------------------------------" << endl;
  cout << "Welcome to the Victoria University Library System" << endl;
  cout << "                 By David Harris" << endl;
  cout <<"----------------------------------------------------" << endl;
  cout << "Please choose from the below options:" << endl;
  cout << "-------------------------------------" << endl;
  cout << "(C)reate new book" << endl;
  cout << "(F)ind a book" << endl;
  cout << "(D)elete a book" << endl;
  cout << "(P)rint contents" << endl;
  cout << "(R)ead from text file" << endl;
  cout << "(E)xit to system" << endl;
  cout << "-------------------------------------" << endl;
  cout << "Choose (slrfcde):  ";

}

void loadText(char* filename) {

  char id[7], title[30], author[12], callcode[12];
  char buffer[256];
  ifstream inputFile;
  inputFile.open(filename);
  if(!inputFile) {
    cout << "\""<< filename << "\"" << " was not found" << endl;
    return;
  }
  if(inputFile.fail()) {
    cout << "Unable to open " << filename << endl;
    return;
  }
  
  cout << "File Reading in progress..." << endl;
  while(inputFile.good()) {

    inputFile.get(buffer, 7);
    strcpy(id, buffer);
    id[7] = '\0';

    inputFile.get(buffer, 31);
    strcpy(title, buffer);
    title[31] = '\0';

    inputFile.get(buffer, 13);
    strcpy(author, buffer);
    author[13] = '\0';

    inputFile.get(buffer, 255);
    strcpy(callcode, buffer);
    callcode[13] = '\0';

    Book book(id, title, author, callcode);
    mapfile->insert(book);

    inputFile.getline(buffer, 256);
  }

  cout << "\""<< filename << "\"" << " was successfully read in" << endl;
  inputFile.close();
}


void loadBinary() {
  resultok = mapfile->openFile("library.dat");
    if (resultok) 
      cout << "File opened ok.\n";
    else {
	  cout << "Unable to open file!\n";
	  leave(1);
    }     
}

void newMapfile() {
  int numBuckets;
  cout << "Creating new mapfile..." << endl;
  cout << "Enter the number of buckets you wish to have:" << endl;
  cin >> numBuckets;
  cin.ignore();
  cin.clear();
  if(numBuckets == 0) {
    cout << "The number of buckets can't be zero!!" << endl;
	displayMenu();
  }
  mapfile = new Mapfile(numBuckets);
  loadBinary();
}


void leave(int quit) {
  cout << "Thanyou for using the Victoria University Library System" << endl;
  mapfile->closeFile();
  delete mapfile;
  exit(quit);
}

