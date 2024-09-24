// This is an assignment for students to complete after reading Chapter 4 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

import java.util.function.Consumer;


/******************************************************************************
 * This class is a homework assignment;
 * A BallSeq is a collection of Ball objects.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods
 * (start, getCurrent, advance and isCurrent).
 *
 ******************************************************************************/
public class BallSeq implements Cloneable
{
	// TODO: Declare the private static Node class.
	// It should have a constructor but no methods.
	// The fields of Node should have "default" access (neither public, nor private)
	// and should not start with underscores.

	
	// TODO: Declare the private fields of BallSeq needed for sequences
	// (in the textbook, page 233 (3rd ed. 226), five are recommended, 
	//  you declare all five.)
	
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	/**
	 * Check the invariant.  For information on what a class invariant is,
	 * please read page 123 in the textbook.
	 * Return false if any problem is found.  Returning the result
	 * of {@link #report(String)} will make it easier to debug invariant problems.
	 * @return whether invariant is currently true
	 */
	private boolean wellFormed() {
		// Invariant:
		// 1. The list must not include a cycle.
		// 2. "tail" must point to the last node in the list starting at "head".
		//    In particular, if the list is empty, "tail" must be null.
		// 3. "manyNodes" is number of nodes in list
		// 4. "precursor" is either null or points to a node in the list, other than the tail.
		// 5. "cursor" is the node after "precursor" (if "precursor" is not null),
		//    or is either null or the head node (otherwise).
		// This is the invariant of the data structure according to the
		// design in the textbook on pages 233-4 (3rd ed. 226-7)
		
		// Implementation:
		// Do multiple checks: each time returning false if a problem is found.
		// (Use "report" to give a descriptive report while returning false.)
		
		// We do the first one for you:
		// check that list is not cyclic
		if (head != null) {
			// This check uses the "tortoise and hare" algorithm attributed to Floyd.
			Node fast = head.next;
			for (Node p = head; fast != null && fast.next != null; p = p.next) {
				if (p == fast) return report("list is cyclic!");
				fast = fast.next.next;
			}
		}
		
		// Implement remaining conditions.
		
		// If no problems found, then return true:
		return true;
	}

	private BallSeq(boolean doNotUse) {} // only for purposes of testing, do not change
	
	/**
	 * Create an empty sequence
	 * @param - none
	 * @postcondition
	 *   This sequence is empty 
	 **/   
	public BallSeq( )
	{
		// TODO: initialize fields (if necessary)
		assert wellFormed() : "invariant failed in constructor";
	}

	/// Simple sequence methods

	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		assert wellFormed() : "invariant wrong at start of size()";
		// TODO: Implemented by student.
		// This method shouldn't modify any fields, hence no assertion at end
	}
	
	/**
	 * Set the current element at the front of this sequence.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert wellFormed() : "invariant wrong at start of start()";
		// TODO: Implemented by student.
		assert wellFormed() : "invariant wrong at end of start()";
	}

	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element that can be retrieved with the 
	 * getCurrent method. 
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean isCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		// TODO: Implemented by student.
		// This method shouldn't modify any fields, hence no assertion at end
	}

	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @return
	 *   the current element of this sequence
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Ball getCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		// TODO: Implemented by student.
		// This method shouldn't modify any fields, hence no assertion at end
	}

	/**
	 * Move forward, so that the current element is now the next element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this sequence 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   advance may not be called.
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant wrong at start of advance()";
		// TODO: Implemented by student.
		assert wellFormed() : "invariant wrong at end of advance()";
	}

	/**
	 * Remove the current element from this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence, and the 
	 *   following element (if there is one) is now the new current element. 
	 *   If there was no following element, then there is now no current 
	 *   element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of removeCurrent()";
		// TODO: Implemented by student.
		// See textbook pp.176-78, 181-184
		assert wellFormed() : "invariant wrong at end of removeCurrent()";
	}

	/**
	 * Add a new element to this sequence, before the current element (if any). 
	 * If the new element would take this sequence beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence.
	 **/
	public void insert(Ball element)
	{
		assert wellFormed() : "invariant failed at start of insert";
		// TODO: Implemented by student.
		assert wellFormed() : "invariant failed at end of insert";
	}
	

	/**
	 * Place the contents of another sequence (which may be the
	 * same one as this!) into this sequence before the current element (if any).
	 * @param addend
	 *   a sequence whose contents will be placed into this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed into
	 *   this sequence. The current element of this sequence (if any)
	 *   is unchanged.  The addend is unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory to increase the size of this sequence.
	 **/
	public void insertAll(BallSeq addend) {
		assert wellFormed() : "invariant failed at start of addAll";
		// TODO: Implement this code.
		assert wellFormed() : "invariant failed at end of addAll";	
	}


	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 *   Whatever was current in the original object is now current in the clone.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public BallSeq clone( )
	{  	 
		assert wellFormed() : "invariant wrong at start of clone()";

		BallSeq result;

		try
		{
			result = (BallSeq) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  
			// This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		// TODO: Implemented by student.
		// Now do the hard work of cloning the list.
		// See pp 200-204, 235 (3rd ed. pp. 193-197, 228)
		// Setting precursor, cursor and tail correctly is tricky.

		assert wellFormed() : "invariant wrong at end of clone()";
		assert result.wellFormed() : "invariant wrong for result of clone()";
		return result;
	}

	
	/**
	 * Used for testing the invariant.  Do not change this code.
	 */
	public static class Spy {
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
		 * A public version of the data structure's internal node class.
		 * This class is only used for testing.
		 */
		public static class Node extends BallSeq.Node {
			// Even if Eclipse suggests it: do not add any fields to this class!
			/**
			 * Create a node with null data and null next fields.
			 */
			public Node() {
				this(null, null);
			}
			/**
			 * Create a node with the given values
			 * @param b data for new node, may be null
			 * @param n next for new node, may be null
			 */
			public Node(Ball b, Node n) {
				super(null, null);
				this.data = b;
				this.next = n;
			}
			
			/**
			 * Change a node by setting the "next" field.
			 * @param n new next field, may be null.
			 */
			public void setNext(Node n) {
				this.next = n;
			}
		}

		/**
		 * Create a debugging instance of a BallSeq
		 * with a particular data structure.
		 * @param h head
		 * @param t tail
		 * @param m many nodes
		 * @param p precursor
		 * @param c cursor
		 * @return a new instance of a BallSeq with the given data structure
		 */
		public BallSeq newInstance(Node h, Node t, int m, Node p, Node c) {
			BallSeq result = new BallSeq(false);
			result.head = h;
			result.tail = t;
			result.manyNodes = m;
			result.precursor = p;
			result.cursor = c;
			return result;
		}

		/**
		 * Return whether debugging instance meets the 
		 * requirements on the invariant.
		 * @param bs instance of to use, must not be null
		 * @return whether it passes the check
		 */
		public boolean wellFormed(BallSeq bs) {
			return bs.wellFormed();
		}
	}
}

