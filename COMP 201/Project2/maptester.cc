#include <iostream>

#include "book.h"
#include "mapfile.h"
#include "bucket.h"

using namespace std;

int main() {

  Mapfile mapfile(5);

  mapfile.openFile("test.data");

  /*Book book1("300001", "File Structures1 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book2("300002", "File Structures2 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book3("300003", "File Structures3 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book4("300004", "File Structures4 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book5("300005", "File Structures5 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book6("300006", "File Structures6 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book7("300007", "File Structures7 : An Object Orientated Approach", "David Harris", "0256456723");*/



Book book1("148000", "A primer in data reduction : ", "aEhrenberg, ", "AQA276.12 E33");
Book book2("058200", "Advanced programming : a pract", "Barron, D. ", "WQA76.6 B277"); 
Book book3("099900", "Almost sure convergence       ", "Stout, Willi", "QA273.5 S889");
Book book4("058900", "Automated reasoning : introduc", "Wos, Larry. ", "QA76.9 A96 A");
Book book5("099800", "Computer case histories;      ", "Summersbee, ", "QA76 S955 C");
Book book6("101100", "Computers and data processing.", "Dopping, Oll", "QA76.5 D692"); 
Book book7("040000", "Concurrent Euclid, The UNIX sy", "Holt, R. C. ", "QA76.73 C64"); 
/*157500Controlling and auditing smallGaston, S. JQA76.5 G256 
108600Coroutines : a programming metMarlin, ChriQA76.6 M348 
060000Distributed computing systems Paker, YakupQA76.9 D5 D6*/



  cout << "Done making new books" << endl;
  
  mapfile.insert(book1);
  mapfile.insert(book2);
  mapfile.insert(book3);
  mapfile.insert(book4);
  mapfile.insert(book5);
  mapfile.insert(book6);
  mapfile.insert(book7);
  
  cout << "After insert" << endl;
  cout << "Now printing..." << endl;
  mapfile.print();

  mapfile.remove("058200");
  mapfile.remove("148000");


  
  //Book foundBook;
  //mapfile.retrieve(&foundBook, "300002");





  //cout << "After remove" << endl; 
  //mapfile.print();  
}

