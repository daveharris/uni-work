#include <iostream>

#include "book.h"
#include "bucket.h"

using namespace std;

int main() {

  /*cout << "Book constructor 1" << endl;
  Book book;

  book.initalise();

  cout << "Id: " << book.getId() << endl;
  cout << "Title: " << book.getTitle() << endl;
  cout << "Author: " << book.getAuthor() << endl;
  cout << "Callcode: " << book.getCallcode() << endl;*/

  /*cout << "\n\nBook constructor 2" << endl;
  Book bookC("300069566", "File Structures : An Object Orientated Approach", "David Harris", "0256456723");
  
  cout << "Id: " << bookC.getId() << endl;
  cout << "Title: " << bookC.getTitle() << endl;
  cout << "Author: " << bookC.getAuthor() << endl;
  cout << "Callcode: " << bookC.getCallcode() << endl;

  Mapfile mapfile;*/

  //cout << "Capacity: " << CAPACITY << endl;
  Book book1("300001", "File Structures1 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book2("300002", "File Structures2 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book3("300003", "File Structures3 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book4("300004", "File Structures4 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book5("300005", "File Structures5 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book6("300006", "File Structures6 : An Object Orientated Approach", "David Harris", "0256456723");
  Book book7("300007", "File Structures7 : An Object Orientated Approach", "David Harris", "0256456723");
  
  Bucket bucket;

  bucket.insert(book1);
  bucket.insert(book2);
  bucket.insert(book3);
  bucket.insert(book4);
  bucket.insert(book5);
  bucket.insert(book6);
  bucket.insert(book7);
  
  cout << "After insert" << endl;
  bucket.print();

  bucket.remove("300001");
  bucket.remove("300003");

  cout << bucket.find("300007") << endl;

  cout << "After remove" << endl; 
  bucket.print();
  
  /*cout << "SIZE OF BOOK OBJECT IS:  " << sizeof(Book) << endl;  
  cout << "SIZE OF BUCKET OBJECT IS:  " << sizeof(Bucket) << endl;
  cout << "SIZE OF int OBJECT IS:  " << sizeof(int) << endl;*/
}
