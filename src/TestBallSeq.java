import java.awt.Color;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Ball;
import edu.uwm.cs351.BallSeq;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;


public class TestBallSeq extends LockedTestCase {
	Ball b1 = new Ball(new Point(1,1), new Vector(), Color.GREEN); 
	Ball b2 = new Ball(new Point(2,2), new Vector(), Color.GREEN);
	Ball b3 = new Ball(new Point(3,3), new Vector(), Color.GREEN);
	Ball b4 = new Ball(new Point(4,4), new Vector(), Color.GREEN);
	Ball b5 = new Ball(new Point(5,5), new Vector(), Color.GREEN);

	BallSeq s;
	
	protected BallSeq newSequence() {
		return new BallSeq();
	}

	@Override
	protected void setUp() {
		s = newSequence();
		try {
			assert 1/((int)b1.getRadius()-15) == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			return;
		}
	}

	protected <T> void assertException(Supplier<T> producer, Class<?> excClass) {
		try {
			T result = producer.get();
			assertFalse("Should have thrown an exception, not returned " + result,true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				ex.printStackTrace();
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	protected <T> void assertException(Class<?> excClass, Runnable f) {
		try {
			f.run();
			assertFalse("Should have thrown an exception, not returned",true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				ex.printStackTrace();
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	/**
	 * Return the Ball as an integer
	 * <dl>
	 * <dt>-1<dd><i>(an exception was thrown)
	 * <dt>0<dd>null
	 * <dt>1<dd>e1
	 * <dt>2<dd>e2
	 * <dt>3<dd>e3
	 * <dt>4<dd>e4
	 * <dt>5<dd>e5
	 * </dl>
	 * @return integer encoding of Ball supplied
	 */
	protected int asInt(Supplier<Ball> g) {
		try {
			Ball n = g.get();
			if (n == null) return 0;
			return n.getLoc().intX();
		} catch (RuntimeException ex) {
			return -1;
		}
	}
	
	/// test0n: locked tests
	
	public void test00() {
		// Nothing inserted yet:
		assertEquals(Ti(1112658640),s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertFalse(s.isCurrent());
	}
	
	public void test01() {
		// Initially empty.
		// -1 for error, 0 for null, 1 for b1, 2 for b2 ...
		assertEquals(Ti(1848063),asInt(() -> s.getCurrent()));
		s.insert(b1);
		assertEquals(Ti(337008384),asInt(() -> s.getCurrent()));
		s.start();
		assertEquals(Ti(901033071),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(257085790),asInt(() -> s.getCurrent()));
	}
	
	public void test02() {
		// Initially empty.
		s.insert(b4);
		s.insert(b5);
		// -1 for error, 0 for null, 1 for b1, 2 for b2 ...
		assertEquals(Ti(1876093076),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(56523864),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(1671626331),asInt(() -> s.getCurrent()));
	}
	
	public void test03() {
		// Initially empty
		s.insert(b3);
		s.advance();
		s.insert(b2);
		// -1 for error, 0 for null, 1 for b1, 2 for b2 ...
		assertEquals(Ti(1068398624),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(2129376917),asInt(() -> s.getCurrent()));
		s.start();
		assertEquals(Ti(2073343044),asInt(() -> s.getCurrent()));
	}
	
	public void test05() {
		// Initially empty
		s.insert(null);
		assertEquals(Ti(1049586199),s.size());
		assertEquals(Tb(1284130738),s.isCurrent());
		// -1 for error, 0 for null, 1 for b1, 2 for b2 ...
		assertEquals(Ti(1728369560),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(718450125),asInt(() -> s.getCurrent()));
	}
	
	public void test06() {
		s.insert(b1);
		s.insert(b2);
		s.start();
		s.advance();
		assertSame(b1,s.getCurrent());
		BallSeq s2 = newSequence();
		s2.insert(b4);
		s.insertAll(s2);
		assertEquals(Ti(1153117195),s.size());
		// -1 for error, 0 for null, 1 for b1, 2 for b2 ...
		assertEquals(Ti(13699069),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(1034153840),asInt(() -> s.getCurrent()));
	}
	
	
	/// test1N: tests of a single element sequence
	
	public void test10() {
		s.insert(b1);
		assertEquals(1,s.size());
	}
	
	public void test11() {
		s.insert(b2);
		assertTrue(s.isCurrent());
	}
	
	public void test12() {
		s.insert(b3);
		assertSame(b3,s.getCurrent());
	}
	
	public void test13() {
		s.insert(b4);
		s.start();
		assertSame(b4,s.getCurrent());
	}
	
	public void test14() {
		s.insert(b5);
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test15() {
		s.insert(b5);
		s.advance();
		s.start();
		assertTrue(s.isCurrent());
	}
	
	/* duplicated by test05
	public void test16() {
		s.insert(null);
		assertEquals(1,s.size());
	}
	*/
	
	public void test17() {
		s.insert(null);
		assertTrue(s.isCurrent());
		assertNull(s.getCurrent());
	}
	
	public void test18() {
		s.insert(null);
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	/// test2N: more complex tests with small sequences
	
	public void test20() {
		s.insert(b1);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertEquals(1,s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		assertEquals(1,s.size());
	}
	
	public void test21() {
		s.start();
		assertException(IllegalStateException.class, () -> s.getCurrent());
	}

	public void test22() {
		assertException(IllegalStateException.class, () -> s.advance());
	}

	public void test23() {
		s.insert(b2);
		s.start();
		s.advance();
		assertException(IllegalStateException.class, () -> s.advance());
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
	}

	public void test24() {
		s.insert(b2);
		s.advance();
		assertException(IllegalStateException.class, () -> s.getCurrent());
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
	}
	
	public void test25() {
		s.insert(b1);
		s.insert(b2);
		assertEquals(2,s.size());
	}
	
	public void test26() {
		s.insert(b3);
		s.insert(b4);
		assertTrue(s.isCurrent());
	}
	
	public void test27() {
		s.insert(b1);
		s.insert(b2);
		assertSame(b2,s.getCurrent());
	}
	
	public void test28() {
		s.insert(b1);
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.insert(b2);
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(2,s.size());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		s.start();
		assertSame(b2,s.getCurrent());
	}
	
	public void test29() {
		s.insert(b1);
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		s.insert(b2);
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertSame(b2,s.getCurrent());
		assertTrue(s.isCurrent());
	}
	
	
	/// test3n: tests of larger sequences
	
	public void test30() {
		s.insert(b1);
		s.insert(b2);
		s.insert(b3);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b3,s.getCurrent());
		assertTrue(s.isCurrent());
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b2,s.getCurrent());
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
		s.start();
		assertSame(b3,s.getCurrent());
		s.advance();
		s.start();
		assertSame(b3,s.getCurrent());
	}
	
	public void test31() {
		s.insert(b1);
		s.advance();
		s.insert(b2);
		s.insert(b3);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b3,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
		s.start();
		assertSame(b1,s.getCurrent());
	}

	public void test32() {
		s.insert(b2);
		s.advance();
		s.insert(b3);
		s.start();
		s.insert(b1);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b3,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
	}
	
	public void test33() {
		s.insert(b1); s.advance();
		s.insert(b2); s.advance();
		s.insert(b3);
		assertSame(b3, s.getCurrent());
		s.start();
		assertEquals(b1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
	}

	public void test34() {
		s.insert(b3);
		s.insert(b1);
		s.advance();
		s.insert(null);
		assertNull(s.getCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.start();
		assertEquals(3,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
	}
	
	public void test35() {
		s.insert(b2);
		s.advance();
		s.insert(null);
		s.start();
		s.insert(null);
		// s.start(); // REDUNDANT
		assertNull(s.getCurrent());
		s.advance();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertNull(s.getCurrent());
		s.advance();
		assertEquals(3,s.size());
		assertFalse(s.isCurrent());
		assertException(IllegalStateException.class, () -> s.getCurrent());
		assertException(IllegalStateException.class, () -> s.advance());
	}
 
	public void test38() {
		s.insert(b1);
		s.insert(b2);
		s.insert(b3);
		s.insert(b4);
		s.insert(b5);
		assertSame(b5,s.getCurrent());
		s.insert(b1);
		s.insert(b2);
		s.insert(b3);
		s.insert(b4);
		s.insert(b5);
		assertEquals(10,s.size());
		assertTrue(s.isCurrent());
		s.start();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test39() {
		Ball b[] = new Ball[] {b1, b2, b3, b4, b5 };
		for (int i=0; i < 39; ++i) {
			s.insert(b[i%b.length]);
			s.advance();
		}
		s.start();
		for (int i=0; i < 39; ++i) {
			assertSame("b[" + i + "]",b[i%b.length],s.getCurrent());
			s.advance();
		}
		assertEquals(39,s.size());
	}

	
	/// test4N: tests of removeCurrent
	
	public void test40() {
		s.insert(b1);
		s.removeCurrent();
		assertFalse(s.isCurrent());
	}
	
	public void test41() {
		s.insert(b2);
		s.removeCurrent();
		assertEquals(0,s.size());
	}
	
	public void test42() {
		s.insert(b3);
		s.advance();
		s.insert(b4);
		s.removeCurrent();
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
		s.start();
		assertSame(b3,s.getCurrent());
	}
	
	public void test43() {
		s.insert(b4);
		s.insert(b3);
		s.start();
		s.removeCurrent();
		assertTrue(s.isCurrent());
		assertSame(b4,s.getCurrent());
	}
	
	public void test44() {
		s.insert(b4);
		s.advance();
		s.insert(null);
		s.start();
		s.removeCurrent();
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
	}
	
	public void test45() {
		s.insert(b5);
		s.insert(b4);
		s.insert(b3);
		s.start();
		s.advance();
		s.removeCurrent();
		assertSame(b5,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(b3,s.getCurrent());
	}
	
	public void test46() {
		s.insert(b4); s.advance();
		s.insert(b5); s.advance();
		s.insert(null);
		s.removeCurrent();
		assertEquals(2,s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test47() {
		s.insert(b5);
		s.insert(b4);
		s.insert(b3);
		s.insert(b2);
		s.insert(b1);
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); 
		s.removeCurrent();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent());
		s.removeCurrent();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test48() {
		s.insert(b5);
		s.insert(b4);
		s.insert(b3);
		s.insert(b2);
		s.insert(b1);
		s.start();
		assertSame(b1,s.getCurrent()); s.removeCurrent();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.removeCurrent();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.removeCurrent();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test49() {
		Ball b[] = new Ball[] {b1, b2, b3, b4, b5 };
		for (int i=0; i < 49; ++i) {
			s.insert(b[i%b.length]);
			s.advance();
		}
		s.start();
		for (int i=0; i < 49; ++i) {
			assertSame("b[" + i + "]",b[i%b.length],s.getCurrent());
			if ((i%2) == 0) s.removeCurrent();
			else s.advance();
		}
		s.start();
		for (int i=0; i < 24; ++i) {
			assertSame("b[" + (i*2+1) + "]", b[(i*2+1)%b.length],s.getCurrent());
			s.removeCurrent();
		}
		assertEquals(0,s.size());
	}
	
	
	/// test5N: errors with removeCurrent
	
	public void test50() {
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test51() {
		s.insert(b1);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test52() {
		s.insert(b1);
		s.removeCurrent();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test53() {
		s.insert(b1); s.advance();
		s.insert(b2);
		s.removeCurrent();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test54() {
		s.insert(b1); s.advance();
		s.insert(b2); s.advance();
		s.insert(b3); s.advance();
		s.insert(b4); s.advance();
		s.insert(b5);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test55() {
		s.insert(b5);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.start();
		s.insert(b4);
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.start();
		s.insert(b3);
		s.advance();
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.start();
		s.insert(b2);
		s.advance();
		s.advance();
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.start();
		s.insert(b1);
		assertEquals(5,s.size());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}

	public void test59() {
		s.insert(null);
		s.advance();
		s.insert(b2);
		s.advance();
		s.insert(null);
		
		assertEquals(3,s.size());
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
		s.removeCurrent();
		assertEquals(2,s.size());
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test60() {
		BallSeq se = newSequence();
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertEquals(0,s.size());
		s.insert(b1);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(1,s.size());
		assertEquals(0,se.size());
		assertSame(b1,s.getCurrent());
		s.advance();
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
		assertEquals(0,se.size());
		s.insert(b2);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(2,s.size());
		assertEquals(0,se.size());
		s.start();
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		assertEquals(2,s.size());
		assertEquals(0,se.size());
	}
	
	public void test61() {
		BallSeq se = newSequence();
		se.insert(b1);
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertTrue(se.isCurrent());
		assertEquals(1,s.size());
		assertEquals(1,se.size());
		s.start();
		assertSame(b1,s.getCurrent());
		assertSame(b1,se.getCurrent());
	}
	
	public void test62() {
		BallSeq se = newSequence();
		se.insert(b1);
		s.insert(b2);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertSame(b1,se.getCurrent());
		assertSame(b2,s.getCurrent());
	}
	
	public void test63() {
		BallSeq se = newSequence();
		se.insert(b1);
		s.insert(b2);
		s.advance();
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertTrue(se.isCurrent());
		assertSame(b1,se.getCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test64() {
		BallSeq se = newSequence();
		se.insert(b1);
		se.advance();
		s.insert(b3);
		s.insert(b2);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertFalse(se.isCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(b1,s.getCurrent());
	}
	
	public void test65() {
		BallSeq se = newSequence();
		se.insert(b1);
		s.insert(b2);
		s.advance();
		s.insert(b3);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(b3,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test66() {
		BallSeq se = newSequence();
		se.insert(b1);
		s.insert(b2);
		s.advance();
		s.insert(b3);
		s.advance();
		assertFalse(s.isCurrent());
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(b1,se.getCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}

	public void test67() {
		BallSeq se = newSequence();
		se.insert(b2);
		se.insert(b1);	
		s.insert(b4);
		s.insert(b3);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
	}

	public void test68() {
		BallSeq se = newSequence();
		se.insert(b2);
		se.insert(b1);
		se.advance();
		s.insert(b3);
		s.advance();
		s.insert(b4);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(b2,se.getCurrent()); se.advance();
		assertFalse(se.isCurrent());
		// check s
		assertSame(b4,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
	}

	public void test69() {
		BallSeq se = newSequence();
		se.insert(b2);
		se.insert(b1);
		se.advance();
		se.advance();
		s.insert(b3);
		s.advance();
		s.insert(b4);
		s.advance();
		assertFalse(s.isCurrent());
		assertFalse(se.isCurrent());
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertFalse(se.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		s.start();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
	}

	public void test70() {
		BallSeq se = newSequence();
		se.insert(b3);
		se.insert(b4);
		se.insert(b5);
		se.insert(b3);
		se.insert(b4);
		se.insert(b5);
		se.insert(b3);
		se.insert(b4);
		se.insert(b5);
		se.insert(b3);
		se.insert(b4);
		se.insert(b5);
		se.insert(b3);
		se.insert(b4);
		se.insert(b5);
		se.insert(b3);
		se.insert(b4);
		se.insert(b5);
		se.insert(b3);
		se.insert(b4);
		se.insert(b5);
		se.insert(b3);
		se.insert(b4);
		se.insert(b5);
		// se has 24 elements
		s.insert(b1);
		s.advance();
		s.insert(b2);
		s.insertAll(se);
		assertEquals(26,s.size());
		s.start();
		s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();		
		s.insertAll(se);
		assertEquals(50,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		// interruption
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		// done with all 24 copies most recently inserted
		// now back to the original
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); // etc.
	}
	
	public void test71() {
		s.insertAll(s);
		assertFalse(s.isCurrent());
		assertEquals(0,s.size());
	}
	
	
	public void test72() {
		s.insert(b1);
		s.insertAll(s);
		assertEquals(2,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
	}
	
	public void test73() {
		s.insert(b1);
		s.advance();
		s.insertAll(s);
		assertEquals(2,s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test74() {
		s.insert(b1);
		s.removeCurrent();
		s.insertAll(s);
		assertEquals(0,s.size());
		assertFalse(s.isCurrent());
	}
	
	public void test75() {
		s.insert(b2);
		s.insert(b1);
		s.insertAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
	}
	
	public void test76() {
		s.insert(b1);
		s.advance();
		s.insert(b2);
		s.insertAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
	}

	public void test77() {
		s.insert(b1);
		s.advance();
		s.insert(b2);
		s.advance();
		assertFalse(s.isCurrent());
		s.insertAll(s);
		assertFalse(s.isCurrent());
		assertEquals(4,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}

	public void test78() {
		s.insert(b1);
		s.advance();
		s.insert(b2);
		s.insertAll(s);
		s.removeCurrent();
		s.insert(b3);
		assertSame(b3,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		s.insertAll(s);
		assertEquals(8,s.size());
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
	}
	
	public void test79() {
		BallSeq se = newSequence();
		se.insert(b1);
		se.advance();
		se.insert(b2);	
		s.insert(b3);
		s.advance();
		s.insert(b4);
		s.insertAll(se); // s = e3 e1 e2 * e4
		s.insert(b5); // s = e3 e1 e2 * e5 e4
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b4,s.getCurrent());
		assertEquals(5,s.size());
		assertEquals(2,se.size());
		assertSame(b2,se.getCurrent());
		se.advance();
		assertFalse(se.isCurrent());
		se.start();
		assertSame(b1,se.getCurrent());
	}
	
	public void test80() {
		BallSeq c = s.clone();
		assertFalse(c.isCurrent());
		assertEquals(0, c.size());
	}
	
	public void test81() {
		s.insert(b1);
		BallSeq c = s.clone();
		
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,c.getCurrent()); c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}
	
	public void test82() {
		s.insert(b1);
		s.advance();
		BallSeq c = s.clone();
		
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}

	public void test83() {
		BallSeq c = s.clone();
		assertFalse(c.isCurrent());
		
		s.insert(b1);
		c = s.clone();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b1,s.getCurrent());
		assertSame(b1,c.getCurrent());
		
		s.advance();
		s.insert(b2);
		c = s.clone();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		
		s.insert(b3);
		assertTrue(s.isCurrent());
		assertFalse(c.isCurrent());
		
		c = s.clone();
		assertSame(b3,s.getCurrent());
		assertSame(b3,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		s.start();
		c.start();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b1,s.getCurrent());
		assertSame(b1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
		
		s.start();
		c = s.clone();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b1,s.getCurrent());
		assertSame(b1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b3,s.getCurrent());
		assertSame(b3,c.getCurrent());		
	}
	
	public void test84() {
		s.insert(b1);
		s.advance();
		s.insert(b3);
		s.insert(b2);
		s.removeCurrent();
		
		BallSeq c = s.clone();
		
		assertEquals(2,c.size());
		
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		
		assertSame(b3,s.getCurrent());
		assertSame(b3,c.getCurrent());
	}

	public void test85() {
		s.insert(b1);
		s.advance();
		s.insert(b2);
		
		BallSeq c = s.clone();
		s.insert(b3);
		c.insert(b4);
		
		assertSame(b3,s.getCurrent());
		assertSame(b4,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		
		s.start();
		c.start();
		assertSame(b1,s.getCurrent());
		assertSame(b1,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(b3,s.getCurrent());
		assertSame(b4,c.getCurrent());
	}
}
