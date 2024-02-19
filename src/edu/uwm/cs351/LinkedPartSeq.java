package edu.uwm.cs351;

import java.util.function.Consumer;

/**
 * A robot implemented with a linked list
 * using sequence methods.
 */
public class LinkedPartSeq implements Robot, Cloneable {
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	private boolean report(String error) {
		reporter.accept(error);
		System.out.println("DS: " + this);
		return false;
	}

	// TODO data structure: Node class and fields
	private Node head;
	private Node precursor;
	private Node tail;
	private int manyNodes;
	private String function;
	
	private boolean wellFormed() {
		// Check the invariant.
		// 1. The list has no cycles
		// 2. The precursor field is null or points to a node in the list.
		// 3. manyNodes accurately contains the number of nodes in the list (starting at head).
		// 4. No function or part is null
		// 5. The "tail" pointer is consistent.
		
		// The first check is given to you:
		// It uses Floyd's Tortoise & Hare algorithm
		
		if (head != null) {
			Node slow = head;
			Node fast = head.next;
			while (fast != null) {
				if (slow == fast) return report("Found cycle in list");
				slow = slow.next;
				fast = fast.next;
				if (fast != null) fast = fast.next;
			}
		}
		
		// TODO
	    // 2. The precursor field is null or points to a node in the list.
		if (precursor != null) {
			Node temp = head;
		    for (; temp != null; temp = temp.next) {
		        if (temp == precursor) {
		            break;
		        }
		    }
		    if (temp == null) {
		        return report("Precursor does not point to a node in the list");
		    }
		}

	    // 3. manyNodes accurately contains the number of nodes in the list (starting at head).
	    int count = 0;
	    for (Node current = head; current !=null; current = current.next) {
	    	count++;
	    }
	    if (count != manyNodes) {
	        return report("manyNodes does not accurately count the number of nodes in the list");
	    }
	    
	    // 4. No function or part is null
	    for (Node cur = head; cur != null; cur = cur.next) {
	        if (cur.function == null || cur.data == null) {
	            return report("Null function or part found");
	        }
	    }

	    // 5. The "tail" pointer is consistent.
	    if (tail != null) {
	        if (head == null) return report("Tail pointer is not consistent");
	        Node lastNode = head;
	        for (; lastNode.next != null; lastNode = lastNode.next) {
	            // Iterating through the linked list until the last node is reached
	        }
	        if (lastNode != tail) {
	            return report("Tail pointer is not consistent");
	        }
	    } else {
	        if (manyNodes > 0) return report("Tail pointer is not consistent");
	    }
	    
	    Node cur = precursor == null ? head : precursor.next;
	    if (cur != null && function != null && !function.equals(cur.function)) 
	    	return report ("current element function '" + cur.function + "' doesn't match '" + function +"'");
		
		// If no problems discovered, return true
		return true;
	}

	// This is only for testing the invariant.  Do not change!
	private LinkedPartSeq(boolean testInvariant) { }

	/**
	 * Create an empty robot..
	 */
	public LinkedPartSeq() {
		//TODO.  Make sure that you assert the invariant at the end only
		head = null;
		precursor = null;
		tail = null;
		manyNodes = 0;
		function = null;
		assert wellFormed(): "invariant broken by the constructor";
	}
	
	@Override // implementation
	public String toString() {
		// don't assert invariant, so we can use this for testing/debugging
		StringBuilder sb = new StringBuilder();
		boolean foundPre = precursor == null;
		boolean foundTail = tail == null && head == null;
		Node lag = null;
		Node fast = head;
		sb.append("[");
		for (Node p=head; p != null; p = p.next) {
			if (p == precursor) foundPre = true;
			if (fast != null) fast = fast.next;
			if (p != head) sb.append(", ");
			if (lag == precursor) {
				sb.append("*");
				foundPre = true;
			}
			sb.append(p.function);
			sb.append(":");
			sb.append(p.data);
			if (p == fast) {
				sb.append(" ???");
				break;
			}
			if (p == tail) {
				foundTail = true;
				sb.append("!");
			}
			lag = p;
			if (fast != null) fast = fast.next;
		}
		sb.append("]:" + manyNodes + (foundPre ? "" : "*?") + (foundTail ? "" : "!?"));
		return sb.toString();
	}
		
	/**
	 * Return the number of Parts in the robot.
	 * @return number of (non-null) Parts
	 */
	public int size() {
		assert wellFormed() : "invariant broken in size";
		return manyNodes;
	}

	
	/// Cursor methods
		
	// TODO Implement model field "cursor" (i.e., "getCursor()")
	private Node getCursor() {
		if (precursor == null) return head;
		else return precursor.next;
	}
	
	/**
	 * Move the cursor to the beginning, first Part in the robot,
	 * if any.
	 */
	public void start() {
		start(null); // let other method handle the invariant
	}
	
	// TODO: A private helper method.  Define once you find yourself
	// doing the same thing three times.
	
