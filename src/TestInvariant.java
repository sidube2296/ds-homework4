import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Ball;
import edu.uwm.cs351.BallSeq;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;


public class TestInvariant extends LockedTestCase {
	protected BallSeq.Spy spy;
	protected int reports;
	protected BallSeq bs;
	
	protected void assertReporting(boolean expected, Supplier<Boolean> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (expected) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get().booleanValue());
			if (!expected) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
	
	protected void assertWellFormed(boolean expected, BallSeq r) {
		assertReporting(expected, () -> spy.wellFormed(r));
	}

	protected BallSeq.Spy.Node bsHead, bsTail, bsPrecursor, bsCursor;
	protected BallSeq.Spy.Node n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
	protected int bsManyNodes;

	@Override // implementation
	protected void setUp() {
		spy = new BallSeq.Spy();
		bsHead = null;
		bsTail = null;
		bsManyNodes = 0;
		bsPrecursor = null;
		bsCursor = null;
		n0 = n1 = n2 = n3 = n4 = n5 = n6 = n7 = n8 = n9 = null;
	}
	
	protected Ball b1 = new Ball(new Point(1,1),new Vector(),Color.RED);
	protected Ball b2 = new Ball(new Point(2,2),new Vector(),Color.BLUE);

	
	protected BallSeq.Spy.Node newNode(Ball b, BallSeq.Spy.Node n) {
		return new BallSeq.Spy.Node(b, n);
	}
	
	protected BallSeq makeInstance() {
		return spy.newInstance(bsHead, bsTail, bsManyNodes, bsPrecursor, bsCursor);
	}
	
	
	/// Locked tests
	
	protected BallSeq.Spy.Node n(int i) {
		switch (i) {
		case 0: return null;
		case 1: return n1;
		case 2: return n2;
		case 3: return n3;
		case 4: return n4;
		case 5: return n5;
		case 6: return n6;
		case 7: return n7;
		case 8: return n8;
		case 9: return n9;
		}
		throw new IllegalArgumentException("Bad node #" + i);
	}
	
	public void test() {
		testTail(true);
		testPrecursor(false);
	}
	
	private void testTail(boolean ignored) {
		n1 = newNode(b2, null);
		n2 = newNode(b1, n1);
		n3 = newNode(b1, null);
		n4 = newNode(b2, n3);
		n5 = newNode(b1, n4);
		n6 = newNode(b2, n2);
		n7 = newNode(null, n6);
		n8 = newNode(b1, n5);
		n9 = newNode(b2, n7);
		// what is the correct tail? (0 for null, 1 for n1, 2 for n2, etc)
		assertWellFormed(true, spy.newInstance(n8, n(Ti(721878576)), 4, null, null)); // head = n8
		assertWellFormed(true, spy.newInstance(n7, n(Ti(1241125164)), 4, null, null)); // head = n7
		assertWellFormed(true, spy.newInstance(n9, n(Ti(1750349917)), 5, null, null)); // head = n9
		assertWellFormed(true, spy.newInstance(null, n(Ti(1260297218)), 0, null, null)); // head is null
	}
	
	private void testPrecursor(boolean ignored) {
		n1 = newNode(b2, null);
		n2 = newNode(b1, n1);
		n3 = newNode(b1, null);
		n4 = newNode(b2, n3);
		n5 = newNode(b1, n4);
		n6 = newNode(b2, n2);
		n7 = newNode(null, n6);
		n8 = newNode(b1, n5);
		n9 = newNode(b2, n7);
		// what is the correct precursor? (0 for null, 1 for n1, 2 for n2, etc)
		assertWellFormed(true, spy.newInstance(n8, n(Ti(721878576)), 4, n(Ti(1454045887)), n8)); // cursor = n8
		assertWellFormed(true, spy.newInstance(n7, n(Ti(1241125164)), 4, n(Ti(2021217737)), n2)); // cursor = n2
		assertWellFormed(true, spy.newInstance(n9, n(Ti(1750349917)), 5, n(Ti(1267783763)), n6)); // cursor = n6
		assertWellFormed(true, spy.newInstance(n8, n(Ti(721878576)), 4, n(Ti(1380475615)), null)); // cursor is null		
	}
	
	
	public void testA0() {
		bs = spy.newInstance(null, null, 0, null, null);
		assertWellFormed(true, bs);
	}
	
