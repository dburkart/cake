/*
 * Cake Calendar
 * Copyright (C) 2009  Hashem Assayari, Dana Burkart, Vladimir Hadzhiyski, 
 * Jack Zhang
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package subsystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to store, manage and organize all the Event objects
 * that are created in the calendar program.
 * All Events are stored in an array that represents the day in which they
 * occurr.
 * Each day array is stored as an entry in a hash map with keys in the
 * following format: YYYYMMDD.
 * each day array will have a capacity of 15 elements.
 * 
 * @author Hashem Assayari
 * @author Dana Burkart
 * @author Robert Middleton
 */
class EventDatabase {

	private HashMap<Integer, ArrayList<Event>> eventMap = 
		new HashMap<Integer, ArrayList<Event>>();

	/**
	 * constructor
	 *
	 */
	EventDatabase() {

	} //EventDatabase

	/**
	 * This method adds a new Event object to the List of Event that
	 * occurs on the same date. if the list does not exist for that
	 * day, a new list is made and the event should be added to that
	 * list and then the list should be added to the map with a key
	 * format of YYYYMMDD
	 * 
	 * @param event the event to be added to the database
	 * @return 		the event added to the map to verify the success of 
	 * 				the process. Also, the event is returned with an 
	 * 				allocated UID.
	 */
	Event addEvent(Event event) {
		event = new Event(event);
		//assert event.isValid();
		int key = event.getRecurring().get(0).toInt();
		//System.out.println( "add key: " + key );
		event.setUID(key * 100);
		ArrayList<Event> t1 = eventMap.get(key);
		if ( t1 != null) {

			event.setUID(event.getUID() + t1.size());
			t1.add(event);
			eventMap.put( key, t1);

		} else {
			
			ArrayList<Event> newList = new ArrayList<Event>();
			newList.add( event );
			eventMap.put( key, newList);
			
		}

		for ( int x = 1; x < event.getRecurring().size() - 1; x++ ) {

			key = event.getRecurring().get(x).toInt();

			if ( eventMap.containsKey(key) ) {

				ArrayList<Event> temp = eventMap.get(key);

				if ( !this.exist(temp, event) ) {

					temp.add(event);
					eventMap.put( key, temp);

				} //end if exist

			} else {
				ArrayList<Event> newList = new ArrayList<Event>();
				newList.add( event );
				eventMap.put( key, newList);
			} // end if contains

		} //end for

		return event;
	} //end addEvent


	/**
	 * This function checks if an event already exists in a particular day(ArrayList)
	 * 
	 * @param temp   the ArrayList that represents the day
	 * @param event  the event object to be searched in the day
	 * @return       true if the event exists in the ArrayList, otherwise false
	 */
	boolean exist( ArrayList<Event> temp, Event event) {

		if (temp.contains(event)) {
			System.out.println("exist");
			return true;
		}
		else
			return false;
	} // end exist


	/**
	 * This method deletes an event from the database given a key that
	 * represents the date and the event itself.
	 * 
	 * @param event the event to be deleted from the database
	 * @return true if the deletion was success, otherwise false.
	 */
	boolean deleteEvent( Event event ) {
		
		System.out.println(event.getUID()+ "=EVENT UID");
			if (event.getUID() != 0) {
				if ( event.getRecurringType().equals("0") || event.getRecurringType().equals(null) ) {
					int key = event.getUID()/100;
					ArrayList<Event> temp = eventMap.get(key);
					temp.set( (event.getUID() % 100), new Event()); // DON'T CHANGE THIS. IT'S JUST SETTING THE EVENT INTO AN EMPTY SPACE
					eventMap.put(key, temp);
				} else {
					
					int u = event.getUID();
					ArrayList<SimpleDate> list = event.getRecurring();
					ArrayList<Event> dayEvent;
					for ( SimpleDate d : list) {
						
						dayEvent = eventMap.get( d.toInt() );
						
						if ( dayEvent != null) {
						
							for ( int i = 0; i < dayEvent.size(); i++ ) {

								if ( dayEvent.get(i).getUID() == u ) {

									dayEvent.set(i, new Event() );
									eventMap.put(d.toInt() , dayEvent);
									break;

								}

							}

						}
						
					}
					
				}
				
				
			} else {
				System.err.println("You are trying to delete an event that doesn't have a UID\n" 
						+ " associated with it which makes deleteing impossible.\n"
						+ "Error in EventDatabase:deleteEvent:Line 133" );
				
			} //end if else UID != 0

		return true;
		
	} //end deleteEvent