	/**
	 * Start running through all parts with the given function.
	 * @param function kind of parts to access, may be null (any part)
	 */
	public void start(String function) {
		// TODO: don't forget to assert the invariant twice: before and after
		assert wellFormed() : "invariant broken in start";
		this.function = function;
		Node lag = null;
	    precursor = lag;
		if (head != null && function != null) {
		    for (Node cur = head; cur != null; lag = cur, cur = cur.next) {
		    	if (this.function.equals(cur.function)) {
		    		break;
		    	}
		    }
		}
	    precursor = lag;
		assert wellFormed() : "invariant broken by start";
	}
	
	
	/**
	 * Return whether we have a current Part
	 * @return whether there is a current Part.
	 */
	public boolean isCurrent() {
		assert wellFormed() : "invariant broken in isCurrent";
		// TODO: one liner since we can assume the invariant
		return getCursor() != null;
	}
	
	/**
	 * Return the current Part.
	 * @exception IllegalStateException if there is no current Part
	 * @return the current Part, never null.
	 */
	public Part getCurrent() {
		// TODO: simple since we can assume the invariant
		assert wellFormed() : "invariant broken in getCurrent";
		if (!isCurrent()) throw new IllegalStateException("no current element");
		return getCursor().data;
		
	}
	
	/**
	 * Move on to the next Part (of the current function), if any. 
	 * If there are no more Parts, then afterwards, {@link #isCurrent()}
	 * will return false.
	 * @throws IllegalStateException if there is no current Part before this operation starts
	 */
	public void advance() {
		assert wellFormed() : "invariant broken in advance";
		// TODO: Don't forget to check the invariant before and after!
		// Check if there is a current part before advancing
	    if (!isCurrent()) {
	        throw new IllegalStateException("No current Part before advancing.");
	    }
	    precursor = getCursor();
	    if (this.function != null) {
	    	if (getCursor() != null && getCursor().function != this.function) {
		    	precursor = tail;
	    	}
	    }
		assert wellFormed() : "invariant broken by advance";
	}
	
	/**
	 * Remove the current Part, advancing the cursor to the next Part.
	 * @throws IllegalStateException if there is no current Part.
	 */
	public void removeCurrent() {
		// TODO: Don't forget to check the invariant before and after!
		// Hint: this is a constant-time method.  Rely on the invariant.
		assert wellFormed() : "invariant broken in removeCurrent";
	    // Check if there is a current part before removing
	    if (!isCurrent()) {
	        throw new IllegalStateException("No current Part to remove.");
	    }
	    
	    // If the current node is the head, move head to the next node
	    if (precursor == null) {
	        head = head.next;
	        Node lag = null;
	        if (head != null && function != null) {
	        	 for (Node cur = head; cur != null; lag = cur,cur = cur.next) {
	        		 if (this.function.equals(cur.function)) {
	 		    		break;
	 		    	}
	        	 }
	        }
	        precursor = lag;
	        if (manyNodes == 1) {
	        	tail = null;
	        }
	    } else {
	        precursor.next = precursor.next.next; 
	        Node cur = precursor == null ? head : precursor.next;
		    if (cur != null && function != null && !function.equals(cur.function)) {
		    	precursor = tail;
		    }
		    // Update the tail if the removed node was the last node
		    if (precursor != null && precursor.next == null) {
		        tail = precursor;
		    }
	    }
	    

	    manyNodes--; // Decrement the count of nodes
	    assert wellFormed() : "invariant broken by removeCurrent";
	}
	
	/**
	 * Add a part before the current element.  If there is no current element,
	 * then add at the beginning.  There must have been a function
	 * defined (See [@link #start(String)}).
	 * If successfully added, the newly added part will be current.
	 * @throws IllegalStateException if no function defined, or if the function was null
	 * @param p part to add at this spot, must not be null.
	 */
	public void addBefore(Part p) {
		assert wellFormed() : "invariant broken in addBefore";
		// TODO
		if (p == null) throw new NullPointerException("Part can't be null");
		if (function == null) throw new IllegalStateException("function is not defined or is null");
	    // Create a new node with the provided part
	    Node newNode = new Node(p, null);
	    newNode.function = this.function;
	    if (precursor == null) {
	        // If there's no precursor, insert newNode at the beginning
	        newNode.next = head;
	        head = newNode;
	        if (tail == null) {
	            tail = newNode;
	        }
	    } else {
	        // If there's a precursor, insert newNode before the current node
	    	if (getCursor() == null) {
	    		precursor= null;
	    		newNode.next = head;
	    		head = newNode;
	    	}
	    	else {
	    		newNode.next = precursor.next;
	    		precursor.next = newNode;
	    	}
	    }
	    
	    manyNodes++; // Increment the count of nodes
	    assert wellFormed() : "invariant broken by addBefore";
	}
	
