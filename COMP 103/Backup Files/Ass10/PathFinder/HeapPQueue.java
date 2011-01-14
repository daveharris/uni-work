import java.lang.RuntimeException;
import java.util.Enumeration;
import java.util.Comparator;
import java.io.*;
import jds.*;

public class HeapPQueue implements FindMin, Collection {
	private int maxSize = 10;
	private int elementCount = 0;
	private Object[] elementData = new Object[10];
	private Comparator test;

	/* Constructor that assumes that the items are Comparable */
	public HeapPQueue() {
		test = new Comparator() {
			public int compare(Object a, Object b) {
				return ((Comparable)a).compareTo(b);
			}
		}
  		;
	}

	/* Constructor that takes an explicit Comparator */
	public HeapPQueue(Comparator test) {
		this.test = test;
	}

	//===================================================================


	/** Add an element to the priority Queue.
			Doubles the size of the array if necessary.
			Ensures that the heap is still partially ordered and balanced */
	public void addElement(Object obj) {
		// first check we have enough room, if not double and copy
		if (elementCount >= maxSize)
			ensureCapacity();
		//Put the object in that array
		elementData[elementCount++] = obj;
		//Call onthe recursive method
		raise(elementCount - 1);
	}

	public void raise(int index) {
		if (index == 0)
			return;
		//Calculate the parents node index
		int parentIndex = (index - 1) / 2;
		if (test.compare(elementData[index], elementData[parentIndex]) < 0) {
			//Swap the object in the array
			swap(index, parentIndex);
			//recursive call
			raise(parentIndex);
		}
	}

	/** Returns the highest priority item in the Priority Queue.
			Does not change the Queue	*/
	public Object getFirst() {
		//Return the object with the top
		//priority, ie. at the start
		return elementData[0];
	}

	/** Remove the highest priority item from the Priority Queue.
			Ensures that the heap is still partially ordered and balanced */
	public void removeFirst() {
		//Set the first element in that array to that at the end and decrement count
		elementData[0] = elementData[--elementCount];
		//Set the last element to null
		elementData[elementCount] = null;
		//Recursive call
		lower(0);
	}

	public void lower(int index) {
		//calculate the childIndex
		int childIndex = 2 * index + 1;
		Object node = elementData[index];
		// if index does not point to leaf
		if (childIndex < elementCount) {
			//check both
			if (childIndex + 1 < elementCount && test.compare(elementData[childIndex], 
					elementData[childIndex + 1]) > 0)
			childIndex++;
			if (test.compare(elementData[index], elementData[childIndex]) > 0) {
				swap(index, childIndex);
				lower(childIndex);
			}
		}
	}

	public void ensureCapacity() {
		int oldMaxSize = maxSize;
		//Double the size of the array
		maxSize = maxSize * 2;
		Object[] temp = new Object[maxSize];
		//Copy all the items over into new array
		for (int i = 0; i < oldMaxSize; i++)
			temp[i] = elementData[i];
		//Call the new array the working one
		elementData = temp;
	}

	public void swap(int a, int b) {
		//Swaps the entries in the array
		Object temp = elementData[a];
		elementData[a] = elementData[b];
		elementData[b] = temp;
	}

	//===================================================================

	public Enumeration elements() {
		return new HeapPQueueEnumeration(this);
	}

	public boolean isEmpty() {
		return elementCount == 0;
	}

	public int size() {
		return elementCount;
	}

	private class HeapPQueueEnumeration implements Enumeration {
		private Object[] data;
		private int count;
		private int maxCount;

		public HeapPQueueEnumeration(HeapPQueue pqueue) {
			data = pqueue.elementData;
			count = 0;
			maxCount = pqueue.elementCount;
		}

		public Object nextElement() {
			return (data[count++]);
		}
		public boolean hasMoreElements() {
			return (count < maxCount);
		}
	}

	// A simple self test method.	Not guaranteed to catch all bugs!!!

	public static void main(String[] args) {
		HeapPQueue pq = new HeapPQueue();
		pq.addElement("A");
		pq.addElement("B");
		pq.addElement("C");
		pq.addElement("D");
		pq.addElement("E");
		pq.addElement("F");
		pq.addElement("G");
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println("- -");
		pq.addElement("G");
		pq.addElement("F");
		pq.addElement("C");
		pq.addElement("D");
		pq.addElement("A");
		pq.addElement("E");
		pq.addElement("B");
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
		System.out.println(pq.getFirst());
		pq.removeFirst();
	}
}
