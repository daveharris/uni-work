import java.lang.RuntimeException;
import java.util.Enumeration;
import java.util.Comparator;
import java.io.*;
import jds.*;

public class ArrayPQueue implements FindMin, Collection {

	private int maxSize = 10;
	private int elementCount = 0;
	private Object[] elementData = new Object[10];
	private Comparator test;

	public ArrayPQueue() {
		test = new Comparator() {
			public int compare(Object a, Object b) {
				return ((Comparable)a).compareTo(b);
			}
		}
		;
	}

	public ArrayPQueue(Comparator test) {
		this.test = test;
	}

	/** Add an element to the priority Queue.
			Doubles the size of the array if necessary. */
	public void addElement(Object obj) {
		if (elementCount == maxSize) {
			int oldMaxSize = maxSize;
			maxSize = maxSize * 2;
			Object[] tmp = new Object[maxSize];
			for (int i = 0; i < oldMaxSize; i++) {
				tmp[i] = elementData[i];
			}
			elementData = tmp;
		}
		elementData[elementCount++] = obj;
	}

	/** Returns the highest priority item in the Priority Queue.
			Does not change the Queue	*/
	public Object getFirst() {
		if (elementCount == 0)
			throw new RuntimeException("Cannot getFirst of empty collection");
		Object topP = elementData[0];
		int topIndx = 0;
		for (int i = 0; i < elementCount; i++) {
			if (test.compare(elementData[i], topP) < 0) {
				topP = elementData[i];
				topIndx = i;
			}
		}
		return topP;
	}

	/** Remove the highest priority item from the Priority Queue. */
	public void removeFirst() {
		if (elementCount == 0)
			throw new RuntimeException("Cannot removeFirst of empty collection");
		// first find highest priority element
		Object topP = elementData[0];
		int topIndx = 0;
		for (int i = 0; i < elementCount; i++) {
			if (test.compare(elementData[i], topP) < 0) {
				topP = elementData[i];
				topIndx = i;
			}
		}
		elementCount--;
		elementData[topIndx] = elementData[elementCount];
	}

	public Enumeration elements() {
		return new ArrayQueueEnumeration(this);
	}

	public boolean isEmpty() {
		return elementCount == 0;
	}

	public int size() {
		return elementCount;
	}

	private class ArrayQueueEnumeration implements Enumeration {
		private Object[] data;
		private int count;
		private int maxCount;

		public ArrayQueueEnumeration(ArrayPQueue pqueue) {
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

	// Simple self test. Not guaranteed to find all bugs

	public static void main(String[] args) {
		ArrayPQueue pq = new ArrayPQueue();
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
