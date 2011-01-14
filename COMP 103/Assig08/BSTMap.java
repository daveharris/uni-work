import jds.Map;
import jds.Stack;
import jds.collection.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;					// *** Added
import java.util.Comparator;

/* BST implementation.
 * This implementation just uses one class, and keeps "dummy" or "sentinel"
 * nodes at the bottom in place of empty subtrees.
 * Dummy nodes are distinguished by having a null value field and null for
 * both left and right subtrees.
 *
 * This implementation uses ((Comparable)value).compareTo(this.value) to
 * compare a new value (value) and an old old (this.value).
 * An alternative is to pass a comparator, say test, to the constructor and
 * use test.compare(value, this.value) to compare value and this,value, as
 * Budd does in his SortedVector and AVLTree classes.
 */

public class BSTMap implements Map {

	/** the node value */
	private Association pair;

	/** the subtrees */
	private BSTMap left;							// left subtree.
	private BSTMap right;						 // right subtree.

	/** Make an empty BST. */
	public BSTMap() {
		pair = null;
		left = null;
		right = null;
	}

	// The collection interface.

	/** Determine whether the collection is empty
		* return true if the collection is empty
		*/
	public boolean isEmpty() {
		return (pair == null);
	}

	/** Determine number of elements in collection
		* @return number of elements in collection as integer
		*/
	public int size() {
		if (isEmpty())
			return 0;
		//Recursivley call the size method in the right and left side
		//Need to add one for the root node
		else
			return (1 + left.size() + right.size());
	}

	/** Return enumerator for collection */
	public Enumeration elements() {
		return new BSTMapEnumeration(this);
	}


	// The Map interface.

	/** Return true if and only if collection contains this key */
	public boolean containsKey(Object key) {
		if (key == null)
			throw new NoSuchElementException();

		// If this is an empty tree, the answer is "no".
		if (isEmpty())
			return false;

		// Otherwise, compare key to the key of the pair at the root and decide
		// whether this is the one we're seeking, and if not where to look
		// next.

		int cmp = ((Comparable)key).compareTo(pair.key);

		if (cmp < 0)
			return left.containsKey(key);
		else if (cmp > 0)
			return right.containsKey(key);
		else
			return true;
	}


	/** Set the value associated with the key to be item */

	public void set(Object key, Object item) {
		if (key == null)
			throw new NoSuchElementException();
		// If this is an empty tree, store the key - item pair here and insert
		// empty subtrees under it.
		if(pair == null) {
			pair = new Association(key, item);
			//make new trees so thay can be added to later
			this.left = new BSTMap();
			this.right = new BSTMap();
			return;
		}
		// Otherwise, compare key to the key of the pair at this root and
		// decide where the new key and item should go.
		int value = ((Comparable)key).compareTo(pair.key);
		//key is to the left
		if (value < 0)
			left.set(key, item);
		//key is to the right
		else if (value > 0)
			right.set(key, item);
		else
			return;
	}


	/** Find and return the item associated with this key
		* Assumes the key is present and throws a NoSuchElementException
		* if the key is not found	 */

	public Object get(Object key) {
		// if this is an empty tree, then the key is not present.
		if (key == null || pair == null)
			throw new NoSuchElementException();

		// Otherwise, compare key to the key of the pair at the root and decide
		// whether this is the one we're seeking, and if not where to look next.
		int value = ((Comparable)key).compareTo(pair.key);
		//key is to the left
		if (value < 0)
			return left.get(key);
		//key is tothe right
		else if (value > 0)
			return right.get(key);
		//else we must be on it, so return the value
		else
			return pair.value;
	}


	/** Remove a key and any associated item from the collection
		* Assumes the key is present and throws a NoSuchElementException
		* if the key is not found */

