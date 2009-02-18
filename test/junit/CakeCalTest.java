package junit;

import subsystem.*;
import junit.framework.TestCase;
import java.io.*;



public class CakeCalTest extends TestCase {

	CalendarSettings comparison = new CalendarSettings();

	CakeCal cal = new CakeCal();
	
	
	public void testSettings() {
		comparison.setName( "New Calendar");
		assertEquals( "loaded Wrong Settings", comparison.getName(), cal.getSettings().getName());
		
		cal.setSettings(comparison);
		assertEquals( "settings have been modified", comparison, cal.getSettings());
	}
	
	public void testModifications() {
		cal = new CakeCal();
		assertEquals( "Nothing should be happening", false, cal.modified);
		
		comparison.setName( "dsafhoi" );
		cal.setSettings(comparison);
		assertEquals( "Something should be happening", true, cal.modified);
		cal.loadCal("sdaf.sdf");
		assertEquals( "incorrect loading should stay true", true, cal.modified);
		
		cal = new CakeCal();
		assertEquals( "Nothing should exist", false, cal.modified );
		
	}
	
	public void testSaveandLoad() {
		comparison.setName( "dsfkhj" );
		assertEquals( "Shouldn't be modified", false, cal.modified);
		cal = new CakeCal();
		
		CakeCal control = new CakeCal();
		
		cal.setSettings(comparison);
		
		assertEquals( "Modification should stay true", true, cal.modified);
		assertNotSame( "Shouldn't be same", control.getSettings().getName(), cal.getSettings().getName());
		try {
		cal.saveCal("allure");
		} catch (Exception e) {
			
		}
		
		assertEquals( "modified variable should change", false, cal.modified);
		assertNotSame( "Shouldn't be same", control, cal);
		
		control.loadCal("allure.cml");
		
		assertEquals( "Should be the same", control.getSettings().getName(), cal.getSettings().getName());
		
		try {
			cal.saveCal( "hobo.cml" );
		} catch (Exception e) {
		
		}
		
		assertEquals( " shouldn't have saved as .cml.cml", true, cal.loadCal( "hobo.cml" ));


	}
	
	public void testDoomsday() {
		
		CakeCal cal = new CakeCal();
		SimpleDate a = new SimpleDate(1,1,1988), b = new SimpleDate(2,29,2004), c = new SimpleDate(3,1,2008), d = new SimpleDate(12,31,2012);
		int n;
		n = CakeCal.getDayOfWeek(a);
		assertEquals("Fucked up on Jan. 1, 1988", 5, n);
		n = CakeCal.getDayOfWeek(b);
		assertEquals("Fucked up on Feb. 29, 2004", 0, n);
		n = CakeCal.getDayOfWeek(c);
		assertEquals("Fucked up on Mar. 1, 2008", 6, n);
		n = CakeCal.getDayOfWeek(d);
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
