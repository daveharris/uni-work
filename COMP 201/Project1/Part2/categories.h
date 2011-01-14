#ifndef __CATEGORIES_HEADER__
#define __CATEGORIES_HEADER__
class Categories;

#include <map>
#include <string>
#include <fstream>

using namespace std;

class Categories {
 public:
  static int newCategory(string name);
  static string getCategory(const int ID);
  static void printCategories(void);
  static void renameCategory(const int ID, string name);
  static void deleteCategory(const int ID);
  static void loadFromFile(ifstream& handle);
  static void saveToFile(ofstream& handle);
  static void displayMenu(char* border);
	
 private:
  Categories(void);
  static map<int, string> categories;
  static int counter;
};
	
#endif
