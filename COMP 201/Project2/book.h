#ifndef __BOOK_HEADER__
#define __BOOK_HEADER__

using namespace std;


class Book {

 public:
  Book();
  Book(char* id, char* title, char* author, char* callcode);

  void initalise();

  void setId(char* id);
  void print();

  char* getId();
  char* getTitle();
  char* getAuthor();
  char* getCallcode();

 private:
  char id[7];
  char title[31];
  char author[13];
  char callcode[13];

};

#endif
