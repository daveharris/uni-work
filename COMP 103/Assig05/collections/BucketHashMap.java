import java.util.Enumeration;
import java.util.NoSuchElementException;
import jds.Map;
import jds.Set;
import java.util.Enumeration;

/**
 * BucketHashMap - a Map collection that does work;
 * @author Peter Andreae
 * @version August 2003
 * @see jds.Collection
 *
 * The implementation uses an array to store the items.
 * Requires that the keys are strings
 * null keys are not allowed, and cause a "NoSuchElementException"
 * It has a fixed maximum size and doesn't allow collisions 
 *	(or rather, ignores collisions and writes on top of previous items)
 * The enumerator doesn't work
 */

public class BucketHashMap implements Map {

	// data areas
	private Map [ ] buckets;

	public BucketHashMap(int n){
		buckets = new Map[n];
		for (int i=0; i<n; i++){
			buckets[i] = new SortedArrayMap();
		}
	}

	// the Collection interface
	/** Determines whether the collection is empty
	 * @return true if the collection is empty */
	public boolean isEmpty () {
		for (int i=0; i<1000; i++){
			if (!(buckets[i].isEmpty()))
	return false;
		}
		return true;
	}

	/** Determines number of elements in collection
	 * @return number of elements in collection as integer */
	public int size () {
		int count=0;
		for (int i=0; i<buckets.length; i++){
			count += buckets[i].size();
		}
		return count;
	}

	/** Yields enumerator for collection
	 * @return an Enumeration that will yield all the keys of the collection*/
	public Enumeration elements () {
		return new BucketHashMapEnumeration(this);
	}

	// the Map Interface

	/** Set the value associated with the key to be item
	 * @param	the element */
	public void set (Object key, Object item) {
		if (key==null)
			throw new NoSuchElementException();
		buckets[hash(key)].set(key, item);
	}


	/** Determine if the set contains a key
	 * @param	the key to look for
	 * @return	boolean */
	public boolean containsKey (Object key){
		if (key == null)
			return false;
		return buckets[hash(key)].containsKey(key);
	}


	/** Returns the element in the set matching a given item.
	 * @param	the item to look for
	 * @return	an item or null
	 * Throws	NoSuchElementException if no such item.
	 */
	public Object get (Object key) {
		if (key==null)
			throw new NoSuchElementException();
		return buckets[hash(key)].get(key);
	}

	/** Remove an element matching a given item
	 * Makes no change to the set if the item is not present.
	 * @param	the item to look for
	 * Throws	NoSuchElementException if no such item.
	 */
	public void removeKey (Object key){
		if (key == null)
			throw new NoSuchElementException();
		buckets[hash(key)].removeKey(key);
	}

	// ArrayMap utility methods

	/** Find the index of the pair containing key.
			Assumes key is not null */
	private int hash(Object key){
		return Math.abs(key.hashCode()) % buckets.length;
	}


	private class BucketHashMapEnumeration implements Enumeration {

		private Map[] buckets;
		private int index = 0;	// where we are up to.
		private Enumeration currentEnumeration;	 // Enumeration of the current bucket.

		private BucketHashMapEnumeration (BucketHashMap m) {
			buckets = m.buckets;
			index = 0;
			currentEnumeration=buckets[0].elements();
		}

		/** Can the enumeration continue?
		 * @return true if enumeration has at least one more element
		 */
		public boolean hasMoreElements () {
			if (currentEnumeration.hasMoreElements())
				return true;
			while (++index < buckets.length) {
				currentEnumeration = buckets[index].elements();
				if (currentEnumeration.hasMoreElements())
					return true;
			}
			return false;
		}

		/** get next element in enumeration
		 * @return value of next element in enumeration
		 */
		public Object nextElement () {
			if (hasMoreElements())
				return currentEnumeration.nextElement();
			else throw new NoSuchElementException();
		}
	}


}


