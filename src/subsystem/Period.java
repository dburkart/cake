/**
 * Describes a time frame, or period, which is used throughout the program to 
 * complete tasks.
 * 
 * @author Dana Burkart
 * @author Hashem Assayari
 */

package subsystem;

import java.util.ArrayList;

public class Period {
	public SimpleDateTime start, end;
	
	/**
	 * Constructor
	 */
	public Period() {
		start = new SimpleDateTime();
		end=new SimpleDateTime();
	}
	
	/**
	 * Copy constructor
	 */
	public Period(Period p){
		this.start = new SimpleDateTime( p.start );
		this.end = new SimpleDateTime( p.end );
	}
	
	/**
	 * Overloaded constructor.
	 * 
	 * @param start beginning of the period
	 * @param end end of the period
	 */
	public Period(SimpleDateTime start, SimpleDateTime end) {
		this.start = start;
		this.end = end;
	}
	
	
	/**
	 * Returns whether or not the period is valid
	 * 
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		
		return start.isValid() && end.isValid();
		
	}
	
	/**
	 * Parses a 'period-stamp' and returns a Period object containing the right
	 * data
	 * 
	 * @param s 'period-stamp' to parse
	 * @return Period derived from the 'period-stamp'
	 */
	public static Period parse(String s) {
		String dtS = s.substring(0,s.indexOf('-'));
		String dtE = s.substring(s.indexOf('-') + 1, s.length());
		return new Period(SimpleDateTime.parse(dtS),SimpleDateTime.parse(dtE));
	}
	
	/**
	 * Returns a list of days in the specified period.
	 * 
	 * @param p period to inspect
	 * @return list of days in the period
	 */
	public static ArrayList<Period> splitIntoDays(Period p) {
		  ArrayList<Period> list = new ArrayList<Period>();
		  int n = 0;
		  
		  int years = p.end.date.year - p.start.date.year;
		  int months;
		  
		  if (years == 0) {
			  months = p.end.date.month - p.start.date.month;
		  } else {
			  if (years > 1)
				  months = (12 - p.start.date.month) + (p.end.date.month) + ((years-1)*12);
			  else months = (12 - p.start.date.month) + (p.end.date.month);
		  }
		  
		  if (years == 0 && months == 0 && p.start.date.day - p.end.date.day == 0) {
			  list.add(p);
			  return list;
		  }
		  
		  int k = p.start.date.month-1;
		  for (int i = 0; i <= years; i++) {
			  int x = 0;
			  int year = p.start.date.year + i;
			  int[] m = CakeCal.getMonths(i+p.start.date.year);
			  if (year == p.start.date.year) x = p.start.date.day-1;
			  if (i == years) {
				  for(;k<=p.end.date.month-1;k++) {
					  int month = k+1;
					  int e = m[k];
					  if (k == p.end.date.month-1 && i == years) e = p.end.date.day;
					  for (; x < e; x++) {
						  int day = x+1;
						  Period temp = Period.parse(year + "." + month + "." + day + ":" + p.start.time.hour +
								  "." + p.start.time.minutes + "-" + year + "." + month + "." + day + ":" +
								  p.end.time.hour + "." + p.end.time.minutes);
						  list.add(temp);
					  }
					  x = 0;
				  }
				  break;
			  }
			  for(;k<=11;k++) {
				  int month = k+1;
				  int e = m[k];
				  if (k == p.end.date.month-1 && i == years) e = p.end.date.day;
				  for (; x < e; x++) {
					  int day = x+1;
					  Period temp = Period.parse(year + "." + month + "." + day + ":" + p.start.time.hour +
							  "." + p.start.time.minutes + "-" + year + "." + month + "." + day + ":" +
							  p.end.time.hour + "." + p.end.time.minutes);
					  list.add(temp);
				  }
				  x = 0;
			  }
			  k=0;
		  }
		  
		  return list;
	  }
	 
	/**
	 * Returns the number of days in the specified period.
	 * 
	 * @param p Period to inspect
	 * @return number of days in the period
	 */
	public static int numberOfDays(Period p) {
		  int n = 0;
		  
		  int years = p.end.date.year - p.start.date.year;
		  /*int months;
		  
		  if (years == 0) {
			  months = p.end.date.month - p.start.date.month;
		  } else {
			  if (years > 1)
				  months = (12 - p.start.date.month) + (p.end.date.month) + ((years-1)*12);
			  else months = (12 - p.start.date.month) + (p.end.date.month);
		  }*/
		  
		  
		  int k = p.start.date.month-1;
		  for (int i = 0; i <= years; i++) {
			  int[] m = CakeCal.getMonths(i+p.start.date.year);
			  if (i == years) {
				  for(;k<=p.end.date.month-1;k++) {
					  n += m[k];
				  } 
				  break;
			  }
			  for(;k<=11;k++) {
				  n += m[k];
			  }
			  k=0;
		  }
		  return n;
	}
	
	/**
	 * Formats the Period into a 'period-stamp'
	 * 
	 * @return formatted 'period-stamp' representation of the Period
	 */
	public String format() {
		return start.format() + "-" + end.format();
	}
} //-- Period