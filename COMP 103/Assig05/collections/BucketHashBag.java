
import java.util.Enumeration;
import jds.Bag;
import jds.collection.SortedVector;
import java.util.Comparator;

/**
 * BucketHashBag - collection based on a vector of buckets;
 */

public class BucketHashBag implements Bag {
	private Bag [ ] buckets;

	// constructors
	/** initialize a newly created Hash table */
	public BucketHashBag (int n) { 
		buckets = new Bag[n];
		for (int i = 0; i < n; i++) buckets[i] = new ArrayBag();
	}

	/** initialize a newly created hash table */
	public BucketHashBag (int n, Comparator test) { 
		buckets = new Bag[n];
		for (int i = 0; i < n; i++) buckets[i] = new SortedVector(test);
	}

	// the Collection interface
	/** Determines whether the collection is empty */
	public boolean isEmpty () {
		return size() == 0;
	}

	/** Determines number of elements in collection */
	public int size () {
		int count = 0;
		for (int i = 0; i < buckets.length; i++)
			count += buckets[i].size();
		return count;
	}

	/** Yields enumerator for collection */
	public Enumeration elements () {
		return new HashBagEnumerator();
	}

	// the Bag interface
	/** add a new value to the collection */
	public void addElement (Object val) {
		bucket(val).addElement(val);
	}

	/** see if collection contains value */
	public boolean containsElement (Object val){
		return bucket(val).containsElement(val);
	}

	/** find element that will test equal to value */
	public Object findElement (Object val){
		return bucket(val).findElement(val);
	}

	/** remove a new value from the collection */
	public void removeElement (Object val){
		bucket(val).removeElement(val);
	}

	private Bag bucket (Object val) {
		return buckets[Math.abs(val.hashCode()) % buckets.length];
	}

	private class HashBagEnumerator implements Enumeration {
		Enumeration currentEnumeration;
		int index;

		public HashBagEnumerator () {
			index = 0;
			currentEnumeration = buckets[0].elements();
		}

		public boolean hasMoreElements() {
			if (currentEnumeration.hasMoreElements()) return true;
			while (++index < buckets.length) {
	currentEnumeration = buckets[index].elements();
	if (currentEnumeration.hasMoreElements())
		return true;
			}
			return false;
		}

		public Object nextElement()
			{ return currentEnumeration.nextElement(); }
	}
}

