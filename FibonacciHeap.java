import java.util.ArrayList;
import java.util.List;

/**
 * /**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over non-negative integers.
 * 
 * @author eyal_
 *
 */
public class FibonacciHeap {

	/**
	 * root
	 */
	private HeapNode min;
	/**
	 * number of elements
	 */
	private int count;
	/**
	 * when we decrease key, we cut the subtree from it's parent and mark the parent
	 */
	private int totalNumOfMark;
	/**
	 * when we link between trees on consolidate
	 */
	private static int totalNumOfLink;
	/**
	 * happen when we decrease key
	 */
	private static int totalNumOfCut;

	/**
	 * Constructor
	 * O(1)
	 */
	public FibonacciHeap() {
		min = null;
		count = 0;
		totalNumOfMark = 0;
		totalNumOfLink = 0;
		totalNumOfCut = 0;
	}

	
	/**
	 * public boolean empty()
	 * precondition: none
	 * 
	 * @return <code>true</code> if and only if the heap is empty else <code>false</code>
	 * 
	 *  O(1)
	 */
	public boolean empty() {
		return min == null;
	}

	/**
	 * public HeapNode insert(int key)
	 *
	 * Creates a node (of type HeapNode) which contains the given key, and inserts
	 * it into the heap.
	 * <p>
	 * @param key 	key for the new node
	 * @return 	
	 * 		node that contain the given key O(1)
	 *  	amortize = actuaclCost +increasePotential = O(1) + 1 = O(1)
	 **/
	public HeapNode insert(int key) {
		HeapNode node = new HeapNode(key);
		min = mergeLists(min, node);
		count++;
		return node;
	}

	/**
	 * public void deleteMin()
	 *
	 * Delete the node containing the minimum key.
	 * 
	 * 
	 * O(log n)
	 */
	public void deleteMin() {
		HeapNode minElement = min;
		if (minElement != null) {
			if (minElement == minElement.next) {
				// this is the only element in the list of roots
				min = null;
			} else {
				min.previous.next = min.next;
				min.next.previous = min.previous;
				min = min.next;
			}
			// clear the parent fields to all min element's children
			if (minElement.child != null) {
				HeapNode curr = minElement.child;
				do {
					curr.parent = null;
					// walk to the next node, then stop if this is the node we started
					curr = curr.next;
				} while (curr != minElement.child);
			}
			// merge child's list to the root list
			min = mergeLists(min, minElement.child);
			if (min == null)
				return;

			consolidate();
		}
		this.count--;
	}

	/**
	 * unioning steps:
	 * 
	 * O(log (n+1))
	 */
	private void consolidate() {
		List<HeapNode> treeTable = new ArrayList<HeapNode>();
		List<HeapNode> toVisit = getTreeTable();
		
		for (HeapNode curr : toVisit) {
			/* Keep merging until a match arises. */
			while (true) {
				/*
				 * Ensure that the list is long enough to hold an element of this degree.
				 */
				while (curr.degree >= treeTable.size())
					treeTable.add(null);

				/*
				 * If nothing's here, we're can record that this tree has this size and are done
				 * processing.
				 */
				if (treeTable.get(curr.degree) == null) {
					treeTable.set(curr.degree, curr);
					break;
				}

				/* Otherwise, merge with what's there. */
				HeapNode other = treeTable.get(curr.degree);
				treeTable.set(curr.degree, null);

				/*
				 * Determine which of the two trees has the smaller root
				 */
				HeapNode min = (other.key < curr.key) ? other : curr;
				HeapNode max = (other.key < curr.key) ? curr : other;

				/*
				 * Break max out of the root list, then merge it into min's child list.
				 */
				max.next.previous = max.previous;
				max.previous.next = max.next;

				/* Make it a singleton so that we can merge it. */
				max.next = max.previous = max;
				min.child = mergeLists(min.child, max);

				max.parent = min;
				max.mark = false;
				++min.degree;

				/* Continue merging this tree. */
				curr = min;
				totalNumOfLink++;
			} // while end

			if (curr.key <= min.key)
				min = curr;
		}
	}

	/**
	 * public HeapNode findMin()
	 *
	 * Return the node of the heap whose key is minimal.
	 * 
	 * @return the minimum key node
	 *  O(1)
	 */
	public HeapNode findMin() {
		return min;
	}

