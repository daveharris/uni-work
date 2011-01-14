#include <iostream>

#include "book.h"

using namespace std;


Book::Book() {
  strcpy(id, "0");
  strcpy(title, "0"); 
  strcpy(author, "0");
  strcpy(callcode, "0");   
}

Book::Book(char* id, char* title, char* author, char* callcode) {
  strncpy(this->id, id, 6);
  strncpy(this->title, title, 30);
  strncpy(this->author, author, 12);
  strncpy(this->callcode, callcode, 12);
  this->id[6] = '\0';
  this->title[30] = '\0';
  this->author[12] = '\0';
  this->callcode[12] = '\0';
}

void Book::initalise() {

  char buffer[256] = "";

  cout << "\nNew Book" << endl;
  cout << "------------------------------" << endl;
  
  cout << "Enter the id for the new book:" << endl;
  cin.getline(buffer, 256);
  strncpy(id, buffer, 6);
  id[6]='\0';
  
  cout << "Enter the title for the new book:" << endl;
  cin.getline(buffer, 256);
  strncpy(title, buffer, 30);
  title[30]='\0';

  cout << "Enter the author for the new book:" << endl;
  cin.getline(buffer, 256);
  strncpy(author, buffer,12);
  author[12]='\0';
  
  cout << "Enter the callcode for the new book:" << endl;
  cin.getline(buffer,256);
  strncpy(callcode, buffer,12);
  callcode[12]='\0';
  
  cout << "\nThe new book was created successsfully" << endl;
}

void Book::setId(char* id) {
  strncpy(this->id, id, 6);
}

void Book::print() {
  cout << "Id: " << getId() << endl;
  cout << "Title: " << getTitle() << endl;
  cout << "Author: " << getAuthor() << endl;
  cout << "Callcode: " << getCallcode() << endl;
}


char* Book::getId() {
  return id;
}

char* Book::getTitle() {
  return title;
}

char* Book::getAuthor() {
  return author;
}

char* Book::getCallcode() {
  return callcode;
}




/*void Book::setId(char buffer[256]) {
  cout << "Enter the id for the new book:" << endl;
  cin.getline(buffer, 256);
  strncpy(id, buffer, 6);
  id[6]='\0';
}

void Book::setTitle(char buffer[256]) {
  cout << "Enter the title for the new book:" << endl;
  cin.getline(buffer, 256);
  strncpy(title, buffer, 30);
  title[30]='\0';
}

void Book::setAuthor(char buffer[256]) {
  cout << "Enter the author for the new book:" << endl;
  cin.getline(buffer, 256);
  strncpy(author, buffer,12);
  author[12]='\0';
}

void Book::setCallcode(char buffer[256]) {
  cout << "Enter the callcode for the new book:" << endl;
  cin.getline(buffer,256);
  strncpy(callcode, buffer,12);
  callcode[12]='\0';
  }*/
