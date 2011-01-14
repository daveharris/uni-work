#include <iostream>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <stdio.h>

#include "mapfile.h"
#include "book.h"
#include "bucket.h"

using namespace std;

Mapfile::Mapfile(int numBuckets) {
  this->numBuckets = numBuckets;

  for(int i=0; i<numBuckets; i++) {
    Bucket bucket;
    write(fd, &bucket, sizeof(bucket));
  }
}

int Mapfile::openFile(char *filename) {
  int openretval = open(filename, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);
  fd = openretval;
  return (openretval >= 0);
}


int Mapfile::closeFile() {
  int closeretval = close(fd);
  return (closeretval >= 0);
}



int Mapfile::insert(Book& book) {
  Bucket bucket;
  Bucket bucketNext;
  ssize_t numbytes;
  int insertretval;
  off_t seekval;
  ssize_t readretval;
  
  int hash = hashFunction(book.getId(), numBuckets);
  //cout << "hash: " << hash << endl;

  seekval = lseek(fd, hash*512, SEEK_SET);
  if (seekval < 0) 
    cout << "get initial seek bad!\n" << endl;
  
  readretval = read(fd, &bucket, sizeof(bucket));
  
  insertretval = bucket.insert(book);

  if(insertretval == 1) {
    seekval = lseek(fd, hash*512, SEEK_SET);
	numbytes = write(fd, &bucket, sizeof(bucket));
    return 1;
  }
  
  if(insertretval == -3) {
    //Book is already in bucket, say so and return
    cout << "Book "<< book.getId() << " is already in the map!! Book not added" << endl;
    return -1;
  }

  //Not in current bucket, so seek to the next one
    int i = 1;
    ssize_t readval;
    int insertval;
    int findval;
    while(i < numBuckets) {
      readval = read(fd, &bucketNext, sizeof(bucketNext));
      findval = bucketNext.find(book.getId());
      if(findval == -1) {
	    insertval = bucketNext.insert(book);
	    if(insertval != -2) {
	      //write to the file
    	  seekval = lseek(fd, (i*512)+(hash*512), SEEK_SET);
	      numbytes = write(fd, &bucketNext, sizeof(bucketNext));
	      if (!numbytes >= 0) {
	        cout << "Write failed.\n";
	        return 0;
	      }
		  return 1;
	    }
      }
      i++;
    }
}
    


int Mapfile::retrieve(Book* book, char* bookId) {
  Bucket bucket;
  Bucket bucketNext;
  int findretval;
  ssize_t readretval;
  off_t seekretval;

  int hash = hashFunction(bookId, numBuckets);

  seekretval = lseek(fd, hash*512, SEEK_SET);
  if (seekretval < 0) 
    cout << "Retrieve initial seek bad!\n" << endl;

  readretval = read(fd, &bucket, sizeof(bucket));
  if (readretval != sizeof(bucket)) {
    cout << "First read not successful" << endl;
    return -1;
  }
  
  findretval = bucket.find(bookId);
  
  if(findretval != -1) {
    //Book is in current bucket
    book = &(bucket.returnBook(findretval));
  }

  else {
    //Not in current bucket, so seek to the next one
    int i = 1;
    ssize_t readval;
    int findval;
    while(i < numBuckets) {
      readval = read(fd, &bucketNext, sizeof(bucket));
      if (readretval != sizeof(bucket)) {
	cout << "Sucessive read not successful" << endl;
	return -1;
      }
      findval = bucketNext.find(bookId);
      if(findval != -1) {
	//In this bucket, so return book at findval
	book = &(bucketNext.returnBook(findval));
      }
      i++;
    }
  }
}
    


int Mapfile::remove(char* bookId){
  Bucket bucket;
  Bucket bucketNext;
  ssize_t numbytes;
  int removeretval;
  off_t seekval;
  ssize_t readretval;
  
  int hash = hashFunction(bookId, numBuckets);
  //cout << "hash: " << hash << endl;

  seekval = lseek(fd, hash*512, SEEK_SET);
  if (seekval < 0) 
    cout << "get initial seek bad!\n" << endl;
  
  readretval = read(fd, &bucket, sizeof(bucket));
  
  removeretval = bucket.remove(bookId);

  if(removeretval == 1) {
    seekval = lseek(fd, hash*512, SEEK_SET);
	numbytes = write(fd, &bucket, sizeof(bucket));
	return 1;
  }
  
   //Not in current bucket, so seek to the next one
    int i = 1;
    ssize_t readval;
    int removeval;
    int findval;
    while(i < numBuckets) {
      readval = read(fd, &bucketNext, sizeof(bucket));
      findval = bucketNext.find(bookId);
      if(findval != -1) {
	    removeval = bucketNext.remove(bookId);
	    if(removeval != -1) {
	      //write to the file
    	  seekval = lseek(fd, (i*512)+(hash*512), SEEK_SET);
	      numbytes = write(fd, &bucketNext, sizeof(bucketNext));
	      if (!numbytes >= 0) {
	        cout << "Write failed.\n";
	        return 0;
	      }
	    }
      }
      i++;
    }
}
    





void Mapfile::print() {
  Bucket bucket;
  int hash; 
  int num;
  ssize_t readretval;
  off_t seekval;

  for(int i=0; i<numBuckets; i++) {
    seekval = lseek(fd, i*512, SEEK_SET);
    readretval = read(fd, &bucket, sizeof(bucket));
    cout << "Bucket " << i << endl;
    for(int i=0; i<7; i++) {
      if(readretval == 512) {
	    cout << "Position: " << i << "  Id:  "<< bucket.getId(i) << endl;
      }
	  else {
		cout << "Position: " << i << "  Id:  "<< endl;
	  }
	}
  }
}





/*
  seekval = lseek(fd, 0, SEEK_SET);
  if (seekval < 0) 
    cout <<"Panic: seek fail in list!" << endl;

  num = 0;
  while (1) {
  readretval = read(fd, &a, sizeof(b));

  if (readretval != sizeof(b))
  break;

  hash = b.getId();

  cout << "Account ID: " << id << "\n";

  num++;
  }

  cout << "Number of accounts: " << num << "\n\n";
  }
*/





int Mapfile::hashFunction(char key[7], int maxAddress) {
  int sum = 0;
  for(int i = 0; i < 7; i+=2)
    sum = (sum + 100 * key[i] + key[i+1]) % 19937;
  return (sum % maxAddress);
}  

    