	/**Modify the selected event.  Makes a copy of this event and deletes the original.
	 * 
	 * @param ev The event to change
	 * @return The event added to the database
	 */
	Event updateEvent( Event ev) {

		Event temp = new Event(ev);
		deleteEvent(ev);
		return addEvent(temp);

	}

	/**
	 * a function to get events that are in multiple days given a starting day and ending at 31
	 * 
	 * @param myList      the list that saves all the event to be returned to the GUI
	 * @param startDate   the start day
	 */
	void getMonth( ArrayList<Event> myList, SimpleDate startDate ) {

		this.getMonth(myList, startDate, 31);

	}

	/**
	 * a function to get events that are in multiple days given the start day and the end day
	 * 
	 * @param myList      the list that saves all the event to be returned to the GUI
	 * @param start       the start day
	 * @param endDay      the end day
	 */
	void getMonth( ArrayList<Event> myList, SimpleDate start, int endDay) {

		int startDay = start.day;
		int startYear = start.year;
		int startMonth = start.month;

		for ( int i = startDay; i <= endDay; i++) {

			int key = ( ( (startYear * 100) + startMonth ) * 100 ) + i;

			ArrayList<Event> tempList;
			if ((tempList = eventMap.get( key ) ) != null )
				for (Event t : tempList) {
					if ( !t.equals(new Event() ) )
						myList.add( t );
				}
		}


	}


	/**
	 * This method returns an array of ArrayLists for all the event that occurs
	 * on the given period. each element in the list is an ArrayList that
	 * represents a day.
	 * 
	 * @param   p	the period in which all events should be returned.
	 * @return	    an array of ArrayLists for all event occurring on the given period.
	 */
	ArrayList<Event> getEvents ( Period p ) {

		// we need to figure out how would we break the period
		// into keys given that not all months have the same
		// number of days.

		ArrayList<Event> myEvent = new ArrayList<Event>();
		int startYear = p.start.date.year;
		int startMonth = p.start.date.month;
		//@SuppressWarnings("unused")
		int startDay = p.start.date.day;
		int endYear = p.end.date.year;
		int endMonth = p.end.date.month;
		int endDay = p.end.date.day;

		//System.out.println("Period formatted: " + p.format());
		ArrayList<Event> evAdded = new ArrayList<Event>();//events already added

		for( int year = startYear; year <= endYear; year++){

			for( int month = startMonth; month <= endMonth; month++){

				for ( int i = 0; i <= endDay; i++) {

					int key = ( ( (startYear * 100) + month ) * 100 ) + i;
					//int key = ((((startYear * 100)+startMonth) * 100) * 100  ) + i;
					//System.out.println("KEY: " + key);

					ArrayList<Event> tempList;
					if ((tempList = eventMap.get( key ) ) != null )
						for (Event t : tempList) {
							if (  !(t.getTitle().equals(""))  ){
								for( int x = 0; x < t.getRecurring().size(); x++){
									Event temp = new Event( t );
									Period ap = new Period( t.getPeriod() );
									ap.start = new SimpleDateTime(t.getRecurring().get(x),t.getPeriod().start.time);
									ap.end = new SimpleDateTime(t.getRecurring().get(x),t.getPeriod().end.time);
									temp.setPeriod(ap);
									if( !added(evAdded,temp) && 
											temp.getPeriod().start.date.day  >= startDay &&
											temp.getPeriod().end.date.day <= endDay &&
											temp.getPeriod().start.date.month >= month &&
											temp.getPeriod().end.date.month <= month  &&
											temp.getPeriod().start.date.year >= year && 
											temp.getPeriod().end.date.year <= year){
										myEvent.add( temp );
										evAdded.add(temp);
									}

								}
								//System.out.println("added to array " + i + " - " + t);
							}

						}
				}
			}
		}


		//
		//
		//  MAKE SURE TO RETURN IN AN ARRAY LIST OF EVENTS FOR RECURRING EVENTS
		//
		//


		return myEvent;
	}

