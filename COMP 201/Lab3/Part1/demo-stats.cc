#include <iostream>
#include "stats.h"

using namespace std;

main(){
  cout << "Demo: Demonstration Program for Stats Class...\n";
  cout << '\n';

  cout << "Demo: About to Declare Stats Variable...\n";
  Stats s;
  cout << "Demo: Done Stats Variable Declaration.\n\n";
  
  cout << "Demo: About to Note 100...\n";
  s.data(100);
  cout << "Demo: Done Note.\n\n";

  cout << "Demo: About to Note 200...\n";
  s.data(200);
  cout << "Demo: Done Note.\n\n";

  cout << "Demo: About to Note 225...\n";
  s.data(225);
  cout << "Demo: Done Note.\n\n";

  cout << "Demo: About to Print Stats...\n";
  s.print();
  cout << "Demo: Done Print.\n\n";

  cout << "Demo: About to Reset Stats...\n";
  s.reset();
  cout << "Demo: Done Reset.\n\n";

  cout << "Demo: About to Print Stats...\n";
  s.print();

  cout << "Demo: About to Note 0...\n";
  s.data(0);
  cout << "Demo: Done Note.\n\n";

  cout << "Demo: About to Print Stats...\n";
  s.print();
  cout << "Demo: Done Print.\n\n";

  cout << "Demo: Demonstration Program Done.\n";
}
