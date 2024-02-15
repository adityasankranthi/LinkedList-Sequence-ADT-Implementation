import edu.uwm.cs351.LinkedPartSeq;
import edu.uwm.cs351.Part;

public class TestLinkedPartSeq extends AbstractTestRobot {

	public TestLinkedPartSeq() {
	}

	protected LinkedPartSeq self;
	
	@Override
	protected void initRobot() {
		r = self = new LinkedPartSeq();
	}

	
	/// testS0x: testing the empty sequence
	
	public void testS00() {
		assertEquals(0, self.size());
	}
	
	public void testS01() {
		Part p1 = new Part("p1");
		assertEquals("IllegalStateException", asString(() -> self.addBefore(p1)));
	}
	
	public void testS02() {
		assertException(IllegalStateException.class, () -> self.addAfter(new Part()));
	}
	
	public void testS04() {
		assertFalse(self.isCurrent());
	}
	
	public void testS05() {
		assertException(IllegalStateException.class, () -> self.getCurrent());
	}
	
	public void testS06() {
		self.start();
		assertFalse(self.isCurrent());
	}
	
	public void testS07() {
		self.start("arm");
		assertEquals(0, self.size());
	}
	
	
	/// testS1x: testing addBefore
	
	public void testS10() {
		self.start(null);
		assertException(IllegalStateException.class, () -> self.addBefore(new Part()));
	}
	
	public void testS11() {
		self.start("arm");
		self.addBefore(new Part("#1"));
		assertEquals(1, self.size());
	}
	
	public void testS12() {
		self.start("leg");
		self.addBefore(new Part("#2"));
		assertTrue(self.isCurrent());
	}
	
	public void testS13() {
		self.start("leg");
		self.addBefore(new Part("#3"));
		assertEquals(new Part("#3"), self.getCurrent());		
	}
	
	public void testS14() {
		self.start("head");
		self.addBefore(new Part("#4"));
		self.addBefore(new Part("14"));
		assertEquals(2, self.size());
	}
	
	public void testS15() {
		self.start("arm");
		self.addBefore(new Part("#5"));
		self.start("leg");
		self.addBefore(new Part("15"));
		assertEquals(new Part("15"), self.getCurrent());
	}
	
	public void testS16() {
		self.start("arm");
		self.addBefore(new Part("6"));
		self.start("leg");
		self.addBefore(new Part("16"));
		self.start("arm");
		self.addBefore(new Part("#6"));
		assertEquals(3, self.size());
		assertEquals(new Part("#6"), self.getCurrent());
	}
	
	public void testS17() {
		self.start("arm");
		self.addBefore(new Part("7"));
		self.start("leg");
		self.addBefore(new Part("17"));
		self.start("arm");
		assertEquals(new Part("7"), self.getCurrent());
	}
	
	public void testS18() {
		self.start("leg");
		self.addBefore(new Part("8"));
		self.start(null);
		assertException(IllegalStateException.class, () -> self.addBefore(new Part("18")));
	}
	
	public void testS19() {
		self.start("leg");
		self.addBefore(new Part("#9"));
		self.start("arm");
		self.addBefore(new Part("9"));
		self.start(null);
		assertEquals(new Part("9"), self.getCurrent());
	}
	
	
	/// testS2x: testing advance
	
	public void testS20() {
		assertException(IllegalStateException.class, () -> self.advance());
	}
	
	public void testS21() {
		self.start("arm");
		assertException(IllegalStateException.class, () -> self.advance());		
	}
	
	public void testS22() {
		self.start("leg");
		self.addBefore(new Part("22"));
		self.advance();
		assertFalse(self.isCurrent());
	}
	
	public void testS23() {
		self.start("arm");
		self.addBefore(new Part("23"));
		self.advance();
		assertException(IllegalStateException.class, () -> self.advance());		
	}
	
	public void testS24() {
		self.start("leg");
		self.addBefore(new Part("24"));
		self.addBefore(new Part("#4"));
		self.advance();
		assertEquals(new Part("24"), self.getCurrent());
	}
	
	public void testS25() {
		self.start("arm");
		self.addBefore(new Part("25"));
		self.start("leg");
		assertException(IllegalStateException.class, () -> self.advance());		
	}
	
	public void testS26() {
		self.start("arm");
		self.addBefore(new Part("#6"));
		self.addBefore(new Part("26"));
		self.start(null);
		self.advance();
		assertEquals(new Part("#6"), self.getCurrent());
	}
	
