#include "invoice.h"


void Invoice::init(float p, int q) {
  unitprice = p;
  quantity = q;
  gst = 12.5;
}


float Invoice::cost()
{
  float costextax, costinctax;

  costextax = quantity * unitprice;

  costinctax = costextax + (costextax * gst) / 100;

  return costinctax;
}

float Invoice::discount()
{
  if (quantity >= 100) 
    return quantity * unitprice * 0.30;
  else
    return 0.0;

}
