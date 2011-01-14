#include <iostream>

#include "invoice.h"

using namespace std;


main()
{
  float inprice;
  int inquantity;
  Invoice order;
  float ordercost;
  float orderdiscount;

  cout << "Enter Unit Price:\n";
  cin >> inprice;

  cout << "Enter Quantity:\n";
  cin >> inquantity;
  
  order.init(inprice, inquantity);

  ordercost = order.cost();

  cout << "Order Cost is " << ordercost << '\n';

  orderdiscount = order.discount();
  
  cout << "Discount is " << orderdiscount << '\n';

}
