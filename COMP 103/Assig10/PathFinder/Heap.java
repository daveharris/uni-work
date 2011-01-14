package jds.collection;

import java.util.Enumeration;
import jds.Indexed;
import jds.FindMin;
import jds.SortAlgorithm;
import java.util.Comparator;

/**
  * Heap - priority queue implemented using the Heap data structure;
  * for use with book
  * <a href="http://www.cs.orst.edu/~budd/books/jds/">Classic Data Structures 
  * in Java</a>
  * by <a href="http://www.cs.orst.edu/~budd">Timothy A Budd</a>, 
  * published by <a href="http://www.awl.com">Addison-Wesley</a>, 2001.
  * 
  * @author Timothy A. Budd
  * @version 1.1 September 1999
  */

public class Heap implements FindMin, SortAlgorithm {

  /**
    * initialize a newly created heap
    * 
    * @param t comparator used to order values
    */
  public Heap(Comparator t) {
    this(t, new Vector());
  }

  /**
    * initialize a newly created heap
    * 
    * @param t comparator used to order values
    * @param data initial data values
    */
  public Heap(Comparator t, Indexed data) {
    test = t;
    buildHeap(data);
    elementData = data;
  }

  private Indexed elementData;
  private Comparator test;

  // implementation of the Collection interface
  /**
    * Yields enumerator for collection
    * 
    * @return an <code>Enumeration</code> that will yield the elements of the 
    * collection @see java.util.Enumeration
    */
  public Enumeration elements() {
    return elementData.elements();
  }

  /**
    * Determines whether the collection is empty
    * 
    * @return true if the collection is empty
    */
  public boolean isEmpty() {
    return elementData.isEmpty();
  }

  /**
    * Determines number of elements in collection
    * 
    * @return number of elements in collection as integer
    */
  public int size() {
    return elementData.size();
  }

  // implementation of the FindMin interface
  /**
    * add a new value to the collection
    * 
    * @param value element to be inserted into collection
    */
  public void addElement(Object val) {
    int position = elementData.size();
    elementData.addElementAt(val, position);
    int parent = (position - 1) / 2;
    Object pvalue = null;
    if (parent >= 0)
      pvalue = elementData.elementAt(parent);
    while ((position > 0) && (test.compare(val, pvalue) < 0)) {
      elementData.setElementAt(pvalue, position);
      position = parent;
      parent = (position - 1) / 2;
      if (parent >= 0)
        pvalue = elementData.elementAt(parent);
    }
    elementData.setElementAt(val, position);
  }

  /**
    * yields the smallest element in collection
    * 
    * @return the first (smallest) value in collection
    */
  public Object getFirst() {
    return elementData.elementAt(0);
  }

  /**
    * removes the smallest element in collection
    * 
    */
  public void removeFirst() {
    int lastPosition = elementData.size() - 1;
    Object last = elementData.elementAt(lastPosition);
    elementData.setElementAt(last, 0);
    elementData.removeElementAt(lastPosition);
    adjustHeap(elementData, elementData.size(), 0);
  }

  // implementation of the SortAlgorithm interface
  /**
    * rearrange collection into asending order
    * 
    * @param data the values to be ordered
    */
  public void sort(Indexed data) {
    // build the initial heap
    buildHeap(data);
    for (int i = data.size() - 1; i > 0; i--) {
      // swap into last position
      Object temp = data.elementAt(i);
      data.setElementAt(data.elementAt(0), i);
      data.setElementAt(temp, 0);
      // and rebuild heap
      adjustHeap(data, i, 0);
    }
  }

  // internal methods
  private void buildHeap(Indexed data) {
    int max = data.size();
    for (int i = max / 2; i >= 0; i--)
      adjustHeap(data, max, i);
  }

  private void adjustHeap(Indexed data, int max, int index) {
    Object value = data.elementAt(index);
    while (index < max) {
      int childpos = index * 2 + 1;
      if (childpos < max) {
        if (((childpos + 1) < max) && 
            (test.compare(
                          data.elementAt(childpos + 1), 
                          data.elementAt(childpos)) < 0))
          childpos++;
        if (test.compare(value, data.elementAt(childpos)) < 0) {
          data.setElementAt(value, index);
          return;
        }
        else {
          Object v = data.elementAt(childpos);
          data.setElementAt(v, index);
          index = childpos;
        }
      }
      else {
        data.setElementAt(value, index);
        return;
      }
    }
  }
}