	public void testS27() {
		self.start("leg");
		self.addBefore(new Part("#7"));
		self.addBefore(new Part("27"));
		self.start("arm");
		self.addBefore(new Part("S27"));
		self.advance();
		assertFalse(self.isCurrent());
	}
	
	public void testS28() {
		self.start("leg");
		self.addBefore(new Part("8"));
		self.start("arm");
		self.addBefore(new Part("28"));
		self.start("leg");
		self.addBefore(new Part("#8"));
		self.advance();
		assertEquals(new Part("8"), self.getCurrent());
	}
	
	public void testS29() {
		self.start("arm");
		self.addBefore(new Part("9"));
		self.start("leg");
		self.addBefore(new Part("29"));
		self.start("arm");
		self.addBefore(new Part("#9"));
		self.start("leg");
		self.advance();
		assertFalse(self.isCurrent());	
		self.addBefore(new Part("S29")); // where does this go?
		self.start();
		// toString of result, or null, or exception name
		assertEquals("Part(S29)", asString(() -> self.getCurrent()));
	}
	
	
	/// testS3x: tests of addAfter
	
	public void testS30() {
		self.start(null);
		assertException(IllegalStateException.class, () -> self.addAfter(new Part()));
	}
	
	public void testS31() {
		self.start("head");
		self.addAfter(new Part("31"));
		assertEquals(1, self.size());
	}
	
	public void testS32() {
		self.start("leg");
		self.addAfter(new Part("32"));
		assertEquals(new Part("32"), self.getCurrent());
	}
	
	public void testS33() {
		self.start("arm");
		self.addAfter(new Part("33"));
		self.addAfter(new Part("#3"));
		assertEquals(new Part("#3"), self.getCurrent());
	}
	
	public void testS34() {
		self.start("arm");
		self.addAfter(new Part("#4"));
		self.addAfter(new Part("34"));
		self.advance();
		assertFalse(self.isCurrent());
	}
	
	public void testS35() {
		self.start("leg");
		self.addAfter(new Part("#5"));
		self.advance();
		self.addAfter(new Part("35"));
		self.start(null);
		assertEquals(new Part("#5"), self.getCurrent());
	}
	
	public void testS36() {
		self.start("arm");
		self.addAfter(new Part("36"));
		self.start("head");
		self.addAfter(new Part("#6"));
		self.advance();
		assertFalse(self.isCurrent());
	}
	
	public void testS37() {
		self.start("leg");
		self.addAfter(new Part("37"));
		self.start("arm");
		self.addBefore(new Part("#7"));
		self.addAfter(new Part("S37"));
		self.start(null);
		assertEquals(new Part("#7"), self.getCurrent());
	}
	
	public void testS38() {
		self.start("head");
		self.addAfter(new Part("H8"));
		self.start("arm");
		self.addBefore(new Part("38"));
		self.advance();
		self.addAfter(new Part("#8"));
		self.advance();
		assertFalse(self.isCurrent());
	}
	
	public void testS39() {
		self.start("head");
		self.addAfter(new Part("S39"));
		self.start("leg");
		self.addBefore(new Part("#9"));
		self.advance();
		self.addAfter(new Part("39"));
		self.start("leg");
		self.addAfter(new Part("9"));
		self.start(null);
		assertEquals(new Part("#9"), self.getCurrent());
		self.advance();
		assertEquals(new Part("9"), self.getCurrent());
		self.advance();
		assertEquals(new Part("S39"), self.getCurrent());
		self.advance();
		assertEquals(new Part("39"), self.getCurrent());
		self.advance();
		assertException(IllegalStateException.class, () -> self.getCurrent());
	}

	
	/// testS4x: tests of removeCurrent
	
	public void testS40() {
		assertException(IllegalStateException.class, () -> self.removeCurrent());
	}
	
	public void testS41() {
		self.start();
		assertException(IllegalStateException.class, () -> self.removeCurrent());
	}
	
	public void testS42() {
		self.start("leg");
		self.addAfter(new Part("2"));
		self.removeCurrent();
		assertException(IllegalStateException.class, () -> self.getCurrent());
		assertEquals(0, self.size());
	}
	
	public void testS43() {
		self.start("arm");
		self.addAfter(new Part("3"));
		self.addAfter(new Part("#3"));
		self.removeCurrent();
		assertException(IllegalStateException.class, () -> self.getCurrent());
		assertEquals(1, self.size());
	}
	
