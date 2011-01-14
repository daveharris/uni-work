import java.util.Enumeration;
import java.util.NoSuchElementException;
import jds.Map;
import java.util.Enumeration;

/**
 * ArrayMap - a Map collection;
 * @author Peter Andreae
 * @version July 2003
 * @see jds.Collection
 *
 * The implementation uses an array to store the items.
 * It does not keep the items in any particular order, and may change the order of
 *	the remaining items when removing items.
 * Only requires that the keys can be compared with	equals(Object)
 * null keys are not allowed, and cause a "NoSuchElementException"
 * It is not particularly efficient
 * When full (like Vector) it will create a new array of double the current size, and
 *	copy all the items over to the new array
 */

public class ArrayMap implements Map {

	// data areas
	private int elementCount = 0;
	private Object [ ] elementData = new Object[10];

	public ArrayMap(){}

	// the Collection interface
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
	 * @return an <code>Enumeration</code> that will yield all the keys of the collection
	 * @see java.util.Enumeration */
	public Enumeration elements () {
		return new ArrayMapEnumeration(this);
	}

	// the Bag Interface (Set extends Bag)

	/** Set the value associated with the key to be item
	 * @param	the element */
	public void set (Object key, Object item) {
		if (key==null)
			throw new NoSuchElementException();
		int index = findKey(key);
		if (index>=0){
			Association pair = (Association)elementData[index];
			pair.value = item;
		}
		else{
			ensureCapacity();
			elementData[elementCount]=new Association(key,item);
			elementCount++;
		}
	}


	/** Determine if the set contains a key
	 * @param	the key to look for
	 * @return	boolean */
	public boolean containsKey (Object key) {
		if (key==null)
			return false;
		return (findKey(key) >= 0);
	}


	/** Returns the element in the set matching a given item.
	 * @param	the item to look for
	 * @return	an item or null
	 * Throws	NoSuchElementException if no such item.
	 */
	public Object get (Object key) {
		if (key==null)
			throw new NoSuchElementException();
		int index = findKey(key);
		if (index >= 0){
			Association pair = (Association)elementData[index];
			return pair.value;
		}
		else 
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
		int index = findKey(key);
		if (index >= 0){
			elementCount--;
			elementData[index]= elementData[elementCount];
			elementData[elementCount]=null;
			}
		else 
			throw new NoSuchElementException();
	}

	// ArrayMap utility methods

	/** Find the index of the pair containing key.
			Assumes key is not null */
	private int findKey(Object key){
		for (int i=0; i<elementCount; i++){
			Association pair = (Association)elementData[i];
			if (key.equals(pair.key))
	return i;
		}
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
			if (other instanceof Association) 
	return key.equals(((Association) other).key);
			return key.equals(other);
		}

		public int hashCode () {
			return key.hashCode();
		}

	}

	private class ArrayMapEnumeration implements Enumeration {

		private Object[] data;
		private int size;
		private int index = 0;	// where we are up to.

		private ArrayMapEnumeration (ArrayMap m) {
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
			return ((Association) data[index++]).key;
		}
	}


}


