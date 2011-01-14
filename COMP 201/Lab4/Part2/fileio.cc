// FILE IO SYSTEM CALL EXPLORER


#include <iostream>

// These include files contain useful declarations for system calls...
using namespace std;

#include <errno.h>    
#include <unistd.h>    
#include <fcntl.h>
#include <sys/types.h>
#include <stdio.h>

// This lets the compiler know "errno" is a global variable defined elsewhere
extern int errno; 

void showdata(char *data, int datasize);

main()
{
  char filename[256];  // Trust that filenames are no longer than this! 
  char buffer[1024];   // Trust we don't need a buffer bigger than this!
  int buffsize;
  int fd;

  int retval;
  ssize_t numbytes;  
  off_t seekval;

  int quit;
  char command;

  quit = 0;
  buffsize = 0;
  fd = -1;
  
  cout << "\nFile I/O Explorer Program...\n\n";

  while(!quit) {
    cout << "\nChoose: [o]pen, [r]ead, [w]rite, [s]eek, [c]lose, [q]uit\n";
    
    cin >> command;
    
    switch (command) {

    case 'q':
      quit = 1;
      break;
    
    case 'o':
      cout << "File name?\n";
      cin >> filename;
      cout << "Openning file named <" << filename << ">...\n";
      retval = open(filename, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);
      cout << "Open return value was " << retval << ".\n";
      if (retval >= 0) {
	cout << "Open was sucessful.\n";
	fd = retval;
	cout << "File descriptor is " << fd << ".\n";
      }
      else {
	cout << "Open failed.\n";
	 // Now call system library function "perror" to explain the error...
	perror("Open error explanation");
      }
      break;
      
    case 'r':
      cout << "Number of characters to read?\n";
      cin >> numbytes;
      cout << "Reading " << numbytes << " characters...\n";
      numbytes = read(fd, &buffer, numbytes);
      cout << "Read return value was " << numbytes << ".\n";
      if (numbytes >= 0) {
	cout << "Read was successful.\n";
	buffsize = numbytes;
	showdata(buffer, buffsize);
      }
      else {
	cout << "Read failed.\n";
	 // Now call system library function "perror" to explain the error...
	perror("Read error explanation");
      }
      break;

    case 'w':

      cout << "Enter buffer to be written, end with a full stop: \n";

      // First must remove the newline character waiting to be read...
      cin.unsetf(ios::skipws); cin >> command; cin.setf(ios::skipws);

      cin.get(buffer, 1024, '.');
      buffsize = strlen(buffer); // This is how much of the buffer really used

      showdata(buffer, buffsize);

      cout << "Writing buffer...\n";

      //cout << "THIS IS WHERE YOU SHOULD HAVE A CALL TO THE WRITE SYSTEM CALL!\n";
      // Replace the following line with an appropriate call...
      //retval = -1; errno = 90;

      numbytes = write(fd, &buffer, buffsize);

      cout << "Write return value was " << numbytes << ".\n";
      if (numbytes >= 0) {
	cout << "Write was successful.\n";
      }
      else {
	cout << "Write failed.\n";
	 // Now call system library function "perror" that explains the error...
	perror("Write error explanation");
      }

      // Now remove the full stop:
      cin.unsetf(ios::skipws); cin >> command; cin.setf(ios::skipws);

      break;

    case 's':
      int relative;
      off_t offset;

      cout << "Seek relative to: [b]eginning, [c]urrent, [e]nd?\n";
      cin >> command;
      
      switch (command) {
      default:
	cout << "Didn't understand that, so...\n";
      case 'b': 
	cout << "Seeking relative to beginning.\n";
	relative = SEEK_SET;
	break;
      case 'c':
	cout << "Seeking relative to current position.\n";
	relative = SEEK_CUR;
	break;
      case 'e':
	cout << "Seeking relative to end.\n";
	relative = SEEK_END;
	break;
      }
      
      cout << "Seek to what offset?\n";
      cin >> offset;      
      cout << "Seeking to offset " << offset << " ...\n";


      //cout << "THIS IS WHERE YOU SHOULD HAVE A CALL TO THE LSEEK SYSTEM CALL!\n";
      // Replace the following line with an appropriate call...
      //seekval = -1; errno = 90;

      seekval = lseek(fd, offset, relative);

      cout << "Seek return value was " << seekval << ".\n";
      if (seekval >= 0) {
	cout << "Seek was successful.\n";
      }
      else {
	cout << "Seek failed.\n";
	// Now call system library function "perror" that explains the error...
	perror("Seek error explanation");
      }
      break;
      
    case 'c':
      retval = close(fd);
      cout << "Close return value was " << retval << "\n";
      if (retval >= 0)
	cout << "Close was successful.\n";
      else {
	cout << "Close failed.\n";
	 // Now call system library function "perror" that explains the error...
	perror("Close error explanation");
      }
      break;
      
    default:
      cout << "This humble program does not understand your wise command...\n";
    }
  }
  cout << "\n\nFile I/O Explorer Program Done.\n";  

  // Note that all files are closed automagically when the program finishes
}


void showdata(char *data, int datasize)
{
  int i;
  cout << "Number of characters in data buffer: " << datasize << ".\n";
  cout << "Contents of data buffer:\n";
  for (i = 0; i < datasize; i++) {
    int nc = (int) data[i];
    cout << "Char " << i << " is <" << data[i] << "> ";
    cout << "Numeric Value: " << nc << "\n";
  }
  cout << "End of contents.\n";
}