	public void testS44() {
		self.start("arm");
		self.addAfter(new Part("4"));
		self.addAfter(new Part("#4"));
		self.start("arm");
		self.removeCurrent();
		assertEquals("Part(#4)", asString(() -> self.getCurrent()));
		assertEquals(1, self.size());
	}
	
	public void testS45() {
		self.start("head");
		self.addAfter(new Part("5"));
		self.start("arm");
		self.addAfter(new Part("45"));
		self.addAfter(new Part("S45"));
		self.start();
		self.removeCurrent();
		self.removeCurrent();
		assertEquals(new Part("S45"), self.getCurrent());
		assertEquals(1, self.size());
	}
	
	public void testS46() {
		self.start("arm");
		self.addAfter(new Part("6"));
		self.start("leg");
		self.addAfter(new Part("46"));
		self.start("arm");
		self.advance();
		self.addAfter(new Part("#6"));
		self.start("arm");
		self.removeCurrent();
		assertEquals(new Part("#6"), self.getCurrent());
		assertEquals(2, self.size());
	}
	
	public void testS47() {
		self.start("head");
		self.addAfter(new Part("47"));
		self.start("arm");
		self.addBefore(new Part("7"));
		self.advance();
		self.addAfter(new Part("#7"));
		self.start("head");
		self.advance();
		self.addAfter(new Part("S47"));
		self.start("arm");
		self.removeCurrent();
		self.removeCurrent();
		assertException(IllegalStateException.class, () -> self.getCurrent());
		assertEquals(2, self.size());
	}
	
	public void testS48() {
		self.start("head");
		self.addAfter(new Part("S48"));
		self.start("leg");
		self.addBefore(new Part("8"));
		self.advance();
		self.addAfter(new Part("#8"));
		self.start("head");
		self.addBefore(new Part("48"));
		self.removeCurrent();
		assertEquals(new Part("S48"), self.getCurrent());
		assertEquals(3, self.size());
	}
	
	public void testS49() {
		self.start("arm");
		self.addBefore(new Part("S"));
		self.start("leg");
		self.addBefore(new Part("4"));
		self.advance();
		self.addAfter(new Part("9"));
		self.start();
		self.advance();
		self.removeCurrent();
		assertEquals(new Part("9"), self.getCurrent());
	}
	
	
	/// testS5x: tests of clone
	
	LinkedPartSeq copy;
	
	private class MyDynamicArraySeq extends LinkedPartSeq {
		public MyDynamicArraySeq() {
			super();
		}
	}
	
	public void testS50() {
		self = new MyDynamicArraySeq();
		self.start("arm");
		self.addAfter(new Part("0"));
		copy = self.clone();
		// If the following fails, you are not using the result of super.clone()
		assertTrue(copy instanceof MyDynamicArraySeq);
	}
	
	public void testS51() {
		self.start("arm");
		self.addAfter(new Part("1"));
		self.addBefore(new Part("#1"));
		copy = self.clone();
		assertEquals(new Part("#1"), self.getCurrent());
		assertEquals(new Part("#1"), copy.getCurrent());
	}
	
	public void testS52() {
		self.start("leg");
		self.addAfter(new Part("2"));
		self.start("body");
		self.addAfter(new Part("52"));
		copy = self.clone();
		self.removeCurrent(); // remove from original should not change copy
		assertException(IllegalStateException.class, () -> self.getCurrent());
		assertEquals(1, self.size());
		assertEquals(new Part("52"), copy.getCurrent());
		assertEquals(2, copy.size());
	}
	
	public void testS53() {
		self.start("head");
		self.addAfter(new Part("3"));
		self.addAfter(new Part("53"));
		self.start();
		copy = self.clone();
		copy.removeCurrent(); // remove from copy should not affect original
		assertEquals(new Part("3"), self.getCurrent());
		assertEquals(2, self.size());
		assertEquals(new Part("53"), copy.getCurrent());
		assertEquals(1, copy.size());
	}
	
	public void testS54() {
		self.start("arm");
		self.addAfter(new Part("4"));
		self.addAfter(new Part("#4"));
		self.start("leg");
		self.addAfter(new Part("54"));
		self.start();
		copy = self.clone();
		self.advance(); // advance on original should not change clone
		assertEquals(new Part("#4"), self.getCurrent());
		assertEquals(new Part("4"), copy.getCurrent());
	}
	
