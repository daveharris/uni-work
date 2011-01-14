class Invoice {
 public:
  void init(float p, int q);
  float cost();
  float discount();
 
 private: 
  int quantity;
  float unitprice;
  float gst;
};




