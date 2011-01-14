#ifndef _BUCKET_HEADER_
#define _BUCKET_HEADER_

#include "book.h"

#define SECTOR_SIZE 512
#define BOOK_SIZE sizeof(Book)
#define CAPACITY ((SECTOR_SIZE/(BOOK_SIZE))-1)

using namespace std;

class Bucket {

 public:
  Bucket();
  
  int insert(Book& book);
  int remove(char* bookId);
  int find(char* bookId);
  Book& returnBook(int position);
  char* getId(int position);
  void print();
  
 private:  
  int bucketCount;
  Book books[CAPACITY];

  //Needed to make the bucketsize 512 bytes
  int filler[15];

};

#endif
