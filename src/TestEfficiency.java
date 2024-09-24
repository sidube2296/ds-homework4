import java.awt.Color;
import java.util.Random;

import edu.uwm.cs.junit.EfficiencyTestCase;
import edu.uwm.cs351.Ball;
import edu.uwm.cs351.BallSeq;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;


public class TestEfficiency extends EfficiencyTestCase {
	Vector v = new Vector();
	Ball b1 = new Ball(new Point(1.0,20.0),v,Color.RED);
	Ball b2 = new Ball(new Point(2.0,40.0),v,Color.BLUE);
	Ball b3 = new Ball(new Point(3.0,60.0),v,Color.YELLOW);
	Ball b4 = new Ball(new Point(4.0,80.0),v,Color.GREEN);
	Ball b5 = new Ball(new Point(5.0,100.0),v,Color.CYAN);
	Ball b6 = new Ball(new Point(6.0,120.0),v,Color.MAGENTA);
	Ball b7 = new Ball(new Point(7.0,140.0),v,Color.ORANGE);
	Ball b8 = new Ball(new Point(8.0,150.0),v,Color.BLACK);

	Ball b[] = {null, b1, b2, b3, b4, b5, b6, b7, b8};
	
	BallSeq s;
	Random r;
	
	@Override
	public void setUp() {
		s = new BallSeq();
		r = new Random();
		try {
			assert 1/(int)(b5.getLoc().x()-5.0) == 42 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
	}

	private static final int POWER = 20;
	private static final int MAX_LENGTH = 1 << POWER; // > 1 million 
	
	
	public void test0() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, s.size());
			s.insert(b[i%b.length]);
		}
	}
	
	public void test1() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.insert(b[i%b.length]);
			s.advance(); 
			assertFalse(s.isCurrent());
		}
		assertEquals(MAX_LENGTH, s.size());
	}
	
	public void test2() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.insert(b[b.length - i%b.length - 1]);
		}
		int j = b.length - MAX_LENGTH%b.length;
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertTrue(s.isCurrent());
			assertEquals(b[j], s.getCurrent());
			s.advance();
			if (++j == b.length) j = 0;
		}
		assertFalse(s.isCurrent());
	}
	
	public void test3() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, s.size());
			s.insert(b[i%b.length]);
		}
		for (int i=0; i < MAX_LENGTH; i += 2) {
			s.removeCurrent();
		}
		assertEquals(MAX_LENGTH/2, s.size());
	}
	
	public void test4() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, s.size());
			s.insert(b[i%b.length]);
		}
		for (int i=0; i < MAX_LENGTH; i += 2) {
			s.removeCurrent();
			s.advance();
		}
		assertEquals(MAX_LENGTH/2, s.size());
	}
	
	public void test5() {
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.insert(b[i%b.length]);
			s.advance(); 
			assertFalse(s.isCurrent());
		}
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.insert(b[i%b.length]);
			s.removeCurrent();
		}
		assertEquals(MAX_LENGTH/2, s.size());
	}
	
	public void test6() {
		BallSeq other = new BallSeq();
		for (int i=0; i < b.length; ++i) {
			other.insert(b[i]);
		}
		for (int i=0; i < MAX_LENGTH; i += b.length) {
			s.start();
			s.insertAll(other);
		}
		int n = MAX_LENGTH + (b.length-MAX_LENGTH%b.length)%b.length;
		assertEquals(n, s.size());
	}
	
	public void test7() {
		BallSeq other = new BallSeq();
		for (int i=0; i < b.length; ++i) {
			other.insert(b[i]);
		}
		for (int i=0; i < MAX_LENGTH; i += b.length) {
			assertFalse(s.isCurrent());
			s.insertAll(other);
		}
		int n = MAX_LENGTH + (b.length-MAX_LENGTH%b.length)%b.length;
		assertEquals(n, s.size());
	}
	
	public void test8() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.insert(b[i%b.length]);
			s.advance();
		}
		BallSeq other = s.clone();
		other.start();
		other.removeCurrent();
		assertEquals(MAX_LENGTH, s.size());
		s.start();
		assertEquals(b[0], s.getCurrent());
	}
	
	public void test9() {
		BallSeq other = new BallSeq();
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.insert(b[i%b.length]);
			s.advance();
		}
		for (int i=MAX_LENGTH/2; i < MAX_LENGTH; ++i) {
			other.insert(b[i%b.length]);
			other.advance();
		}
		s.insertAll(other);
		assertEquals(MAX_LENGTH, s.size());
	}
}