	public void testA1() {
		bs = spy.newInstance(newNode(null,null), null, 0, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testA2() {
		bs = spy.newInstance(newNode(b1,null), null, 0, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testA3() {
		bs = spy.newInstance(newNode(b1,null), null, 1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testA4() {
		bs = spy.newInstance(null, newNode(b2, null), 0, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testA5() {
		bs = spy.newInstance(null, newNode(b2, null), 1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testA6() {
		n1 = newNode(b1, null);
		bs = spy.newInstance(n1, n1, 1, null, null);
		assertWellFormed(true, bs);
	}
	
	public void testA7() {
		n1 = newNode(b2, null);
		n2 = newNode(b2, null);
		bs = spy.newInstance(n1, n2, 1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testA8() {
		n1 = newNode(b2, null);
		n2 = newNode(b2, null);
		bs = spy.newInstance(n1, n2, 2, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testA9() {
		n1 = newNode(null, null);
		bs = spy.newInstance(n1, n1, 1, null, null);
		assertWellFormed(true, bs);
	}

	public void testB0() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n2, 2, null, null);
		assertWellFormed(true, bs);
	}
	
	public void testB1() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n1, 2, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testB2() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n1, 1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testB3() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, null, 2, null, null);
		assertWellFormed(false, bs);
	}

	public void testB4() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, newNode(b2, null), 2, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testB5() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		n1.setNext(n1);
		bs = spy.newInstance(n1, n2, 2, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testB6() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		n1.setNext(n1);
		bs = spy.newInstance(n1, n1, 1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testB7() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		n2.setNext(n2);
		bs = spy.newInstance(n1, n2, 2, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testB8() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		n2.setNext(n2);
		n3 = newNode(b2, null);
		bs = spy.newInstance(n1, n3, 2, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testB9() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		n2.setNext(n1);
		n3 = newNode(b2, null);
		bs = spy.newInstance(n1, n3, 2, null, null);
		assertWellFormed(false, bs);
	}

	public void testC0() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n3, 3, null, null);
		assertWellFormed(true, bs);
	}
	
	public void testC1() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, null, 3, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testC2() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n2, 3, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testC3() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n2, 2, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testC4() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n1, 3, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testC5() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n1, 1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testC6() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		n4 = newNode(b2, null);
		bs = spy.newInstance(n1, n4, 3, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testC7() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		n4 = newNode(b2, null);
		n3.setNext(n2);
		bs = spy.newInstance(n1, n4, 3, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testC8() {
		n3 = newNode(b2, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		n4 = newNode(b2, null);
		n3.setNext(n1);
		bs = spy.newInstance(n1, n4, 3, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testC9() {
		n3 = newNode(b1, null);
		n2 = newNode(b1, n3);
		n1 = newNode(b1, n2);
		n3.setNext(n1);
		bs = spy.newInstance(n1, n3, 3, null, null);
		assertWellFormed(false, bs);
	}

	public void testD0() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n4, 4, null, null);
		assertWellFormed(true, bs);
	}

	public void testD1() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n3, 4, null, null);
		assertWellFormed(false, bs);
	}

	public void testD2() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n3, 3, null, null);
		assertWellFormed(false, bs);
	}

	public void testD3() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n2, 4, null, null);
		assertWellFormed(false, bs);
	}

	public void testD4() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n2, 2, null, null);
		assertWellFormed(false, bs);
	}

	public void testD5() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n1, 4, null, null);
		assertWellFormed(false, bs);
	}

	public void testD6() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n1, 1, null, null);
		assertWellFormed(false, bs);
	}

	public void testD7() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, null, 4, null, null);
		assertWellFormed(false, bs);
	}

