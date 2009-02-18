package junit;


import subsystem.*;
import java.util.ArrayList;

import junit.framework.TestCase;

public class EventDatabaseTest extends TestCase {

	public void testAddEvent() {
		
		EventDatabase database = new EventDatabase();
		Event myEvent = new Event( Period.parse("2009.02.07:03.00-2009.02.07:05.00"), "test event", "test description", "0");
		Event temp = database.addEvent( myEvent );
		assertNotNull("the event returned is null", temp);
		assertNotNull("the xml is null", database.toXML());
		assertEquals("the two object are not equal", database.toXML(), temp.toXML());
		assertTrue( "Event is not added", database.added( temp ));
		
		
	}
	
	public void testModifyEvent() {
		
		EventDatabase database = new EventDatabase();
		Event myEvent = new Event( Period.parse("2009.02.07:03.00-2009.02.07:05.00"), "test event", "test description", "0");
		Event temp = database.addEvent( myEvent );
		myEvent.setPeriod(Period.parse("2009.02.07:04.00-2009.02.07:06.00"));
		myEvent.setUID(2009020700);
		Event temp2 = database.updateEvent( myEvent );
		Event temp3 = new Event( Period.parse("2009.02.07:04.00-2009.02.07:06.00"), "test event", "test description", "0");
		//System.out.println( temp2.toXML() + "\n");
		//System.out.println( temp3.toXML() );
		//System.out.println( database.toXML() );
		assertEquals( "events are not equal.", temp2.toString(), temp3.toString());
		
	}
	
	public void testDeleteEvent() {
		
		EventDatabase database = new EventDatabase();
		Event myEvent = new Event( Period.parse("2009.02.07:03.00-2009.02.07:05.00"), "test event", "test description", "0");
		Event temp = database.addEvent( myEvent );
		assertNotNull( "null", database.toXML() );
		database.deleteEvent(temp);
		assertEquals("not same", "", database.toXML() );
		
	}
	
	public void testRecurringEvent() {
		
		EventDatabase database = new EventDatabase();
		Event myEvent = new Event( Period.parse("2009.02.07:03.00-2009.02.14:05.00"), "test event", "test description", "[1]");
		Event temp = database.addEvent(myEvent);
		ArrayList<Event> list = database.getEvents( Period.parse("2009.02.06:03.00-2009.02.15:05.00"));
		assertEquals("number of event is wrong", 8, list.size());
		
		database.deleteEvent( temp );
		
		myEvent = new Event( Period.parse("2009.02.07:03.00-2009.02.14:05.00"), "test event", "test description", "[2]");
		temp = database.addEvent(myEvent);
		list = database.getEvents( Period.parse("2009.02.06:03.00-2009.02.15:05.00"));
		assertEquals("number of event is wrong", 4, list.size());
		
		database.deleteEvent(temp);
		
		myEvent = new Event( Period.parse("2009.02.07:03.00-2009.02.20:05.00"), "test event", "test description", "[-2]");
		temp = database.addEvent(myEvent);
		list = database.getEvents( Period.parse("2009.02.06:03.00-2009.02.20:05.00"));
		assertEquals("number of event is wrong", 2, list.size() );
		
		
	}
	
	public void testExist() {
		
		EventDatabase database = new EventDatabase();
		Event myEvent = new Event( Period.parse("2009.02.07:03.00-2009.02.07:05.00"), "test event", "test description", "0");
		Event temp = database.addEvent( myEvent );
		Event temp2 = database.addEvent( myEvent );
		ArrayList<Event> list = database.getEvents( Period.parse("2009.02.06:03.00-2009.02.07:05.00"));
		
		for (Event x : list) {
			
			System.out.println( x.toString() );
			
		}
		
		//assertEquals( "not equal", 1, list.size() );
		
	}
	
	public void testGetEvents() {
		
		EventDatabase database = new EventDatabase();
		Event myEvent = new Event( Period.parse("2009.02.07:03.00-2009.02.14:05.00"), "test event", "test description", "[1]");
		Event temp = database.addEvent(myEvent);
		ArrayList<Event> list = database.getEvents( Period.parse("2009.02.06:03.00-2009.02.15:05.00"));
		assertEquals("number of event is wrong", 8, list.size());
		
	}
	
	public void testToXML() {
		
	}
	
}