	public void removeKey(Object key) {
		// if this is an empty tree, then the key is not present.
		if (key == null || pair == null)
			throw new NoSuchElementException();

		// Otherwise, compare key to the key of the pair at the root to decide
		// whether this is the one we're seeking.
		int value = ((Comparable)key).compareTo(pair.key);

		// If key is in the left subtree, remove it from there
		//Recursivley call the removekey() method, on the left tree
		if (value < 0)
			left.removeKey(key);

		// If key is in the right subtree, remove it from there
		//Recursivley call the removekey() method, on the right tree
		else if (value > 0)
			right.removeKey(key);

		// If key is here, then it depends what kind of node this is.
		// If this node is a leaf (its children are both empty)
		//	turn it into an empty tree.
		else {
			//There is an empty tree below (ie. the last item)
			//Therefore we can just remove it and don't worry any children
			if(right.isEmpty() && left.isEmpty()) {
				pair = null;
				right = null;
				left = null;
			}

			// If this node has an empty tree on its left
			// replace it by its right child.
			else if(left.isEmpty() && !right.isEmpty()) {
				//Move the item up, and move it's chidren with it
				pair = right.pair;
				left = right.left;
				right = right.right;
			}

			// If this node has an empty tree on its right
			// replace it by its left child.
			else if(right.isEmpty() && !left.isEmpty()) {
				//Move the item up, and move it's chidren with it
				pair = left.pair;
				right = left.right;
				left = left.left;
			}

			// If both subtrees are non-empty; replace it by the smallest element in
			// right subtree, and remove that node.
			else if(!right.isEmpty() && !left.isEmpty()) {
				BSTMap rest = right;
					//get the smallest item in the right treeby going left
				 for(; !rest.left.isEmpty(); rest = rest.left) {}
				//Move the item up to the top
				pair = rest.pair;

				if(rest.right.isEmpty()) {
					//If it is the last item, we can delete it
					//without worrying about any children
					rest.right = null;
					rest.pair = null;
					rest.left = null;
				}
				else{
					//Move the children up with the parent node
					rest.pair = rest.right.pair;
					rest.left = rest.right.left;
					rest.right = rest.right.right;
				}
			}
		}
	}



	/*	private class Association	{ */
	private class Association {
		public Object key;
		public Object value;

		Association(Object k, Object v) {
			key = k;
			value = v;
		}

		public boolean equals(Object other) {
			if (other instanceof Association) {
				Association pair = (Association)other;
				return key.equals(pair.key);
			}
			return key.equals(other);
		}

		public int hashCode() {
			return key.hashCode();
		}

	}


	private class BSTMapEnumeration implements Enumeration {

		private Stack stack = new Vector();

		private BSTMapEnumeration(BSTMap root) {
			slideLeft(root);
		}

		/** Can the enumeration continue?
			* @return true if enumeration has at least one more element
			*/
		public boolean hasMoreElements() {
			return (!stack.isEmpty());
		}

		/** get next element in enumeration
			* @return value of next element in enumeration
			*/
		public Object nextElement() {
			BSTMap node = (BSTMap)stack.getLast();
			stack.removeLast();
			slideLeft(node.right);
			return node.pair.key;
		}

		private void slideLeft(BSTMap node) {
			while ((node != null) && !node.isEmpty()) {
				stack.addLast(node);
				node = node.left;
			}
		}
	}

	// Print a simple representation of a tree, for debugging.
	private void printTree() {
		System.out.println("Tree:");
		printTree(0);
		System.out.println("-----");
	}

	private void printTree(int depth) {
		if (!isEmpty()) {
			left.printTree(depth + 1);
			for (int i = 0; i < depth; i++)
				System.out.print("	");
			System.out.print("[" + pair.key + "->" + pair.value + "]\n");
			right.printTree(depth + 1);
		}
	}

	public static void main(String[] args) {
		BSTMap map = new BSTMap();

		if (map.isEmpty())
			System.out.println("Map is initially empty");
		else
			System.out.println("ERROR: Map is not initially empty");

		System.out.println("Adding 5, 3, and 8:");
		map.set("5", "val5");
		map.set("3", "val3");
		map.set("8", "val8");
		map.printTree();

		System.out.println("Adding 1, 2, 4, 6, 7, 9:");
		map.set("1", "val1");
		map.set("2", "val2");
		map.set("4", "val4");
		map.set("6", "val6");
		map.set("7", "val7");
		map.set("9", "val9");
		map.printTree();


		for (int i = 1; i < 10; i++) {
			if (map.containsKey("" + i))
				System.out.println("Found " + i);
			else
				System.out.println("ERROR: did not find key " + i);
		}

		for (int i = 1; i < 10; i++) {
			if (map.containsKey("" + i))
				if (map.get("" + i).equals("val" + i))
					System.out.println("get " + i + " -> val" + i);
				else
					System.out.println("ERROR: key " + i + " does not get val" + i);
		}

		System.out.println("\nRemoving 2");
		map.removeKey("2");
		map.printTree();

		System.out.println("Removing 8");
		map.removeKey("8");
		map.printTree();

		System.out.println("Removing 5");
		map.removeKey("5");
		map.printTree();


		System.exit(0);

	}

	private void debug(String msg) {
		System.out.println(msg);
	}

}