	public void testD8() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, null, 0, null, null);
		assertWellFormed(false, bs);
	}

	public void testD9() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(b2, n2);
		n5 = newNode(b1, null);
		bs = spy.newInstance(n1, n5, 4, null, null);
		assertWellFormed(false, bs);
	}
	
	
	public void testF0() {
		n0 = newNode(null,null);
		bs = spy.newInstance(null, null, 0, null, null);
		assertWellFormed(true, bs);
	}
	
	public void testF1() {
		n0 = newNode(null,null);
		bs = spy.newInstance(null, null, 0, n0, null);
		assertWellFormed(false, bs);
	}
	
	public void testF2() {
		n1 = newNode(b1, null);
		bs = spy.newInstance(n1, n1, 1, null, null);
		assertWellFormed(true, bs);
	}
	
	public void testF3() {
		n1 = newNode(b1, null);
		bs = spy.newInstance(n1, n1, 1, n1, null);
		assertWellFormed(false, bs);
	}
	
	public void testF4() {
		n1 = newNode(b1, null);
		n2 = newNode(b1, null);
		bs = spy.newInstance(n1, n1, 1, n2, null);
		assertWellFormed(false, bs);
	}
	
	public void testF5() {
		n1 = newNode(b1, null);
		n2 = newNode(b2, n1);
		bs = spy.newInstance(n2, n1, 2, n2, n1);
		assertWellFormed(true, bs);
	}
	
	public void testF6() {
		n1 = newNode(b1, null);
		n2 = newNode(b2, n1);
		bs = spy.newInstance(n2, n1, 2, null, n2);
		assertWellFormed(true, bs);
	}
	
	public void testF7() {
		n1 = newNode(b1, null);
		n2 = newNode(b2, n1);
		bs = spy.newInstance(n2, n1, 2, n1, null);
		assertWellFormed(false, bs);
	}
	
	public void testF8() {
		n1 = newNode(b1, null);
		n2 = newNode(b2, n1);
		n3 = newNode(b2, n1);
		bs = spy.newInstance(n2, n1, 2, n3, n1);
		assertWellFormed(false, bs);
	}
	
	public void testF9() {
		n1 = newNode(b1, null);
		n2 = newNode(b2, n1);
		bs = spy.newInstance(n2, n1, 2, null, null);
		assertWellFormed(true, bs);
	}

	public void testG0() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, null, null);
		assertWellFormed(true, bs);
	}

	public void testG1() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, null, n1);
		assertWellFormed(true, bs);
	}

	public void testG2() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n1, n2);
		assertWellFormed(true, bs);
	}

	public void testG3() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n2, n3);
		assertWellFormed(true, bs);
	}

	public void testG4() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n3, n4);
		assertWellFormed(true, bs);
	}

	public void testG5() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n4, null);
		assertWellFormed(false, bs);
	}

	public void testG6() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		n5 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n5, null);
		assertWellFormed(false, bs);
	}

	public void testG7() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		n5 = newNode(null, n3);
		bs = spy.newInstance(n1, n4, 4, n5, null);
		assertWellFormed(false, bs);
	}

	public void testG8() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		n5 = newNode(b2, n4);
		bs = spy.newInstance(n1, n4, 4, n5, null);
		assertWellFormed(false, bs);
	}

	public void testG9() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		n5 = newNode(b1, null);
		bs = spy.newInstance(n1, n4, 4, n5, null);
		assertWellFormed(false, bs);
	}


	public void testK0() {
		n1 = newNode(null, null);
		bs = spy.newInstance(null, null, 0, null, n1);
		assertWellFormed(false, bs);
	}

	public void testK1() {
		n1 = newNode(null, null);
		bs = spy.newInstance(n1, n1, 1, null, n1);
		assertWellFormed(true, bs);
	}
	
	public void testK2() {
		n1 = newNode(null, null);
		bs = spy.newInstance(n1, n1, 1, null, null);
		assertWellFormed(true, bs);
	}
	
	public void testK3() {
		n1 = newNode(null, null);
		n2 = newNode(null, null);
		bs = spy.newInstance(n1, n1, 1, null, n2);
		assertWellFormed(false, bs);
	}
	
	public void testK4() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n2, 2, null, n1);
		assertWellFormed(true, bs);
	}
	
	public void testK5() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		n3 = newNode(b1, n2);
		bs = spy.newInstance(n1, n2, 2, null, n3);
		assertWellFormed(false, bs);
	}
	
	public void testK6() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n2, 2, n1, n2);
		assertWellFormed(true, bs);
	}
	
	public void testK7() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		n3 = newNode(b2, null);
		bs = spy.newInstance(n1, n2, 2, n1, n3);
		assertWellFormed(false, bs);
	}
	
	public void testK8() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n2, 2, n1, n1);
		assertWellFormed(false, bs);
	}
	
	public void testK9() {
		n2 = newNode(b2, null);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n2, 2, n1, null);
		assertWellFormed(false, bs);
	}
	
	public void testL0() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, null, n1);
		assertWellFormed(true, bs);
	}
	
	public void testL1() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, null, n2);
		assertWellFormed(false, bs);
	}
	
	public void testL2() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		n5 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, null, n5);
		assertWellFormed(false, bs);
	}

	public void testL3() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		n5 = newNode(null, n2);
		bs = spy.newInstance(n1, n4, 4, n1, n5);
		assertWellFormed(false, bs);
	}

	public void testL4() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n1, n3);
		assertWellFormed(false, bs);
	}

	public void testL5() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		n5 = newNode(b2, n4);
		bs = spy.newInstance(n1, n4, 4, n2, n5);
		assertWellFormed(false, bs);
	}

	public void testL6() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n2, n4);
		assertWellFormed(false, bs);
	}

	public void testL7() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		n5 = newNode(b1, null);
		bs = spy.newInstance(n1, n4, 4, n3, n5);
		assertWellFormed(false, bs);
	}

	public void testL8() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n3, null);
		assertWellFormed(false, bs);
	}

	public void testL9() {
		n4 = newNode(b1, null);
		n3 = newNode(b2, n4);
		n2 = newNode(null, n3);
		n1 = newNode(b1, n2);
		bs = spy.newInstance(n1, n4, 4, n3, n2);
		assertWellFormed(false, bs);
	}
	
	
	/* same as testA0:
	public void testN0() {
		bs = spy.newInstance(null, null, 0, null, null);
		assertWellFormed(true, bs);
	}*/
	
	public void testN1() {
		bs = spy.newInstance(null, null, 1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testN2() {
		bs = spy.newInstance(null, null, -1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testN3() {
		n1 = newNode(null, null);
		bs = spy.newInstance(n1, n1, 0, null, n1);
		assertWellFormed(false, bs);
	}
	
	public void testN4() {
		n2 = newNode(b1, null);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n2, 2, null, null);
		assertWellFormed(true, bs);
	}
	
	public void testN5() {
		n2 = newNode(b1, null);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n2, 1, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testN6() {
		n2 = newNode(b1, null);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n2, 3, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testN7() {
		n3 = newNode(b1, null);
		n2 = newNode(null, n3);
		n1 = newNode(b2, n2);
		bs = spy.newInstance(n1, n3, 2, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testN8() {
		n4 = newNode(b2, null);
		n3 = newNode(b1, n4);
		n2 = newNode(b2, n3);
		n1 = newNode(null, n2);
		bs = spy.newInstance(n1, n4, 5, null, null);
		assertWellFormed(false, bs);
	}
	
	public void testN9() {
		n5 = newNode(b1, null);
		n4 = newNode(null, n5);
		n3 = newNode(b2, n4);
		n2 = newNode(b1, n3);
		n1 = newNode(null, n2);
		bs = spy.newInstance(n1, n5, 5, null, null);
		assertWellFormed(true, bs);
	}	
}
