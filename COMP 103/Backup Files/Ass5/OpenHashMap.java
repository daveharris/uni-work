import java.util.NoSuchElementException;
import java.util.Enumeration;
import jds.Map;

/* Code for Assignment 5, Aug 2003
 * Name: David Harris
 * Usercode: harrisdavi3
 * ID: 300069566
 */

public class OpenHashMap implements Map {
	private Association[] elementData;
	private int elementCount = 0;

	// constructors either on arguments, or can give it the size
	public OpenHashMap() {
		elementData = new Association[11];
	}

	public OpenHashMap(int size) {
		elementData = new Association[size];
	}


	public boolean isEmpty() {
	//Determine whether the arrayp is empty or not
		return elementCount == 0;
	}

	public int size() {
	//returns how many Association objects in the array
		return elementCount;
	}

	public Enumeration elements() {
	//Returns all the elements
		return new OpenHashMapEnumerator(this);
	}


	public void set(Object key, Object val) {
		if (key == null)
			throw new NoSuchElementException();
		//If the map is more than 80% full, then double it
		if (elementCount + 1 >= .8 * elementData.length)
			reAdjustTable();
		int index = hash(key);

		while (elementData[index] != null) {
		//Go through the array until you reasch the end
			if (key.equals(elementData[index].key))
				throw new RuntimeException("key is already in map");
			//Add at the start if the end is reached
			if (++index >= elementData.length)
				index = 0;
		}
		//Add the value to the array, and increment the count
		elementData[index] = new Association(key, val);
		elementCount++;
	}


	public boolean containsKey(Object key) {
	//Needed because you are not allowed duplicates
	//Same as set, except that you just return true or false
		if (key == null)
			throw new NullPointerException("key cannot be null");
		int index = hash(key);
		while (elementData[index] != null) {
			if (key.equals(elementData[index].key))
				return true;
			if (++index >= elementData.length)
				index = 0;
		}
		return false;
	}

	public Object get(Object key) {
	//Same again expcept that you return the object at the point rather
	//than true or false
		if (key == null)
			throw new NoSuchElementException();
		int index = hash(key);
		while (elementData[index] != null) {
			if (key.equals(elementData[index].key))
				return elementData[index].value;
			if (++index >= elementData.length)
				index = 0;
		}
		throw new NoSuchElementException();
	}

	public void removeKey(Object key) {
	}

	private int hash(Object key) {
	//Returns the hash of the given key, using the java standard hashCode
		if (key == null)
			throw new NoSuchElementException();
		return Math.abs(key.hashCode()) % elementData.length;
	}


	private void reAdjustTable() {
		/*Called upon when the array is more than 80% full
		so you don't have to search down long 'runs'
		Makes a new array and copies all the elements into
		a new one of double the size */
		Association[] oldData = elementData;
		elementData = new Association[oldData.length * 2 - 1];
		elementCount = 0;
		for (int i = 0; i < oldData.length; i++) {
			if (oldData[i] != null)
				set(oldData[i].key, oldData[i].value);
			}
	}


	private class Association {
		public Object key;
		public Object value;
		//The constructor, takes two objects as arguments
		public Association(Object k, Object v) {
			key = k;
			value = v;
		}

		public boolean equals(Object other) {
		//Needed so we can compare the given key with the key
		//of an Association object
			Association pair = (Association)other;
			return key.equals(pair.key);
		}
	}

	/* Enumerator class */
	private class OpenHashMapEnumerator implements Enumeration {
		private Association[] data;
		private int index = 0;
		//The constructor, takes a map as argument
		public OpenHashMapEnumerator(OpenHashMap map) {
			data = map.elementData;
			while (index < data.length && data[index] == null)
				index++;
		}

		public boolean hasMoreElements() {
			//Returns true if you are not at the end, or
			//false if there are no more elements
			while (index < data.length && data[index] == null)
				index++;
			return (index < data.length);
		}

		public Object nextElement() {
		//Returns the next element in the array
			if (index >= data.length)
				throw new NoSuchElementException();
			return data[index++].value;
		}
	}

}