	/**
	 * Add a part after the current element.  If there is no current element,
	 * then add at the end.  There must have been a function
	 * defined (See [@link #start(String)}).
	 * If successfully added, the newly added part will be current.
	 * @throws IllegalStateException if no function defined, or if the function was null
	 * @param p part to add at this spot, must not be null.
	 */
	public void addAfter(Part p) {
		// TODO: (remember the invariant!)
		assert wellFormed() : "invariant broken in addAfter";
		if (function == null) throw new IllegalStateException("function is not defined or is null");

	    // Create a new node with the provided part
	    Node newNode = new Node(p, null);
	    newNode.function = this.function;
	    if (manyNodes == 0) {
	        // If there's no precursor, insert newNode at the beginning
	        newNode.next = head;
	        head = newNode;
	    }
	    else if (getCursor() == null) {
    		newNode.next = null;
    		precursor.next = newNode;
	    }
	    else {
	        // If there's a precursor, insert newNode after the current node
    		newNode.next = getCursor().next;
    		getCursor().next = newNode;
    		precursor = getCursor();
	        // Update tail if necessary
	    }
        if (newNode.next == null) {
            tail = newNode;
        }
	    manyNodes++; // Increment the count of nodes
	    assert wellFormed() : "invariant broken by addAfter";
	}
	
	@Override // decorate
	public LinkedPartSeq clone() {
	    assert wellFormed() : "invariant broken in clone";
	    LinkedPartSeq result;
	    try {
	        result = (LinkedPartSeq) super.clone();
	    } catch (CloneNotSupportedException e) {
	        throw new AssertionError("forgot to implement cloneable?");
	    }

	    // Clone each node and its data
	    if (head != null) {
	        Node originalNode = head;
	        Node clonedNode = new Node(originalNode.data, null);
	        result.head = clonedNode;
	        result.head.function = originalNode.function;

	        while (originalNode.next != null) {
	            originalNode = originalNode.next;
	            clonedNode.next = new Node(originalNode.data, null);
	            clonedNode.next.function = originalNode.function;
	            clonedNode = clonedNode.next;
	        }

	        // Update tail pointer
	        result.tail = clonedNode;
	    }

	    // Clone precursor
	    if (precursor != null) {
	        Node originalNode = head;
	        Node clonedNode = result.head;
	        while (originalNode != null && originalNode != precursor) {
	            originalNode = originalNode.next;
	            clonedNode = clonedNode.next;
	        }
	        result.precursor = clonedNode;
	    } else {
	        result.precursor = null;
	    }

	    // Clone other fields
	    result.manyNodes = this.manyNodes;
	    result.function = this.function;
	    assert result.wellFormed() : "invariant broken in result of clone";
	    assert wellFormed() : "invariant broken by clone";
	    return result;
	}
	
	@Override // required
	public boolean addPart(String function, Part part) {
		assert wellFormed() : "invariant broken in addPart";
		// TODO: mainly do the work with public methods
		assert wellFormed() : "invariant broken by addPart";
		return true;
	}

	@Override // required
	public Part removePart(String function) {
		assert wellFormed() : "invariant broken in removePart";
		Part result = null;
		// TODO: mainly do the work with public methods
		assert wellFormed() : "invariant broken by removePart";
		return result;
	}


	@Override // required
	public Part getPart(String func, int index) {
		return null;
	}
	
	private static class Node{
		String function;
		Part data;
		Node next = null;
		
		public Node(Part p, Node n) {
			this.data = p;
			this.next = n;
		}
		
	}

	/**
	 * Class for internal testing.  Do not modify.
	 * Do not use in client/application code
	 */
	public static class Spy {
		/**
		 * A public version of the data structure's internal node class.
		 * This class is only used for testing.
		 */
		public static class Node extends LinkedPartSeq.Node {
			
			
			/**
			 * Create a node with null data and null next fields.
			 */
			public Node() {
				this(null, null, null);
			}
			/**
			 * Create a node with the given values
			 * @param p data for new node, may be null
			 * @param n next for new node, may be null
			 */
			public Node(String f, Part p, Node n) {
				super(null, null);
				this.function = f;
				this.data = p;
				this.next = n;
			}
		}
		
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}

		/**
		 * Create a node for testing.
		 * @param f function, may be null
		 * @param p Part, may be null
		 * @param n next node, may be null
		 * @return newly created test node
		 */
		public Node newNode(String f, Part p, Node n) {
			return new Node(f, p, n);
		}
		
		/**
		 * Change a node's next field
		 * @param n1 node to change, must not be null
		 * @param n2 node to point to, may be null
		 */
		public void setNext(Node n1, Node n2) {
			n1.next = n2;
		}
		
		/**
		 * Create an instance of the ADT with given data structure.
		 * This should only be used for testing.
		 * @param h head of linked list
		 * @param s size
		 * @param p precursor
		 * @param t tail of linked list
		 * @return instance of LinkedPartSeq with the given field values.
		 */
		public LinkedPartSeq create(Node h, int s, Node p, Node t) {
			LinkedPartSeq result = new LinkedPartSeq(false);
			result.head = h;
			result.manyNodes = s;
			result.precursor = p;
			result.tail = t;
			return result;
		}
		
		/**
		 * Return whether the wellFormed routine returns true for the argument
		 * @param s seq to check.
		 * @return
		 */
		public boolean wellFormed(LinkedPartSeq s) {
			return s.wellFormed();
		}

		
	}
}
