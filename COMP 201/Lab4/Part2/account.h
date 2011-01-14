class Account {

public:
  Account(int id, char *n, int init);
  Account();
  void credit(int x);
  void debit(int x);
  int getid();
  int getbalance();
  void print();

private:
  int idnumber;
  char name[32];
  int amount;
  int transcount;
};