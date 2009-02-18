package subsystem;

/**
 * This class describes a simple date, with a day, month, and year and provides methods
 * for manipulation of dates.
 * 
 * @author Dana Burkart (dsb3573)
 *
 */

public class SimpleDate {
	public int day, month, year;
	
	public SimpleDate() {
		day =0;
		month=0;
		year=0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param month month
	 * @param day day
	 * @param year year
	 */
	public SimpleDate(int month, int day, int year) {
	    this.day = day;
	    this.month = month;
	    this.year = year;
	}
	
	/**Copy constructor.
	 * 
	 * @param d The SimpleDate to make a copy of.
	 */
	public SimpleDate(SimpleDate d){
		this.day = d.day;
		this.month = d.month;
		this.year = d.year;
	}
	
	/**Checks to make sure this SimpleDate is valid; calls each field's isValid.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		
		return month <= 12 && month >= 1 && day <= CakeCal.getMonths(year)[month-1] && day >= 1;
		
	}
	
	/**
	 * Concatenates the month, day, and year into a single int.
	 * 
	 * @return concatenated date
	 */
	public int toInt() {
		return (year * 10000) + (month * 100) + day;
	}
	
	/**
	 * Parses a formatted 'date-stamp'
	 * 
	 * @param s 'date-stamp'
	 * @return SimpleDate representation of the date-stamp
	 */
	public static SimpleDate parse(String s) {
		String y = s.substring(0, s.indexOf('.'));
		String m = s.substring(s.indexOf('.')+1, s.lastIndexOf('.'));
		String d = s.substring(s.lastIndexOf('.')+1, s.length());
		return new SimpleDate(Integer.parseInt(m), Integer.parseInt(d), Integer.parseInt(y));
	}
	
	/**
	 * Returns a formatted 'date-stamp'. This is used for saving events.
	 * 
	 * @return formatted 'date-stamp'
	 */
	public String format() {
		return new String("") + year + "." + month + "." + day;
	}
	
	/**
	 * String representation of this date.
	 * 
	 * @return string representation
	 */
	public String toString(){
		return new String("") + toInt();
	}
} //-- SimpleDate
