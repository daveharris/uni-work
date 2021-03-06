import java.util.Enumeration;
import java.util.NoSuchElementException;
import jds.Set;
import jds.Bag;
import java.util.Enumeration;

/**
 * ArraySet - a Set collection;
 * @author Peter Andreae
 * @version July 2002
 * @see jds.Collection
 *
 * The implementation uses an array to store the items.
 * It does not keep the items in any particular order, and may change the order of
 *  the remaining items when removing items.
 * Only requires that the elements can be compared with  equals(Object)
 * null items are not allowed, and cause a "NoSuchElementException"
 * It is not particularly efficient
 * When full (like Vector) it will create a new array of double the current size, and
 *  copy all the items over to the new array
 */

public class ArraySet implements Set {

  // data areas
  private int elementCount = 0;
  private Object [ ] elementData = new Object[10];

  public ArraySet(){}

  // The Collection interface

  /** Determines whether the collection is empty
   * @return true if the collection is empty */
  public boolean isEmpty () {
    return elementCount == 0;
  }

  /** Determines number of elements in collection
   * @return number of elements in collection as integer */
  public int size () {
    return elementCount;
  }

  /** Yields enumerator for collection
   * @return an <code>Enumeration</code> that will yield the elements of the collection
   * @see java.util.Enumeration */
  public Enumeration elements () {
    return new ArraySetEnumeration(this);
  }

  // the Bag Interface (Set extends Bag)

  /** add an element to the set, but only if not present already
   * @param	the element */
  public void addElement (Object item) {
    if (item == null)
      throw new NoSuchElementException();
    if (!containsElement(item)){
      ensureCapacity();
      elementData[elementCount]=item;
      elementCount++;
    }
  }


  /** Determine if the set contains an item
   * @param	the item to look for
   * @return	boolean */
  public boolean containsElement (Object item) {
    if (item==null) return false;
    return (findIndex(item) >= 0);
  }


  /** Returns the element in the set matching a given item.
   * @param	the item to look for
   * @return	an item or null
   * Throws  NoSuchElementException if no such item.
   */
  public Object findElement (Object item) {
    if (item==null) throw new NoSuchElementException();
    int index = findIndex(item);
    if (index >=0)
	return elementData[index];
    throw new NoSuchElementException();
  }

  /** Remove an element matching a given item
   * Makes no change to the set if the item is not present.
   * @param	the item to look for
   * Throws  NoSuchElementException if no such item.
   */
  public void removeElement (Object item) {
    if (item==null) throw new NoSuchElementException();
    int index = findIndex(item);
    if (index >=0){
	elementCount--;
	elementData[index]= elementData[elementCount];
    }
    else
      throw new NoSuchElementException();
  }

  // the rest of the Set Interface


  /** form union with argument set
   * @param other collection to be joined to current
   */
  public void unionWith (Bag other){
    // add each element of other to this (duplicates won't be added)
    for (Enumeration e = other.elements(); e.hasMoreElements(); ){
      addElement(e.nextElement());
    }
  }

  /** form intersection with argument set
   * @param other collection to be intersected with current
   */
  public void intersectWith (Bag other){
    // Every item in other that is also in this, move to the front, then "drop off" the others
    int newCount = 0;  // number of items to keep because they are in both sets
    for (Enumeration e = other.elements(); e.hasMoreElements(); ){
      Object item = e.nextElement();
      for (int i=0; i<elementCount; i++){
	if (item.equals(elementData[i])){
	  //swap items at i and at newCount;
	  Object temp = elementData[i];
	  elementData[i] = elementData[newCount];
	  elementData[newCount] = temp;
	  newCount++;
	  break;
	}
      }
    }
    elementCount=newCount;
  }

  /** form difference from argument set
   * @param aSet collection to be compared to current
   */
  public void differenceWith (Bag other){
    for (Enumeration e = other.elements(); e.hasMoreElements(); ){
      Object item = e.nextElement();
      // Remove item from this set if present
      for (int i=0; i<elementCount; i++){
	if (item.equals(elementData[i])){
	  elementCount--;
	  elementData[i]= elementData[elementCount];
	  break;
	}
      }
    }
  }

  /** is current set a subset of another collecton
   * @param other collection to be tested against
   * @return true if current set is subset of argument collection
   */
  public boolean subsetOf (Bag other){
    // must check that each item of this is also in the other.
    // this implementation depends on other being a Bag.
    for (int i=0; i<elementCount; i++){
      if (!other.containsElement(elementData[i])){
	//item i is missing from other
	return false;
      }
    }
    // we didn't find any item that was missing from other
    return true;
  }

  // ArraySet utility methods

  /** Find the index of an element in the dataarray, or -1 if not present
   *  Assumes that the item is not null */
  private int findIndex(Object item){
    for (int i=0; i<elementCount; i++)
      if (item.equals(elementData[i]))
	return i;
    return -1;
  }

  /** Ensure elementData array has sufficient number of elements
   * to add a new element */
  private void ensureCapacity () {
    if (elementCount < elementData.length) return;
    Object [ ] newArray = new Object[elementData.length*2];
    for (int i = 0; i < elementCount; i++)
      newArray[i] = elementData[i];
    elementData = newArray;
  }

  private class ArraySetEnumeration implements Enumeration{

    private Object[] data;    // The elements. 
			      // Note, this is a reference to the array in the Bag, not a copy of the array
    private int size;         // The number of elements in the collection
    private int index = 0;    // The index of the next element the enumeration should return

    private ArraySetEnumeration (ArraySet b) {
      data = b.elementData;
      size = b.elementCount;
    }

    /** Can the enumeration continue?
     * @return true if enumeration has at least one more element */
    public boolean hasMoreElements () {
      return index < size;
    }

    /** get next element in enumeration
     * @return value of next element in enumeration */
    public Object nextElement () {
      return data[index++];
    }
  }


}


