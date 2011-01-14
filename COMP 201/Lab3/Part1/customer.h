#include "account.h"
#include "stats.h"

class Customer {
 public:
  Customer(char *newname);
  void purchase(int x);
  void payment(int x);
  void report();

 private:

  char name[50];
  Account acct;
  Stats purchase_stats;
  Stats payment_stats;
};
