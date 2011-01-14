import java.util.Enumeration;
import java.util.NoSuchElementException;
import jds.Bag;
import java.util.Enumeration;

/**
 * SortedArrayBag - a Bag collection;
 * @author Peter Andreae
 * @version July 2003
 * @see jds.Collection
 *
 * A bag is a collection of items with no order or structure
 * Duplicates are allowed
 * This implementation uses an array to store the elements.
 * When full (like Vector) it will create a new array of double the current size, and
 *	copy all the items over to the new array
 *	ie, items must implement Comparable
 * It does not allow null as an element of a bag.
 * It is efficient for accessing items; it is slower to insert.
 * When full (like Vector) it will create a new array of double the current size, and
 *	copy all the items over to the new array
 */

public class SortedArrayBag implements Bag {

	// data areas
	private int elementCount = 0;
	private Object [ ] elementData = new Object[10];

	public SortedArrayBag(){}

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
		return new ArrayBagEnumeration(this);
	}

	// the Bag Interface

	/** add an element to the bag, but only if not present already
	 * @param	the element */
	public void addElement (Object item) {
		if (item == null)
			throw new NoSuchElementException();
		int index = binarySearch(item);				 // where item should be.
		ensureCapacity();
		for (int i=elementCount; i>index; i--)
			elementData[i]=elementData[i-1];
		elementData[index]= item;
		elementCount++;
	}


	/** Determine if the bag contains an item
	 * @param	the item to look for
	 * @return	boolean */
	public boolean containsElement (Object item) {
		if (item==null) return false;
		int index = binarySearch(item);
		if (index == elementCount)
			return false;
		return (item.equals(elementData[index]));
	}


	/** Returns the element in the set matching a given item.
	 * @param	the item to look for
	 * @return	an item or null
	 * Throws	NoSuchElementException if no such item.
	 */
	public Object findElement (Object item) {
		if (item==null) throw new NoSuchElementException();
		int index = binarySearch(item);
		if (index<elementCount && item.equals(elementData[index]))
			return elementData[index];
		throw new NoSuchElementException();
	}

	/** Remove an element matching a given item
	 * Makes no change to the set if the item is not present.
	 * @param	the item to look for
	 * Throws	NoSuchElementException if no such item.
	 */
	public void removeElement (Object item) {
		if (item==null) throw new NoSuchElementException();
		int index = binarySearch(item);
		if (index<elementCount && item.equals(elementData[index])){
			elementCount--;
			for (int i=index; i<elementCount; i++)
	elementData[i]= elementData[i+1];
			elementData[elementCount]=null;
		}
		else
			throw new NoSuchElementException();
	}


	// ArrayBag utility methods

	/** Find the index of where an element is in the dataarray,
	 *	(or where it ought to be, if it's not there).
	 *	Assumes that the item is not null.
	 *	Uses binary search and requires that the items are kept in order. */
	private int binarySearch(Object itm){
		Comparable item = (Comparable)itm;
		int low = 0;							// minimum possible position of item
		int high	=	elementCount;									// maximum possible position of item
									//item should be at position [low .. high]
		while (low < high){
			int mid	=	(low + high) / 2;					// low < high, therefore mid >= low and mid < high
			if (item.compareTo(elementData[mid]) > 0)	// item should be after mid (ie [mid+1 .. high] )
	low = mid + 1;							// item should still be at [low .. high]	low <= high
			else																	// item should be at or before mid (ie [low .. mid] )
	high = mid;							// item should still be at [low .. high], low<=high
		}
		// item should be at [low .. high] and low = high	therefore item should be at low.
		return low;										
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

	private class ArrayBagEnumeration implements Enumeration{

		private Object[] data;		// The elements. 
						// Note, this is a reference to the array in the Bag, not a copy of the array
		private int size;				 // The number of elements in the collection
		private int index = 0;		// The index of the next element the enumeration should return

		public ArrayBagEnumeration (SortedArrayBag b) {
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


