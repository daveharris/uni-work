#include <iostream>

#include "book.h"
#include "mapfile.h"
#include "bucket.h"

using namespace std;

void print();
void hashFunction(char key[7], int maxAddress);
void writeFile();

Bucket bucket;
  Bucket bucketNext;
  ssize_t numbytes;
  int insertretval;
  off_t seekval;
  ssize_t readretval;
  char buffer[1024];
  int buffsize;
  int fd;

main() {

  Mapfile mapfile(5);

  fd = mapfile.openFile("test.dat");
  
  writeFile();

  /*int hash = hashFunction(book.getId(), numBuckets);
  cout << "hash: " << hash << endl;

  seekval = lseek(fd, hash*512, SEEK_SET);
  if (seekval < 0) 
    cout << "get initial seek bad!\n" << endl;
  
  readretval = read(fd, &bucket, sizeof(bucket));
  
  insertretval = bucket.insert(book);
  
  if(insertretval == -1) {
    //Book is already in bucket, say so and return
    cout << "Book "<< book.getId() << " is already in the map!! Book not added" << endl;
    return -1;
  }

  else if(insertretval == -2) {
    //Not in current bucket, so seek to the next one
    int i = 1;
    ssize_t readval;
    int insertval;
    int findval;
    while(i < numBuckets) {
      readval = read(fd, &bucketNext, sizeof(bucket));
      findval = bucketNext.find(book.getId());
      if(findval != -1) {
	//Not in this bucket, so loop back again
	insertval = bucketNext.insert(book);
	if(insertval != -2) {
	  //In the file, so add it at the found position
	  //and write to the file
	  numbytes = write(fd, &bucketNext, sizeof(bucketNext));
	  if (!numbytes >= 0) {
	    cout << "Write failed.\n";
	    return 0;
	  }
	}
      }
      i++;
    }
  }*/
}



void Mapfile::print() {
  Bucket bucket;
  int hash; 
  int num;
  ssize_t readretval;
  off_t seekval;

  seekval = lseek(fd, 0, SEEK_SET);
  readretval = read(fd, &bucket, sizeof(bucket));
  cout << "First bucket" << endl;
  for(int i=0; i<7; i++) {
	cout << "Position: " << i << "  Id:  "<< bucket.returnId(i) << endl;
  }
  readretval = read(fd, &bucket, sizeof(bucket));
  cout << "Second bucket" << endl;
  for(int i=0; i<7; i++) {
	cout << "Position: " << i << "  Id:  "<< bucket.returnId(i) << endl;
  }
}



int Mapfile::hashFunction(char key[7], int maxAddress) {
  int sum = 0;
  for(int i = 0; i < 7; i+=2)
    sum = (sum + 100 * key[i] + key[i+1]) % 19937;
  return (sum % maxAddress);
}  

void writeFile() {
      cout << "Enter buffer to be written, end with a full stop: \n";

      cin.get(buffer, 1024, '.');
      buffsize = strlen(buffer); // This is how much of the buffer really used

      numbytes = write(fd, &buffer, buffsize);

      cout << "Write return value was " << numbytes << ".\n";
      if (numbytes >= 0) {
	    cout << "Write was successful.\n";
      }
      else {
	    cout << "Write failed.\n";
	   }

}

