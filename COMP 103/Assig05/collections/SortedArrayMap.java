import java.util.Enumeration;
import java.util.NoSuchElementException;
import jds.Map;
import java.util.Enumeration;

/**
 * SortedArrayMap - a Map collection;
 * @author Peter Andreae
 * @version July 2003
 * @see jds.Collection
 *
 * The implementation uses an array to store the items.
 * It keeps the items in order, determined by the compareTo method on the keys
 *	ie, keys must implement Comparable
 * null keys are not allowed, and cause a "NoSuchElementException"
 * It is efficient for accessing items; it is slower to insert.
 * When full (like Vector) it will create a new array of double the current size, and
 *	copy all the items over to the new array
 */

public class SortedArrayMap implements Map {

	// data areas
	private int elementCount = 0;
	private Association [ ] elementData = new Association[10];

	public SortedArrayMap(){}

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
		return new SortedArrayMapEnumeration(this);
	}

	// the Map Interface

	/** add an element to the set, but only if not present already
	 * @param	the element */
	public void set (Object key, Object item) {
		if (key == null)
			throw new NoSuchElementException();
		int index = binarySearch(key);				 // where item should be.
		if (index < elementCount && key.equals(elementData[index].key)) {
			// set value associated with key to be the new item
			elementData[index].value = item;
		}
		else { // insert new key-value pair and move items up
			ensureCapacity();
			for (int i=elementCount; i>index; i--)
				elementData[i]=elementData[i-1];
			elementData[index]= new Association(key, item);
			elementCount++;
		}
	}


	/** Determine if the set contains an item
	 * @param	the item to look for
	 * @return	boolean */
	public boolean containsKey (Object key) {
		if (key==null) return false;
		int index = binarySearch(key);
		if (index == elementCount)
			return false;
		return (key.equals(elementData[index].key));
	}


	/** Returns the element in the set matching a given item.
	 * @param	the item to look for
	 * @return	an item or null
	 * Throws	NoSuchElementException if no such item.
	 */
	public Object get (Object key) {
		if (key==null) throw new NoSuchElementException();
		int index = binarySearch(key);
		if (index < elementCount && key.equals(elementData[index].key))
			return elementData[index].value;
		throw new NoSuchElementException();
	}

	/** Remove an element matching a given item
	 * Makes no change to the set if the item is not present.
	 * @param	the item to look for
	 * Throws	NoSuchElementException if no such item.
	 */
	public void removeKey (Object key) {
		if (key==null)
			throw new NoSuchElementException();
		int index = binarySearch(key);
		if (index < elementCount && key.equals(elementData[index].key)){
			elementCount--;
			for (int i=index; i<elementCount; i++)
	elementData[i]= elementData[i+1];
			elementData[elementCount]=null;
		}
		else
			throw new NoSuchElementException();
	}


	// ArrayMap utility methods

	/** Find the index of where an element is in the dataarray,
	 *	(or where it ought to be, if it's not there).
	 *	Assumes that the item is not null.
	 *	Uses binary search and requires that the items are kept in order. */
	private int binarySearch(Object key){
		Comparable k = (Comparable)key;
		int low = 0;							// minimum possible position of item
		int high	=	elementCount;									// maximum possible position of item
									//key should be at position [low .. high]
		while (low < high){
			int mid	=	(low + high) / 2;					// low < high, therefore mid >= low and mid < high
			if (k.compareTo(elementData[mid].key) > 0)	// key should be after mid (ie [mid+1 .. high] )
	low = mid + 1;							// key should still be at [low .. high]	low <= high
			else																	// key should be at or before mid (ie [low .. mid] )
	high = mid;							// key should still be at [low .. high], low<=high
		}
		// key should be at [low .. high] and low = high	therefore key should be at low.
		return low;										
	}


	/** Ensure elementData array has sufficient number of elements
	 * to add a new element */
	private void ensureCapacity () {
		if (elementCount < elementData.length) return;
		Association [ ] newArray = new Association[elementData.length*2];
		for (int i = 0; i < elementCount; i++)
			newArray[i] = elementData[i];
		elementData = newArray;
	}

	/*	private class Association	{ */
	private class Association {
		public Object key;
		public Object value;

		Association (Object k, Object v) {
			key = k;
			value = v;
		}

		/*
		public int compareTo (Object other) {
		if (other instanceof Association) 
			return key.compareTo(((Association) other).key);
		return key.equals(other);
		}
		*/


		public boolean equals (Object other) {
			if (other instanceof Association) {
	Association pair = (Association) other;
	return key.equals(pair.key);
			} 
			return key.equals(other);
		}

		public int hashCode () {
			return key.hashCode();
		}

	}

	private class SortedArrayMapEnumeration implements Enumeration {

		private Association[] data;
		private int size;
		private int index = 0;	// where we are up to.

		private SortedArrayMapEnumeration (SortedArrayMap m) {
			data = m.elementData;
			size = m.elementCount;
		}

		/** Can the enumeration continue?
		 * @return true if enumeration has at least one more element
		 */
		public boolean hasMoreElements () {
			return index < size;
		}

		/** get next element in enumeration
		 * @return value of next element in enumeration
		 */
		public Object nextElement () {
			return (data[index++]).key;
		}
	}



}


