#include <iostream>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include "account.h"
#include "accountfile.h"
#include <stdio.h>

using namespace std;

AccountFile::AccountFile() {
}

int AccountFile::afopen(char *filename) {
  int openretval;

  openretval = open(filename, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);
  fd = openretval;
  return (openretval >= 0);
}

int AccountFile::afclose() {
  int closeretval;

  closeretval = close(fd);
  return (closeretval >= 0);
}

void AccountFile::list() {
  Account a;
  int id; 
  int num;
  ssize_t readretval;
  off_t seekval;

  seekval = lseek(fd, 0, SEEK_SET);

  if (seekval < 0) 
    perror("Panic: seek fail in list!");

  num = 0;
  while (1) {
    readretval = read(fd, &a, sizeof(a));

    if (readretval != sizeof(a))
      break;

    id = a.getid();

    cout << "Account ID: " << id << "\n";

    num++;
    }

  cout << "Number of accounts: " << num << "\n\n";
}

int AccountFile::put(Account &x) {
  ssize_t numbytes;
  off_t seekretval;
  int relative;

  int position = getAccountPosition(x.getid());
  cout << "position: " << position << endl;
  if(position == -1)
	return 0;
  else if(position == -2) {
  //Not in file
  seekretval = lseek(fd, 0, SEEK_END);
  }
	
  else
    seekretval = lseek(fd, position, SEEK_SET);
  
  if (seekretval < 0) 
      cout << "get initial seek bad!\n";

    numbytes = write(fd, &x, sizeof(x));

    if (numbytes >= 0) {
      cout << "Write was successful.\n";
      return 1;
    }
    else {
      cout << "Write failed.\n";
	// Now call system library function "perror" that explains the error...
	perror("Write error explanation");
      return 0;
    }
}

int AccountFile::get(int i, Account &x) {
  Account y;
  int myid;
  ssize_t readretval;
  off_t seekretval;

  myid = i;

  seekretval = lseek(fd, 0, SEEK_SET);
  if (seekretval < 0) cout << "get initial seek bad!\n";

  while (1) {
    readretval = read(fd, &y, sizeof(y));
    if (readretval != sizeof(y))
      return 0;
    if (myid == y.getid()) {
      x = y;
      return 1;
    }
  }
}


int AccountFile::getAccountPosition(int id) {
  Account a;
  int offset = 0;
  ssize_t readretval;
  off_t seekval;

  seekval = lseek(fd, 0, SEEK_SET);
  if (seekval < 0) 
    return -1;

  while (1) {
    readretval = read(fd, &a, sizeof(a));
    //Got to the end of file, and not found
    if (readretval != sizeof(a))
      return -2;

    if(id == a.getid())
	   return offset;
    offset += sizeof(a);
  } 
  
}