	/**
	 * public void meld (FibonacciHeap heap2)
	 *
	 * Meld the heap with heap2 only touch root list
	 *
	 * @param heap2
	 * O(1)
	 *  amortaizeCost = O(1)+0 = O(1)
	 */
	public void meld(FibonacciHeap heap2) {
		this.min = mergeLists(heap2.min, this.min);
		if ((this.min != null || heap2.min != null) && heap2.min.key < this.min.key)
			this.min = heap2.min;
		this.count += heap2.count;
		heap2 = null;
	}

	/**
	 * merge two different lists
	 * 
	 * @param min1
	 * @param min2
	 * @return the min Node of the merged Lists
	 * O(1)
	 */
	private static HeapNode mergeLists(HeapNode min1, HeapNode min2) {
		if (min1 == null && min2 == null)
			return null;
		if (min1 != null && min2 == null)
			return min1;
		if (min1 == null && min2 != null)
			return min2;

		// both not null
		HeapNode oneNext = min1.next;
		min1.next = min2.next;
		min1.next.previous = min1;
		min2.next = oneNext;
		min2.next.previous = min2;

		return min1.key < min2.key ? min1 : min2;
	}

	/**
	 * public int size()
	 *
	 * @return the number of elements in the heap
	 *  o(1)
	 */
	public int size() {
		return count; 
	}

	/**
	 * public int[] countersRep()
	 *
	 * @return a counters array, where the value of the i-th entry is the number of
	 *         trees of order i in the heap.
	 * O(n)
	 */
	public int[] countersRep() {
		List<HeapNode> toVisit = getTreeTable();
		List<Integer> treeTable = new ArrayList<Integer>();

		for (HeapNode curr : toVisit) {
			while (curr.degree >= treeTable.size())
				treeTable.add(0);

			treeTable.set(curr.degree, treeTable.get(curr.degree) + 1);
		}
		return treeTable.stream().mapToInt(i -> i).toArray();
	}

	/**
	 * public void delete(HeapNode x)
	 *
	 * Deletes the node x from the heap.
	 * 
	 * @param x
	 * O(log n)
	 */
	public void delete(HeapNode x) {
		/* make x the minimum node */
		decreaseKeyUnchecked(x, Integer.MIN_VALUE);
		deleteMin();
	}

	/**
	 * public void decreaseKey(HeapNode x, int delta)
	 *
	 * The function decreases the key of the node x by delta. The structure of the
	 * heap should be updated to reflect this chage (for example, the cascading cuts
	 * procedure should be applied if needed).
	 * 
	 * @param x 	node
	 * @param 	delta for decrease from the key of x
	 * <p>
	 * O (marked) = O(n)
	 * amortaize = O(1)
	 */
	public void decreaseKey(HeapNode x, int delta) {
		if (delta > x.key)
			throw new IllegalArgumentException("delta exceeds key.");
		/* Forward this to a helper function. */
		decreaseKeyUnchecked(x, delta);
	}

	/**
	 * Decreases the key of a node in the tree without doing any checking to ensure
	 * that the new priority is valid.
	 *
	 * @param entry - The node whose key should be decreased.
	 * @param delta
	 * O(1)
	 */
	private void decreaseKeyUnchecked(HeapNode entry, int delta) {
		/* First, change the node's priority. */
		entry.key -= delta;

		/*
		 * If the node no longer has a higher key than its parent, cut it. Note
		 * that this also means that if we try to run a delete operation that decreases
		 * the key to -infinity, it's guaranteed to cut the node from its parent.
		 */
		if (entry.parent != null && entry.key <= entry.parent.key)
			cutNode(entry);

		/*
		 * If our new value is the new min, mark it as such. Note that if we ended up
		 * decreasing the key in a way that ties the current minimum priority, this will
		 * change the min accordingly.
		 */
		if (entry.key <= min.key)
			min = entry;
	}

