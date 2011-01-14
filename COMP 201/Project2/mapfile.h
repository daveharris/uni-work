#ifndef __MAPFILEHEADER__
#define __MAPFILEHEADER__
 
#include "book.h"

using namespace std;

class Mapfile {

 public:
  Mapfile(int numBuckets);

  int openFile(char *filename);
  int closeFile();

  int insert(Book& book);
  int retrieve(Book* book, char* bookId);
  int remove(char* bookId);
  void print();
  
  int hashFunction(char key[7], int maxAddress);

 private:
  int fd;
  int numBuckets;

};

#endif
