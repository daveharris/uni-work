#include <iostream>

#include "bucket.h"
#include "book.h"

using namespace std;

Bucket::Bucket() {
  bucketCount = 0;  
}


int Bucket::insert(Book& book) {

  if(bucketCount == 0) {
    books[bucketCount++] = book;
    return 1;
  }
    
  if(bucketCount == CAPACITY)
    return -3;

  int i;
  for(int i=0; i< CAPACITY; i++) {
    //Check if the book id is in the array already
    if(books[i].getId() == book.getId())
      return -1;
    else if(!strcmp(books[i].getId(), "######")) {
      //A tombstone, so overwrite
      books[bucketCount++] = book;
      return 1;
    }
    else if(!strcmp(books[i].getId(), "0")) {
      //A null, so overwrite
      books[bucketCount++] = book;
      return 1;
    }
  }
  if(i+1 == CAPACITY) {
  //Not in this bucket, so return and
  //check next bucket
  return -2;
  }
}


int Bucket::remove(char* bookId) {

  //If only one book in array and id's match
  if((bucketCount == 1) && (!strcmp(books[bucketCount-1].getId(), bookId))) {
    bucketCount--;
    return 1;
  }
  
  for(int i=0; i< bucketCount; i++) {
    //Found in the array
    if(!strcmp(books[i].getId(), bookId)) {
      //Set the id to a tombstone
      books[i].setId("######");
      return 1;
    }
  }
  return -1;
}


int Bucket::find(char* bookId) {

  for(int i=0; i< bucketCount; i++) {
    if(!strcmp(books[i].getId(), bookId))
      return i;
  }
  return -1;
}

Book& Bucket::returnBook(int position) {
  return books[position];
}

char* Bucket::getId(int position) {
  return books[position].getId();
}


void Bucket::print() {

  for(int i=0; i< bucketCount; i++)
    cout << "Position: " << i << " Id: " << books[i].getId() 
	 << " Title: " << books[i].getTitle() << endl;
}



