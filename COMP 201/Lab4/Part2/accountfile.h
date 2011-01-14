class AccountFile {
 public: 
  // Constructor.
  AccountFile();

  // Open a file.
  // Return 0 on failure, otherwise 1.
  int afopen(char *filename);

  // Close the file.
  // Return 0 on failure, otherwise 1.
  int afclose();

  // List accounts in file.
  void list();

  // Write account back to file.
  // If the account id exists, overwrite it.  Otherwise append it to the end of the file.
  // Return 0 on failure, otherwise 1.
  int put(Account &x);

  // Get an account from the file and place it into x.
  // Return 0 on failure, otherwise 1.
  int get(int i, Account &x);

  //Find the correct position to write to
  int getAccountPosition(int id);

 private:
  int fd;
};