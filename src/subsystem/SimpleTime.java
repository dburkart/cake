package subsystem;
/**
 * Holds a general time, in hours and minutes and provides some simple methods
 * for manipulating time. Assumes military time.
 * 
 * @author Dana Burkart (dsb3573)
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