	/**
	 * Cuts a node from its parent. If the parent was already marked, recursively
	 * cuts that node from its parent as well.
	 *
	 * @param entry
	 *            The node to cut from its parent.
	 * O (log n)
	 */
	private void cutNode(HeapNode entry) {
		/* clearing the node's mark, since we just cut it. */
		entry.mark = false;
		totalNumOfMark--;
		totalNumOfCut++;

		/* If the node has no parent, we're done. */
		if (entry.parent == null)
			return;

		/* Rewire the node's siblings around it */
		if (entry.next != entry) {
			entry.next.previous = entry.previous;
			entry.previous.next = entry.next;
		}

		/* pick other child to parent */
		if (entry.parent.child == entry) {
			/* If there are any other children, pick one of them arbitrarily. */
			if (entry.next != entry) {
				entry.parent.child = entry.next;
			}
			/*
			 * Otherwise, there aren't any children left and we should clear the pointer and
			 * drop the node's degree.
			 */
			else {
				entry.parent.child = null;
			}
		}

		/* Decrease the degree of the parent, since it just lost a child. */
		--entry.parent.degree;

		/* make the node singleton and invoking the merge subroutine to root. */
		entry.previous = entry.next = entry;
		min = mergeLists(min, entry);

		/*
		 * Mark the parent and recursively cut it if it's already been marked.
		 */
		if (entry.parent.mark)
			cutNode(entry.parent);
		else {
			entry.parent.mark = true;
			totalNumOfMark++;
		}

		/* Clear the relocated node's parent; it's now a root. */
		entry.parent = null;
	}
	
	/**
	 *  public int potential()
	 *  
	 *  <p>
	 *  potential = #trees + 2*#marked
	 *  The potential equals to the number of trees in the heap
	 * 	plus twice the number of marked nodes in the heap.
	 *  
	 * O(n)
	 * 
	 * @return	the current potential of the heap
	 */
	public int potential() {
		List<HeapNode> trees = getTreeTable();
		return trees.size() + 2 * totalNumOfMark;
	}

	/*
	 * To add everything, we'll iterate across the elements until we find the first
	 * element twice. We check this by looping while the list is empty or while the
	 * current element isn't the first element of that list.
	 *  WC O(n)
	 */
	private List<HeapNode> getTreeTable() {
		List<HeapNode> toVisit = new ArrayList<HeapNode>();
		for (HeapNode curr = min; toVisit.isEmpty() || toVisit.get(0) != curr; curr = curr.next)
			toVisit.add(curr);

		return toVisit;
	}

	/**

	 */
	
	/**
	 * 	public static int totalLinks()
	 *
	 * This static function @returns the total number of link operations made during
	 * the run-time of the program. A link operation is the operation which gets as
	 * input two trees of the same rank, and generates a tree of rank bigger by one,
	 * by hanging the tree which has larger value in its root on the tree which has
	 * smaller value in its root.
	 * O(1)
	 * 
	 * @return 	total number of links
	 */
	public static int totalLinks() {
		return totalNumOfLink;
	}

	/**
	 * public static int totalCuts()
	 *
	 * This static function @returns the total number of cut operations made during
	 * the run-time of the program. A cut operation is the operation which
	 * diconnects a subtree from its parent (during decreaseKey/delete methods).
	 * O(1)
	 * 
	 *  @return 	total number of cuts
	 */
	public static int totalCuts() {
		return totalNumOfCut;
	}

	
	/** function to display - debug mode **/
	public void print() {
		System.out.println("Fibonacci heap:");
		if (min != null) {
			min.print(0);
		}
	}

	/**
	 * public class HeapNode
	 * 
	 * If you wish to implement classes other than FibonacciHeap (for example
	 * HeapNode), do it in this file, not in another file
	 * 
	 */
	public class HeapNode {

		public HeapNode child, previous, next, parent;
		public int key;
		public boolean mark;
		public int degree;

		public HeapNode(int key) {
			this.key = key;
			this.next = this;
			this.previous = this;
			this.degree = 0;
			this.mark = false;
			this.child = null;
			this.parent = null;
		}

		public int getKey() {
			return this.key;
		}

		@Override
		public String toString() {
			return "HeapNode [child=" + child + ", left=" + previous + ", right=" + next + ", parent=" + parent
					+ ", key=" + key + ", mark=" + mark + ", degree=" + degree + "]";
		}

		private void print(int level) {
			HeapNode curr = this;
			do {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < level; i++) {
					sb.append(" ");
				}
				sb.append(curr.key);
				System.out.println(sb.toString());
				if (curr.child != null) {
					curr.child.print(level + 1);
				}
				curr = curr.next;
			} while (curr != this);
		}

	}
}
