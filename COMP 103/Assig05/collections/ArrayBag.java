import java.util.Enumeration;
import java.util.NoSuchElementException;
import jds.Bag;
import jds.util.IndexedEnumeration;

/**
 * ArrayBag - a bag collection;
 * @author Peter Andreae
 * @version July 2002
 *
 * A bag is a collection of items with no order or structure
 * Duplicates are allowed
 * This implementation uses an array to store the elements.
 * When full (like Vector) it will create a new array of double the current size, and
 *	copy all the items over to the new array
 * The order of elements in the array may be changed when an element is deleted
 * Only requires that the elements can be compared with	equals(Object)
 * null items are not allowed, and cause a "NoSuchElementException"
 */

public class ArrayBag implements Bag {

	// data fields
	private int elementCount = 0;
	private Object [ ] elementData = new Object[10];

	// Constructor
	public ArrayBag(){
	}

	// the Collection interface

	/** Determines whether the collection is empty
	 * @return true if the collection is empty
	 */
	public boolean isEmpty () {
		return elementCount == 0;
	}

	/** Determines number of elements in collection
	 * @return number of elements in collection as integer
	 */
	public int size () {
		return elementCount;
	}

	/** Yields enumerator for collection
	 * @return an <code>Enumeration</code> that will yield the elements of the collection
	 * @see java.util.Enumeration
	 */
	public Enumeration elements () {
		return new ArrayBagEnumeration(this);
	}

	// The Bag Interface

	/** Add an element to the bag
	 * @param	the element
	 */
	public void addElement (Object item) { 
		if (item==null) throw new NoSuchElementException();
		ensureCapacity();
		elementData[elementCount]=item;
		elementCount++;
	}


	/** Determine if bag contains an item
	 * @param	the item to look for
	 * @return	boolean
	 */
	public boolean containsElement (Object item) { 
		if (item==null) return false;
		return (findIndex(item)>=0);
	}


	/** Return an element matching a given item
	 * @param	the item to look for
	 * @return	an item or null
	 */
	public Object findElement (Object item) {
		if (item==null) throw new NoSuchElementException();
		int index = findIndex(item);
		if (index>=0)
			return elementData[index];
		else
			throw new NoSuchElementException();
	}

	/** Remove an element matching a given item
	 * @param	the item to look for
	 * @return	an item or null
	 */
	public void removeElement (Object item) {
		if (item==null) throw new NoSuchElementException();
		int index = findIndex(item);
		if (index>=0){
	elementCount--;
	elementData[index]= elementData[elementCount];
	elementData[elementCount] = null;
		}
		else
			throw new NoSuchElementException();
	}


	// ArrayBag utility methods

	/** Find the index of an element in the dataarray, or -1 if not present
	 *	Assumes that the item is not null */
	private int findIndex(Object item){
		for (int i=0; i<elementCount; i++)
			if (item.equals(elementData[i]))
	return i;
		return -1;
	}

	/** Ensure elementData array has sufficient number of elements
	 * to add a new element
	 */
	private void ensureCapacity () {
		if (elementCount < elementData.length) return;
		Object [ ] newArray = new Object[elementData.length*2];
		for (int i = 0; i < elementCount; i++)
			newArray[i] = elementData[i];
		elementData = newArray;
	}

	private class ArrayBagEnumeration implements Enumeration{

		private Object[] data;		// The elements. 
						// Note, this is a reference to the array in the Bag, not a copy of the array
		private int size;				 // The number of elements in the collection
		private int index = 0;		// The index of the next element the enumeration should return

		public ArrayBagEnumeration (ArrayBag b) {
			data = b.elementData;
			size = b.elementCount;
		}

		/** See if enumeration should continue
		 * @return true if enumeration has at least one more element
		 */
		public boolean hasMoreElements () {
			return index < size;
		}

		/** Get next element in enumeration
		 * @return value of next element in enumeration
		 */
		public Object nextElement () {
			return data[index++];
		}
	}


}


