import edu.uwm.cs351.LinkedPartSeq;
import edu.uwm.cs351.Part;
import junit.framework.TestCase;

public class TestEfficiency extends TestCase {
	
	LinkedPartSeq s;
	
	@Override
	public void setUp() {
		s = new LinkedPartSeq();
		try {
			assert 1/s.getPart(null,0).hashCode() == 42 : "OK";
			assertTrue(true);
		} catch (NullPointerException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
	}
	
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (Throwable ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	private static final int MAX_LENGTH = 1000000;
	
	
	public void test0() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("arm", new Part("RSN" + (MAX_LENGTH-i-1))); // always adds to the tail
		}
		assertEquals(MAX_LENGTH, s.size());
	}
	
	public void test1() { 
		s.start("arm");
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addBefore(new Part("RSN" + (MAX_LENGTH-i-1))); // always adds to front and changes head
		}
		assertEquals(MAX_LENGTH, s.size());
	}
	
	public void test2() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("arm", new Part("RSN" + (MAX_LENGTH-i-1)));
		}
		s.start();
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.advance(); // checks advance()'s efficiency
		}
	}
	
	public void test3() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("arm", new Part("RSN" + i));
		}
		s.start();
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(new Part("RSN" + i), s.getCurrent()); // checks getCurrent()'s efficiency
			s.advance();
		}
	}
	
	public void test4() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("arm", new Part("RSN" + (MAX_LENGTH-i-1))); // always adds to tail
		}
		s.start();
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertTrue(s.isCurrent());
			assertEquals(new Part("RSN" + (MAX_LENGTH-i-1)), s.getCurrent());
			s.advance();
		}
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertFalse(s.isCurrent());
		}
	}
	
	public void test5() {
		s.start("arm");
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addBefore(new Part("RSN" + (MAX_LENGTH-i-1))); // always adds to front and changes head
		}
		s.start();
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertTrue(s.isCurrent());
			assertEquals(new Part("RSN" + i), s.getCurrent());
			s.advance();
		}
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertFalse(s.isCurrent());
		}
	}
	
	public void test6() {
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.addPart("arm", new Part("RSN" + i));
		}
		s.start("arm");
		for (int i=MAX_LENGTH/2; i < MAX_LENGTH; ++i) {
			s.addAfter(new Part("RSN" + i));
			s.advance(); // skip over every other part
		}
		s.start();
		for (int i=0; i<MAX_LENGTH; ++i) {
			if(i%2 == 0) assertEquals(new Part("RSN" + (i/2)), s.getCurrent());
			else assertEquals(new Part("RSN" + (MAX_LENGTH/2 + (i/2))), s.getCurrent());
			s.advance();
		}
	}
	
	public void test7() {
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.addPart("arm", new Part("RSN" + i));
		}
		s.start("arm");
		for (int i=MAX_LENGTH/2; i < MAX_LENGTH; ++i) {
			s.addBefore(new Part("RSN" + i));
			s.advance(); // skip over every other part (needs two advances)
			s.advance();
		}
		s.start();
		for (int i=0; i<MAX_LENGTH; ++i) {
			if(i%2 == 0) assertEquals(new Part("RSN" + (MAX_LENGTH/2 + (i/2))), s.getCurrent());
			else assertEquals(new Part("RSN" + (i/2)), s.getCurrent());
			s.advance();
		}
	}
	
	public void test8() { 
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("arm", new Part("RSN" + i));
		}
		s.start();
		for (int i=0; i < MAX_LENGTH; ++i) { // removeCurrent every other part
			assertEquals(new Part("RSN" + i), s.getCurrent());
			if ((i%2) == 0) s.advance(); // skips over every other part
			else s.removeCurrent();
		}
		assertEquals(MAX_LENGTH/2, s.size());		
	}
	
	public void test9() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("arm", new Part("RSN" + i));
			++i;
			s.addPart("leg", new Part("RSN" + i));
			++i;
			s.addPart("wheel", new Part("RSN" + i));
			++i;
			s.addPart("antenna", new Part("RSN" + i));
			// We assume MAX_LENGTH is evenly divisible by 4.
		}
		s.start("leg");
		while(s.isCurrent() != false) {
			s.removeCurrent();
		}
		s.start("leg");
		assertException(IllegalStateException.class, () -> s.getCurrent()); // all of the legs are removed
		assertEquals(MAX_LENGTH - (MAX_LENGTH/4), s.size()); // checks that size is 3/4 the original size
	}
	
}
