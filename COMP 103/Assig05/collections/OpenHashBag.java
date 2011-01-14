import java.util.NoSuchElementException;
import java.util.Enumeration;
import jds.Bag;

/**
  * OpenHashBag - Bag collection stored using open address hashing;
  * @author Peter Andreae
  * @version August 2003
  * @see jds.Collection
  * 
  * The implementation stores items in an array.
  * It hashes the item to determine the cell the item be stored in.
  * If there is a collision - the cell already contains an item
  * it uses linear probing (looks at the next adjacent cell) until
  * it finds an empty cell
  * Null items are not allowed, and cause a "NoSuchElementException"
  * When the array reaches maximum capacity, it doubles the size of
  * the array, and re-inserts each of the items into the new array.
  */

public class OpenHashBag implements Bag {
  private Object[] data;
  private int count = 0;
  private double capacityFraction = 0.8;
  private double capacity;

  // contructors
  /**initialize newly created hash table	 */
  public OpenHashBag(int size) {
    data = new Object[size];
    capacity = Math.min(size, size * capacityFraction);
  }

  public OpenHashBag(int size, double frac) {
    data = new Object[size];
    capacityFraction = frac;
    capacity = Math.min(size, size * capacityFraction);
  }

  // the Collection interface
  public boolean isEmpty() {
    return count == 0;
  }
  public int size() {
    return count;
  }
  public Enumeration elements() {
    return new OpenHashBagEnumerator();
  }

  // the Bag interface
  public void addElement(Object val) {
    if (val == null)
      return;
    // make certain we have room for element
    if (count >= capacity)
      reAdjustTable();
    // then add to table
    int index = hash(val);
    while (data[index] != null)
      if (++index >= data.length)
        index = 0;
    data[index] = val;
    count++;
  }

  public boolean containsElement(Object val) {
    if (val == null)
      return false;
    int index = hash(val);
    while (data[index] != null) {
      if (val.equals(data[index]))
        return true;
      if (++index >= data.length)
        index = 0;
    }
    return false;
  }

  public Object findElement(Object val) {
    int index = hash(val);
    while (data[index] != null) {
      if (val.equals(data[index]))
        return data[index];
      if (++index >= data.length)
        index = 0;
    }
    throw new NoSuchElementException(val.toString());
  }

  public void removeElement(Object val) {
    //throw new NoSuchElementException(val.toString());
  }

  /** Find the index of item. */
  private int hash(Object value) {
    if (value == null)
      throw new NoSuchElementException();
    return Math.abs(value.hashCode()) % data.length;
  }

  private void reAdjustTable() {
    Object[] oldData = data;
    data = new Object[oldData.length * 2 - 1];
    count = 0;
    for (int i = 0; i < oldData.length; i++) {
      if (oldData[i] != null)
        addElement(oldData[i]);
    }
    capacity = Math.min(data.length, data.length * capacityFraction);
  }

  private class OpenHashBagEnumerator implements Enumeration {
    private int index;    //guarantees to point to the next item to be returned,
    // or to be >= data.length, meaning there are no more.

    public OpenHashBagEnumerator() {
      index = 0;
      while (index < data.length && data[index] == null)
        index++;
    }

    public boolean hasMoreElements() {
      return (index < data.length);
    }

    public Object nextElement() {
      if (index > data.length)
        throw new NoSuchElementException();
      Object ans = data[index];
      index++;                                    // always advance to next
      while (index < data.length && data[index] == null)
        index++;
      return ans;
    }
  }

}

