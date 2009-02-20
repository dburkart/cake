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

package gui;

/**
 * An exception that is thrown if the input is not legal.
 * 
 * @author Dana Burkart
 *
 */
public class IllegalInputException extends Exception {
	private String message;
	
	/**Constructor for a new exception.  Takes a string as a parameter for the message.
	 * 
	 * @param s The string to construct for the message
	 */
	public IllegalInputException(String s) {
		message = s;
	}
	
	/**Get the message of this exception
	 * 
	 * @return The message of this exception.
	 */
	public String getMessage() {
		return message;
	}
}
