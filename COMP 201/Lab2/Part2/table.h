#define MAX_ENTRIES 2
#define MAX_KEYCHARS 32

class Table {
 public:
    int num_entries;
  // init:    initialise table
  void init();
  // insert:  insert key and value into table
  int insert(char key[], int value);
  // find:    find the value that matches a given key
  int find(char key[], int *value);
  // print:   print out entire table of keys and values
  void print();

 private:
  //int num_entries;
  char keys[MAX_ENTRIES][MAX_KEYCHARS];
  int values[MAX_ENTRIES];
};

