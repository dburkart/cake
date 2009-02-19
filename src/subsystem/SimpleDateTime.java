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

/**
 * A data-class that holds a date and a time and provides some basic methods
 * for manipulation.
 * 
 * @author Dana Burkart
 */
public class SimpleDateTime {
	public SimpleDate date;
	public SimpleTime time;
	
	/**Makes a new SimpleDateTime with default values.
	 * 
	 */
	public SimpleDateTime() {
		date=new SimpleDate();
		time=new SimpleTime();
	}
	
	/**A copy constructor.
	 * 
	 * @param ddab The SimpleDateTime to make a copy of.
	 */
	public SimpleDateTime(SimpleDateTime ddab){
		date = new SimpleDate( ddab.date );
		time = new SimpleTime( ddab.time );
	}
	
	/**
	 * Overloaded contructor taking a date and a time.
	 * 
	 * @param date the date
	 * @param time the time
	 */
	public SimpleDateTime(SimpleDate date, SimpleTime time) {
		this.date = date;
		this.time = time;
	}
	
	/**Checks to make sure that this SimpleDateTime is valid; calls each field individually to make sure it is valid.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		
		return date.isValid() && time.isValid();
		
	}
	
	/**
	 * Parses a 'date/time-stamp' and creates a SimpeDateTime object containing
	 * the relevant data.
	 * 
	 * @param s the 'date/time-stamp' to process
	 * @return SimpleDateTime representation of the 'date/time-stamp'
	 */
	public static SimpleDateTime parse(String s) {
		String date = s.substring(0,s.indexOf(':'));
		String time = s.substring(s.indexOf(':') + 1);
		
		return new SimpleDateTime(SimpleDate.parse(date), SimpleTime.parse(time));
	}
	
	/**
	 * Formats the SimpleDateTime object into a 'date/time-stamp'
	 * 
	 * @return formatted date/time-stamp
	 */
	public String format() {
		return new String("") + date.format() + ":" + time.format();
	}
} //-- SimpleDateTime