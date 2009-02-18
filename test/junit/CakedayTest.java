package junit;

import subsystem.*;
import junit.framework.TestCase;

public class CakedayTest extends TestCase {
	public void testDoomsday() {
		@SuppressWarnings("unused")
		CakeCal cal = new CakeCal();
		SimpleDate a = new SimpleDate(1,1,1988), b = new SimpleDate(2,29,2004), c = new SimpleDate(3,1,2008), d = new SimpleDate(12,31,2012);
		int n;
		n = Cakeday.getDayOfWeek(a);
		assertEquals("Fucked up on Jan. 1, 1988", 5, n);
		n = Cakeday.getDayOfWeek(b);
		assertEquals("Fucked up on Feb. 29, 2004", 0, n);
		n = Cakeday.getDayOfWeek(c);
		assertEquals("Fucked up on Mar. 1, 2008", 6, n);
		n = Cakeday.getDayOfWeek(d);
		assertEquals("Fucked up on Dec. 31, 2012", 1, n);
		
		int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int[] monthsLeap = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		
		int[] testCase1 = cal.getMonths(2009);
		int[] testCase2 = cal.getMonths(2008);
		
		for( int i = 0; i < 12; i++ ) {
			assertEquals( "Wrong months dude", testCase1[i], months[i] );
			assertEquals( "Wrong months dude", testCase2[i], monthsLeap[i] );
		}
		
	}
}
