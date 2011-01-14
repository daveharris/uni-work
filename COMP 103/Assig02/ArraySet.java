import java.util.Enumeration;
import java.util.NoSuchElementException;
import jds.Set;
import jds.Bag;

/**
 * ArraySet - a Set collection;
 * @author: David Harris
 * @ID:300069566
 * @Assignment 2
 * @see jds.Collection
 *
 * The implementation uses an array to store the items.
 * It does not keep the items in any particular order, and may change the order of
 *the remaining items when removing items.
 * It does not allow null as an element of a set.
 * It is not particularly efficient.
 * When full (like Vector) it will create a new array of double the current size, and
 *copy all the items over to the new array
 */

public class ArraySet implements Set {
// data areas
	private int elementCount = 0;
	private Object [ ] elementData = new Object[10];

	public ArraySet(){}

// the Collection interface
/** Determines whether the collection is empty
 * @return true if the collection is empty */
	public boolean isEmpty () {
	// YOUR CODE HERE
		return (size() == 0);
	}

	/** Determines number of elements in collection
	 * @return number of elements in collection as integer */
	public int size () {
	// YOUR CODE HERE
		return elementCount;
	}

	/** Yields enumerator for collection
	 * @return an <code>Enumeration</code> that will yield the elements of the collection
	 * @see java.util.Enumeration */
	public Enumeration elements () {
	// YOUR CODE HERE
		return new ArraySetEnumeration(this);
	}

	// the Bag Interface (Set extends Bag, but has additional constraints on the methods )

	/** Add an element to the set, but only if not present already
	 * @param	the element */
	public void addElement (Object item) {
	// YOUR CODE HERE
		if (!containsElement(item)) {
			ensureCapacity();
			elementData[size()] = item;
			elementCount++;
		}
	}

	/** Determine if the set contains an item
	 * @param	the item to look for
	 * @return	boolean */
	public boolean containsElement (Object item) {
	// YOUR CODE HERE
		for(int i=0; i<size(); i++) {
			if(elementData[i] != null && elementData[i].equals(item))
				return true;
		}
		return false;
	}

	/** Returns the element in the set matching a given item.
	 * @param	the item to look for
	 * @return	an item or null
	 * ThrowsNoSuchElementException if no such item. */
	public Object findElement (Object item) {
	// YOUR CODE HERE
		for (int i=0; i<size(); i++) {
			if(elementData[i] != null && elementData[i].equals(item))
				return elementData[i];
		}
		throw new NoSuchElementException();
	}

	/** Remove an element matching a given item
	 * Makes no change to the set if the item is not present.
	 * @param	the item to look for
	 * ThrowsNoSuchElementException if no such item. */
	public void removeElement (Object item) {
	// YOUR CODE HERE
		for (int i=0; i<size(); i++) {
			if(containsElement(item)) {
				elementData[i] = elementData[size()-1];
				elementData[size()] = null;
				elementCount--;
				return;
			}
								
		}
		throw new NoSuchElementException();
	}

	// the rest of the Set Interface

	/** Form union with argument set
	 * @param other collection to be joined to current set */
	public void unionWith (Bag other){
		throw new RuntimeException("ArraySet.unionWith is not yet implemented");
	// You do not need to implement this for assignment 2
	}

	/** Form intersection with argument set
	 * @param other collection to be intersected with current set */
	public void intersectWith (Bag other){
		throw new RuntimeException("ArraySet.intersectWith is not yet implemented");
	// You do not need to implement this for assignment 2
	}

	/** Form difference from argument set
	 * @param aSet collection to be compared to current set */
	public void differenceWith (Bag other){
		throw new RuntimeException("ArraySet.differenceWith is not yet implemented");
	// You do not need to implement this for assignment 2
	}

	/** Is current set a subset of another collecton
	 * @param other collection to be tested against
	 * @return true if current set is subset of argument collection
	 */
	public boolean subsetOf (Bag other){
		throw new RuntimeException("ArraySet.subsetOf is not yet implemented");
		
	// You do not need to implement this for assignment 2
	}

	// ArraySet utility methods, works exactly like the Vector class.

	/** Ensure elementData array has sufficient number of elements
	 * to add a new element
	 */ 
	private void ensureCapacity () {
	 	int newSize = size()+1;
		while(newSize>=elementData.length) {
			Object newArray[] = new Object[(2 * elementData.length)];
			for (int i=0; i<size(); i++) {
				newArray[i] = elementData[i];
			}
			elementData = newArray;
		}
	}

private class ArraySetEnumeration implements Enumeration{

	/** Fields of the Enumeration object. */
	// YOUR CODE HERE
	private int index = 0;
	private ArraySet data;
	
	public ArraySetEnumeration (ArraySet b) {
	// YOUR CODE HERE
		data = b;
	}

	/** Can the enumeration continue?
	 * @return true if enumeration has at least one more element */
	public boolean hasMoreElements () {
	// YOUR CODE HERE+
		return (index < data.size());
	}


	/** Get next element in enumeration
	 * @return value of next element in enumeration */
	public Object nextElement () {
	// YOUR CODE HERE
		return data.elementData[index++];
	}
}

}


