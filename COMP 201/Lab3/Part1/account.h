class Account {

public:
  Account(int x);
  Account();
  void credit(int x);
  void debit(int x);
  int balance();
  void print();

private:

  int amount;
  int transcount;
};
