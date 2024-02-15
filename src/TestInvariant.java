import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs351.LinkedPartSeq;
import edu.uwm.cs351.Part;
import junit.framework.TestCase;


public class TestInvariant extends TestCase {
	protected LinkedPartSeq.Spy spy = new LinkedPartSeq.Spy();
    protected int reports;
    
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
    
	LinkedPartSeq dr;
	
	String f1 = "arm";
	String f2 = "leg";
	String f3 = "head";
	String f4 = "antenna";
	String f5 = "panel";
	String f6 = null;
	
	Part t1 = new Part("1");
	Part t2 = new Part("2");
	Part t2a= new Part("2");
	Part t3 = new Part("3");
	Part t4 = new Part("4");
	Part t5 = new Part("5");
	
	
	// manyNodes accurately contains the number of nodes in the list (starting at head)
	public void testA01() {
		dr = spy.create(null, 0, null, null);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testA02() {
		dr = spy.create(null, 1, null, null);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA03() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, null);
		dr = spy.create(h, 1, null, h);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testA04() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, null);
		dr = spy.create(h, 0, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA05() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, null);
		dr = spy.create(h, -1, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA06() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, null);
		dr = spy.create(h, 2, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA07() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1,t1, null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2, null);
		
		dr = spy.create(h, 2, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA08() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f2, t2, null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, null);
		
		dr = spy.create(h, 0, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA09() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f3, t1, t);
		
		dr = spy.create(h, 1, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA10() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f3, t2, null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t5, t);
		