	public void testS55() {
		self.start("arm");
		self.addAfter(new Part("5"));
		self.addAfter(new Part("#5"));
		self.start("leg");
		self.addAfter(new Part("55"));
		self.start();
		copy = self.clone();
		copy.advance(); // advance on copy should not change original
		assertEquals(new Part("5"), self.getCurrent());
		assertEquals(new Part("#5"), copy.getCurrent());
	}
	
	public void testS56() {
		self.start("arm");
		self.addAfter(new Part("56"));
		copy = self.clone();
		self.addAfter(new Part("S56")); // add on original after cloning should not affect the clone
		assertEquals(new Part("S56"), self.getCurrent());
		assertEquals(2, self.size());
		assertEquals(new Part("56"), copy.getCurrent());
		assertEquals(1, copy.size());
	}
	
	public void testS57() {
		self.start("arm");
		self.addAfter(new Part("57"));
		copy = self.clone();
		copy.addAfter(new Part("S57")); // add on clone should not affect the original
		assertEquals(new Part("57"), self.getCurrent());
		assertEquals(1, self.size());
		assertEquals(new Part("S57"), copy.getCurrent());
		assertEquals(2, copy.size());
	}
	
	public void testS58() {
		copy = new LinkedPartSeq();
		copy.start("leg");
		copy.addAfter(new Part("58"));
		copy.start("body");
		copy.addBefore(new Part("#58"));
		copy.start("arm");
		copy.addAfter(new Part("S58"));
		copy = self.clone();
		LinkedPartSeq c = copy.clone();
		assertException(IllegalStateException.class, () -> self.getCurrent());
		assertEquals(0, self.size());
		assertException(IllegalStateException.class, () -> c.getCurrent());
		assertEquals(0, c.size());
	}
	
	
	/// testX0x: tests of getPart combined with sequence methods
	
	public void testX00() {
		self.start("arm");
		self.addAfter(new Part("00"));
		self.start("head");
		self.addBefore(new Part("0"));
		self.advance();
		self.addAfter(new Part("#0"));
		self.start("leg");
		self.addAfter(new Part("X00"));
		assertEquals(new Part("X00"), self.getCurrent());
		assertEquals(new Part("0"), self.getPart(null, 0)); // correct traversal when function is null 
		assertEquals(new Part("0"), self.getCurrent()); // getPart changes the current
	}
	
	public void testX01() {
		self.start("arm");
		self.addAfter(new Part("01"));
		self.start("head");
		self.addBefore(new Part("1"));
		self.advance();
		self.addAfter(new Part("#1"));
		self.start("leg");
		self.addAfter(new Part("X01"));
		assertEquals(new Part("X01"), self.getCurrent());
		assertEquals(new Part("01"), self.getPart(null, 1)); // correct traversal when function is null 
		assertEquals(new Part("01"), self.getCurrent()); // getPart changes the current
	}
	
	public void testX02() {
		self.start("arm");
		self.addAfter(new Part("02"));
		self.start("head");
		self.addBefore(new Part("2"));
		self.advance();
		self.addAfter(new Part("#2"));
		self.start("leg");
		self.addAfter(new Part("X02"));
		assertEquals(new Part("X02"), self.getCurrent());
		assertEquals(new Part("X02"), self.getPart(null, 3)); // correct traversal when function is null
		assertEquals(new Part("X02"), self.getCurrent());
	}
	
	public void testX03() {
		self.start("arm");
		self.addAfter(new Part("03"));
		self.start("head");
		self.addBefore(new Part("3"));
		self.advance();
		self.addAfter(new Part("#3"));
		self.start("leg");
		self.addAfter(new Part("X03"));
		assertEquals(new Part("X03"), self.getCurrent());
		assertNull(self.getPart(null, 4)); // correct traversal when function is null (push past the end)
		assertFalse(self.isCurrent()); // if no part is found, then there is no current
	}
	
	public void testX04() {
		self.start("leg");
		self.addAfter(new Part("04"));
		self.start("arm");
		self.addBefore(new Part("4"));
		self.advance();
		self.addAfter(new Part("#4"));
		self.start("head");
		self.addAfter(new Part("X04"));
		assertEquals(new Part("X04"), self.getCurrent());
		assertEquals(new Part("#4"), self.getPart("arm", 1)); // correct traversal when function = a type in the array
		assertEquals(new Part("#4"), self.getCurrent());
	}
	
	public void testX05() {
		self.start("leg");
		self.addAfter(new Part("05"));
		self.start("arm");
		self.addBefore(new Part("5"));
		self.advance();
		self.addAfter(new Part("#5"));
		self.start("head");
		self.addAfter(new Part("X05"));
		assertEquals(new Part("X05"), self.getCurrent());
		assertNull(self.getPart("arm", 2)); // no current (push past the end with type in the array)
		assertFalse(self.isCurrent());
	}
	
