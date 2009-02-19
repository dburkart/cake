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
 * Holds a general time, in hours and minutes and provides some simple methods
 * for manipulating time. Assumes military time.
 * 
 * @author Dana Burkart
 */
public class SimpleTime {
	public int hour; /**The hour that this SimpleTime represents.*/
	public int minutes; /**The minutes that this SimpleTime represents*/
	
	/**Default constructor; both fields 0.
	 * 
	 */
	public SimpleTime() {
		hour=0;
		minutes=0;
	}
	
	/**
	 * Overloaded constructor, takes hour and minutes.
	 * 
	 * @param hour the hour
	 * @param minutes minutes
	 */
	public SimpleTime(int hour, int minutes) {
		this.hour = hour;
		this.minutes = minutes;
	}
	
	/**Copy constructor
	 * 
	 * @param t The SimpleTime to copy.
	 */
	public SimpleTime(SimpleTime t){
		this.hour = t.hour;
		this.minutes = t.minutes;
	}
	
	/**Checks to make sure that this SimpleTime is a valid time.
	 * 
	 * @return true if this SimpleTime is valid, false otherwise.
	 */
	public boolean isValid() {
		
		return hour <= 23 && hour >= 0 && minutes <= 59  && minutes >= 0;
		
	}
	
	/**
	 * Parses a 'time-stamp'
	 * 
	 * @param s the 'time-stamp' to parse
	 * @return a SimpleTime representation of the 'time-stamp'
	 */
	public static SimpleTime parse(String s) {
		String h = s.substring(0, s.indexOf('.'));
		String m = s.substring(s.indexOf('.') + 1);
		return new SimpleTime(Integer.parseInt(h), Integer.parseInt(m));
	}
	
	/**
	 * Fromats the SimpleTime into a 'time-stamp'
	 * 
	 * @return the formatted 'time-stamp'
	 */
	public String format() {
		return new String("") + hour + "." + minutes;
	}
	
	/**
	 * Overloaded toString method, prints out the time in HH:MM form.
	 */
	public String toString() {
		String h = hour + "";
		String p = " am";
		if (hour >= 12) {
			p = " pm";
			if (hour != 12) h = hour % 12 + "";
		}
		String m = minutes + "";
		if (minutes < 10) m = "0"+m;
		return new String("") + h + ":" + m + p;
	}
} //-- SimpleTime