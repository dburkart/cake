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

/**
 * This class primarily gets the day of the week for any given date. It
 * makes use of the Doomsday Algorithm, which is a fast way of getting the
 * day of a date.
 *
 * @author Dana Burkart
 */
public class Cakeday {
  public static final String DAYS[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
    "Friday", "Saturday"};
  public static final String MONTHS[] = {"January", "February", "March", "April", "May",
	  "June", "July", "August", "September", "October", "November", "December"};

  private static int months[]     = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
  private static int monthsL[]    = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
  private static int doomsdays[]  = { 3, 28,  7,  4,  9,  6, 11,  8,  5, 10,  7, 12};
  private static int doomsdaysL[] = { 4, 29,  7,  4,  9,  6, 11,  8,  5, 10,  7, 12};
  
  /**
   * Uses the Doomsday Algorithm to generate the day of the week.
   *
   * @param d a SimpleDate to find the day of
   * @return a number 0-6, which signifies the day of the week. Remember: our week starts on sunday!
   */
  public static int getDayOfWeek(SimpleDate d) {
    int y, dx, doomsday, dayOfWeek;
    int anchordays[] = {2, 0, 5, 3};
    int tDDays[];

    y = (d.year - ((int)Math.floor(d.year * .01) * 100));
    dx = (int)Math.floor(y/12) + (int)Math.floor(y%12) + 
      (int)Math.floor((int)Math.floor(y%12)/4);

    if (dx > 7) dx = dx % 7;

    doomsday = anchordays[(int)Math.floor(d.year * .01) % 4] + dx;
    
    // Account for leap years.
    if ((d.year % 4) == 0) tDDays = doomsdaysL;
    else tDDays = doomsdays;
     
    if (d.day > tDDays[d.month - 1]) 
    	dayOfWeek = ((Math.abs(tDDays[d.month - 1] - d.day) % 7) + doomsday) % 7;
    else {
    	dayOfWeek = (doomsday - (Math.abs(tDDays[d.month - 1] - d.day) % 7));
    	dayOfWeek = (dayOfWeek + 7) % 7; 		// We have to do this because java is a giant f*cktard
    }
    return dayOfWeek;
  }
  
  /**
   * Returns an array with the number of days in each month for the specified
   * year.
   * 
   * @param year the year to get months for
   * @return an array containing the months
   */
  static public int[] getMonths(int year) {
	  if ((year % 4) == 0) return monthsL;
	  return months;
  }
} //-- Cakeday