	public void testX06() {
		self.start("leg");
		self.addAfter(new Part("6"));
		self.addAfter(new Part("06"));
		self.addAfter(new Part("X06"));
		self.addBefore(new Part("#6"));
		assertEquals(new Part("#6"), self.getCurrent());
		assertNull(self.getPart("body", 0)); // calling getPart when the function is not in the array (will push past the end)
		assertFalse(self.isCurrent()); // no current
	}
	
	public void testX07() {
		self.start("body");
		self.addAfter(new Part("#7"));
		self.start("arm");
		self.addAfter(new Part("7"));
		self.addAfter(new Part("07"));
		self.addAfter(new Part("X07"));
		self.start("arm");
		self.removeCurrent();
		assertEquals(new Part("07"), self.getCurrent());
		assertEquals(new Part("07"), self.getPart(null, 1)); // null still works directly after a removal
		assertEquals(new Part("07"), self.getCurrent());
	}
	
	public void testX08() {
		self.start("body");
		self.addAfter(new Part("#8"));
		self.start("arm");
		self.addAfter(new Part("8"));
		self.addAfter(new Part("08"));
		self.addAfter(new Part("X08"));
		self.start("arm");
		self.removeCurrent();
		assertEquals(new Part("08"), self.getCurrent());
		assertEquals(new Part("08"), self.getPart("arm", 0)); // function = type still works directly after a removal
		assertEquals(new Part("08"), self.getCurrent());
	}
	
	public void testX09() {
		self.start("body");
		self.addAfter(new Part("#9"));
		self.start("arm");
		self.addAfter(new Part("9"));
		self.addAfter(new Part("09"));
		self.addAfter(new Part("X09"));
		self.removeCurrent();
		self.start("leg");
		self.addAfter(new Part("#09")); // remove and then add in the same place
		self.start();
		assertEquals(new Part("#09"), self.getPart(null, 3));
		self.start();
		assertEquals(new Part("#09"), self.getPart("leg", 0)); // two ways of getting to the same part
	}
	
	
	/// testX1x: More tests of getPart 
	
	public void testX10() {
		self.start("arm");
		self.addAfter(new Part("X21"));
		assertEquals(new Part("X21"), self.getPart(null, 0));
		assertException(IllegalStateException.class, () -> self.addBefore(new Part("10")));
	}
	
	public void testX11() {
		self.start("leg");
		self.addAfter(new Part("#1"));
		self.addBefore(new Part("X11"));
		assertNull(self.getPart(null, 2));
		assertException(IllegalStateException.class, () -> self.addBefore(new Part("11")));
	}
	
	public void testX12() {
		self.start("arm");
		self.addAfter(new Part("X12"));
		self.start("leg");
		self.addBefore(new Part("#12"));
		assertEquals(new Part("#12"), self.getPart(null, 0));
		self.advance();
		assertEquals("Part(X12)", asString(() -> self.getCurrent()));
	}
	
	public void testX13() {
		self.start("arm");
		self.addBefore(new Part("X13"));
		self.start("leg");
		self.addAfter(new Part("#13"));
		self.start("arm");
		self.advance();
		self.addAfter(new Part("3"));
		self.start("leg");
		self.getPart("arm", 0);
		self.advance();
		assertEquals(new Part("3"), self.getCurrent());
	}
	
	public void testX14() {
		self.start("leg");
		self.addBefore(new Part("X-A"));
		self.addAfter(new Part("X-B"));
		self.addAfter(new Part("X-C"));
		assertNull(self.getPart(null, 10));
		assertFalse(self.isCurrent());
	}
	
	public void testX15() {
		self.start("head");
		self.addAfter(new Part("#15"));
		assertNull(self.getPart("arm",12));
		self.addBefore(new Part("X15"));
		self.start("arm");
		assertEquals(new Part("X15"), self.getCurrent());
	}

	
	/// testX2x: tests of addPart combined with sequence methods
	
	public void testX20() {
		self.start("body");
		self.addAfter(new Part("0"));
		self.addAfter(new Part("20"));
		assertTrue(self.addPart("arm", new Part("X20")));
		assertEquals(3, self.size());
		assertEquals(new Part("X20"), self.getCurrent());
		assertEquals(new Part("X20"), self.getPart(null, self.size() -1)); // adds to end
	}
	