		dr = spy.create(h, 2, null, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testA11() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f3, t3, null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2, t4, t);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA12() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t2, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f2, t3, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f3, t4, n);
		
		dr = spy.create(h, 2, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testA13() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f3, t1, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f2, t3, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t2, n);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testA14() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f3, t3, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, n);
		
		dr = spy.create(h, 4, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	// no function or part is null
	public void testB01() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, null, null);
		dr = spy.create(h, 1, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB02() {
		LinkedPartSeq.Spy.Node h = spy.newNode(null, t1, null);
		dr = spy.create(h, 1, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB03() { // testD
		LinkedPartSeq.Spy.Node h = spy.newNode(null, null, null);
		dr = spy.create(h, 1, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB04() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, null, null);
		dr = spy.create(h, 0, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB05() {
		LinkedPartSeq.Spy.Node h = spy.newNode(null, t1, null);
		dr = spy.create(h, 0, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB06() { // testE
		LinkedPartSeq.Spy.Node h = spy.newNode(null, null, null);
		dr = spy.create(h, 0, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB07() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f2, null, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f3, t4, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t2, n);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB08() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f6, t3, null); // null function (hidden a little bit with f6)
		LinkedPartSeq.Spy.Node n = spy.newNode(f2, t2, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2, t2a, n);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB09() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f6, null, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f3, t3, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2, t2, n);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB10() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f6, null, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f3, t1, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t3, n);
		
		dr = spy.create(h, 2, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB11() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f4, t4, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f6, t1, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2, t2, n);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB12() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f2, null, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2, t2, n);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB13() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f6, null, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2, t3, n);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testB14() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f5, t5, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f6, null, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f3, t3, n);
		
		dr = spy.create(h, 2, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	// Note: since this implementation is a singly-linked list and i is never placed into the list in
	// a reachable way, it shouldn't actually be checked by wellFormed. So this test is most likely not
	// necessary to leave in.
	public void testB15() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f4, t1, null);
		LinkedPartSeq.Spy.Node i = spy.newNode(null, null, t);
		LinkedPartSeq.Spy.Node n1 = spy.newNode(f1, t3, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2, t5, n1);
		spy.setNext(i, n1);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	// The "tail" pointer is consistent
	public void testC01() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, null);
		dr = spy.create(h, 1, null, null);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testC02() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		dr = spy.create(null, 0, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testC03() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		dr = spy.create(null, 1, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testC04() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1,t1,null);
		LinkedPartSeq.Spy.Node t = spy.newNode(f1,t1,null);
		
		dr = spy.create(h, 1, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testC05() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f1,t1,null);
		LinkedPartSeq.Spy.Node t = spy.newNode(f1,t1,null);
		
		dr = spy.create(h, 2, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testC06() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f2, t2, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, n);
		
		dr = spy.create(h, 3, null, null);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testC07() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f3, t3, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f2, t2, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, n);
		
		dr = spy.create(h, 3, null, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testC08() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f2, t2, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f3, t3, n);
		
		dr = spy.create(h, 3, null, n);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testC09() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f2, t2a, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f3, t4, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f5, t5, n);
		
		dr = spy.create(h, 3, null, impostor);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	// The precursor field is null or points to a node in the list
	public void testD01() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		dr = spy.create(h, 1, h, h);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testD02() { // part 1 (pairs with test41 & test42)
		LinkedPartSeq.Spy.Node t = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1,t1,t);
		
		dr = spy.create(h, 2, null, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testD03() { // part 2
		LinkedPartSeq.Spy.Node t = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1,t1,t);
		
		dr = spy.create(h, 2, h, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}

	public void testD04() { // part 3
		LinkedPartSeq.Spy.Node t = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1,t1,t);
		
		dr = spy.create(h, 2, t, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testD05() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f1, t1, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f2, t4, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f5, t3, n);
		
		dr = spy.create(h, 3, h, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testD06() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f3, t1, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f3, t2, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f4, t3, n);
		
		dr = spy.create(h, 3, n, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testD07() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f3, t5, null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f4, t3, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f2, t1, n);
		
		dr = spy.create(h, 3, t, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	public void testD08() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f2,t2,null);
		
		dr = spy.create(h, 1, impostor, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testD09() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f2,t2,null);
		
		dr = spy.create(h, 2, impostor, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}

	public void testD10() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f1,t1,h);
		
		dr = spy.create(h, 1, impostor, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testD11() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f1,t1,h);
		
		dr = spy.create(h, 2, impostor, h);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testD12() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f4, t4, null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f2, t3, t);
		LinkedPartSeq.Spy.Node n1 = spy.newNode(f3, t2, t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1, t1, n1);
		
		dr = spy.create(h, 3, impostor, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	// cycle tests
	/* These tests involve cycles.
	 * For Homework #4,  we have done the check for you.
	 * So these tests should all pass unless you broke them.
	 */
	public void testE01() { // simple cycle
		LinkedPartSeq.Spy.Node t = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1,t1,t);
		
		spy.setNext(t, h);
		dr = spy.create(h, 2, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testE02() {
		LinkedPartSeq.Spy.Node n4 = spy.newNode(f5,t5,null);
		LinkedPartSeq.Spy.Node n3 = spy.newNode(f4,t4,n4);
		LinkedPartSeq.Spy.Node n2 = spy.newNode(f3,t3,n3);
		LinkedPartSeq.Spy.Node n1 = spy.newNode(f2,t2,n2);
		LinkedPartSeq.Spy.Node n0 = spy.newNode(f1,t1,n1);

		// more cycle checks
		// Note: might break this test up into a lot of smaller tests and add a little bit more
		
		spy.setNext(n4, n0);
		dr = spy.create(n0, 5, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 6, null, n0);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, -1, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));

		spy.setNext(n4, n1);
		dr = spy.create(n0, 5, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 6, null, n1);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, -1, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));

		spy.setNext(n4, n2);
		dr = spy.create(n0, 5, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 6, null, n2);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, -1, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));

		spy.setNext(n4, n3);
		dr = spy.create(n0, 5, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 6, null, n3);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, -1, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));

		spy.setNext(n4, n4);
		dr = spy.create(n0, 5, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 6, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, -1, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		
		spy.setNext(n4,  null);
		dr = spy.create(n0, 5, null, n4);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	// combination tests
	public void testX01() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f2,t2,null);
		
		dr = spy.create(h, 1, impostor, impostor);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testX02() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f2,t2,null);
		
		dr = spy.create(h, 2, impostor, impostor);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testX03() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f1,t1,h);
		
		dr = spy.create(h, 1, impostor, impostor);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testX04() {
		LinkedPartSeq.Spy.Node h = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node impostor = spy.newNode(f1,t1,h);
		
		dr = spy.create(h, 2, impostor, impostor);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void tesX05() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f2,t2,null);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1,t1,t);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(h, 3, h, t);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(h, 3, t, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testX06() {
		LinkedPartSeq.Spy.Node t = spy.newNode(f3,t3,null);
		LinkedPartSeq.Spy.Node n = spy.newNode(f2,t2,t);
		LinkedPartSeq.Spy.Node h = spy.newNode(f1,t1,n);
		
		dr = spy.create(h, 3, null, t);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(h, 3, h, t);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(h, 3, n, t);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(h, 3, t, t);
		assertReporting(true, () -> spy.wellFormed(dr));

		// impostor nodes
		LinkedPartSeq.Spy.Node i1 = spy.newNode(f3,t3, null);
		LinkedPartSeq.Spy.Node i2 = spy.newNode(f2,t2, t);
		LinkedPartSeq.Spy.Node i3 = spy.newNode(f1,t1, n);

		dr = spy.create(h, 2, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(h, 4, null, t);
		assertReporting(false, () -> spy.wellFormed(dr));

		dr = spy.create(h, 3, i1, t);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(h, 3, i2, t);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(h, 3, i3, t);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	public void testX07() {
		LinkedPartSeq.Spy.Node n4 = spy.newNode(f5,t5,null);
		LinkedPartSeq.Spy.Node n3 = spy.newNode(f4,t4,n4);
		LinkedPartSeq.Spy.Node n2 = spy.newNode(f3,t3,n3);
		LinkedPartSeq.Spy.Node n1 = spy.newNode(f2,t2,n2);
		LinkedPartSeq.Spy.Node n0 = spy.newNode(f1,t1,n1);
		
		dr = spy.create(n0, 5, null, n4);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, n0, n4);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, n1, n4);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, n2, n4);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, n3, n4);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, n4, n4);
		assertReporting(true, () -> spy.wellFormed(dr));

		LinkedPartSeq.Spy.Node i4 = spy.newNode(f5,t5, null);
		LinkedPartSeq.Spy.Node i3 = spy.newNode(f4,t4, n4);
		LinkedPartSeq.Spy.Node i2 = spy.newNode(f3,t3, n3);
		LinkedPartSeq.Spy.Node i1 = spy.newNode(f2,t2,n2);
		LinkedPartSeq.Spy.Node i0 = spy.newNode(f1,t1,n1);

		dr = spy.create(n0, 4, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 4, n1, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 6, null, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 6, n2, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		
		dr = spy.create(n0, 5, i0, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, i1, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, i2, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, i3, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
		dr = spy.create(n0, 5, i4, n4);
		assertReporting(false, () -> spy.wellFormed(dr));
	}
	
	// Note: since this implementation is a singly-linked list, this test doesn't really have a
	// way that it should fail unless the programmer does something very unexpected. So it most
	// likely is not necessary to leave in.
	public void testX08() { // cut into the middle of the list
		LinkedPartSeq.Spy.Node t = spy.newNode(f4, t1, null);
		LinkedPartSeq.Spy.Node n4 = spy.newNode(f5, t4, t);
		LinkedPartSeq.Spy.Node n3 = spy.newNode(f2, t1, n4);
		LinkedPartSeq.Spy.Node n2 = spy.newNode(f3, t3, n3);
		LinkedPartSeq.Spy.Node n1 = spy.newNode(f1, t3, n2);
		LinkedPartSeq.Spy.Node n0 = spy.newNode(f2, t5, n1);
		
		spy.setNext(n0, n1);
		dr = spy.create(n2, 4, null, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
	// Note: since this implementation is a singly-linked list, this test doesn't really have a
	// way that it should fail unless the programmer does something very unexpected. So it most
	// likely is not necessary to leave in.
	public void testX09() { // multiple start points
		LinkedPartSeq.Spy.Node t = spy.newNode(f5, t5, null);
		LinkedPartSeq.Spy.Node n4 = spy.newNode(f4, t4, t);
		LinkedPartSeq.Spy.Node n3 = spy.newNode(f3, t3, n4);
		LinkedPartSeq.Spy.Node n2 = spy.newNode(f2, t2a, n3);
		LinkedPartSeq.Spy.Node c0 = spy.newNode(f1, t1, n3);
		LinkedPartSeq.Spy.Node b1 = spy.newNode(f1, t3, n2);
		LinkedPartSeq.Spy.Node b0 = spy.newNode(f1, t1, b1);
		LinkedPartSeq.Spy.Node a1 = spy.newNode(f1, t3, n2);
		LinkedPartSeq.Spy.Node a0 = spy.newNode(f1, t1, a1);
		
		dr = spy.create(n2, 4, t, t);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(a0, 6, t, t);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(b0, 6, t, t);
		assertReporting(true, () -> spy.wellFormed(dr));
		dr = spy.create(c0, 4, t, t);
		assertReporting(true, () -> spy.wellFormed(dr));
	}
	
}