	/**Checks to see if an event in this event list has already been added.  True if it has.
	 * 
	 * @param eventList The array list to search the event for
	 * @param e The event to search for
	 * @return true if the event exists in the array; false otherwise
	 */
	public boolean added(ArrayList<Event> eventList, Event e){
		for( int x = 0; x < eventList.size(); x++){
			if( eventList.get(x).equals(e)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 
	 */
	public boolean added( Event e ) {
		
		int UID = e.getUID();
		ArrayList<Event> temp = eventMap.get( UID / 100 );
		if ( temp.get(UID % 100).equals(e))
			return true;
		else
			return false;
			
	}


	/**
	 * Accessor method to the eventMap private variable
	 * 
	 * @param map
	 */
	void setEventDatabase ( HashMap<Integer, ArrayList<Event>> map ) {
		eventMap = map;
	}




	@SuppressWarnings("unchecked")
	/**
	 * this function formats the events so that it can be easily populated in many softwares
	 * 
	 * @return String  a string that hold all the event information in XML format
	 */
	String toXML() {
		String xml = "";

		Set entrySet = eventMap.entrySet();
		Iterator iter = entrySet.iterator();
		ArrayList<Event> added = new ArrayList<Event>();
		
		while( iter.hasNext() ) {
			Map.Entry entry = (Map.Entry)iter.next();
			ArrayList<Event> list = (ArrayList<Event>) entry.getValue();
			
			for (Event e : list) {
				//if ( e.isValid() && !e.equals(null) )
				if ( !added.contains(e) )
				{
					xml += e.toXML();
					added.add(e);
				}
			}

		}

		return xml;
	}


	/**
	 * just for testing
	 * 
	 * @return true if the test went perfect, otherwise false
	 */
	boolean test() {
		try {
			EventDatabase db = new EventDatabase();

			SimpleDate temp[] = new SimpleDate[52];

			for ( int i = 0; i < 52; i++) {
				temp[i] = new SimpleDate(05, i+1, 2008);
			}

			System.out.println("0");

			SimpleDateTime s1 = new SimpleDateTime(temp[0], new SimpleTime(4, 30));
			SimpleDateTime e1 = new SimpleDateTime(temp[0], new SimpleTime(6, 31));

			System.out.println("0.5");

			//Period p = new Period (s, e);
			Period p1 = new Period (s1, e1);
			Period p2 = Period.parse("2008.01.01:01.01-2009.01.01:01.01");
			//Period p3 = new Period(s2, e2);

			System.out.println("0.75");

			//Event[] myEvent = new Event[52];


			Event event = new Event( p2, "test", "just for testing", "30" );
			Event event1 = new Event( p1, "test1", "just for testing1", "0" );
			//Event event2 = new Event( p3, "test2", "just for testing2", "0" );

			System.out.println("1");
			try{
				db.addEvent( event1 );
				db.addEvent( event );
			}
			catch(Exception baal){
				System.out.println("OMG HAI LOOK EXCEPTOIN PLZ 2 FIX KTHANX");
				baal.printStackTrace();
			}
			System.out.println("1.5");
			//b.addEvent( event2 );
			//db.modifyEvent(event1 , new Event( Period.parse("2008.05.02:6:30-2008.05.02:08.30"), event1.title, event1.description, "0"));
			try{
				db.deleteEvent( event );
			}
			catch(Exception balh){
				System.out.println("EXCEPTIO WO+HHHHHH");
				balh.printStackTrace();
			}
			//db.modifyEvent(event, new Event(event.period, event.title, event.description, "29"));

			//System.out.println( toXML() );

			System.out.println("2");

			//ArrayList<Event> list = db.getEvents( Period.parse("2008.01.01:01.01-2009.12.28:01.01") );
			try{
				System.out.println();
				System.out.println("Event Map:");
				System.out.println(db.eventMap);
				System.out.println();
			}
			catch(Exception anubis){
				System.out.println("OMG WHOA ERROR ");
				anubis.printStackTrace();
			}

			System.out.println("2.5");

			//System.out.println( list );
			//System.out.println( list.get(50) );

			System.out.println("3");

			//db.deleteEvent(event);
			//list = db.getEvents( p2 );

			System.out.println("4");

			/*
			if (list.isEmpty()) {
				System.out.println("empty");
			} else {
				System.out.println("not empty");
				for ( Event e : list ) {

					if (list.get(i).UID == 0)
						System.out.println( "event" + i + " is empty" );
					else
						System.out.println(list.get(i));


				}

			}
			 */
			if (db.toXML() != null) {
				System.out.println("attempt to print");
				System.out.println( db.toXML() );
			}
			else
				System.out.println( "xml is empty." );
			System.out.println("Good Bye...");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("here");
			return false;
		}

		return true;
	}
} //EventDatabase