	public void testX21() {
		self.start("body");
		self.addAfter(new Part("1"));
		self.addAfter(new Part("21"));
		self.start(); // change current back to beginning
		assertTrue(self.addPart("arm", new Part("X21")));
		assertEquals(3, self.size());
		assertEquals(new Part("X21"), self.getCurrent());
		assertEquals(new Part("X21"), self.getPart(null, self.size() -1)); // always adds to end (regardless of current)
	}
	
	public void testX22() {
		self.start("arm");
		self.addAfter(new Part("2"));
		self.start("leg");
		self.addBefore(new Part("22"));
		self.advance();
		self.addAfter(new Part("#22"));
		self.getPart(null, 1); // taking advantage of the fact that getPart changes current
		self.removeCurrent();
		assertTrue(self.addPart("leg", new Part("X22")));
		assertEquals(3, self.size());
		assertEquals(new Part("X22"), self.getPart(null, self.size() -1));
	}
	
	public void testX23() {
		self.addPart("head", new Part("3"));
		self.addPart("head", new Part("#3"));
		self.addPart("leg", new Part("23"));
		self.addPart("arm", new Part("#23"));
		self.start();
		self.removeCurrent();
		self.removeCurrent();
		self.addPart("head", new Part("X23"));
		assertEquals(new Part("23"), self.getPart(null, 0));
		assertEquals(new Part("#23"), self.getPart(null, 1));
		assertEquals(new Part("X23"), self.getPart(null, 2));
		assertEquals(3, self.size());
	}
	
	public void testX24() {
		self.addPart("head", new Part("X24"));
		self.addBefore(new Part("24"));
		self.start("head");
		assertEquals(new Part("24"), self.getCurrent());
		self.advance();
		assertEquals(new Part("X24"), self.getCurrent());
	}
	
	public void testX25() {
		self.start("arm");
		assertException(NullPointerException.class, () -> self.addPart(null, new Part("X25")));
		self.addAfter(new Part("25"));
		assertEquals(new Part("25"), self.getPart("arm", 0));
	}
		
	
	/// testX3x: tests of removePart combined with sequence methods
	
	public void testX30() {
		self.addPart("leg", new Part("30"));
		self.addBefore(new Part("0"));
		self.addPart("head", new Part("#30"));
		self.addPart("arm", new Part("#0"));
		self.addPart("arm", new Part("X30"));
		self.start("arm");
		self.removeCurrent();
		assertEquals(4, self.size());
		assertEquals(new Part("0"), self.removePart(null)); // can remove correctly when function is null
		assertEquals(3, self.size());
		assertEquals(new Part("30"), self.getCurrent());
	}
	
	public void testX31() {
		self.addPart("leg", new Part("31"));
		self.addAfter(new Part("1"));
		self.addPart("head", new Part("#1"));
		self.addPart("head", new Part("#31"));
		self.addPart("head", new Part("X31"));
		self.addPart("arm", new Part("#X31"));
		self.start("head");
		self.advance();
		assertEquals(new Part("#1"), self.removePart("head")); // removes first part of type regardless of current
		assertEquals(new Part("#31"), self.getCurrent());
	}
	
	public void testX32() {
		self.addPart("arm", new Part("1"));
		self.addPart("leg", new Part("2"));
		self.addPart("body", new Part("3"));
		self.addPart("head", new Part("4"));
		self.addPart("arm", new Part("5"));
		self.addPart("leg", new Part("6"));
		self.addPart("head", new Part("7"));
		assertEquals(new Part("1"), self.removePart(null));
		assertEquals(new Part("2"), self.getCurrent());
		self.advance();
		assertEquals(new Part("2"), self.removePart(null));
		assertEquals(new Part("3"), self.getCurrent());
		self.advance();
		assertEquals(new Part("3"), self.removePart(null));
		assertEquals(new Part("4"), self.getCurrent());
		self.getPart("head", 1); // changes current
		assertEquals(new Part("4"), self.removePart("head"));
		assertEquals(new Part("7"), self.getCurrent());
	}
	
	public void testX33() {
		self.start("arm");
		self.removePart("head");
		self.addBefore(new Part("X33"));
		self.start("head");
		assertEquals(new Part("X33"), self.getCurrent());
	}
	
	public void testX34() {
		self.start("leg");
		self.addAfter(new Part("X34"));
		self.addAfter(new Part("4"));
		self.removePart(null);
		assertException(IllegalStateException.class, () -> self.addAfter(new Part("#4")));
	}
}
