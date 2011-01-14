#include <iostream>
using namespace std;

class Listitem {
public:
  void setnum(int nm);
  void setnext(Listitem *nx);
  int getnum();
  Listitem * getnext();
private:
  int num;
  Listitem *next;
};

void Listitem::setnum(int nm) {
  num = nm;
}
void Listitem::setnext(Listitem *nx) {
  next = nx;
}
int Listitem::getnum() {
  return num;
}
Listitem * Listitem::getnext() {
  return next;
}

main()
{ 
  Listitem *firstitem;
  Listitem *lastitem;
  Listitem *newitem;
  int searchNum;

  int numin;

  firstitem = 0;

  cout << "Enter a list of integers, one per line, zero to stop:\n";

  while (1) {
    cin >> numin;
    if (numin == 0) break;

    newitem = new Listitem;
    newitem->setnum(numin);
    newitem->setnext(0);

    if (firstitem == 0)
      firstitem = lastitem = newitem;
    else {
      lastitem->setnext(newitem);
      lastitem = newitem;
    }
  }
  
  Listitem *p;

  cout << "Here are the numbers you entered:\n";

  for (p = firstitem; p; p = p->getnext())
    cout << p->getnum() << '\n';

  cout << "Enter a number to search for:" << endl;
  cin >> searchNum;
  

  int i = 1;
  int found = 0;
  for(Listitem *Li = firstitem; Li; Li = Li->getnext()) {
    if(Li->getnum() == searchNum) {
       cout << searchNum << " found at position " << i << endl;
       found = 1;
    }
    i++;
  }
    if(!found)
      cout << searchNum << " was not found! " << endl;
     
}

    




